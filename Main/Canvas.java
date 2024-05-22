package Main;
import java.awt.Color;

import javax.swing.*;

public class Canvas extends JFrame {
    int canvasWidth = 652;
    int canvasHeight = 675;
    Canvas(Game game){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(canvasWidth,canvasHeight);
        this.add(game);
        this.setResizable(false);
        this.setVisible(true);
    }
}