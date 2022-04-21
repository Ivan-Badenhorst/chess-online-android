package be.kuleuven.chess.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.List;

public abstract class Piece {
    protected Color color;
    protected Board board;
    protected Tile tile;
    protected List<Tile> moves;

    public Piece(Board board)
    {
        this.board = board;
    }



    public abstract Drawable getImage(Context ctx);
    public abstract void generateMoves();

    protected void determineTile(){
        for(int i = 0; i<8;i++){
            for(int j = 0; j<8;j++){
                if(board.getTile(i,j).getPiece().isPresent()){
                    if(board.getTile(i,j).getPiece().get().equals(this)){
                        this.tile = board.getTile(i,j);
                    }
                }
            }
        }
    }

}
