package Main;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Pieces {
   // int pieceType;
    public enum piecEnum{pawn,knight,bishop,rook,queen,king}
    piecEnum pieceType;
    boolean whitePiece;
    int x;
    int y;
    Rectangle2D.Double rectangle;
    Pieces(int x,int y,piecEnum pieceType, boolean whitePiece){
        this.pieceType = pieceType;
        this.whitePiece = whitePiece;
        this.x = x;
        this.y = y;
        rectangle = new Rectangle.Double(x,y,Game.tileSize,Game.tileSize);
    }

    public int getAvailableMoves(){
        return(0);
    }
}
