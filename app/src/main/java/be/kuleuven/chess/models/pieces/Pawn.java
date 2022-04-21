package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class Pawn extends Piece {
    private final Color color;
    private boolean hasMoved;

    public Pawn(Color color, Board board) {
        super(board);
        this.color = color;
        hasMoved = false;
    }

    @Override
    public Drawable getImage(Context ctx) {
        Resources r = ctx.getResources();
        Drawable symbol;
        if(color == Color.black) {
            symbol = r.getDrawable(R.drawable.black_pawn);
        }
        else{
            symbol = r.getDrawable(R.drawable.white_pawn);
        }
        return symbol;
    }
    @Override
    public void generateMoves()
    {
        determineTile();
        int[] pos = tile.getPosition();
        moves.clear();

        if(pos[0] < 7 && pos[0] > 0){
            if(this.color == Color.white){
                moves.add(board.getTile(pos[0] - 1, pos[1]));
            }
            else{
                moves.add(board.getTile(pos[0] + 1, pos[1]));
            }

        }


    }
}
