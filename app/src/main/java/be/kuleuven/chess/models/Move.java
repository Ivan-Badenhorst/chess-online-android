package be.kuleuven.chess.models;

import android.util.Log;

import java.util.List;

import be.kuleuven.chess.models.pieces.King;
import be.kuleuven.chess.models.pieces.Pawn;
import be.kuleuven.chess.models.pieces.Rook;

public class Move {
    private final Board board;
    private final Tile firstTile;
    private final Tile secondTile;
    private final Move previousMove;

    private Piece piece;
    private Piece piece2Undo;
    private Piece piece1Undo;
    private Piece secPiece;

    private boolean hasMoved;

    public Move(Tile firstTile, Tile secondTile, Move previousMove) {

        this.firstTile = firstTile;
        this.secondTile = secondTile;
        this.board = Board.getBoardObj();
        this.previousMove = previousMove;

        if(firstTile.getPiece().isPresent()){
            this.piece = firstTile.getPiece().get();
            hasMoved = false;
        }

    }


    public boolean makeMove()
    {
        board.calculateMoves(previousMove);

        piece = firstTile.getPiece().get();
        setupForUndo();

        hasMoved = false;

        if(secondTile.getPiece().isPresent()){
            secPiece = secondTile.getPiece().get();
        }
        else{
            secPiece = null;
        }

        if(piece.getMoves().contains(secondTile)){

            if(!(piece instanceof King || piece instanceof Pawn) ){
                normalMove();
            }
            else if(piece instanceof King){
                kingMove();
            } else{
                pawnMove();
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
            return true;
        }
        return false;
    }


    private void setupForUndo(){
        if(secondTile.getPiece().isPresent()){
            piece2Undo = secondTile.getPiece().get();
        }
        else{
            piece2Undo = null;
        }
        try{
            piece1Undo = (Piece) piece.clone();
        }catch(CloneNotSupportedException e){
            Log.e("clone fail",e.getMessage());
        }
    }

    public void undoMove(){

        if(piece2Undo != null){
            secondTile.addPiece(piece2Undo);
        }
        else{
            secondTile.removePiece();
        }
        firstTile.addPiece(piece1Undo);
        hasMoved = false;

        if(piece instanceof Pawn){
            if(((Pawn) piece).isMadeEnPassant()){

                previousMove.getSecondTile().addPiece(previousMove.getPiece());
                ( (Pawn) piece).setMadeEnPassant(false);

            }
        }
        hasMoved = false;

    }


    private void normalMove(){

        makeBasicMove();

        if(board.isCheck(piece.getColor())){
            undoMove();
        }
    }

    private void makeBasicMove(){
        secondTile.addPiece(piece);
        firstTile.removePiece();
        hasMoved = true;
    }

    private void kingMove(){

        if(secondTile.getPosition()[1] < firstTile.getPosition()[1] - 1) {
            castlingLeft();
        }
        else if(secondTile.getPosition()[1] > firstTile.getPosition()[1] + 1){
            castlingRight();
        }
        else if(piece.moves.contains(secondTile)){
            kingBasicMove();
        }
    }

    private void castlingLeft(){
        List<Tile> castlingTiles = ((King) piece).getCastling();
        makeBasicMove();
        castlingTiles.get(3).addPiece(castlingTiles.get(0).getPiece().get());
        castlingTiles.get(0).removePiece();
    }

    private void castlingRight(){
        List<Tile> castlingTiles = ((King) piece).getCastling();
        makeBasicMove();
        castlingTiles.get(4).addPiece(castlingTiles.get(0).getPiece().get());
        castlingTiles.get(6).removePiece();
    }

    private void kingBasicMove(){

        makeBasicMove();

        if(secondTile.checkCheck(piece.getColor())){
            undoMove();
        }
    }


    private void pawnMove(){

        if(secondTile.getPosition()[1] != firstTile.getPosition()[1] && !secondTile.getPiece().isPresent()){
            enPassant();
        }
        else {
            makeBasicMove();
            if(board.isCheck(piece.getColor())){
                undoMove();
            }
        }

    }

    private void enPassant(){

        makeBasicMove();
        secPiece = previousMove.getSecondTile().getPiece().get();
        previousMove.getSecondTile().removePiece();
        ( (Pawn) piece).setMadeEnPassant(true);

        if(board.isCheck(piece.getColor())){

            firstTile.addPiece(piece);
            secondTile.removePiece();
            hasMoved = false;

            previousMove.getSecondTile().addPiece(secPiece);
            ( (Pawn) piece).setMadeEnPassant(false);
        }
    }


    public Piece getPiece() {
        return this.piece;
    }

    public Tile getFirstTile(){
        return this.firstTile;
    }

    public Tile getSecondTile() {
        return secondTile;
    }
}