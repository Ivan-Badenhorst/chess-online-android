package be.kuleuven.chess.models;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Optional;

import be.kuleuven.chess.R;
import be.kuleuven.chess.activities.MainActivity;

@SuppressLint("NewApi")
public class Game {
    private Board board;
    private Tile firstTile, secondTile;
    private boolean firstClick;
    private Move move;
    private int counterForTesting;
    private Color turnColor;
    private AppCompatActivity activity;
    private Color myColor;
    private int gameId;
    private boolean firstMove;
    private Move prevMov;

    private DBGame db;

    public Game(AppCompatActivity activity, Color myColor, int gameId) {
        board = Board.getBoardObj();
        board.generateBoard();
        move = null;
        firstClick = true;

        counterForTesting = 0;

        turnColor = Color.white;
        this.activity = activity;//MAYBE FIND BETTER WAY, CHECK COMMENT DB CONNECT CLASS
        db = new DBGame(activity, turnColor, this);
        //initialize db correctly
        //somewhere in this class we'll need to create a loop that runs
        firstMove=false;

        this.myColor = myColor;
        this.gameId = gameId;

        if(myColor == Color.black){
            setClickableAll(false);
            //go look in database for the next move
            db.readMove(gameId);
        }
        else{
            setClickableAll(true);
        }

    }

    public Board getBoard() {
        return board;
    }


    public void addClick(int row, int column){
        Optional<Piece> first = board.getTile(row, column).getPiece();
        if (firstClick){
            if(first.isPresent()){
                firstTile = board.getTile(row, column);
                if(firstTile.getPiece().get().getColor() == turnColor){
                    firstClick = false;
                }
            }
        }
        else{
            firstClick = true;
            counterForTesting += 1;

            if(counterForTesting == 3){
                Log.d("here", "makingmove");
            }

            move = new Move(firstTile, board.getTile(row, column), prevMov);
            boolean moved = move.makeMove();

            if(moved){
                setClickableAll(false);
                //check cm for oponent
                Color opCol;
                if(myColor == Color.white){
                    opCol = Color.black;
                }
                else{
                    opCol = Color.white;
                }

                if(board.getKingTile(opCol).checkCheck(opCol)){
                    if(isCheckMate(opCol)){
                        quickCMaction();
                    };
                }


                db.addMove(move.getFirst().getPosition()[0], move.getFirst().getPosition()[1], move.getSec().getPosition()[0], move.getSec().getPosition()[1], gameId, myColor);
            }

        }

    }
    public void myMove(){
        changeColor();
        if(board.getKingTile(myColor).checkCheck(myColor)){
            if(isCheckMate(myColor)){
                //do something useful! - call a method
                quickCMaction(); //removes quarter of the board
            }
        }
        else {
            setClickableAll(true);
            if (board.getKingTile(myColor).checkCheck(myColor)) {
                if (isCheckMate(myColor)) {
                    Log.d("checkMate", "check worked");
                }
            }
        }
        display();

    }
    private void display(){
        if(myColor == Color.white){
            ( (MainActivity) activity).display(0);
        }
        else
        {
            ( (MainActivity) activity).display(7);
        }
    }

    private void setClickableAll(boolean val){
        TableLayout tableLayout= activity.findViewById(R.id.gdBoard);
        for(int i = 0; i<8; i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for(int j = 0; j<8; j++){
                ImageView imageView =  (ImageView) row.getChildAt(j);
                           imageView.setClickable(val);

            }

        }
    }

    public void changeColor(){
        if(turnColor == Color.white){
            turnColor = Color.black;
        }
        else{
            turnColor = Color.white;
        }
    }

    public Move getMove(){
        return move;
    }

    public void setPrevMov(Move lMove){
        prevMov = lMove;
    }

    public boolean isCheckMate(Color c){

        for(int i = 0; i<8; i++){
            for(int j =0; j<8; j++){
                if(i == 6 && j ==0){
                    System.out.println();
                }
                Tile tile1 = board.getTile(i,j);

                if(tile1.getPiece().isPresent()){

                    Piece p = tile1.getPiece().get();

                    if(p.getColor() == c){

                        //for a move we need: prev mov, tile 1, tile 2

                        for(int k =0; k<p.getMoves().size(); k++){
                            Tile tile2 = p.getMoves().get(k);
                            Move moveTry = new Move(tile1, tile2, prevMov);
                            boolean possible = moveTry.makeMove();
                            if(possible){
                                //undo move
                                moveTry.undoMove();
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }




    public void resigned(){//possbily delete tyhis!!
        for(int i = 0; i<8; i++){
            for(int j =0; j<5; j++){

                board.getTile(i,j).removePiece();

            }
        }
        display();
    }


    public void quickCMaction(){
        for(int i = 0; i<5; i++){
            for(int j =0; j<5; j++){

                board.getTile(i,j).removePiece();

            }
        }
        display();
    }
}
