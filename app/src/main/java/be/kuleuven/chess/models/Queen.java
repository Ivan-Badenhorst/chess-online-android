package be.kuleuven.chess.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;

public class Queen extends Piece {
    private final Color color;

    public Queen(Color color) {
        this.color = color;
    }

    @Override
    public Drawable getImage(Context ctx) {
        Resources r = ctx.getResources();
        Drawable symbol;
        if(color == Color.black) {
            symbol = r.getDrawable(R.drawable.white_queen);
        }
        else{
            symbol = r.getDrawable(R.drawable.black_queen);
        }
        return symbol;
    }
}
