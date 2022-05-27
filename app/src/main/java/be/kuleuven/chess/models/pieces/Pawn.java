package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import be.kuleuven.chess.R;
import be.kuleuven.chess.interfaces.HasMoved;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Move;
import be.kuleuven.chess.models.Piece;
import be.kuleuven.chess.models.Tile;

public class Pawn extends Piece implements HasMoved {

    private boolean hasMoved;
    private ArrayList<Tile> enPassant;
    private boolean madeEnPassant;

    public Pawn(Color color) {
        super(color);
        enPassant = null;
        madeEnPassant = false;
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
    public void generateMoves(){
        generateMoves(null);
    }

    public void generateMoves(Move prev)
    {

        moves.clear();
        madeEnPassant = false;

        determineTile();

        int[] pos = tile.getPosition();
        int currentRow = pos[0];
        int currentColumn = pos[1];

        promotion(currentRow);

        if((this.color == Color.white && currentRow == 3) || (this.color == Color.black && currentRow == 4)){
            generateEnPassant(pos);
        }
        else{
            enPassant = null;
        }

        if(enPassant != null){
            addEnPassantMoves(prev);
        }

        addMoves(currentRow, currentColumn);
    }


    private void promotion(int currentRow) {
        if(this.color == Color.white && currentRow == 0){
            tile.addPiece(new Queen(this.color));
        }
        else if(currentRow == 7){
            tile.addPiece(new Queen(this.color));
        }
    }

    private void addEnPassantMoves(Move prev) {
        int vert;

        if(color == Color.white){
            vert = -1;
        }
        else{
            vert = 1;
        }

        if(prev.getFirstTile().equals(enPassant.get(0)) && prev.getSecondTile().equals(enPassant.get(2)) ){

            if(prev.getPiece() instanceof Pawn){
                moves.add(board.getTile(tile.getPosition()[0]+vert , tile.getPosition()[1] -1));
            }

        }
        else if(prev.getFirstTile() == enPassant.get(1) && prev.getSecondTile() == enPassant.get(3)){

            if(prev.getPiece() instanceof Pawn){
                moves.add(board.getTile(tile.getPosition()[0]+vert , tile.getPosition()[1] + 1));
            }

        }
    }

    private void addMoves(int currentRow, int currentColumn) {

        int left = currentColumn-1;
        int right = currentColumn+1;
        int up, doubleUp;
        Color opponentColor;

        if(color == Color.white){
            up = currentRow-1;
            doubleUp = currentRow-2;
            opponentColor = Color.black;
        }
        else{
            up = currentRow+1;
            doubleUp = currentRow+2;
            opponentColor = Color.white;
        }


        if(currentRow < 7 && currentRow > 0){

                addNormalMove(up, currentColumn);

                if (!hasMoved && !board.getTile(up, currentColumn).getPiece().isPresent()) {
                    addNormalMove(doubleUp, currentColumn);
                }

                if (currentColumn != 0) {
                    addCaptures(up, left, opponentColor);
                }

                if (currentColumn != 7) {
                    addCaptures(up, right, opponentColor);
                }
        }
    }


    private void generateEnPassant(int[] pos) {
        /*
    format:
    [0] - left top/bottom
    [1] - right top/bottom
    [2] - left
    [3] - right
    */

        enPassant = new ArrayList<>(4);
        int vert;
        if(color == Color.white){
            vert = -2;
        }
        else{
            vert = 2;
        }

    //USING 4 IF'S BECAUSE WE ORDER WE ADD IT TO THE ARRAYLIST MATTERS

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

    public void setHasMoved(boolean val){ hasMoved = val;}

    public void setMadeEnPassant(boolean madeEnPassant) { this.madeEnPassant = madeEnPassant;}

    public boolean isMadeEnPassant() { return madeEnPassant;}
}
