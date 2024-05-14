package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class Drawing extends JPanel{
    private Mouse_Input mouse_Input = new Mouse_Input(this);
    int tileSize = 80;

    public Drawing(){
        addMouseListener(mouse_Input);
        addMouseMotionListener(mouse_Input);
    }
    
    public void MouseClicked(){
        System.out.println("Click");
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        //Drawing board
        for(int y = 0; y < 8; y++){
            for(int x = y % 2; x < 8; x+=2){
                g2d.setColor(Color.black);
                g2d.fill(new Rectangle(x*tileSize,y*tileSize,tileSize,tileSize));
            }
        } 
    }
}
