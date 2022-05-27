package be.kuleuven.chess.models.SpecialMoves;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;
import be.kuleuven.chess.models.Tile;
import be.kuleuven.chess.models.pieces.Rook;

public class Castling {

    private final Board board;

    private final int[] position;
    private final boolean hasMoved;
    private final Color color;


    public Castling(int[] position, boolean hasMoved, Color color){
        board = Board.getBoardObj();
        this.hasMoved = hasMoved;
        this.position = position;
        this.color = color;
    }


    public boolean castlingPossible(Tile rookTile, boolean left){
        return castlingRowCheck(left) && checkCastlingRook(rookTile);
    }

    private boolean castlingRowCheck(boolean left){
        int value;
        if (left){
            value = -1;
        }
        else{
            value = 1;
        }

        for(int i = 1; i < 3; i++){

            Tile tileToCheck = board.getTile(position[0], position[1] + value*i);

            if(tileToCheck.getPiece().isPresent()){
                return false;
            }
            else if(tileToCheck.checkCheck(color)){
                return false;
            }
        }

        if(left){
            Tile tileToCheck = board.getTile(position[0], position[1] + value*3);
            if(tileToCheck.getPiece().isPresent()){
                return false;
            }
        }

        return true;

    }

    public boolean checkCastlingRook(Tile rookTile){
        if(rookTile.getPiece().isPresent()){

            Piece pc = rookTile.getPiece().get();
            if(pc instanceof Rook){

                if( !( (Rook) pc).hasMoved()){
                    return true;

                }

            }

        }
        return false;
    }

    public List<Tile> getCastlingSquares(){
        /*
        send array in the format:
        4 left, 3 left, 2 left, 1 left, 1 right, 2 right, 3 right
         */

        int[] current = new int[2];
        List<Tile> castlingSquares = new ArrayList<>();
        boolean empty = true;

        current[0] = position[0];

        for(int i = -4; i<5; i++){
            if(hasMoved){
                return null;
            }
            if(i != 0){
                current[1] = position[1] + i;
                if(current[1] >= 0 && current[1] <8){
                    empty = false;
                    castlingSquares.add(board.getTile(current[0], current[1]));
                }

            }
        }
        if(empty){
            return null;
        }
        return castlingSquares;

    }
}
