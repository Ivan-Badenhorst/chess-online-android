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
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    private static Board boardobj;

    private boolean hasInitialized = true;


    private Board(){
        board = new Tile[8][8];
    }

    static {
        boardobj = new Board();
    }

    public static Board getBoardObj()
    {
        return boardobj;
    }

    public Tile getTile(int row, int column){
        return board[row][column];
    }



    public Tile[][] getBoard(){
        return board;
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
        /*this is a first version that can only initialize a default position in order
        to check if the placement works.
        Replace with an algo that takes some input and uses it to create custom position
         */
        for(int i = 0; i<2; i++){

            for(int j = 0; j <5; j++){

                if(i == 0 ){//first row of black:
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
        calculateMoves(null);
    }

    private void readPieces(){
        //for now this only works for the initial setup, not for a midgame start
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        whitePieces.clear();
        blackPieces.clear();
        //REFACTOR TO NOT USE GET 60000 TIMES
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

    public Tile getKingTile(Color color){
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

}


