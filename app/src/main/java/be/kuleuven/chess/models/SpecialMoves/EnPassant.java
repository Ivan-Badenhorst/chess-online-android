package be.kuleuven.chess.models.SpecialMoves;

import java.util.ArrayList;

import be.kuleuven.chess.models.Move;
import be.kuleuven.chess.models.Piece;
import be.kuleuven.chess.models.Tile;
import be.kuleuven.chess.models.pieces.Pawn;

public class EnPassant {
    private Move move;
    private ArrayList<Tile> eP;
    private Tile tileToCheck;
    private Piece removedPiece;

    public EnPassant(Move move, ArrayList<Tile> eP){
        this.move = move;
        this.eP = eP;
    }

    public boolean isValid(){
        if (eP.subList(0, 2).contains(move.getSec())) {
            //this condition means the pawn is trying to move to a position that could be en pas
            if (!(move.getSec().getPiece().isPresent())) {
                return enPassant(eP);
            }
        }
        return false;
    }

    private boolean enPassant(ArrayList<Tile> tiles){
        int i = tiles.indexOf(move.getSec());
        tileToCheck = tiles.get(i+2);

        if(tileToCheck.getPiece().isPresent()){

            if(tileToCheck.getPiece().get() instanceof Pawn){
                //now we know the square we checking is indeed a pawn. Next we check if the previous move
                //was for this pawn and it was the first move! - check that its not null - first move
                Piece pieceToCheck = tileToCheck.getPiece().get();
                if(move.getPreviousMove().getPiece().equals(pieceToCheck)){
                    //check that the pawn moved two squares!
                    if(pieceToCheck.getColor() != move.getPiece().getColor()){

                        if(move.getPreviousMove().getFirst().getPosition()[0] == 1){
                            return true;
                        }
                        else if(move.getPreviousMove().getFirst().getPosition()[0] == 6){
                            return true;
                        }
                    }
                }
            }
        }

        return false;

    }

    public void complete(){
        move.getSec().addPiece(move.getFirst().getPiece().get());
        move.getFirst().removePiece();
        removedPiece = tileToCheck.getPiece().get();
        tileToCheck.removePiece();
    }

    public void undo(){
        tileToCheck.addPiece(removedPiece);
        move.getFirst().addPiece(move.getPiece());
        move.getSec().removePiece();
    }
}
