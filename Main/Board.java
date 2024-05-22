package Main;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class Board {
    Rectangle2D.Double tile;
    Color colour;
    public Board(int x, int y, Color color){
        colour = color;
        tile = new Rectangle2D.Double(x,y,Game.tileSize,Game.tileSize);
    }    
}
