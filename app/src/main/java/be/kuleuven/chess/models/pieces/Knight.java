package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class Knight extends Piece {

    public Knight(Color color, Board board) {
        super(board, color);
    }

    @Override
    public Drawable getImage(Context ctx) {
        Resources r = ctx.getResources();
        Drawable symbol;
        if (color == Color.black) {
            symbol = r.getDrawable(R.drawable.black_knight);
        } else {
            symbol = r.getDrawable(R.drawable.white_knight);
        }
        return symbol;
    }

    @Override
    public void generateMoves() {

        determineTile();
        moves.clear();
        int[] pos = tile.getPosition();

        int horizontalComb[] = {-1, 1, 2, 2, -2, -2, -1, 1};
        int verticalComb[] = {-2, -2, -1, 1, -1, 1, 2, 2};


        // Check if each possible move is valid or not
        for (int i = 0; i < 8; i++) {

            // Position of knight after move
            int newRow = pos[0] + horizontalComb[i];
            int newCol = pos[1] + verticalComb[i];
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                if(board.getTile(newRow,newCol).getPiece().isPresent()){
                    if(board.getTile(newRow,newCol).getPiece().get().getColor() != this.color)
                    {moves.add(board.getTile(newRow,newCol));}
                }

            }
            else{
                moves.add(board.getTile(newRow,newCol));
            }
        }

    }
}
