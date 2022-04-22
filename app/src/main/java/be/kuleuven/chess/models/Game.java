package be.kuleuven.chess.models;

import android.annotation.SuppressLint;

import java.util.Optional;
@SuppressLint("NewApi")
public class Game {
    Board board;
    Tile firstTile, secondTile;
    boolean firstClick;
    Move move;

    public Game() {
        board = new Board();
        move = null;
        firstClick = true;
    }

    public Board getBoard() {
        return board;
    }


    public void addClick(int row, int column){
        Optional<Piece> first = board.getTile(row, column).getPiece();
        if (firstClick){
            if(first.isPresent()){
                firstTile = board.getTile(row, column);
                firstClick = false;
            }
        }
        else{
            firstClick = true;
            Move prevMov = move;
            move = new Move(firstTile, board.getTile(row, column), board, prevMov);
            move.makeMove();
        }

    }
}
