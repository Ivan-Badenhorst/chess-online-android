package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class Rook extends Piece {
    private boolean hasMoved;

    public Rook(Color color) {
        super(color);
        hasMoved = false;
    }

    @Override
    public Drawable getImage(Context ctx) {
        Resources r = ctx.getResources();
        Drawable symbol;
        if(color == Color.black) {
            symbol = r.getDrawable(R.drawable.black_rook);
        }
        else{
            symbol = r.getDrawable(R.drawable.white_rook);
        }
        return symbol;
    }
    @Override
    public void generateMoves()
    {
        moves.clear();
        Log.d("ROOK", "generateMoves");
        getStraightMoves();
    }

    public void setHasMoved(boolean val){
        hasMoved = val;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
}
