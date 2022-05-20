package be.kuleuven.chess.models;

import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//import be.kuleuven.chess.models.SpecialMoves.EnPassant;
import be.kuleuven.chess.models.pieces.King;
import be.kuleuven.chess.models.pieces.Pawn;
import be.kuleuven.chess.models.pieces.Rook;

public class Move {
    private final Board board;
    private final Tile first;
    private final Tile sec;
    private Move previousMove;
    private Piece piece;

    private Piece piece2Undo;
    private Piece piece1Undo;

    private Piece secPiece;

    private boolean hasMoved;

    public Move(Tile first, Tile sec, Move previousMove) {
        this.first = first;
        this.sec = sec;
        this.board = Board.getBoardObj();
        this.previousMove = previousMove;
        if(first.getPiece().isPresent()){
            this.piece = first.getPiece().get();
            hasMoved = false;
        }


    }

    public void setPreviousMove (Move move){
        previousMove = move;
    }

    public void undoMove(){
       first.addPiece(piece1Undo);
        if(piece2Undo != null) {
            sec.addPiece(piece2Undo);
        }
        else {
            sec.removePiece();
        }
        if(piece instanceof Pawn){
            if(((Pawn) piece).isMadeEnPassant()){

                previousMove.getSec().addPiece(previousMove.getPiece());
                ( (Pawn) piece).setMadeEnPassant(false);

            }
        }
        hasMoved = false;

    }

    public boolean makeMove()
    {
        board.calculateMoves(previousMove);

        piece = first.getPiece().get();
        if(sec.getPiece().isPresent()){
            piece2Undo = sec.getPiece().get();
        }
        else{
            piece2Undo = null;
        }
        try{
            piece1Undo = (Piece) piece.clone();
        }catch(CloneNotSupportedException e){
            Log.e("clone fail",e.getMessage());
        }

        hasMoved = false;

        if(piece.getMoves().contains(sec)){
            secPiece = null;

            Log.d("moveClass", "array contains the tile");
            if(!(piece instanceof King || piece instanceof Pawn) ){
                if(sec.getPiece().isPresent()){
                    secPiece = sec.getPiece().get();
                }
                makeBasicMove();
//MAKE THIS BOARD.ISCHECK(COLOR)
                if(board.getKingTile(piece.getColor()).checkCheck(piece.getColor())){
                    //if the move puts the king in check
                    //undo the move!
                    if(secPiece != null){
                        sec.addPiece(secPiece);

                    }
                    else{
                        sec.removePiece();
                    }
                    first.addPiece(piece);
                    hasMoved = false;
                }

                if(piece instanceof Pawn){
                    ( (Pawn) piece).generateMoves(this);
                }else{
                    piece.generateMoves();
                }

            }
            else if(piece instanceof King){///if the piece is a king
                //first check castling + direction
                List<Tile> castlingTiles = ((King) piece).getCastling();

                if(sec.getPosition()[1] < first.getPosition()[1] - 1) {
                    //he is castling to the left
                    makeBasicMove();
                    castlingTiles.get(3).addPiece(castlingTiles.get(0).getPiece().get());
                    castlingTiles.get(0).removePiece();

                }
                else if(sec.getPosition()[1] > first.getPosition()[1] + 1){
                    //he is castling to the right
                    makeBasicMove();
                    castlingTiles.get(4).addPiece(castlingTiles.get(0).getPiece().get());
                    castlingTiles.get(6).removePiece();

                }
                else if(piece.moves.contains(sec)){ //this is regular moves for king
                    if(sec.getPiece().isPresent()){
                        secPiece = sec.getPiece().get();
                    }
                    makeBasicMove();

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
                        hasMoved = false;
                    }
                }


            } else{ //piece is pawn and move is inside the array of the pawn
                if(sec.getPosition()[1] != first.getPosition()[1] && !sec.getPiece().isPresent()){
                    //we know the move is en passant
                    makeBasicMove();
                    secPiece = previousMove.getSec().getPiece().get();
                    previousMove.getSec().removePiece();
                    ( (Pawn) piece).setMadeEnPassant(true);


                    if(board.getKingTile(piece.getColor()).checkCheck(piece.getColor())){

                        first.addPiece(piece);
                        sec.removePiece();
                        previousMove.getSec().addPiece(secPiece);
                        hasMoved = false;
                        ( (Pawn) piece).setMadeEnPassant(false);
                    }

                }
                else {
                    if(sec.getPiece().isPresent()){
                        secPiece = sec.getPiece().get();
                    }

                    makeBasicMove();
                    Log.d("moveClass", "we made the move");
                    if(board.getKingTile(piece.getColor()).checkCheck(piece.getColor())){
                        //if the move puts the king in check
                        //undo the move!

                        Log.d("moveClass", "it is check");
                        if(secPiece != null){
                            sec.addPiece(secPiece);

                        }
                        else{
                            sec.removePiece();
                        }
                        first.addPiece(piece);
                        hasMoved = false;

                    }
                }
            }
        }



        if(hasMoved){
            if(piece instanceof King) {
                ((King) piece).setHasMoved(true);
            }else if (piece instanceof Rook){
                ((Rook) piece).setHasMoved(true);
            }else if(piece instanceof Pawn){
                ((Pawn) piece).setHasMoved(true);
            }
            //board.calculateMoves(previousMove);
            return true;
        }
        //board.calculateMoves(this);
        return false;


    }

    private void makeBasicMove(){
        sec.addPiece(piece);
        first.removePiece();
        hasMoved = true;
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