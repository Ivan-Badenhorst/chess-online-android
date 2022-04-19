package be.kuleuven.chess.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Optional;

import be.kuleuven.chess.R;
import be.kuleuven.chess.models.Game;
import be.kuleuven.chess.models.Piece;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new Game();
        display();
    }

    public void display(){
        TableLayout tableLayout= findViewById(R.id.gdBoard);
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

    public static void main(String[] args) {
        
    }
}