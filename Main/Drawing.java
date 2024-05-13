package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class Drawing extends JPanel{
    private Mouse_Input mouse_Input = new Mouse_Input(this);
    Rectangle2D.Double rectangle = new Rectangle.Double(0,0,100,100);

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
        g2d.setColor(Color.black);
        g2d.fill(rectangle); 
    }
}
