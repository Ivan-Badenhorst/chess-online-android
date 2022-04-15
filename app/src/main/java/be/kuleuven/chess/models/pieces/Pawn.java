package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class Pawn extends Piece {
    private Color color;

    public Pawn(Color color){
        this.color = color;
    }

    @Override
    public Drawable getImage(Context ctx) {
        Resources r = ctx.getResources();
        Drawable symbol;
        if(color == Color.black) {
            symbol = r.getDrawable(R.drawable.white_pawn);
        }
        else{
            symbol = r.getDrawable(R.drawable.black_pawn);
        }
        return symbol;
    }
}
