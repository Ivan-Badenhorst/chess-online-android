package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Move;
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


    private int counter = 0;
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
        int currentRank = pos[0];
        int currentFile = pos[1];

        //pawn promotes to queen automatically
        if(this.color == Color.white && currentRank == 0){
            tile.addPiece(new Queen(this.color));
        }
        else if(currentRank == 7){
            tile.addPiece(new Queen(this.color));
        }


        if((this.color == Color.white && currentRank == 3) || (this.color == Color.black && currentRank == 4)){
            generateEnPassant(pos);
        }
        else{
            enPassant = null;
        }


        if(enPassant != null){

            //check if prev move, sec tile is positione 1 or 2 in the array
            //check if the previous move as a pawn
            //check if it was a double move
            int vert;


            counter ++;

            if(counter == 18){
                System.out.println("yo");
                System.out.println("how about here");
            }

            if(color == Color.white){
                vert = -1;
            }
            else{
                vert = 1;
            }


            if(prev.getFirst().equals(enPassant.get(0)) && prev.getSec().equals(enPassant.get(2)) ){

                if(prev.getPiece() instanceof Pawn){
                    moves.add(board.getTile(tile.getPosition()[0]+vert , tile.getPosition()[1] -1));
                }

            }
            else if(prev.getFirst() == enPassant.get(1) && prev.getSec() == enPassant.get(3)){

                if(prev.getPiece() instanceof Pawn){
                    moves.add(board.getTile(tile.getPosition()[0]+vert , tile.getPosition()[1] + 1));
                }

            }

        }



        int up = pos[0]-1;
        int doubleUp = pos[0]-2;
        int down = pos[0]+1;
        int doubleDown = pos[0]+2;
        int left = pos[1]-1;
        int right = pos[1]+1;


        if(currentRank < 7 && currentRank > 0){

            if(this.color == Color.white) {





                addNormalMove(up, currentFile);

                if (!hasMoved && !board.getTile(up, currentFile).getPiece().isPresent()) {
                    addNormalMove(doubleUp, currentFile);
                }



                //capture diagonal left
                if (currentFile != 0) {
                    addCaptures(up, left, Color.black);
                }
                //capture diagonal right
                if (currentFile != 7) {
                    addCaptures(up, right, Color.black);
                }






            }
            else if (this.color == Color.black)
            {

                addNormalMove(down, currentFile);

                if (!hasMoved && !board.getTile(down, currentFile).getPiece().isPresent()) {
                    addNormalMove(doubleDown, currentFile);
                }


                if(currentFile!=0) {
                    addCaptures(down, left, Color.white);
                }

                if(currentFile!=7) {
                    addCaptures(down, right, Color.white);
                }
            }
        }
    }



    private void generateEnPassant(int[] pos) {
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

    public void setHasMoved(boolean val){
        hasMoved = val;
    }

    public ArrayList<Tile> getEnPassant(){
        return enPassant;
    }

    public void setMadeEnPassant(boolean madeEnPassant) {
        this.madeEnPassant = madeEnPassant;
    }

    public boolean isMadeEnPassant() {
        return madeEnPassant;
    }
}
