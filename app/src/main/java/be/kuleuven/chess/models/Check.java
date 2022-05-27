package be.kuleuven.chess.models;

import be.kuleuven.chess.models.pieces.Bishop;
import be.kuleuven.chess.models.pieces.King;
import be.kuleuven.chess.models.pieces.Knight;
import be.kuleuven.chess.models.pieces.Pawn;
import be.kuleuven.chess.models.pieces.Queen;
import be.kuleuven.chess.models.pieces.Rook;

public class Check {

    private final Board board;
    private final Color colorToCheck;
    private final int[] position;

    public Check(Color colorToCheck, int[] position) {
        this.colorToCheck = colorToCheck;
        this.position = position;

        board = Board.getBoardObj();
    }

    public boolean checkStraight(){
        boolean left = true, right = true, top = true, bottom = true;
        int[] current = new int[2];

        for(int i = 1; i<8; i++){

            if(left) {
                current[0] = position[0] ;
                current[1] = position[1] - i;

                Piece p = getCheckPiece(current);

                if(p != null){
                    left = false;
                    if (isPieceCheckStraight(colorToCheck, i, p)) return true;
                }
            }

            if(right) {
                current[0] = position[0];
                current[1] = position[1] + i;

                Piece p = getCheckPiece(current);

                if(p != null) {
                    right = false;
                    if (isPieceCheckStraight(colorToCheck, i, p)) return true;
                }
            }

            if(top) {
                current[0] = position[0] - i;
                current[1] = position[1];

                Piece p = getCheckPiece(current);

                if(p != null) {
                    top = false;
                    if (isPieceCheckStraight(colorToCheck, i, p)) return true;
                }
            }

            if(bottom){
                current[0] = position[0] + i;
                current[1] = position[1];

                Piece p = getCheckPiece(current);

                if(p != null) {
                    bottom = false;
                    if (isPieceCheckStraight(colorToCheck, i, p)) return true;
                }
            }

        }

        return false;
    }

    private boolean isPieceCheckStraight(Color color, int i, Piece p) {
        if (p.getColor() != color) {
            if (p instanceof Rook || p instanceof Queen) {
                return true;
            }
            if (p instanceof King && i == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDiagonal(){

        int[] current = new int[2];
        boolean leftTop = true, leftBottom = true, rightTop = true, rightBottom = true;

        for(int i = 1; i<8; i++){

            if(leftTop) {
                current[0] = position[0] - i;
                current[1] = position[1] - i;

                Piece p = getCheckPiece(current);

                if(p != null) {
                    leftTop = false;
                    if (isPieceCheckDiagonal(colorToCheck, i, p, Color.white)) return true;
                }
            }

            if(rightBottom) {
                current[0] = position[0] + i;
                current[1] = position[1] + i;

                Piece p = getCheckPiece(current);

                if(p != null) {
                    rightBottom  = false;
                    if (isPieceCheckDiagonal(colorToCheck, i, p, Color.black)) return true;
                }
            }

            if(rightTop){
                current[0] = position[0] - i;
                current[1] = position[1] + i;

                Piece p = getCheckPiece(current);

                if(p != null) {

                    rightTop = false;

                    if (isPieceCheckDiagonal(colorToCheck, i, p, Color.white)) return true;
                }

            }

            if(leftBottom){
                current[0] = position[0] + i;
                current[1] = position[1] - i;

                Piece p = getCheckPiece(current);

                if(p != null) {
                    leftBottom = false;
                    if (isPieceCheckDiagonal(colorToCheck, i, p, Color.black)) return true;
                }
            }

        }

        return false;
    }

    private boolean isPieceCheckDiagonal(Color color, int i, Piece p, Color white) {
        if (p.getColor() != color) {

            if (p instanceof Bishop || p instanceof Queen) {
                return true;
            }
            if (i == 1 && p instanceof Pawn && color == white) {
                return true;
            }
            if (p instanceof King && i == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean checkKnight(){

        int[] horizontalComb = {-1, 1, 2, 2, -2, -2, -1, 1};
        int[] verticalComb = {-2, -2, -1, 1, -1, 1, 2, 2};

        for (int i = 0; i < 8; i++) {

            int newRow = position[0] + horizontalComb[i];
            int newCol = position[1] + verticalComb[i];

            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {

                if(board.getTile(newRow,newCol).getPiece().isPresent()){
                    Piece piece = board.getTile(newRow,newCol).getPiece().get();

                    if(piece.getColor() != colorToCheck && piece instanceof Knight){
                        return true;
                    }

                }

            }

        }
        return false;
    }

    private Piece getCheckPiece(int[] current) {

        if (current[0] >= 0 && current[1] >= 0 && current[0] < 8 && current[1] < 8) {

            if (board.getTile(current[0], current[1]).getPiece().isPresent()) {
                return board.getTile(current[0], current[1]).getPiece().get();
            }

        }

        return null;
    }
}
