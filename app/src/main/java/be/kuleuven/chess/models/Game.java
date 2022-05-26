package be.kuleuven.chess.models;

import android.annotation.SuppressLint;

import java.util.Optional;

import be.kuleuven.chess.activities.MainActivity;
import be.kuleuven.chess.models.Database.DBGame;

@SuppressLint("NewApi")
public class Game {

    private final MainActivity activity;
    private final DBGame db;
    private final Board board;

    private Move move;
    private Move prevMov;
    private Tile firstTile;

    private boolean firstClick;
    private Color turnColor;
    private final Color myColor;
    private final Color opponentColor;
    private final int gameId;


    private int counter = 0;


    public Game(MainActivity activity, Color myColor, int gameId) {
        this.myColor = myColor;
        this.gameId = gameId;
        this.activity = activity;

        board = Board.getBoardObj();
        board.generateBoard();
        move = null;
        firstClick = true;
        turnColor = Color.white;
        db = new DBGame(activity, turnColor, this);

        if(myColor == Color.black){
            opponentColor = Color.white;
            activity.setClickable(false);
            db.readMove(gameId);
        }
        else{
            opponentColor = Color.black;
            activity.setClickable(true);
        }

    }


    public void addClick(int row, int column){

        if (firstClick){
            setupFirstTile(row, column);
        }
        else{
            firstClick = true;
            move = new Move(firstTile, board.getTile(row, column), prevMov);

            if(move.makeMove()){
                counter++;
                if(counter == 4){
                    System.out.println("lol");
                }

                activity.setClickable(false);
                checkOpponentMate();

                db.addMove(move.getFirstTile().getPosition()[0], move.getFirstTile().getPosition()[1], move.getSecondTile().getPosition()[0], move.getSecondTile().getPosition()[1], gameId, myColor);
            }
        }

    }

    public void setupFirstTile(int row, int column){
        Optional<Piece> first = board.getTile(row, column).getPiece();

        if(first.isPresent()){
            firstTile = board.getTile(row, column);
            if(firstTile.getPiece().get().getColor() == turnColor){
                firstClick = false;
            }
        }

    }

    public void checkOpponentMate(){

        if(noPossibleMoves(opponentColor)){

            if(board.isCheck(opponentColor)){
                checkmate(true);
            }
            else{
                activity.gameOver("Stalemate! It's a draw");
            }

        }

    }


    public boolean noPossibleMoves(Color color){
        return board.isNoMovePossible(color, prevMov);
    }


    public void myMove(){
        changeColor();
        boolean canClick = true;

        if(noPossibleMoves(myColor)) {
            canClick = false;

            if(board.isCheck(myColor)){
                checkmate(false);
            }
            else{
                stalemate();
            }

        }

        activity.setClickable(canClick);
        activity.display();
    }

    public void changeColor(){

        if(turnColor == Color.white){
            turnColor = Color.black;
        }
        else{
            turnColor = Color.white;
        }

    }


    public void stalemate(){
        activity.gameOver("Stalemate! It's a draw");
    }


    public void checkmate(boolean won){
        activity.gameOver(won);
        activity.display();
    }

    public Board getBoard() { return board;}

    public Move getMove(){ return move;}

    public void setPrevMov(Move lMove){ prevMov = lMove;}
}
