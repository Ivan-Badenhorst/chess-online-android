package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;
import be.kuleuven.chess.models.SpecialMoves.Castling;
import be.kuleuven.chess.models.Tile;

public class King extends Piece {

    private boolean hasMoved;
    private List<Tile> castlingTiles;

    public King(Color color) {
        super(color);
        hasMoved = false;
        castlingTiles = new ArrayList<>();
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

        addRegularMoves();
        addCastlingMoves();

    }

    private void addRegularMoves(){

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

    public void addCastlingMoves(){
        Castling castling = new Castling(tile.getPosition(), hasMoved,color);
        castlingTiles = castling.getCastlingSquares();

        if(castlingTiles != null && !tile.checkCheck(color)) {

            if(castling.castlingPossible(castlingTiles.get(0), true)){
                moves.add(castlingTiles.get(2));
            }
            if(castling.castlingPossible(castlingTiles.get(6), false)){
                moves.add(castlingTiles.get(5));
            }

        }

    }


    private int[] getBound(int row, int column){
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


    public void setHasMoved(boolean val){
        hasMoved = val;
    }

    public List<Tile> getCastling(){
        return castlingTiles;
    }

}
