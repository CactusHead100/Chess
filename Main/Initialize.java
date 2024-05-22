package Main;
public class Initialize {
    private Canvas canvas;
    private Game drawing;
    public Initialize(){
    drawing = new Game();
    canvas = new Canvas(drawing);
    drawing.requestFocus();
    }
}