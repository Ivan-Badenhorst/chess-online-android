package be.kuleuven.chess.models;

import java.util.ArrayList;
import be.kuleuven.chess.models.SpecialMoves.EnPassant;
import be.kuleuven.chess.models.pieces.King;
import be.kuleuven.chess.models.pieces.Pawn;

public class Move {
    private final Board board;
    private final Tile first;
    private final Tile sec;
    private final Move previousMove;
    private Piece piece;

    public Move(Tile first, Tile sec, Board board, Move previousMove) {
        this.first = first;
        this.sec = sec;
        this.board = board;
        this.previousMove = previousMove;
    }

    public void makeMove()
    {
        //HAVE TO ADD SOMETHIG THAT CHEKC EN PASSANT FOR CHECK AND UNDOS IT IF NEEDED!!

        boolean hasMoved = false;
        piece = first.getPiece().get();

        if(piece instanceof Pawn){
            ((Pawn) piece).setHasMoved();

            if(!piece.getMoves().contains(sec)) {
                ArrayList<Tile> eP = ((Pawn) piece).getEnPassant();
                if(eP != null){

                    EnPassant ep = new EnPassant(this, eP);

                    if(ep.isValid()){
                        ep.complete();
                        hasMoved = true;
                    }

                }

            }
        }
        if(piece.getMoves().contains(sec) && !hasMoved){
            Piece secPiece = null;


            if(!(piece instanceof King) ){
                if(sec.getPiece().isPresent()){
                    secPiece = sec.getPiece().get();
                }
                sec.addPiece(piece);
                first.removePiece();
                piece.generateMoves();
                /*if(!(board.getKingTile(piece.getColor()).checkCheck(piece.getColor()))){
                    //if the move puts the king in check
                    //undo the move!
                    if(secPiece != null){
                        sec.addPiece(secPiece);
                        first.addPiece(piece);
                    }
                }*/
            }
            else{///if the piece is a king

                if(sec.getPiece().isPresent()){
                    secPiece = sec.getPiece().get();
                }
                sec.addPiece(piece);
                first.removePiece();

                //at this moment we made the move
                //now we check if its okay
                if(sec.checkCheck(piece.getColor())){
                    //if its check we undo the move
                    if(secPiece != null){
                        sec.addPiece(secPiece);
                    }
                    else{
                        sec.removePiece();
                    }
                    first.addPiece(piece);
                }
            }


        }

        board.calculateMoves();

    }



    public Piece getPiece() {
        return this.piece;
    }

    public Tile getFirst(){
        return this.first;
    }

    public Tile getSec() {
        return sec;
    }

    public Move getPreviousMove(){
        return this.previousMove;
    }

}




/*    private boolean enPassant(ArrayList<Tile> tiles){
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

    }*/
/*
    private void enPassant(Tile first, Tile second, Tile take){
        second.addPiece(first.getPiece().get());
        first.removePiece();
        take.removePiece();
    }*/
