package be.kuleuven.chess.models;


import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

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

    public Board(){
        board = new Tile[8][8];
        generateBoard();
    }

    public Tile getTile(int row, int column){
        return board[row][column];
    }

    public Tile[][] getBoard(){
        return board;
    }

    private void generateBoard(){
        for(int i =0; i<board.length; i++){
            for(int j = 0; j<board[0].length; j++){
                if( (i %2 == 0 & j%2 != 0) || (i %2 != 0 & j%2 == 0) ) {
                    board[i][j] = new Tile(this, Color.white);
                }
                else{
                    board[i][j] = new Tile(this, Color.black);
                }
            }
        }

        for(int i =0; i<board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j].calcPosition();
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
                            getTile(i, j).addPiece(new Rook(Color.black, this));
                            getTile(i, 7).addPiece(new Rook(Color.black, this));

                            getTile(7, j).addPiece(new Rook(Color.white, this));
                            getTile(7, 7).addPiece(new Rook(Color.white, this));
                            break;
                        }
                        case 1:{
                            getTile(i, j).addPiece(new Knight(Color.black, this));
                            getTile(i, 7-j).addPiece(new Knight(Color.black, this));

                            getTile(7-i, j).addPiece(new Knight(Color.white, this));
                            getTile(7-i, 7-j).addPiece(new Knight(Color.white, this));
                            break;
                        }
                        case 2:{
                            getTile(i, j).addPiece(new Bishop(Color.black, this));
                            getTile(i, 7-j).addPiece(new Bishop(Color.black, this));

                            getTile(7-i, j).addPiece(new Bishop(Color.white, this));
                            getTile(7-i, 7-j).addPiece(new Bishop(Color.white, this));
                            break;

                        }
                        case 3:{
                            getTile(i, j).addPiece(new Queen(Color.black, this));
                            getTile(7-i, j).addPiece(new Queen(Color.white, this));
                            break;

                        }
                        case 4:{
                            getTile(i, j).addPiece(new King(Color.black, this));
                            getTile(7-i, j).addPiece(new King(Color.white, this));
                            break;
                        }

                    }
                }
                else{
                    getTile(i, j).addPiece(new Pawn(Color.black, this));
                    getTile(7-i, j).addPiece(new Pawn(Color.white, this));

                    getTile(i, 7-j).addPiece(new Pawn(Color.black, this));
                    getTile(7-i, 7-j).addPiece(new Pawn(Color.white, this));
                }

            }
        }

        readPieces();
        calculateMoves();
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

    public void calculateMoves(){
        readPieces();
        for(int i = 0; i<whitePieces.size(); i++){
            whitePieces.get(i).generateMoves();
        }
        for(int i = 0; i<blackPieces.size(); i++){
            blackPieces.get(i).generateMoves();
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


