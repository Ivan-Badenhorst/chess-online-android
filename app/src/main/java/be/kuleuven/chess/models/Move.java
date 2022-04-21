package be.kuleuven.chess.models;

public class Move {
    Board board;
    Tile first;
    Tile sec;

    public Move(Tile first, Tile sec, Board board) {
        this.first = first;
        this.sec = sec;
        this.board = board;
    }

    public void makeMove()
    {
        Piece piece = first.getPiece().get();
        if(piece.getMoves().contains(sec)){
            sec.addPiece(piece);
            first.removePiece();
            piece.generateMoves();
            board.calculateMoves();
        }

    }
}
