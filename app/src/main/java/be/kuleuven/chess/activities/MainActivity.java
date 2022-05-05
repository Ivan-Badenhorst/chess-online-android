package be.kuleuven.chess.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.DBConnect;
import be.kuleuven.chess.models.Game;
import be.kuleuven.chess.models.Piece;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

    private Game game;
    private ImageView tile;
    private List<Integer> start;
    private Color color;
    private int gameId;
    private DBConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        //for inverting display
        start = new ArrayList<>();
        start.add(0);
        start.add(7);

        //for now we hardcode it in such a way that we assume we play multiplayer across devices

        //step 1: find a game - and in doing so determine our color
                //check if there is a game available - if yes join it and get the ID of the game + my color is black
                //if not, make a new record for me - I am white + get the ID
        db = new DBConnect(this); //CHANGE THE THIS!!!!
        createGame();

        game = new Game(this);
        display(start.get(0));
    }

    public void display(int start){
        int sign;
        if(start == 0){
             sign = 1;
        }
        else{
             sign = -1;
        }

        TableLayout tableLayout= findViewById(R.id.gdBoard);
        for(int i = 0; i<8; i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            for(int j = 0; j<8; j++){
                ImageView imageView =  (ImageView) row.getChildAt(j);
                Optional<Piece> piece = game.getBoard().getTile(start+sign*i, start+sign*j).getPiece();

                if(piece.isPresent()){
                    Drawable[] image = game.getBoard().getTile(start+sign*i,start+sign*j).getImage(this.getApplicationContext());
                    LayerDrawable layers = new LayerDrawable(image);
                    imageView.setImageDrawable(layers);
                    }
                else {
                    Drawable image = game.getBoard().getTile(start+sign*i, start+sign*j).getTileImage(this.getApplicationContext());
                    imageView.setImageDrawable(image);
                    }

                }

            }

        }


    public void tileClick(View caller){ 
        int id = caller.getId();

         tile = (ImageView) findViewById(id);


         int row, column;
        TableRow tblRow = (TableRow) tile.getParent();
         column = tblRow.indexOfChild(tile);

         row = ((TableLayout) tblRow.getParent()).indexOfChild(tblRow);

         game.addClick(row, column);

         display(start.get(1)); // just for testing! Normall has to do with your actual color!

             /*Resources r = getResources();
             Drawable im = r.getDrawable(R.drawable.tester_on_click);
             tile.setImageDrawable(im);*/



    }

    public void createGame(){
        //here I have a loop that runs until I have a game
        db.getGame();
        while(db.getGameId() == 0){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                Log.e( "Sleep", e.getMessage(), e );
            }


        }
        this.color = db.getColorPlayer();
        this.gameId = db.getGameId();
    }

    public static void main(String[] args) {

    }
}

/*THIS IS THE CORRECT ONE IF I FUCK IT UP
for(int i = 0; i<8; i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            for(int j = 0; j<8; j++){
                ImageView imageView =  (ImageView) row.getChildAt(j);
                Optional<Piece> piece = game.getBoard().getTile(i, j).getPiece();

                if(piece.isPresent()){
                    Drawable[] image = game.getBoard().getTile(i,j).getImage(this.getApplicationContext());
                    LayerDrawable layers = new LayerDrawable(image);
                    imageView.setImageDrawable(layers);
                    }
                else {
                    Drawable image = game.getBoard().getTile(i, j).getTileImage(this.getApplicationContext());
                    imageView.setImageDrawable(image);
                    }

                }

            }

        }
 */