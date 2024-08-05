package Main;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
/**
 * This is a class name Board as it holds values for the squares that make up the board of the game
 */
public class Board {

    //initilization of variables that store its dimensions and colour
    Rectangle2D.Double tile;
    Color colour;

    //Constructor that sets these variables when class initialized
    public Board(int x, int y, Color color){
        colour = color;
        
        //creates a rectangle from these given variables which is later used to draw the piece
        tile = new Rectangle2D.Double(x,y,Game.tileSize,Game.tileSize);
    }    
}
