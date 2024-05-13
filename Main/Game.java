package Main;
public class Game {
    private Canvas canvas;
    private Drawing drawing;
    public Game(Drawing drawing){
        this.drawing = drawing;
    }
    public Game(){
    drawing = new Drawing();
    canvas = new Canvas(drawing);
    drawing.requestFocus();
    }
}