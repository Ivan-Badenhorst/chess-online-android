package be.kuleuven.chess.models;

import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chess.models.SpecialMoves.EnPassant;
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

    public Move(Tile first, Tile sec, Move previousMove) {
        this.first = first;
        this.sec = sec;
        this.board = Board.getBoardObj();
        this.previousMove = previousMove;
        if(first.getPiece().isPresent()){
            this.piece = first.getPiece().get();
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

        boolean hasMoved = false;

        /*if(piece instanceof Pawn){

            if(!piece.getMoves().contains(sec)) {
                ArrayList<Tile> eP = ((Pawn) piece).getEnPassant();
                if(eP != null){

                    EnPassant ep = new EnPassant(this, eP);

                    if(ep.isValid()){
                        ep.complete();
                        if(board.getKingTile(piece.getColor()).checkCheck(piece.getColor())){
                            ep.undo();
                        }
                        else{
                            hasMoved = true;
                        }


                    }


                }

            }

        }*/
        if(piece.getMoves().contains(sec)){
            Piece secPiece = null;

            Log.d("moveClass", "array contains the tile");
            if(!(piece instanceof King || piece instanceof Pawn) ){
                if(sec.getPiece().isPresent()){
                    secPiece = sec.getPiece().get();
                }
                sec.addPiece(piece);
                first.removePiece();
                hasMoved = true;

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
                if(piece.moves.contains(sec)){ //this is regular moves for king
                    if(sec.getPiece().isPresent()){
                        secPiece = sec.getPiece().get();
                    }
                    sec.addPiece(piece);
                    first.removePiece();
                    hasMoved = true;

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

                    sec.addPiece(piece);
                    first.removePiece();
                    secPiece = previousMove.getSec().getPiece().get();
                    previousMove.getSec().removePiece();
                    hasMoved = true;

                    if(board.getKingTile(piece.getColor()).checkCheck(piece.getColor())){

                        first.addPiece(piece);
                        sec.removePiece();
                        previousMove.getSec().addPiece(secPiece);
                        hasMoved = false;

                    }

                }
                else {
                    if(sec.getPiece().isPresent()){
                        secPiece = sec.getPiece().get();
                    }

                    sec.addPiece(piece);
                    first.removePiece();
                    hasMoved = true;
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
        }else if(piece instanceof King) //MAKE NEW CLASS FOR CASTLING!!!
        { //this is illegal move - check if castling
            //2 left, 1 left, 1 right, 2 right
            List<Tile> castlingTiles = ((King) piece).getCastlingSquares();
            if(castlingTiles != null && !first.checkCheck(piece.color)){
                //this means list is not empty and the king is not in check

                if(sec.equals(castlingTiles.get(2))){
                    //this means castling to the left
                    if(checkCastlingRook(0, castlingTiles)){
                        //means there is a rook and he hasnt moved
                        if(checkCastling(castlingTiles, 2,3)){
                            //do castling
                            hasMoved = true;
                            sec.addPiece(piece);
                            first.removePiece();
                            castlingTiles.get(3).addPiece(castlingTiles.get(0).getPiece().get());
                            castlingTiles.get(0).removePiece();

                        }
                    }

                }
                else if(sec.equals(castlingTiles.get(5))) {

                    if(checkCastlingRook(6, castlingTiles)){
                        //means there is a rook and he hasnt moved
                        if(checkCastling(castlingTiles, 4,5)){
                            //do castling
                            hasMoved = true;
                            sec.addPiece(piece);
                            first.removePiece();
                            castlingTiles.get(4).addPiece(castlingTiles.get(0).getPiece().get());
                            castlingTiles.get(6).removePiece();

                        }
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
            board.calculateMoves(previousMove);
            return true;
        }
        board.calculateMoves(this);
        return false;


    }

    public boolean checkCastlingRook(int i, List<Tile> ls){
        if(ls.get(0).getPiece().isPresent()){
            //means there is a piece on the left most square
            Piece pc = ls.get(0).getPiece().get();

            if(pc instanceof Rook){
                //the piece is a rook
                if( !( (Rook) pc).hasMoved()){
                    //the rook hasn't moved either
                    return true;

                }

            }

        }
        return false;
    }

    private boolean checkCastling(List<Tile> ls, int i1, int i2){
        //used to check the two squares to the left
        if(i1 > i2){
            int t = i1;
            i1 = i2;
            i2 = t;
        }
        //i1 is always the smaller one of the two

        for(int i = i1; i<=i2; i++){
            if(ls.get(i).getPiece().isPresent()) {
                return false;
            }
            if(ls.get(i).checkCheck(piece.getColor())){
                return false;
            }
        }
        return true;//if the two squares are empty and are not attacked by the enemy


    }

    public boolean isFirstPresent(){
        return first.getPiece().isPresent();
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
