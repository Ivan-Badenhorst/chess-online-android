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

    private DBConnect db;

    public Game(AppCompatActivity activity, Color myColor, int gameId) {
        board = new Board();
        move = null;
        firstClick = true;

        counterForTesting=0;

        turnColor = Color.white;
        this.activity = activity;//MAYBE FIND BETTER WAY, CHECK COMMENT DB CONNECT CLASS
        db = new DBConnect(activity, board, turnColor, this);
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
            Move prevMov = move;
            counterForTesting += 1;
            if(counterForTesting == 12){
                System.out.println("hi");
            }
            move = new Move(firstTile, board.getTile(row, column), board, prevMov);
            boolean moved = move.makeMove();

            if(moved){
                setClickableAll(false);
                db.addMove(move.getFirst().getPosition()[0], move.getFirst().getPosition()[1], move.getSec().getPosition()[0], move.getSec().getPosition()[1], gameId, myColor);
                /* we have now reached the point where our move is made and we send it to database.
                now we wait until it has been written */
                /*while(!db.getWritten())
                {
                    db.checkMoveWritten(gameId);
                    try{
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e)
                    {
                        Log.e( "Move Writing Wait", e.getMessage(), e );
                    }

                }
                */
            }


        }

    }
    public void myMove(){
        changeColor();
        setClickableAll(true);
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

    private void multipleDeviceGame(){
        boolean gameDone = false;
        while(!gameDone){

            while(db.getMove()== null && firstMove)
            {
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {
                    Log.e( "Move Wait", e.getMessage(), e );
                }
                finally {
                    db.checkPrevMove(gameId);
                }
            }
            firstClick = true;
            //We must make a move before the loop restarts



            // check if its my move
            //if it is, activate something that makes it possible for me to make a move

            Move lMove = db.getMove();
            lMove.makeMove();

            while(turnColor==myColor)
            {
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {
                    Log.e( "Move Wait", e.getMessage(), e );
                }
            }


            //if not:
            //check the database if the last move was mine
            //if yes, wait
            //if no, read the last move and make it

            //also, after each move we should check the enemy for checkmate
            //if yes, game done should be true
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
}
