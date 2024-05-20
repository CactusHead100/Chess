package Main;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Pieces {
    int pieceType;
    boolean whitePiece;
    int x;
    int y;
    Rectangle2D.Double rectangle = new Rectangle.Double(0,0,80,80);
    Pieces(int x,int y,int pieceType, boolean whitePiece){
        this.pieceType = pieceType;
        this.whitePiece = whitePiece;
        this.x = x;
        this.y = y;
    }
}
