package be.kuleuven.chess.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

public abstract class Piece {
    protected Color color;

    public Piece(){
    }



    public abstract Drawable getImage(Context ctx);
    public abstract void generateMoves();

}
