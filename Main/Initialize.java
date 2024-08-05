package Main;

/**
 * initializes the game and canvas and passes the game to the canvas so it can add the game to 
 * canvas allowing drawing on the canvas
 */
public class Initialize {
    private Canvas canvas;
    private Game game;

    //note this was case sensitive as its a constructor
    public Initialize(){
    game = new Game();
    canvas = new Canvas(game);
    game.requestFocus();
    }
}