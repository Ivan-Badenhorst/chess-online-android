package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class Knight extends Piece {
    private Color color;

    public Knight(Color color){
        super();
        this.color = color;
    }

    @Override
    public Drawable getImage(Context ctx) {
        Resources r = ctx.getResources();
        Drawable symbol;
        if(color == Color.black) {
            symbol = r.getDrawable(R.drawable.white_knight);
        }
        else{
            symbol = r.getDrawable(R.drawable.black_knight);
        }
        return symbol;
    }
}
