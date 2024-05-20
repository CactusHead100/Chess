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
    int mouseX;
    int mouseY;
    Pieces pieces[][] = {{new Pieces(0,0,4,false),new Pieces(1,0,2,false),new Pieces(2,0,3,false),new Pieces(3,0,5,false),new Pieces(4,0,6,false),new Pieces(5,0,3,false),new Pieces(6,0,2,false),new Pieces(7,0,4,false)},
                         {new Pieces(0,1,1,false),new Pieces(1,1,1,false),new Pieces(2,1,1,false),new Pieces(3,1,1,false),new Pieces(4,1,1,false),new Pieces(5,1,1,false),new Pieces(6,1,1,false),new Pieces(7,1,1,false)},
                         {new Pieces(0,2,0,false),new Pieces(1,2,0,false),new Pieces(2,2,0,false),new Pieces(3,2,0,false),new Pieces(4,2,0,false),new Pieces(5,2,0,false),new Pieces(6,2,0,false),new Pieces(7,2,0,false)},
                         {new Pieces(0,3,0,false),new Pieces(1,3,0,false),new Pieces(2,3,0,false),new Pieces(3,3,0,false),new Pieces(4,3,0,false),new Pieces(5,3,0,false),new Pieces(6,3,0,false),new Pieces(7,3,0,false)},
                         {new Pieces(0,4,0,false),new Pieces(1,4,0,false),new Pieces(2,4,0,false),new Pieces(3,4,0,false),new Pieces(4,4,0,false),new Pieces(5,4,0,false),new Pieces(6,4,0,false),new Pieces(7,4,0,false)},
                         {new Pieces(0,5,0,false),new Pieces(1,5,0,false),new Pieces(2,5,0,false),new Pieces(3,5,0,false),new Pieces(4,5,0,false),new Pieces(5,5,0,false),new Pieces(6,5,0,false),new Pieces(7,5,0,false)},
                         {new Pieces(0,6,1,true),new Pieces(1,6,1,true),new Pieces(2,1,1,false),new Pieces(3,6,1,true),new Pieces(4,6,1,true),new Pieces(5,6,1,true),new Pieces(6,6,1,true),new Pieces(7,6,1,true)},
                         {new Pieces(0,7,4,true),new Pieces(1,7,2,true),new Pieces(2,0,3,false),new Pieces(3,7,5,true),new Pieces(4,7,6,true),new Pieces(5,7,3,true),new Pieces(6,7,2,true),new Pieces(7,7,4,true)},};
    public Drawing(){
        addMouseListener(mouse_Input);
        addMouseMotionListener(mouse_Input);
    }
    
    public void MouseClicked(int mouseXpos,int mouseYpos){
        mouseX = mouseXpos/80;
        mouseY = mouseYpos/80;
        System.out.println(mouseX+"&"+mouseY);
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //Drawing board
        for(int y = 0; y < 8; y++){
            for(int x = ((y % 2)-1)/-1; x < 8; x+=2){
                g2d.setColor(Color.black);
                g2d.fill(new Rectangle(x*tileSize,y*tileSize,tileSize,tileSize));
            }
        } 
        g2d.setColor(Color.blue);
        g2d.fill(new Rectangle(pieces[7][4].x*tileSize,pieces[7][4].y*tileSize,tileSize,tileSize));
    }
}
