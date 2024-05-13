package Main;
import javax.swing.*;

public class Canvas extends JFrame {
    int canvasWidth = 640;
    int canvasHeight = 640;
    Canvas(Drawing drawing){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(canvasWidth,canvasHeight);
        this.add(drawing);
        this.setResizable(false);
        this.setVisible(true);
    }
}