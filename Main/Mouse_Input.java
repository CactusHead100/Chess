package Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
/**
 * a class that listens for input then calls methods in the Game class 
 */
public class Mouse_Input implements MouseListener, MouseMotionListener{
    private Game game;
    public Mouse_Input(Game game) {
        this.game = game;
    }
    static Boolean firstClick = true;//allows us to tell what we are calling in game class
    
    //most of these are not used but are empty due to otherwise causing errors
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    /**
     * imput mouse event, returns nothing
     * checks what type of click happened then calls different functions depending on the click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        
        //checks what button is clicked then calls different functions of game for different clicks
        if(e.getButton() == 1){
            if(firstClick){
                game.mouseClicked(e.getX(),e.getY());
            }else{
                game.mouseReleased(e.getX(),e.getY());
            }

            //this is used so that players can reset and play another game once finished 
        }else if(e.getButton() == 2){//chose the mousewheel as the reset button as its quite hard to accidentally press it
            game.clearBoard();
            game.createPieces();
            game.createTiles();
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
