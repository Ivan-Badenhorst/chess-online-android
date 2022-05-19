package be.kuleuven.chess.models;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.chess.activities.MainActivity;

public class DBGame {

    private RequestQueue requestQueue;
    private AppCompatActivity activity;
    private Board board;
    private Move lMove;
    private String color;
    private Game game;


    public DBGame(AppCompatActivity activity, Color color, Game game){
        this.activity = activity;
        this.board = Board.getBoardObj();
        lMove = null;
        this.game = game;
        if(color == Color.white){
            this.color = "white";
        }
        else{
            this.color = "black";
        }
    }

    public void readMove(int idGame) //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);    //OR MAYBE JUST MAKE A REQUEST CUE
        // IN THE ACTIVITY AND GIVE IT HERE
        String requestURL = "https://studev.groept.be/api/a21pt402/readMove/" +  idGame;
        lMove = null;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                        try {

                            for( int i = 0; i < response.length(); i++ )
                            {
                                JSONObject curObject = response.getJSONObject( i );


                                Tile first = board.getTile(curObject.getInt("fRow"), curObject.getInt("fCol"));
                                Tile sec = board.getTile(curObject.getInt("sRow"), curObject.getInt("sCol"));
                                lMove = new Move(first, sec, game.getMove());

                                if(lMove.isFirstPresent()){
                                    //call method to make enemy move
                                    lMove.makeMove();
                                    game.setPrevMov(lMove);
                                    //make it possible for us to move again!!
                                    game.myMove();
                                }
                                else{

                                    try{
                                        Thread.sleep(1000);
                                    } catch(InterruptedException e){
                                        Log.e("readMove error", e.getMessage());
                                    }

                                    readStatus(idGame);
                                }

                            }
                            if (response.length() == 0){
                                try{
                                    Thread.sleep(1000);
                                } catch(InterruptedException e){
                                    Log.e("readMove error", e.getMessage());
                                }
                                readMove(idGame);
                            }

                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database readMove", e.getMessage(), e );

                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e( "readMove Volley", error.getMessage());
                    }
                }
        );

        requestQueue.add(submitRequest);
    }


    //readStatus


    public void readStatus(int idGame) //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);    //OR MAYBE JUST MAKE A REQUEST CUE
        // IN THE ACTIVITY AND GIVE IT HERE
        String requestURL = "https://studev.groept.be/api/a21pt402/getStatus/" +  idGame;
        //lMove = null;



        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    Log.d("readMove", "resp");
                    try {

                        for( int i = 0; i < response.length(); i++ )
                        {
                            JSONObject curObject = response.getJSONObject( i );
                            int resigned = curObject.getInt("status");

                            if(resigned == 1){
                                //do something
                                //for now as a test I remove pieces from the board!!!
                                game.resigned();//removes half the board

                                //maybe have to do some more?
                                //or just actually call a method that stops everything and
                                //displays whats needed
                            }
                            else{
                                readMove(idGame);
                            }
                            //HAVE TO SET THE PREVIOUS MOVE IN THE GAME CLASS WHEN WE GET THIS!!
                        }

                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    public void addMove(int frow, int fcol, int srow, int scol, int gameId, Color color) //View v )
    {
        Log.d("addMove", "got here");
        requestQueue = Volley.newRequestQueue(activity);
        String col = color.name();
        String requestURL = "https://studev.groept.be/api/a21pt402/createMove/"+ frow+"/"+fcol+"/"+ srow+"/"+scol+ "/"+ gameId+"/"+col;
        //this will then add me to the game
        Log.d("addMove", "got here 2");
        Log.d("addMove", requestURL);
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {

                        game.changeColor();
                        readMove(gameId);

                    }
                },

                error -> Log.d("addMove", "error")
        );

        requestQueue.add(submitRequest);
    }



}


