package Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Mouse_Input implements MouseListener, MouseMotionListener{
    private Game game;

    public Mouse_Input(Game game) {
        this.game = game;
    }
    Boolean firstClick = true;
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(firstClick){
            game.MouseClicked(e.getX(),e.getY());
            firstClick = false;
        }else{
            game.MouseReleased(e.getX(),e.getY());
            firstClick = true;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}
