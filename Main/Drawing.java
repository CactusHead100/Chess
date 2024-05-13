package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Drawing extends JPanel{
    private Mouse_Input mouse_Input = new Mouse_Input(this);
    
    public Drawing(){
        addMouseListener(mouse_Input);
        addMouseMotionListener(mouse_Input);
    }
    
    public void MouseClicked(){
        System.out.println("Click");
    }

    public void paintcomponent(Graphics graphics){
        super.paint(graphics);
        Graphics graphics2d = (Graphics2D) graphics;
        
    }
}
