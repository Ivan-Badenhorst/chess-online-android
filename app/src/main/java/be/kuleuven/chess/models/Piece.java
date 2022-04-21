package be.kuleuven.chess.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

public abstract class Piece {
    protected Color color;
    protected Board board;

    public Piece(Board board)
    {
        this.board = board;
    }



    public abstract Drawable getImage(Context ctx);
    public abstract void generateMoves();

    private void determineTile(){

    }

}
