package be.kuleuven.chess.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Ivan Badenhorst, Sudarshan Iyengar
 *
 * @Definition: Contains all properties and methods used by different types of pieces.
 *
 * @Structure:  Parent of classes in package: be.kuleuven.chess.models.pieces
 */
public abstract class Piece implements Cloneable{

    protected Board board;
    protected Tile tile;

    protected Color color;
    protected List<Tile> moves;

    public Piece(Color color)
    {
        this.board = Board.getBoardObj();
        this.color = color;
        moves = new ArrayList<>();

        determineTile();
    }


    public abstract Drawable getImage(Context ctx);
    public abstract void generateMoves();

    /**
     *Method determines the Tile that this Piece is positioned on
     */
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


    /**
     * Method determines all tiles that a specific piece can move to in straight lines
     * Straight line - left, right, up or down
     *
     * @return A list of Tiles the specific could move to based on mentioned directions
     */

    protected void getStraightMoves(){
        determineTile();

        int[] pos = tile.getPosition();
        List<Tile> moves = new ArrayList<>();
        int[] current = new int[2];
        boolean left = true, right = true, top = true, bottom = true;

        for(int i = 1; i<8; i++){

            if(left) {
                current[0] = pos[0] ;
                current[1] = pos[1] - i;

                left = checkTileForMove(current);
            }

            if(right) {
                current[0] = pos[0];
                current[1] = pos[1] + i;

                right = checkTileForMove(current);
            }

            if(top){
                current[0] = pos[0] - i;
                current[1] = pos[1];

                top = checkTileForMove(current);
            }

            if(bottom){
                current[0] = pos[0] + i;
                current[1] = pos[1];

                bottom = checkTileForMove(current);
            }

        }

    }

    /**
     *  Method determines all tiles that a specific piece can move to in diagonal lines
     *  Straight line - left top/bottom, right top/bottom
     *
     * @return A list of Tiles the specific could move to based on mentioned directions
     */
    protected void getDiagonalMoves(){
        determineTile();

        int[] pos = tile.getPosition();
        List<Tile> moves = new ArrayList<>();
        int[] current = new int[2];
        boolean leftTop = true, leftBottom = true, rightTop = true, rightBottom = true;

        for(int i = 1; i<8; i++){

            if(leftTop) {
                current[0] = pos[0] - i;
                current[1] = pos[1] - i;

                leftTop = checkTileForMove(current);
            }

            if(rightBottom) {
                current[0] = pos[0] + i;
                current[1] = pos[1] + i;

                rightBottom = checkTileForMove(current);
            }

            if(rightTop){
                current[0] = pos[0] - i;
                current[1] = pos[1] + i;

                rightTop = checkTileForMove(current);
            }

           if(leftBottom){
               current[0] = pos[0] + i;
               current[1] = pos[1] - i;

               leftBottom = checkTileForMove(current);
           }

        }

    }

    protected boolean checkTileForMove(int[] tilePosition){
        Pair<Boolean, Tile> res = getMove(tilePosition);

        if(res.second != null){
            moves.add(res.second);
        }

        return res.first;
    }

    /**
     * Determines whether or not a specific tile is a valid tile to land on
     * This means:
     *      The tile is in the board
     *      The tile is not occupied by a piece of the same color as this piece
     *
     * @param current The current indices of the tile being investigated
     * @return A pair - boolean indicates if there is a piece on the tile. Tile is the object
     *                  of the tile as the specified index. Only assigned if the boolean is true
     *
     */
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
        return new Pair<>(ret, tile);
    }


    public void addNormalMove(int row, int col)
    {
        if(!board.getTile(row, col).getPiece().isPresent())
        {
            moves.add(board.getTile(row, col));
        }
    }

    public void addCaptures(int row, int col, Color oppColor)
    {
        if(board.getTile(row, col).getPiece().isPresent())
        {
            if(board.getTile(row, col).getPiece().get().getColor() == oppColor)
            {
                moves.add(board.getTile(row, col));
            }
        }
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Tile getTile(){
        return this.tile;
    }

    public List<Tile> getMoves() {
        return moves;
    }

    public Color getColor(){
        return this.color;
    }
}
