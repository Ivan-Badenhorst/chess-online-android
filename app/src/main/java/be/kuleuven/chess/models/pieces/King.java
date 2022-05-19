package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;
import be.kuleuven.chess.models.Tile;

public class King extends Piece {
    //to do: write code to get the correct file
    private boolean hasMoved;

    public King(Color color) {
        super(color);
        hasMoved = false;
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
    @Override
    public void generateMoves()
    {
        determineTile();
        moves.clear();
        int[] pos = tile.getPosition();
        int[] bounds = getBound(pos[0], pos[1]);

        for(int i = bounds[0]; i <= bounds[2]; i++)
        {
            for (int j = bounds[1]	; j<= bounds[3]; j++)
            {
                addNormalMove(i, j);
                if(this.color == Color.white) {
                    addCaptures(i, j, Color.black);
                }
                else if (this.color == Color.black) {
                    addCaptures(i, j, Color.white);
                }


            }
        }



    }

    public int[] getBound(int row, int column){
        //order: lowerH, lowerV, higherH, higherV
        int[] ret = new int[4];


        if(row == 0){
            ret[0] = row;
        }
        else{
            ret[0] = row-1;
        }

        if(column == 0){
            ret[1] = column;
        }
        else{
            ret[1] = column-1;
        }

        if(row == 7){
            ret[2] = row;
        }
        else{
            ret[2] = row + 1;
        }

        if(column == 7){
            ret[3] = column;
        }
        else{
            ret[3] = column + 1;
        }

        return ret;


    }

    public List<Tile> getCastlingSquares(){
        /*
        send array in the format:
        4 left, 3 left, 2 left, 1 left, 1 right, 2 right, 3 right, 4 right
         */

        int[] pos = tile.getPosition();
        int[] current = new int[2];
        List<Tile> ls = new ArrayList<>();
        boolean empty = true;

        current[0] = pos[0];

        for(int i = -4; i<5; i++){
            if(hasMoved){
                return null;
            }
            if(i != 0){
                current[1] = pos[1] + i;
                if(current[1] >= 0 && current[1] <8){
                    empty = false;
                    ls.add(board.getTile(current[0], current[1]));
                }

            }
        }
        if(empty){
            return null;
        }
        return ls;

    }

    public void setHasMoved(boolean val){
        hasMoved = val;
    }


}
