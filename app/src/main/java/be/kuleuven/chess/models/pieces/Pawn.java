package be.kuleuven.chess.models.pieces;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Piece;

public class Pawn extends Piece {
    private boolean hasMoved;

    public Pawn(Color color, Board board) {
        super(board, color);
    }

    @Override
    public Drawable getImage(Context ctx) {
        Resources r = ctx.getResources();
        Drawable symbol;
        if(color == Color.black) {
            symbol = r.getDrawable(R.drawable.black_pawn);
        }
        else{
            symbol = r.getDrawable(R.drawable.white_pawn);
        }
        return symbol;
    }
    @Override
    public void generateMoves()
    {
        determineTile();
        int[] pos = tile.getPosition();
        moves.clear();

        int[] captureTilesWhite = {pos[0]-1, pos[1]-1, pos[1]+1, pos[0]+1 };
        int[] captureTilesBlack = {pos[0]+1, pos[1]-1, pos[1]+1 };

        if(pos[0] < 7 && pos[0] > 0){
            if(this.color == Color.white)
            {

               if(!board.getTile(pos[0] - 1, pos[1]).getPiece().isPresent())
               {
                    moves.add(board.getTile(pos[0] - 1, pos[1]));
               }

                if(pos[1]!=0) {
                    if (board.getTile(captureTilesWhite[0], captureTilesWhite[1]).getPiece().isPresent()) {
                        if (board.getTile(captureTilesWhite[0], captureTilesWhite[1]).getPiece().get().getColor() == Color.black)
                        {
                            moves.add(board.getTile(captureTilesWhite[0], captureTilesWhite[1]));
                        }

                    }
                }
                if(pos[1]!=7) {
                    if (board.getTile(captureTilesWhite[0], captureTilesWhite[2]).getPiece().isPresent()) {
                        if (board.getTile(captureTilesWhite[0], captureTilesWhite[2]).getPiece().get().getColor() == Color.black) {
                            moves.add(board.getTile(captureTilesWhite[0], captureTilesWhite[2]));
                        }

                    }
                }

            }
            else if (this.color == Color.black)
            {
                if(!board.getTile(pos[0] + 1, pos[1]).getPiece().isPresent())
                {
                    moves.add(board.getTile(pos[0] + 1, pos[1]));
                }

                if(pos[1]!=0) {
                    if (board.getTile(captureTilesWhite[3], captureTilesWhite[1]).getPiece().isPresent()) {
                        if (board.getTile(captureTilesWhite[3], captureTilesWhite[1]).getPiece().get().getColor() == Color.white)
                        {
                            moves.add(board.getTile(captureTilesWhite[3], captureTilesWhite[1]));
                        }

                    }
                }
                if(pos[1]!=7) {
                    if (board.getTile(captureTilesWhite[3], captureTilesWhite[2]).getPiece().isPresent()) {
                        if (board.getTile(captureTilesWhite[3], captureTilesWhite[2]).getPiece().get().getColor() == Color.white) {
                            moves.add(board.getTile(captureTilesWhite[3], captureTilesWhite[2]));
                        }

                    }
                }

            }

        }

    }
}
