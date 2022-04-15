package be.kuleuven.chess.models;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.Optional;

import be.kuleuven.chess.R;

public class Tile {
    private Optional<Piece> piece;
    private Board board;
    private int row;
    private int column;
    private Color color;


    public Tile(Board board, Color color){
        //piece = Optional.empty();
        this.board = board;
        this.color = color;
    }

    public void addPiece(){
        //for testing purpose we declare a fixed piece
        //normally use parameter
        //piece = Optional.of(new King(Color.white));
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

    public Drawable getTileImage(){
        if(this.color == Color.white){
            return Resources.getSystem().getDrawable(R.drawable.dark_square);
        }
        else{
            return Resources.getSystem().getDrawable(R.drawable.light_square);
        }
    }

}
