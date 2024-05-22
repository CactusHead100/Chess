package Main;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Pieces {
    int pieceType;
    boolean whitePiece;
    int x;
    int y;
    Rectangle2D.Double rectangle;
    Pieces(int x,int y,int pieceType, boolean whitePiece){
        this.pieceType = pieceType;
        this.whitePiece = whitePiece;
        this.x = x;
        this.y = y;
        rectangle = new Rectangle.Double(x*Game.tileSize,y*Game.tileSize,80,80);
    }
}
