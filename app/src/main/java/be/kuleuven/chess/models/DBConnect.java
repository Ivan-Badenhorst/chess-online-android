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

import be.kuleuven.chess.R;

//IN THE GAME, IF WE READ A MOVE, MAKE SURE WE DONT TRY TO READ AGAIN UNTIL WE HAVE SUCCESSFULLY WROTE OUR MOVE
//THAT MEANS WE GOT A RESPONSE - ADD IN RESPONSE HANDLER OR WRITE
public class DBConnect
{
    private RequestQueue requestQueue;
    private AppCompatActivity activity;
    private Board board;
    private Move lMove;
    private final String color;


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

    public Move getMove(){
        Move r = this.lMove;
        lMove = null;
        return r;
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