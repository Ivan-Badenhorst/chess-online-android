package be.kuleuven.chess.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected Color color;
    protected Board board;
    protected Tile tile;
    protected List<Tile> moves;

    public Piece(Board board)
    {
        this.board = board;
        moves = new ArrayList<>();
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

    public List<Tile> getMoves() {
        return moves;
    }

    protected List<Tile> getStraightMoves(){
        determineTile();

        int[] pos = tile.getPosition();
        List<Tile> moves = new ArrayList<>();
        //first horizontal
        for(int i = 0; i<8; i++){
            if(i != pos[1]){
                moves.add(board.getTile(pos[0], i));
            }
        }
        //second vertical
        for(int i = 0; i<8; i++){
            if(i != pos[0]){
                moves.add(board.getTile(i, pos[1]));
            }
        }
        return moves;
    }

    protected List<Tile> getDiagonalMoves(){
        determineTile();

        int[] pos = tile.getPosition();
        List<Tile> moves = new ArrayList<>();

        for(int i = 0; i<8; i++){
            int[] current = new int[2];
            current[0] = pos[0] - i;
            current[1] = pos[1] - i;

            if(current[0] >= 0 && current[1] >= 0){
                moves.add(board.getTile(current[0], current[1]));
            }

            current[0] = pos[0] + i;
            current[1] = pos[1] + i;

            if(current[0] < 8 && current[1] < 8){
                moves.add(board.getTile(current[0], current[1]));
            }

            current[0] = pos[0] - i;
            current[1] = pos[1] + i;

            if(current[0] >= 0 && current[1] < 8){
                moves.add(board.getTile(current[0], current[1]));
            }

            current[0] = pos[0] + i;
            current[1] = pos[1] - i;

            if(current[0] < 8 && current[1] >= 0){
                moves.add(board.getTile(current[0], current[1]));
            }


        }

        return moves;

    }
}
