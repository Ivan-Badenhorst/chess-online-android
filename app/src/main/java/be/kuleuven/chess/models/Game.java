package be.kuleuven.chess.models;

public class Game {
    Board board;

    public Game() {
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }
}
