package Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Mouse_Input implements MouseListener, MouseMotionListener{
    private Game game;

    public Mouse_Input(Game game) {
        this.game = game;
    }
    static Boolean firstClick = true;
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == 1){
            if(firstClick){
                game.MouseClicked(e.getX(),e.getY());
            }else{
                game.MouseReleased(e.getX(),e.getY());
            }
        }else if(e.getButton() == 2){
            game.clearBoard();
            game.CreatePieces();
            game.whitesMove = true;
            game.repaint();
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
