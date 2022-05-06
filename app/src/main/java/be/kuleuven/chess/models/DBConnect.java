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
import be.kuleuven.chess.activities.MainActivity;

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

    private boolean moveWritten;



    public DBConnect(AppCompatActivity activity, Board board, Color color){
        this.activity = activity;
        this.board = board;
        lMove = null;
        moveWritten = false;


        if(color == Color.white){
            this.color = "white";
        }
        else{
            this.color = "black";
        }
    }

    public DBConnect(AppCompatActivity activity){
        this.activity = activity;
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
        //String requestURL = "https://studev.groept.be/api/a21pt402/getColorMove/" + 13;
        Log.d("getGame","get game");

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d("getGame","on response");
                        int idTemp = 0;
                        try {
                            for( int i = 0; i < response.length(); i++ )
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                Log.d("getGame","PlayerB: " + curObject.getString("playerb"));
                                idTemp = curObject.getInt("idGame");
                                if(curObject.getString("playerb").equals("null") && colorPlayer == null){
                                    //this means there is a game that is waiting to be played.
                                    Log.d("getGame","first if");
                                    //add myself to the game
                                    addPlayerB(curObject.getInt("idGame")); //player b should somehow tell me that I cans start the game
                                    //once thats done the game starts

                                }
                                else if (colorPlayer == null)
                                {
                                    Log.d("getGame","second if");
                                    createNewGame();

                                }
                                else if (!curObject.getString("playerb").equals("null") && colorPlayer != null){//this means I am white but now I have a second player
                                    Log.d("getGame","third if");
                                    gameId = curObject.getInt("idGame");
                                    ( (MainActivity) activity).gameFound();

                                }
                                //HAVE TO SET THE PREVIOUS MOVE IN THE GAME CLASS WHEN WE GET THIS!!!
                            }
                            if(gameId == 0 && colorPlayer != null){
                                getGame();
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
                        Log.d("getGame","on error");
                        //createNewGame();
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    public void addPlayerB(int idGame) //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);

        Log.d("PB", "Got here");
        int randomNum = (int) (Math.random() * (1000000000 - 1)) + 1;

        String requestURL = "https://studev.groept.be/api/a21pt402/addB/" + String.valueOf(randomNum)+ "/" + String.valueOf(idGame);
        //this will then add me to the game
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d("PB", "Resp");
                        //try {
                            //if I have a response, I simply set the values of Color and GameID and the activity class will then know the game is ready
                            //JSONObject curObject = response.getJSONObject( 0 );
                            colorPlayer = Color.black;
                            gameId = idGame;
                            ( (MainActivity) activity).gameFound();

                        /*}
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }*/
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("PB", "ER");
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    public void createNewGame() //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);

        int randomNum = (int) (Math.random() * (10000000 - 1)) + 1;

        String requestURL = "https://studev.groept.be/api/a21pt402/createGame/" + String.valueOf(randomNum);
        System.out.println("in create game");
        System.out.println(requestURL);
        //this will then add me to the game
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        System.out.println("create response");
                        colorPlayer = Color.white;
                        getGame();

                        //try {
                            //if I have a response, I simply set the values of Color and GameID and the activity class will then know the game is ready
                            //JSONObject curObject = response.getJSONObject( 0 );



                            /*while(gameId == 0){
                                getGame();
                                try{
                                    Thread.sleep(1000);
                                }catch(InterruptedException e){
                                    Log.e( "Sleep in database", e.getMessage(), e );
                                }
                            }*/
                            //DONT HAVE THE GAME ID, WILL HAVE TO SET THAT WHEN I CHECK IF THERE IS A PLAYER B
                       /* }
                        catch( JSONException e )
                        {
                            Log.e( "Database", e.getMessage(), e );
                        }*/
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        System.out.println("create error");
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    public void addMove(int frow, int fcol, int srow, int scol, int gameId, Color color) //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);
        moveWritten = false;

        String col = color.name();
        String requestURL = "https://studev.groept.be/api/a21pt402/createMove/"+ frow+"/"+fcol+"/"+ srow+"/"+scol+ "/"+ gameId+"/"+col;
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
        return colorPlayer;
    }

    public void checkMoveWritten(int idGame) //View v )
    {
        requestQueue = Volley.newRequestQueue(activity);    //OR MAYBE JUST MAKE A REQUEST CUE
        // IN THE ACTIVITY AND GIVE IT HERE
        String requestURL = "https://studev.groept.be/api/a21pt402/readMove/" +  String.valueOf(idGame);

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
                                if(curObject.getString("color").equals(color))
                                {
                                    moveWritten= true;
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

    public boolean getWritten()
    {
        return moveWritten;
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