package be.kuleuven.chess.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Optional;
import java.util.stream.Stream;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Board;
import be.kuleuven.chess.models.Game;
import be.kuleuven.chess.models.Piece;

import be.kuleuven.chess.models.Tile;

public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setupImageView();
        game = new Game();
        display();
    }

    private void setupImageView(){
        /*
        if we setup a display function this will no longer be necessary! This is just to
        check if it works
         */


        TableLayout tableLayout= findViewById(R.id.gdBoard);
        for(int i = 0; i<8; i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            for(int j = 0; j<8; j++){
                ImageView imageView =  (ImageView) row.getChildAt(j);

                Resources r = getResources();
                Drawable[] layers = new Drawable[2];

                if( (i %2 == 0 & j%2 != 0) || (i %2 != 0 & j%2 == 0) ){
                    layers[0] = r.getDrawable(R.drawable.dark_square);
                }
                else{
                    layers[0] = r.getDrawable(R.drawable.light_square);
                }

                LayerDrawable layers2 = new LayerDrawable(layers);
                imageView.setImageDrawable(layers2);

            }

        }





    }

    public void display(){
        TableLayout tableLayout= findViewById(R.id.gdBoard);
        for(int i = 0; i<8; i++){
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            for(int j = 0; j<8; j++){
                ImageView imageView =  (ImageView) row.getChildAt(j);

                Optional<Piece> piece = game.getBoard().getTile(i, j).getPiece();
                /*if(piece.isPresent()){
                    Piece piece2 = piece.get();
                    Drawable[] image = piece.get().getImage();

                    LayerDrawable layers = new LayerDrawable(image);
                    imageView.setImageDrawable(layers);
                    }
                else {*/
                    Drawable image = game.getBoard().getTile(i, j).getTileImage();
                    imageView.setImageDrawable(image);
                //}


                    //figure out how to do this
                    /*
                    this method will work - but also need to give piece a reference to its color!
                    but its gonna be ugly since we'll need many cases (12)

                    better to try and figure out how we can get a piece to return a Drawable
                    but for some reason when I add the code there it doesn't recognize the methods
                    no matter which classes I import
                     */

                }




            }

        }

}