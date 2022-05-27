package be.kuleuven.chess.models.Database;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.chess.activities.MainActivity;
import be.kuleuven.chess.models.Color;

public class DBActivity {

    private RequestQueue requestQueue;
    private final MainActivity activity;
    private Color colorPlayer;
    private int gameId;
    private int gameIdGameNotFound;


    public DBActivity(MainActivity activity){
        this.activity = activity;
        colorPlayer = null;
        gameId = 0;
    }

    public void getGame()
    {
        requestQueue = Volley.newRequestQueue(activity);
        String requestURL = "https://studev.groept.be/api/a21pt402/getGame";

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    int idTemp = 0;
                    try {
                        for( int i = 0; i < response.length(); i++ )
                        {
                            JSONObject curObject = response.getJSONObject( i );
                            idTemp = curObject.getInt("idGame");

                            if(curObject.getString("playerb").equals("null") && colorPlayer == null){
                                addPlayerB(idTemp);
                            }
                            else if (colorPlayer == null)
                            {
                                createNewGame();
                            }
                            else if (!curObject.getString("playerb").equals("null") && colorPlayer != null){
                                gameId = curObject.getInt("idGame");
                                activity.gameFound();
                            }
                        }
                        if(gameId == 0 && colorPlayer != null){
                            gameIdGameNotFound = idTemp;
                            getGame();
                        }
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },

                error -> Log.e("getGame DBActivity",error.getMessage())
        );

        requestQueue.add(submitRequest);
    }

    public void addPlayerB(int idGame)
    {
        requestQueue = Volley.newRequestQueue(activity);
        int randomNum = (int) (Math.random() * (1000000000 - 1)) + 1;

        String requestURL = "https://studev.groept.be/api/a21pt402/addB/" + randomNum + "/" + idGame;
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    colorPlayer = Color.black;
                    gameId = idGame;
                    activity.gameFound();
                },

                error -> Log.e("addPlayerB DBActivity", error.getMessage())
        );

        requestQueue.add(submitRequest);
    }

    public void createNewGame()
    {
        requestQueue = Volley.newRequestQueue(activity);
        int randomNum = (int) (Math.random() * (10000000 - 1)) + 1;
        String requestURL = "https://studev.groept.be/api/a21pt402/createGame/" + randomNum;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    colorPlayer = Color.white;
                    getGame();
                },

                error -> Log.e("createNewGame DBActivity", error.getMessage())
        );

        requestQueue.add(submitRequest);
    }


    public void setGameStatus(boolean resigned, int gameid)
    {
        requestQueue = Volley.newRequestQueue(activity);
        int booleanValue;

        if(resigned){
            booleanValue = 1;
        }
        else{
            booleanValue = 0;
        }

        String requestURL = "https://studev.groept.be/api/a21pt402/setStatus/" + booleanValue + "/" + gameid;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> activity.gameOver("You resigned!"),

                error -> Log.e("addMove", "error")
        );

        requestQueue.add(submitRequest);
    }


    public void closeGame()
    {
        requestQueue = Volley.newRequestQueue(activity);
        String requestURL = "https://studev.groept.be/api/a21pt402/getGame";

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {

                        for( int i = 0; i < response.length(); i++ )
                        {
                            JSONObject curObject = response.getJSONObject( i );

                            if(curObject.getInt("idGame") == gameIdGameNotFound){

                                if(curObject.isNull("playerb")){
                                    deleteGame();
                                }
                                else{
                                    activity.closeActivity();
                                }

                            }

                        }

                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                },

                error -> Log.e("getGame DBActivity",error.getMessage())
        );

        requestQueue.add(submitRequest);
    }


    public void deleteGame()
    {
        requestQueue = Volley.newRequestQueue(activity);
        String requestURL = "https://studev.groept.be/api/a21pt402/deleteGame/" + gameIdGameNotFound;

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> activity.closeActivity(),

                error -> Log.e("deleteGame", error.getMessage())
        );

        requestQueue.add(submitRequest);
    }

    public int getGameId(){
        return this.gameId;
    }

    public Color getColorPlayer(){
        return colorPlayer;
    }
}