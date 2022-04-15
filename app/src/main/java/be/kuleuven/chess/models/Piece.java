package be.kuleuven.chess.models;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;


import be.kuleuven.chess.R;

public abstract class Piece {
    protected Color color;

    public Piece(Color color){
        this.color = color;
    }

    public Drawable getTileImage(){
        if(this.color == Color.white){
            return Resources.getSystem().getDrawable(R.drawable.dark_square);
        }
        else{
            return Resources.getSystem().getDrawable(R.drawable.light_square);
        }
    }

    public abstract Drawable[] getImage();

    ;

}
