package be.kuleuven.chess.models;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;

public class King extends Piece{
    //to do: write code to get the correct file
    public King(Color color){
        super(color);
    }

    public Drawable[] getImage(){
        Drawable tile = super.getTileImage();
        Drawable symbol = Resources.getSystem().getDrawable(R.drawable.white_castle);

        Drawable[] layers = new Drawable[2];
        layers[0] = tile;
        layers[1] = symbol;

        return layers;

    }
}
