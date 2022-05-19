package be.kuleuven.chess.models;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;


import java.util.Optional;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.pieces.Bishop;
import be.kuleuven.chess.models.pieces.King;
import be.kuleuven.chess.models.pieces.Knight;
import be.kuleuven.chess.models.pieces.Pawn;
import be.kuleuven.chess.models.pieces.Queen;
import be.kuleuven.chess.models.pieces.Rook;


public class Tile {
    private Optional<Piece> piece;
    private final Board board;
    private  int row;
    private  int column;
    private final Color color;

    public Tile(Color color){
        piece = Optional.empty();
        this.board = Board.getBoardObj();
        this.color = color;

    }


    public void addPiece(Piece piece){
        //for testing purpose we declare a fixed piece
        //normally use parameter
        this.piece = Optional.of(piece);
    }




    public Optional<Piece> getPiece(){
        return piece;
    }

    public void calcPosition(){
        for(int i =0; i<8 ; i++){
            for(int j =0; j<8 ; j++){
                if(board.getTile(i, j).equals(this)){
                    row = i;
                    column = j;
                    break;
                }
            }

        }
    }

    public Drawable getTileImage(Context ctx){
        Resources r = ctx.getResources();
        if(this.color == Color.white){
            return r.getDrawable(R.drawable.dark_square);
        }
        else{
            return r.getDrawable(R.drawable.light_square);
        }
    }

    public Drawable[] getImage(Context ctx){
        Drawable tile = getTileImage(ctx);
        Drawable image = piece.get().getImage(ctx);

        Drawable[] layers = new Drawable[2];
        layers[0] = tile;
        layers[1] = image;
        return layers;
    }

    public void removePiece(){
        piece = Optional.empty();
    }

    public int[] getPosition(){
        return new int[]{row, column};
    }

    public boolean checkCheck(Color color){
        /*

            idea for checking a tile for check:
            go in each straight till you find a piece. if its a rook/queen its check

            go in each diagonal till you find a piece. if its on the first square + pawn its check
            if its a bishop its check, if its a queen its check

            check for knight

        */
        return checkStraight(color) || checkDiagonal(color) || checkKnight(color);
    }

    private boolean checkStraight(Color color){
        boolean left = true, right = true, top = true, bottom = true;
        int[] pos = this.getPosition();
        int[] current = new int[2];

        for(int i = 1; i<8; i++){

            if(left) {
                current[0] = pos[0] ;
                current[1] = pos[1] - i;

                Piece p = getCheckPiece(current, color);//remove color from method later

                if(p != null){
                    left = false;
                    if (p.getColor() != color) {
                        if (p instanceof Rook || p instanceof Queen) {
                            return true;
                        }
                        if (p instanceof King && i == 1) {
                            return true;
                        }
                    }
                }
            }

            if(right) {
                current[0] = pos[0];
                current[1] = pos[1] + i;

                Piece p = getCheckPiece(current, color);

                if(p != null) {
                    right = false;
                    if (p.getColor() != color) {
                        if (p instanceof Rook || p instanceof Queen) {
                            return true;
                        }
                        if (p instanceof King && i == 1) {
                            return true;
                        }
                    }
                }
            }

            if(top) {
                current[0] = pos[0] - i;
                current[1] = pos[1];

                Piece p = getCheckPiece(current, color);

                if(p != null) {
                    top = false;
                    if (p.getColor() != color) {
                        if (p instanceof Rook || p instanceof Queen) {
                            return true;
                        }
                        if (p instanceof King && i == 1) {
                            return true;
                        }
                    }
                }
            }


            if(bottom){
                current[0] = pos[0] + i;
                current[1] = pos[1];

                Piece p = getCheckPiece(current, color);

                if(p != null) {
                    bottom = false;
                    if (p.getColor() != color) {
                        if (p instanceof Rook || p instanceof Queen) {
                            return true;
                        }
                        if (p instanceof King && i == 1) {
                            return true;
                        }
                    }
                }
            }

        }

        return false;
    }

    private boolean checkDiagonal(Color color){
        int[] pos = this.getPosition();
        int[] current = new int[2];
        boolean leftTop = true, leftBottom = true, rightTop = true, rightBottom = true;

        for(int i = 1; i<8; i++){
            //Add check for pawn!!

            if(leftTop) {
                current[0] = pos[0] - i;
                current[1] = pos[1] - i;

                Piece p = getCheckPiece(current, color);

                if(p != null) {
                    leftTop = false;
                    if (p.getColor() != color) {

                        if (p instanceof Bishop || p instanceof Queen) {
                            return true;
                        }
                        if (i == 1 && p instanceof Pawn && color == Color.white) {
                            return true;
                        }
                        if (p instanceof King && i == 1) {
                            return true;
                        }
                    }
                }
            }

            if(rightBottom) {
                current[0] = pos[0] + i;
                current[1] = pos[1] + i;

                Piece p = getCheckPiece(current, color);

                if(p != null) {
                    rightBottom  = false;
                    if (p.getColor() != color) {


                        if (p instanceof Bishop || p instanceof Queen) {
                            return true;
                        }
                        if (i == 1 && p instanceof Pawn && color == Color.black) {
                            return true;
                        }
                        if (p instanceof King && i == 1) {
                            return true;
                        }
                    }
                }
            }

            if(rightTop){
                current[0] = pos[0] - i;
                current[1] = pos[1] + i;

                Piece p = getCheckPiece(current, color);

                if(p != null) {

                    if (p.getColor() != color) {
                        rightTop = false;

                        if (p instanceof Bishop || p instanceof Queen) {
                            return true;
                        }
                        if (i == 1 && p instanceof Pawn && color == Color.white) {
                            return true;
                        }
                        if (p instanceof King && i == 1) {
                            return true;
                        }
                    }
                }

            }

            if(leftBottom){
                current[0] = pos[0] + i;
                current[1] = pos[1] - i;

                Piece p = getCheckPiece(current, color);

                if(p != null) {
                    leftBottom = false;
                    if(p.getColor() != color) {

                        if (p instanceof Bishop || p instanceof Queen) {
                            return true;
                        }
                        if (i == 1 && p instanceof Pawn && color == Color.black) {
                            return true;
                        }
                        if (p instanceof King && i == 1) {
                            return true;
                        }
                    }
                }
            }

        }

        return false;
    }

    private boolean checkKnight(Color color){
        int[] pos = this.getPosition();

        int[] horizontalComb = {-1, 1, 2, 2, -2, -2, -1, 1};
        int[] verticalComb = {-2, -2, -1, 1, -1, 1, 2, 2};


        // Check if each possible move is valid or not
        for (int i = 0; i < 8; i++) {

            // Position of knight after move
            int newRow = pos[0] + horizontalComb[i];
            int newCol = pos[1] + verticalComb[i];
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {

                if(board.getTile(newRow,newCol).getPiece().isPresent()){
                    Piece piece = board.getTile(newRow,newCol).getPiece().get();

                    if(piece.getColor() != color && piece instanceof Knight){
                        return true;
                    }

                }


            }

        }
        return false;
    }

    private Piece getCheckPiece(int[] current, Color color) {

        if (current[0] >= 0 && current[1] >= 0 && current[0] < 8 && current[1] < 8) {

            if (board.getTile(current[0], current[1]).getPiece().isPresent()) {
                    return board.getTile(current[0], current[1]).getPiece().get();
                }

            }


        return null;
    }

}
