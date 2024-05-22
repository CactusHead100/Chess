package Main;
public class Initialize {
    private Canvas canvas;
    private Game game;
    public Initialize(){
    game = new Game();
    canvas = new Canvas(game);
    game.requestFocus();
    }
}