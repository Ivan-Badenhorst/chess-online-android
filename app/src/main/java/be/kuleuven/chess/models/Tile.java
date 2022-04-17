package be.kuleuven.chess.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.Optional;

import be.kuleuven.chess.R;
@SuppressLint("NewApi")
public class Tile {
    private Optional<Piece> piece;
    private final Board board;
    private int row;
    private int column;
    private final Color color;



    public Tile(Board board, Color color){
        piece = Optional.empty();
        this.board = board;
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

    private void calcPosition(){
        for(int i =0; i<8 ; i++){

            for(int j =0; j<8 ; j++){
                if(board.getBoard()[i][j].equals(this)){
                    row = i;
                    column = j;
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

}
