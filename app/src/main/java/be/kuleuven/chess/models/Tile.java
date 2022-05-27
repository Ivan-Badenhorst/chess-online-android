package be.kuleuven.chess.models;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.Optional;

import be.kuleuven.chess.R;



public class Tile {

    private Optional<Piece> piece;
    private final Board board;

    private int row;
    private int column;
    private final Color color;

    public Tile(Color color){
        this.board = Board.getBoardObj();
        this.color = color;

        piece = Optional.empty();
    }


    public Drawable getTileImage(Context ctx){

        Resources r = ctx.getResources();

        if(this.color == Color.white){
            return r.getDrawable(R.drawable.light_square);
        }
        else{
            return r.getDrawable(R.drawable.dark_square);
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


    public boolean checkCheck(Color kingColor){
        Check check = new Check(kingColor, getPosition());

        return check.checkStraight() || check.checkDiagonal() || check.checkKnight();
    }


    public void addPiece(Piece piece){ this.piece = Optional.of(piece);}

    public Optional<Piece> getPiece(){
        return piece;
    }

    public void removePiece(){
        piece = Optional.empty();
    }

    public int[] getPosition(){
        return new int[]{row, column};
    }

}
