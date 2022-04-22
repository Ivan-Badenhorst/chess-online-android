package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class King extends Piece {
    //to do: write code to get the correct file

    public King(Color color, Board board) {
        super(board, color);
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

                if(board.getTile(i,j).getPiece().isPresent()) {
                    if (board.getTile(i, j).getPiece().get().getColor() != this.color) {
                        moves.add(board.getTile(i, j));
                    }

                }
                else{
                    moves.add(board.getTile(i, j));
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
}
