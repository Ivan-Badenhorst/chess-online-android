package be.kuleuven.chess.models;

import android.annotation.SuppressLint;

import java.util.Optional;
@SuppressLint("NewApi")
public class Game {
    private Board board;
    private Tile firstTile, secondTile;
    private boolean firstClick;
    private Move move;
    private int counterForTesting;
    private Color turnColor;

    public Game() {
        board = new Board();
        move = null;
        firstClick = true;
        counterForTesting=0;
        turnColor = Color.white;
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
}
