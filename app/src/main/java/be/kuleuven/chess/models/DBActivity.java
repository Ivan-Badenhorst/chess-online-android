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

public class DBActivity {

    private RequestQueue requestQueue;
    private AppCompatActivity activity;
    private Color colorPlayer;
    private int gameId;


    public DBActivity(AppCompatActivity activity){
        this.activity = activity;
        colorPlayer = null;
        gameId = 0;
    }

    public void getGame()
    {
        requestQueue = Volley.newRequestQueue(activity);
        String requestURL = "https://studev.groept.be/api/a21pt402/getGame";
        Log.d("getGame","get game");

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d("getGame","on response");
                        int idTemp;
                        try {
                            for( int i = 0; i < response.length(); i++ )
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                Log.d("getGame","PlayerB: " + curObject.getString("playerb"));
                                idTemp = curObject.getInt("idGame");
                                if(curObject.getString("playerb").equals("null") && colorPlayer == null){
                                    addPlayerB(idTemp);
                                }
                                else if (colorPlayer == null)
                                {
                                    createNewGame();
                                }
                                else if (!curObject.getString("playerb").equals("null") && colorPlayer != null){//this means I am white but now I have a second player
                                    Log.d("getGame","third if");
                                    gameId = curObject.getInt("idGame");
                                    ( (MainActivity) activity).gameFound();
                                }
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
                        Log.d("getGame DBActivity",error.getMessage());
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    public void addPlayerB(int idGame)
    {
        requestQueue = Volley.newRequestQueue(activity);

        Log.d("PB", "Got here");
        int randomNum = (int) (Math.random() * (1000000000 - 1)) + 1;

        String requestURL = "https://studev.groept.be/api/a21pt402/addB/" + randomNum + "/" + idGame;
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d("PB", "Resp");
                        colorPlayer = Color.black;
                        gameId = idGame;
                        ( (MainActivity) activity).gameFound();
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("addPlayerB DBActivity", error.getMessage());
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    public void createNewGame()
    {
        requestQueue = Volley.newRequestQueue(activity);

        int randomNum = (int) (Math.random() * (10000000 - 1)) + 1;

        String requestURL = "https://studev.groept.be/api/a21pt402/createGame/" + randomNum;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        colorPlayer = Color.white;
                        getGame();
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("createNewGame DBActivity", error.getMessage());
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    public int getGameId(){
        return this.gameId;
    }

    public Color getColorPlayer(){
        return colorPlayer;
    }

    public void setGameStatus(boolean resigned, int gameid)
    {
        requestQueue = Volley.newRequestQueue(activity);
        int boolval;
        if(resigned){
            boolval = 1;
        }
        else{
            boolval = 0;
        }
        String requestURL = "https://studev.groept.be/api/a21pt402/setStatus/" + boolval + "/" + gameid;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        ((MainActivity) activity).resigned(true);
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("addMove", "error");
                    }
                }
        );

        requestQueue.add(submitRequest);
    }
}
