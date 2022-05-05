package be.kuleuven.chess.models;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

import be.kuleuven.chess.R;

//IN THE GAME, IF WE READ A MOVE, MAKE SURE WE DONT TRY TO READ AGAIN UNTIL WE HAVE SUCCESSFULLY WROTE OUR MOVE
//THAT MEANS WE GOT A RESPONSE - ADD IN RESPONSE HANDLER OR WRITE
public class DBConnect
{
    private RequestQueue requestQueue;
    private AppCompatActivity activity;
    private Board board;
    private Move lMove;
    private  String color;


    //for starting the game:
    private Color colorPlayer;
    private int gameId;



    public DBConnect(AppCompatActivity activity, Board board, Color color){
        this.activity = activity;
        this.board = board;
        lMove = null;


        if(color == Color.white){
            this.color = "white";
        }
        else{
            this.color = "black";
        }
    }

    public DBConnect(AppCompatActivity activity){
        colorPlayer = null;
        gameId = 0;
    }
    /**
     * Retrieve information from DB with Volley JSONRequest
     */

    private void readMove(int idGame) //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);    //OR MAYBE JUST MAKE A REQUEST CUE
                                                            // IN THE ACTIVITY AND GIVE IT HERE
        String requestURL = "https://studev.groept.be/api/a21pt402/readMove/" +  String.valueOf(idGame);
        lMove = null;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            String responseString = "";
                            for( int i = 0; i < response.length(); i++ )
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                Tile first = board.getTile(curObject.getInt("fRow"), curObject.getInt("fCol"));
                                Tile sec = board.getTile(curObject.getInt("sRow"), curObject.getInt("sCol"));
                                lMove = new Move(first, sec, board, null);
                                //HAVE TO SET THE PREVIOUS MOVE IN THE GAME CLASS WHEN WE GET THIS!!!
                            }

                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
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

    public void checkPrevMove(int idGame) //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);
        String requestURL = "https://studev.groept.be/api/a21pt402/getColorMove/" +  String.valueOf(idGame);
        lMove = null;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            String responseString = "";
                            for( int i = 0; i < response.length(); i++ )
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                if(!curObject.getString("color").equals(color)){
                                    readMove(idGame);
                                }
                                //HAVE TO SET THE PREVIOUS MOVE IN THE GAME CLASS WHEN WE GET THIS!!!
                            }

                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
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

    public void getGame() //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);
        String requestURL = "https://studev.groept.be/api/a21pt402/getGame";


        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            String responseString = "";
                            for( int i = 0; i < response.length(); i++ )
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                if(curObject.getString("playerb") == null && colorPlayer == null){
                                    //this means there is a game that is waiting to be played.

                                    //add myself to the game
                                    addPlayerB(curObject.getInt("idGame")); //player b should somehow tell me that I cans start the game
                                    //once thats done the game starts

                                }
                                else if(colorPlayer == null){
                                    createNewGame(); //should somehow tell me that I can start the game
                                }
                                else if (curObject.getString("playerb") != null && colorPlayer != null){//this means I am white but now I have a second player
                                    gameId = curObject.getInt("idGame");
                                }
                                //HAVE TO SET THE PREVIOUS MOVE IN THE GAME CLASS WHEN WE GET THIS!!!
                            }

                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
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

    public void addPlayerB(int idGame) //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);

        int randomNum = ThreadLocalRandom.current().nextInt(1, 1000000000);

        String requestURL = "https://studev.groept.be/api/a21pt402/addB/" + String.valueOf(randomNum)+ "/" + String.valueOf(idGame);
        //this will then add me to the game
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            //if I have a response, I simply set the values of Color and GameID and the activity class will then know the game is ready
                            JSONObject curObject = response.getJSONObject( 0 );
                            colorPlayer = Color.black;
                            gameId = idGame;
                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
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

    public void createNewGame() //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);

        int randomNum = ThreadLocalRandom.current().nextInt(1, 1000000000);

        String requestURL = "https://studev.groept.be/api/a21pt402/createGame/" + String.valueOf(randomNum);
        //this will then add me to the game
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            //if I have a response, I simply set the values of Color and GameID and the activity class will then know the game is ready
                            JSONObject curObject = response.getJSONObject( 0 );
                            colorPlayer = Color.white;
                            while(gameId == 0){
                                getGame();
                                try{
                                    Thread.sleep(1000);
                                }catch(InterruptedException e){
                                    Log.e( "Sleep in database", e.getMessage(), e );
                                }
                            }
                            //DONT HAVE THE GAME ID, WILL HAVE TO SET THAT WHEN I CHECK IF THERE IS A PLAYER B
                        }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }
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

    public Move getMove(){
        Move r = this.lMove;
        lMove = null;
        return r;
    }

    public int getGameId(){
        return this.gameId;
    }

    public Color getColorPlayer(){
        return this.getColorPlayer();
    }
}



/*
public void theRightWay( View v )
{
    requestQueue = Volley.newRequestQueue( this );
    String requestURL = "";

    StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,

            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    try {
                        JSONArray responseArray = new JSONArray(response);
                        String responseString = "";
                        for( int i = 0; i < responseArray.length(); i++ )
                        {
                            JSONObject curObject = responseArray.getJSONObject( i );
                            // responseString += curObject.getString( );
                        }
                        txtResponse.setText(responseString);
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                }
            },

            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    txtResponse.setText( error.getLocalizedMessage() );
                }
            }
    );

    requestQueue.add(submitRequest);
}

 */