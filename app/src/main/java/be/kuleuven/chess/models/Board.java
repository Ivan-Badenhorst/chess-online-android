package be.kuleuven.chess.models;

import java.util.Arrays;
import java.util.stream.Stream;

public class Board {
    private Tile[][] board;

    public Board(){
        board = new Tile[8][8];
        generateBoard();
    }

    private void generateBoard(){
        for(int i =0; i<board.length; i++){
            for(int j = 0; j<board[0].length; j++){
                board[i][j] = new Tile();
            }
        }

        board[7][7].addPiece();
    }
}
