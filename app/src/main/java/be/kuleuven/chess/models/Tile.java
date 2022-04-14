package be.kuleuven.chess.models;

import java.util.Optional;

public class Tile {
    Optional<Piece> piece;

    public Tile(){
        piece = Optional.empty();
    }

    public void addPiece(){
        //for testing purpose we declare a fixed piece
        //normally use parameter
        piece = Optional.of(new King());
    }

    public Optional<Piece> getPiece(){
        return piece;
    }

}
