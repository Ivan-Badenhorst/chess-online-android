package be.kuleuven.chess.models;

public class Tile {
    Piece piece;

    public Tile(){
    }

    public void addPiece(){
        //for testing purpose we declare a fixed piece
        //normally use parameter
        piece = new King();
    }

}
