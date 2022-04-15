package be.kuleuven.chess.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;


import be.kuleuven.chess.R;

public abstract class Piece {
    protected Color color;

    public Piece(){

    }



    public abstract Drawable getImage(Context ctx);

    ;

}
