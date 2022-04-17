package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class King extends Piece {
    //to do: write code to get the correct file
    private Color color;
    public King(Color color){
        super();
        this.color = color;
    }

    @Override
    public Drawable getImage(Context ctx){
        Resources r = ctx.getResources();
        Drawable symbol;
        if(color == Color.black) {
            symbol = r.getDrawable(R.drawable.black_king);
        }
        else{
            symbol = r.getDrawable(R.drawable.white_king);
        }
        return symbol;

    }
}
