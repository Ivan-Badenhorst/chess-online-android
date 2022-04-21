package be.kuleuven.chess.models;

public class Move {
    Board board;
    Tile first;
    Tile sec;

    public Move(Tile first, Tile sec) {
        this.first = first;
        this.sec = sec;
    }

    public void makeMove()
    {
        Piece piece = first.getPiece().get();
        if(piece.getMoves().contains(sec)){
            sec.addPiece(piece);
            first.removePiece();
            piece.generateMoves();
        }

    }
}
