package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
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
        int[] position = tile.getPosition();

        int[] horizontalCombination = {-1, 1, 2, 2, -2, -2, -1, 1};
        int[] verticalCombination = {-2, -2, -1, 1, -1, 1, 2, 2};

        for (int i = 0; i < 8; i++) {

            int newRow = position[0] + horizontalCombination[i];
            int newCol = position[1] + verticalCombination[i];
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {

                addNormalMove(newRow,newCol);

                if(this.color == Color.white) {
                    addCaptures(newRow, newCol, Color.black);
                }
                else if (this.color == Color.black) {
                    addCaptures(newRow, newCol, Color.white);
                }

            }

        }
    }
}
