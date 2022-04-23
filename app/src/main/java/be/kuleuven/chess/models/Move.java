package be.kuleuven.chess.models;

import java.util.ArrayList;

import be.kuleuven.chess.models.pieces.Pawn;

public class Move {
    private Board board;
    private Tile first;
    private Tile sec;
    private Move previousMove;
    private Piece piece;

    public Move(Tile first, Tile sec, Board board, Move previousMove) {
        this.first = first;
        this.sec = sec;
        this.board = board;
        this.previousMove = previousMove;
    }

    public void makeMove()
    {
        boolean hasMoved = false;
        piece = first.getPiece().get();
        if(piece instanceof Pawn){
            ((Pawn) piece).setHasMoved();

            if(!piece.getMoves().contains(sec)) {
                ArrayList<Tile> eP = ((Pawn) piece).getEnPassant();
                if(eP != null){
                    if (eP.subList(0, 2).contains(sec)) {
                        //this condition means the pawn is trying to move to a position that could be en pas
                        if (!(sec.getPiece().isPresent())) {
                            //can only be en pas if there is no piece on this square!
                            hasMoved = enPassant(eP);
                        }
                    }
                }

            }
        }
        if(piece.getMoves().contains(sec) && !hasMoved){
            sec.addPiece(piece);
            first.removePiece();
            piece.generateMoves();
        }

        board.calculateMoves();

    }

    private boolean enPassant(ArrayList<Tile> tiles){
        int i = tiles.indexOf(sec);
        Tile tileToCheck = tiles.get(i+2);

        if(tileToCheck.getPiece().isPresent()){

            if(tileToCheck.getPiece().get() instanceof Pawn){
                //now we know the square we checking is indeed a pawn. Next we check if the previous move
                //was for this pawn and it was the first move! - check that its not null - first move
                Piece pieceToCheck = tileToCheck.getPiece().get();
                if(previousMove.getPiece().equals(pieceToCheck)){
                    //check that the pawn moved two squares!
                    if(pieceToCheck.getColor() != piece.getColor()){

                        if(previousMove.getFirst().getPosition()[0] == 1){
                            enPassant(first, sec, tileToCheck);
                            return true;
                        }
                        else if(previousMove.getFirst().getPosition()[0] == 6){
                            enPassant(first, sec, tileToCheck);
                            return true;
                        }
                    }
                }
            }
        }

        return false;

    }

    private void enPassant(Tile first, Tile second, Tile take){
        second.addPiece(first.getPiece().get());
        first.removePiece();
        take.removePiece();
    }

    private Piece getPiece() {
        return this.piece;
    }

    private Tile getFirst(){
        return this.first;
    }
}
