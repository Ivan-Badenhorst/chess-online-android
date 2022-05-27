package be.kuleuven.chess.models.Database;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.chess.activities.MainActivity;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Game;
import be.kuleuven.chess.models.Move;
import be.kuleuven.chess.models.Tile;

public class DBGame {

    private RequestQueue requestQueue;
    private final MainActivity activity;
    private final Board board;
    private Move lMove;
    private final Game game;


    public DBGame(MainActivity activity, Game game){
        this.activity = activity;
        this.board = Board.getBoardObj();
        this.game = game;
        lMove = null;
    }

    public void readMove(int idGame)
    {
        requestQueue = Volley.newRequestQueue(activity);
        String requestURL = "https://studev.groept.be/api/a21pt402/readMove/" +  idGame;
        lMove = null;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {

                    try {

                        for( int i = 0; i < response.length(); i++ )
                        {
                            JSONObject curObject = response.getJSONObject( i );


                            Tile first = board.getTile(curObject.getInt("fRow"), curObject.getInt("fCol"));
                            Tile sec = board.getTile(curObject.getInt("sRow"), curObject.getInt("sCol"));
                            lMove = new Move(first, sec, game.getMove());

                            if(lMove.getPiece() != null){
                                lMove.makeMove();
                                game.setPrevMov(lMove);
                                game.myMove();
                            }
                            else{
                                waitSecond();
                                readStatus(idGame);
                            }

                        }

                        if (response.length() == 0){
                            waitSecond();
                            readMove(idGame);
                        }
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database readMove", e.getMessage(), e );

                    }
                },

                error -> Log.e( "readMove Volley", error.getMessage())
        );

        requestQueue.add(submitRequest);
    }

    private void waitSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e("readMove error", e.getMessage());
        }
    }


    public void readStatus(int idGame)
    {
        requestQueue = Volley.newRequestQueue(activity);
        String requestURL = "https://studev.groept.be/api/a21pt402/getStatus/" +  idGame;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {

                        for( int i = 0; i < response.length(); i++ )
                        {
                            JSONObject curObject = response.getJSONObject( i );
                            int resigned = curObject.getInt("status");

                            if(resigned == 1){

                                activity.gameOver("Opponent resigned!");
                            }
                            else{
                                readMove(idGame);
                            }
                        }

                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },

                error -> Log.e( "readMove Volley", error.getMessage())
        );

        requestQueue.add(submitRequest);
    }

    public void addMove(int firstRow, int firstColumn, int secondRow, int secondColumn, int gameId, Color color)
    {

        requestQueue = Volley.newRequestQueue(activity);
        String col = color.name();
        String requestURL = "https://studev.groept.be/api/a21pt402/createMove/"+ firstRow+"/"+firstColumn+"/"+ secondRow+"/"+secondColumn+ "/"+ gameId+"/"+col;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    game.changeColor();
                    readMove(gameId);
                },

                error -> Log.d("addMove", "error")
        );

        requestQueue.add(submitRequest);
    }

}


