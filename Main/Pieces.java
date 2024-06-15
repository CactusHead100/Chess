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
    int moveDirecion;
    boolean pawnStartingSquare;
    int x;
    int y;
    Rectangle2D.Double rectangle;
    Pieces(int x,int y,piecEnum pieceType, boolean whitePiece){
        this.pieceType = pieceType;
        this.whitePiece = whitePiece;
        this.x = x;
        this.y = y;
        rectangle = new Rectangle.Double(x*Game.tileSize+10,y*Game.tileSize+10,Game.tileSize-20,Game.tileSize-20);
        if(pieceType == piecEnum.pawn){
            if(this.whitePiece){
                moveDirecion = -1;
            }else{
                moveDirecion = 1;
            }
        }
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
                case queen:
                    Pathing(1,0,this.x,this.y);
                    Pathing(-1,0,this.x,this.y);
                    Pathing(0,1,this.x,this.y);
                    Pathing(0,-1,this.x,this.y);
                    Pathing(1,1,this.x,this.y);
                    Pathing(-1,1,this.x,this.y);
                    Pathing(1,-1,this.x,this.y);
                    Pathing(-1,-1,this.x,this.y);
                break;
                case knight:
                        Jumping(1,2, this.x, this.y);
                        Jumping(2,1, this.x, this.y);
                        Jumping(1,-2, this.x, this.y);
                        Jumping(2,-1, this.x, this.y);
                break;
                case king:
                        Jumping(1,1, this.x, this.y);
                        Jumping(0,1, this.x, this.y);
                        Jumping(1,0, this.x, this.y);
                        Jumping(0,-1, this.x, this.y);
                        Jumping(1,-1, this.x, this.y);
                break;
                case pawn:
                if(((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3))){
                    this.pawnStartingSquare = true;
                }
                    PawnMovement(this.moveDirecion);
                    TakeSideways(moveDirecion, 1);
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

    private void Jumping(int xIncrease, int yIncrease, int xPos, int yPos){
        try {
            if((xPos+xIncrease<Game.boardSize)&&(xPos+xIncrease>=0)&&(yPos+yIncrease<Game.boardSize)&&(yPos+yIncrease>=0)){
                if((Game.pieces[yPos+yIncrease][xPos+xIncrease] == null)||(Game.pieces[yPos+yIncrease][xPos+xIncrease].whitePiece != this.whitePiece)){
                    Game.tiles[yPos+yIncrease][xPos+xIncrease].colour = Color.red;
                    Mouse_Input.firstClick = false;
                }
            } 
        } catch (Exception e) {
        }  
        try {
            if((Game.pieces[yPos+yIncrease][xPos+xIncrease*-1] == null)||(Game.pieces[yPos+yIncrease][xPos+xIncrease*-1].whitePiece != this.whitePiece)){
                Game.tiles[yPos+yIncrease][xPos+xIncrease*-1].colour = Color.red;
                Mouse_Input.firstClick = false;
            }
        } catch (Exception e) {
        }
    }


    private void TakeSideways(int yIncrease, int xIncrease){
        try {
            if(Game.pieces[this.y+yIncrease][this.x-xIncrease].whitePiece != this.whitePiece){
                Game.tiles[this.y+yIncrease][this.x-xIncrease].colour = Color.red;
                Mouse_Input.firstClick = false;
            }
        } catch (Exception e) {
        }
        try{
            if(Game.pieces[this.y+yIncrease][this.x+xIncrease].whitePiece != this.whitePiece){
                Game.tiles[this.y+yIncrease][this.x+xIncrease].colour = Color.red;
                Mouse_Input.firstClick = false;
            }
        } catch (Exception e) {
        }
    }
    private void PawnMovement(int yIncrease){
        if(pawnStartingSquare == false){
            if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                if(Game.pieces[this.y+yIncrease][this.x] == null){
                    Game.tiles[this.y+yIncrease][this.x].colour = Color.red;
                    Mouse_Input.firstClick = false;
                }
            }
        }else{
                if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                    if(Game.pieces[this.y+yIncrease][this.x] == null){
                        pawnStartingSquare = false;
                        PawnMovement(yIncrease*2);
                        Game.tiles[this.y+yIncrease][this.x].colour = Color.red;
                        Mouse_Input.firstClick = false;
                    }
                }
        }
    }
}