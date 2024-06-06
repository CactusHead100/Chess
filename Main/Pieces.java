package Main;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Dictionary;
import java.util.Hashtable;


public class Pieces {
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
        rectangle = new Rectangle.Double(x*Game.tileSize,y*Game.tileSize,Game.tileSize,Game.tileSize);
    }

    public void movePiece(int x, int y){
        Game.pieces[this.y][this.x] = null;
        this.x = x;
        this.y = y;
        Game.pieces[this.y][this.x] = new Pieces(this.x, this.y, this.pieceType, this.whitePiece);
    }

    public Boolean getAvailableMoves(){
            switch (pieceType) {
                case rook:
                        Pathing(1,0,this.x,this.y);
                        Pathing(-1,0,this.x,this.y);
                        Pathing(0,1,this.x,this.y);
                        Pathing(0,-1,this.x,this.y);
                break;
                case bishop:
                    Pathing(1,1,this.x,this.y);
                    Pathing(-1,1,this.x,this.y);
                    Pathing(1,-1,this.x,this.y);
                    Pathing(-1,-1,this.x,this.y);
                break;
            }
        return(true);
    }
    private void Pathing(int xIncrement, int yIncrement, int xPos, int yPos){
        try {
            if((xPos+xIncrement<Game.boardSize)&&(xPos+xIncrement>=0)&&(yPos+yIncrement<Game.boardSize)&&(yPos+yIncrement>=0)){
                if(Game.pieces[yPos+yIncrement][xPos+xIncrement] == null){
                    Pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement);
                    Game.tiles[yPos+yIncrement][xPos+xIncrement].colour = Color.red;
                    Mouse_Input.firstClick = false;
                }else if(Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece != this.whitePiece){
                    Game.tiles[yPos+yIncrement][xPos+xIncrement].colour = Color.red;
                    Mouse_Input.firstClick = false;
                }
            }
        } catch (Exception e) {
        }
    }
}