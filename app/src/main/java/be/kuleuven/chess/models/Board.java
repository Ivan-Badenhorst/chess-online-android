package be.kuleuven.chess.models;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chess.models.pieces.Bishop;
import be.kuleuven.chess.models.pieces.King;
import be.kuleuven.chess.models.pieces.Knight;
import be.kuleuven.chess.models.pieces.Pawn;
import be.kuleuven.chess.models.pieces.Queen;
import be.kuleuven.chess.models.pieces.Rook;

public class Board {

    private final Tile[][] board;
    private static final Board boardObj;

    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;


    private Board(){
        board = new Tile[8][8];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
    }

    static {
        boardObj = new Board();
    }

    public static Board getBoardObj()
    {
        return boardObj;
    }


    public void generateBoard(){
        for(int i =0; i<board.length; i++){
            for(int j = 0; j<board[0].length; j++){
                if( (i %2 == 0 & j%2 != 0) || (i %2 != 0 & j%2 == 0) ) {
                    board[i][j] = new Tile(Color.white);
                }
                else{
                    board[i][j] = new Tile(Color.black);
                }
            }
        }

        for (Tile[] tiles : board) {
            for (int j = 0; j < board[0].length; j++) {
                tiles[j].calcPosition();
            }
        }

        placePieces();

    }

    private void placePieces(){

        for(int i = 0; i<2; i++){

            for(int j = 0; j <5; j++){

                if(i == 0 ){
                    switch(j){
                        case 0:{
                            getTile(i, j).addPiece(new Rook(Color.black));
                            getTile(i, 7).addPiece(new Rook(Color.black));

                            getTile(7, j).addPiece(new Rook(Color.white));
                            getTile(7, 7).addPiece(new Rook(Color.white));
                            break;
                        }
                        case 1:{
                            getTile(i, j).addPiece(new Knight(Color.black));
                            getTile(i, 7-j).addPiece(new Knight(Color.black));

                            getTile(7-i, j).addPiece(new Knight(Color.white));
                            getTile(7-i, 7-j).addPiece(new Knight(Color.white));
                            break;
                        }
                        case 2:{
                            getTile(i, j).addPiece(new Bishop(Color.black));
                            getTile(i, 7-j).addPiece(new Bishop(Color.black));

                            getTile(7-i, j).addPiece(new Bishop(Color.white));
                            getTile(7-i, 7-j).addPiece(new Bishop(Color.white));
                            break;

                        }
                        case 3:{
                            getTile(i, j).addPiece(new Queen(Color.black));
                            getTile(7-i, j).addPiece(new Queen(Color.white));
                            break;

                        }
                        case 4:{
                            getTile(i, j).addPiece(new King(Color.black));
                            getTile(7-i, j).addPiece(new King(Color.white));
                            break;
                        }

                    }
                }
                else{
                    getTile(i, j).addPiece(new Pawn(Color.black));
                    getTile(7-i, j).addPiece(new Pawn(Color.white));

                    getTile(i, 7-j).addPiece(new Pawn(Color.black));
                    getTile(7-i, 7-j).addPiece(new Pawn(Color.white));
                }

            }
        }

        readPieces();
    }


    private void readPieces(){

        whitePieces.clear();
        blackPieces.clear();

        for(int i =0; i<8 ; i++) {
            for (int j = 0; j < 8; j++) {

                if(getTile(i, j).getPiece().isPresent()){
                    Piece p  = getTile(i, j).getPiece().get();
                    Color color = p.getColor();

                    if(color == Color.black){
                        blackPieces.add(getTile(i, j).getPiece().get());
                    }
                    else{
                        whitePieces.add(getTile(i, j).getPiece().get());
                    }

                }
            }
        }
    }


    public void calculateMoves(Move move){
        readPieces();
        for(int i = 0; i<whitePieces.size(); i++){
            if(whitePieces.get(i) instanceof Pawn){
                ( (Pawn) whitePieces.get(i)).generateMoves(move);
            }
            else{
                whitePieces.get(i).generateMoves();
            }


        }
        for(int i = 0; i<blackPieces.size(); i++){

            if(blackPieces.get(i) instanceof Pawn){
                ( (Pawn) blackPieces.get(i)).generateMoves(move);
            }
            else{
                blackPieces.get(i).generateMoves();
            }

        }
        //whitePieces.stream().forEach(Piece::generateMoves);
        //blackPieces.stream().forEach(n -> n.generateMoves());
    }

    private Tile getKingTile(Color color){

        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){

                Tile t = board[i][j];
                if(t.getPiece().isPresent()){

                    if(t.getPiece().get() instanceof King && t.getPiece().get().getColor() == color){
                        return t;
                    }

                }

            }
        }
        return null;

    }

    public boolean isCheck(Color color){
        return getKingTile(color).checkCheck(color);
    }


    public boolean isNoMovePossible(Color color, Move prevMov){
        calculateMoves(prevMov);
        readPieces();
        List<Piece> piecesToCheck;
        if(color == Color.white){
            piecesToCheck = whitePieces;
        }
        else{
            piecesToCheck = blackPieces;
        }

        for(int j = 0; j<piecesToCheck.size(); j++)
        {
            Piece piece = piecesToCheck.get(j);

          for(int i = 0; i<piece.getMoves().size(); i++){
              Tile tile2 = piece.getMoves().get(i);
              Move moveTry = new Move(piece.getTile(), tile2, prevMov);
              boolean possible = moveTry.makeMove();
              if(possible){
                  moveTry.undoMove();
                  return false;
              }
          }
        }

        return true;
    }


    public Tile getTile(int row, int column){
        return board[row][column];
    }

    public Tile[][] getBoard(){
        return board;
    }

}