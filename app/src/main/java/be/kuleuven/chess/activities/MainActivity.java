package be.kuleuven.chess.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import java.util.Optional;

import be.kuleuven.chess.R;
import be.kuleuven.chess.interfaces.Display;
import be.kuleuven.chess.models.Color;
import be.kuleuven.chess.models.Database.DBActivity;
import be.kuleuven.chess.models.Game;
import be.kuleuven.chess.models.Piece;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity implements Display {

    private Game game;
    private DBActivity db;
    private ProgressDialog progress;

    private Color color;
    private int gameId;
    private boolean resigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        resigned = false;
        db = new DBActivity(this);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Finding a match...");
        progress.setCancelable(true);
        progress.show();

        db.getGame();
    }

    public void gameFound(){
        this.color = db.getColorPlayer();
        this.gameId = db.getGameId();

        game = new Game(this, color, gameId);

        progress.dismiss();
        findViewById(R.id.gdBoard).setVisibility(View.VISIBLE);
        findViewById(R.id.btnResign).setVisibility(View.VISIBLE);

        display();
    }



    public void tileClick(View caller){
        int id = caller.getId();

        ImageView tile = (ImageView) findViewById(id);
        TableRow tblRow = (TableRow) tile.getParent();

        int column = tblRow.indexOfChild(tile);
        int row = ((TableLayout) tblRow.getParent()).indexOfChild(tblRow);

        if(this.color == Color.white){
            game.addClick(row, column);
        }
        else{
            game.addClick(7-row, 7-column);
        }

        display();

    }

    @Override
    public void setClickable(boolean canClick){

        TableLayout tableLayout= findViewById(R.id.gdBoard);
        for(int i = 0; i<8; i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for(int j = 0; j<8; j++){
                ImageView imageView =  (ImageView) row.getChildAt(j);
                imageView.setClickable(canClick);

            }

        }
    }


    public void display(){

        int sign, start;

        if(color == Color.white){
            sign = 1;
            start = 0;
        }
        else{
            sign = -1;
            start = 7;
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


    public void gameOver(String message){
        TextView cmText = findViewById(R.id.cmText);
        cmText.setVisibility(View.VISIBLE);
        if(!resigned){
            cmText.setText(message);
        }


        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setVisibility(View.VISIBLE);

        Button btnResign = findViewById(R.id.btnResign);
        btnResign.setVisibility(View.GONE);
    }

    public void gameOver(boolean won){
        if(won){
            gameOver("Checkmate! You won!");
        }
        else{
            gameOver("Checkmate! You lost :(");
        }
    }


    public void btnResignedClick(View caller){

        TableLayout t = findViewById(R.id.gdBoard);
        t.setClickable(false);
        db.setGameStatus(true, gameId);
        gameOver("You resigned!");
        resigned = true;

    }

    public void btnReturnToMainClick(View caller)
    {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){

        if(gameId == 0){
            db.closeGame();
        }
        else{
            btnResignedClick(findViewById(R.id.btnResign));
            closeActivity();
        }

    }

    public void closeActivity(){
        super.onBackPressed();
    }


}
