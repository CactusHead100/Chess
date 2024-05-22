package Main;
import java.awt.Color;

import javax.swing.*;

public class Canvas extends JFrame {
    int canvasWidth = 652;
    int canvasHeight = 675;
    Canvas(Game drawing){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(canvasWidth,canvasHeight);
        this.add(drawing);
        this.setResizable(false);
        this.setVisible(true);
    }
}