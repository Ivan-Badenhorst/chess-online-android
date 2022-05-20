package be.kuleuven.chess.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.DBActivity;
import be.kuleuven.chess.models.Game;
import be.kuleuven.chess.models.Piece;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

    private Game game;
    private ImageView tile;
    private List<Integer> start;
    private Color color;
    private int gameId;
    private DBActivity db;

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
        db = new DBActivity(this); //CHANGE THE THIS!!!!
        //createGame();
        db.getGame();
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

         if(this.color == Color.white){
             game.addClick(row, column);
         }
         else{
             game.addClick(7-row, 7-column);
         }


        if(color == Color.white){
            display(start.get(0));
        }
        else
        {
            display(start.get(1));
        }

    }


    public static void main(String[] args) {

    }

    public void btnResignedClick(View caller){
        TableLayout t = findViewById(R.id.gdBoard);
        t.setClickable(false);
        db.setGameStatus(true, gameId);
    }

    public void checkmateVisibility(boolean won){
        TextView cmText = findViewById(R.id.cmText);
        cmText.setVisibility(View.VISIBLE);

        if(won){
            cmText.setText("Checkmate! You won!");
        }
        else{
            cmText.setText("Checkmate! You lost :(");
        }

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setVisibility(View.VISIBLE);

        Button btnResign = findViewById(R.id.btnResign);
        btnResign.setVisibility(View.GONE);
    }

    public void returnToMain(View caller)
    {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void resigned(boolean loser){
            //game.resigned();
        TextView cmText = findViewById(R.id.cmText);
        cmText.setVisibility(View.VISIBLE);
        Button btnResign = findViewById(R.id.btnResign);
        btnResign.setVisibility(View.GONE);
        if(loser){
            cmText.setText("You resigned!");
        }
        else{
            cmText.setText("Opponent resigned!");
        }

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setVisibility(View.VISIBLE);
    }


    public void gameFound(){
        this.color = db.getColorPlayer();
        this.gameId = db.getGameId();

        game = new Game(this, color, gameId);
        if(color == Color.white){
            display(start.get(0));
        }
        else
        {
            display(start.get(1));
        }
    }
}
