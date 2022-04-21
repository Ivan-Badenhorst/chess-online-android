package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class Queen extends Piece {
    private final Color color;
    public Queen(Color color, Board board) {
        super(board);
        this.color = color;
    }


    @Override
    public Drawable getImage(Context ctx) {
        Resources r = ctx.getResources();
        Drawable symbol;
        if(color == Color.black) {
            symbol = r.getDrawable(R.drawable.black_queen);
        }
        else{
            symbol = r.getDrawable(R.drawable.white_queen);
        }
        return symbol;
    }

    @Override
    public void generateMoves()
    {

    }
}
