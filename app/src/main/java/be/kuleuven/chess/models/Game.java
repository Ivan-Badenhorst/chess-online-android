package be.kuleuven.chess.models;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Optional;
@SuppressLint("NewApi")
public class Game {
    private Board board;
    private Tile firstTile, secondTile;
    private boolean firstClick;
    private Move move;
    private int counterForTesting;
    private Color turnColor;
    private AppCompatActivity activity;

    private DBConnect db;

    public Game(AppCompatActivity activity) {
        board = new Board();
        move = null;
        firstClick = true;
        counterForTesting=0;
        turnColor = Color.white;
        this.activity = activity;//MAYBE FIND BETTER WAY, CHECK COMMENT DB CONNECT CLASS

        //initialize db correctly
        //somewhere in this class we'll need to create a loop that runs

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
                if(turnColor == Color.white){
                    turnColor = Color.black;
                }
                else{
                    turnColor = Color.white;
                }
            }


        }

    }

    private void multipleDeviceGame(){
        boolean gameDone = false;
        while(!gameDone){
            //check if its my move
            //if it is, activate something that makes it possible for me to make a move

            //if not:
            //check the database if the last move was mine
            //if yes, wait
            //if no, read the last move and make it

            //also, after each move we should check the enemy for checkmate
            //if yes, game done should be true
        }
    }
}
