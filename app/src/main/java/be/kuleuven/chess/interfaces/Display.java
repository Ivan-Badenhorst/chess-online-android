package be.kuleuven.chess.interfaces;

public interface Display {
    void setClickable(boolean canClick);
    void display();
    void gameOver(String message);
    void gameOver(boolean won);

}
