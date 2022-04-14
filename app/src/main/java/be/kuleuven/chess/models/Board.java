package be.kuleuven.chess.models;


public class Board {
    private Tile[][] board;

    public Board(){
        board = new Tile[8][8];
        generateBoard();
    }

    public Tile getTile(int row, int column){
        return board[row][column];
    }

    public Tile[][] getBoard(){
        return board;
    }

    private void generateBoard(){
        for(int i =0; i<board.length; i++){
            for(int j = 0; j<board[0].length; j++){
                board[i][j] = new Tile(this);
            }
        }

        board[7][7].addPiece();
    }

}
