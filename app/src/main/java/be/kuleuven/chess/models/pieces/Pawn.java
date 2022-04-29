package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;
import be.kuleuven.chess.models.Tile;

public class Pawn extends Piece {
    private boolean hasMoved;
    private ArrayList<Tile> enPassant; /*
    format:
    [0] - left top/bottom
    [1] - right top/bottom
    [2] - left
    [3] - right
    */

    public Pawn(Color color, Board board) {
        super(board, color);
        enPassant = null;
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

        //pawn promotes to queen automatically
        if(this.color == Color.white && pos[0] == 0){
            tile.addPiece(new Queen(this.color, this.board));
        }
        else if(pos[0] == 7){
            tile.addPiece(new Queen(this.color, this.board));
        }

        if((this.color == Color.white && pos[0] == 3) || pos[0] == 4){
            generateEnPassant(pos);
        }
        else{
            enPassant = null;
        }

        moves.clear();

        int[] captureTilesWhite = {pos[0]-1, pos[1]-1, pos[1]+1, pos[0]+1 };

        if(pos[0] < 7 && pos[0] > 0){
            if(this.color == Color.white) {

                if (!board.getTile(pos[0] - 1, pos[1]).getPiece().isPresent()) {
                    moves.add(board.getTile(pos[0] - 1, pos[1]));
                }

                if (pos[1] != 0) {
                    if (board.getTile(captureTilesWhite[0], captureTilesWhite[1]).getPiece().isPresent()) {
                        if (board.getTile(captureTilesWhite[0], captureTilesWhite[1]).getPiece().get().getColor() == Color.black) {
                            moves.add(board.getTile(captureTilesWhite[0], captureTilesWhite[1]));
                        }

                    }
                }
                if (pos[1] != 7) {
                    if (board.getTile(captureTilesWhite[0], captureTilesWhite[2]).getPiece().isPresent()) {
                        if (board.getTile(captureTilesWhite[0], captureTilesWhite[2]).getPiece().get().getColor() == Color.black) {
                            moves.add(board.getTile(captureTilesWhite[0], captureTilesWhite[2]));
                        }

                    }
                }

                if (!board.getTile(pos[0] - 1, pos[1]).getPiece().isPresent()) {
                    moves.add(board.getTile(pos[0] - 1, pos[1]));

                }

                if (!hasMoved) {
                    if(!board.getTile(pos[0] - 2, pos[1]).getPiece().isPresent()) {
                        moves.add(board.getTile(pos[0] - 2, pos[1]));
                    }
                }

            }
            else if (this.color == Color.black)
            {
                if(!board.getTile(pos[0] + 1, pos[1]).getPiece().isPresent())
                {
                    moves.add(board.getTile(pos[0] + 1, pos[1]));
                }
                if(pos[1]!=0) {
                    if (board.getTile(captureTilesWhite[3], captureTilesWhite[1]).getPiece().isPresent()) {
                        if (board.getTile(captureTilesWhite[3], captureTilesWhite[1]).getPiece().get().getColor() == Color.white)
                        {
                            moves.add(board.getTile(captureTilesWhite[3], captureTilesWhite[1]));
                        }

                    }
                }
                if (!board.getTile(pos[0] + 1, pos[1]).getPiece().isPresent()) {
                    moves.add(board.getTile(pos[0] + 1, pos[1]));
                }
                if (!hasMoved) {
                    if(!board.getTile(pos[0] + 2, pos[1]).getPiece().isPresent()) {
                        moves.add(board.getTile(pos[0] + 2, pos[1]));
                    }
                }
                if(pos[1]!=7) {
                    if (board.getTile(captureTilesWhite[3], captureTilesWhite[2]).getPiece().isPresent()) {
                        if (board.getTile(captureTilesWhite[3], captureTilesWhite[2]).getPiece().get().getColor() == Color.white) {
                            moves.add(board.getTile(captureTilesWhite[3], captureTilesWhite[2]));
                        }

                    }
                }

            }

        }

    }

    private void generateEnPassant(int[] pos) {
        enPassant = new ArrayList<>(4);
        int vert = 0;
        if(color == Color.white){
            vert = -1;
        }
        else{
            vert = 1;
        }
        /*
    USING 4 IF'S BECAUSE WE ORDER WE ADD IT TO THE ARRAYLIST MATTERS
    */
        if(pos[1] >0){
            enPassant.add( board.getTile(pos[0] + vert, pos[1] - 1));
        }else{
            enPassant.add(null);
        }
        if(pos[1] <7){
            enPassant.add(board.getTile(pos[0] + vert, pos[1] + 1));
        }else{
            enPassant.add(null);
        }
        if(pos[1] >0){
            enPassant.add(board.getTile(pos[0], pos[1] - 1));
        }else{
            enPassant.add(null);
        }
        if(pos[1] <7){
            enPassant.add(board.getTile(pos[0], pos[1] + 1));
        }else{
            enPassant.add(null);
        }

    }

    public void setHasMoved(boolean val){
        hasMoved = val;
    }

    public ArrayList<Tile> getEnPassant(){
        return enPassant;
    }
}
