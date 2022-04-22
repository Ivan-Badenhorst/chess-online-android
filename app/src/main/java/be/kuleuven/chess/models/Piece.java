package be.kuleuven.chess.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected Color color;
    protected Board board;
    protected Tile tile;
    protected List<Tile> moves;

    public Piece(Board board, Color color)
    {
        this.board = board;
        moves = new ArrayList<>();
        this.color = color;
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
        int[] current = new int[2];
        boolean left = true, right = true, top = true, bottom = true;

        for(int i = 1; i<8; i++){

            if(left) {
                current[0] = pos[0] ;
                current[1] = pos[1] - i;

                Pair<Boolean, Tile> res = getMove(current);
                left = res.first;
                if(res.second != null){
                    moves.add(res.second);
                }
            }

            if(right) {
                current[0] = pos[0];
                current[1] = pos[1] + i;

                Pair<Boolean, Tile> res = getMove(current);
                right = res.first;
                if(res.second != null){
                    moves.add(res.second);
                }
            }

            if(top){
                current[0] = pos[0] - i;
                current[1] = pos[1];

                Pair<Boolean, Tile> res = getMove(current);
                top = res.first;
                if(res.second != null){
                    moves.add(res.second);
                }
            }

            if(bottom){
                current[0] = pos[0] + i;
                current[1] = pos[1];

                Pair<Boolean, Tile> res = getMove(current);
                bottom = res.first;
                if(res.second != null){
                    moves.add(res.second);
                }
            }

        }

        return moves;
    }

    protected List<Tile> getDiagonalMoves(){
        determineTile();

        int[] pos = tile.getPosition();
        List<Tile> moves = new ArrayList<>();
        int[] current = new int[2];
        boolean leftTop = true, leftBottom = true, rightTop = true, rightBottom = true;

        for(int i = 1; i<8; i++){

            if(leftTop) {
                current[0] = pos[0] - i;
                current[1] = pos[1] - i;

                Pair<Boolean, Tile> res = getMove(current);
                leftTop = res.first;
                if(res.second != null){
                    moves.add(res.second);
                }
            }

            if(rightBottom) {
                current[0] = pos[0] + i;
                current[1] = pos[1] + i;

                Pair<Boolean, Tile> res = getMove(current);
                rightBottom = res.first;
                if(res.second != null){
                    moves.add(res.second);
                }
            }

            if(rightTop){
                current[0] = pos[0] - i;
                current[1] = pos[1] + i;

                Pair<Boolean, Tile> res = getMove(current);
                rightTop = res.first;
                if(res.second != null){
                    moves.add(res.second);
                }
            }

           if(leftBottom){
               current[0] = pos[0] + i;
               current[1] = pos[1] - i;

               Pair<Boolean, Tile> res = getMove(current);
               leftBottom = res.first;
               if(res.second != null){
                   moves.add(res.second);
               }
           }

        }

        return moves;

    }

    private Pair<Boolean, Tile> getMove(int[] current){

        boolean ret = true;
        Tile tile = null;

        if (current[0] >= 0 && current[1] >= 0 && current[0] <8 && current[1] <8) {

            if(board.getTile(current[0], current[1]).getPiece().isPresent()){
                ret = false;
                if(board.getTile(current[0], current[1]).getPiece().get().getColor() != this.color){
                    tile = (board.getTile(current[0], current[1]));
                }

            }
            else{
                tile = (board.getTile(current[0], current[1]));
            }

        }
        return new Pair<Boolean, Tile>(ret, tile);
    }

    public Color getColor(){
        if (color == Color.black){
            return Color.black;
        }
        return Color.white;
    }
}
