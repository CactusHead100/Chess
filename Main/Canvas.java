package Main;
import java.awt.Color;

import javax.swing.*;
/**
 * class canvas uses JFrame to create a game window in which you can play in
 */
public class Canvas extends JFrame {

    //constants defining the canvas size 
    final int canvasWidth = 652;
    final int canvasHeight = 675;

    /*
     * sets up rules of the window
     */
    Canvas(Game game){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(canvasWidth,canvasHeight);
        this.add(game);//allows game to draw in/on using 
        this.setResizable(false);//set it no not resizable as wasn't sure how to access the window size once resized
        this.setVisible(true);
    }
}