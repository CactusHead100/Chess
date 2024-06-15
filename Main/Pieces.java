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
    boolean pawnDoubleMove = false;
    boolean pawnEnPassant = false;
    int moveDirecion;
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

    public void movePiece(int newX, int newY){
        if((this.pieceType == piecEnum.pawn)&&((this.y+2 == newY)||(this.y-2 == newY))){
            Game.pieces[this.y][this.x] = null;
            this.x = newX;
            this.y = newY;
            Game.pieces[this.y][this.x] = new Pieces(this.x, this.y, this.pieceType, this.whitePiece);
            Game.pieces[this.y][this.x].pawnEnPassant = true;
        }else{
            Game.pieces[this.y][this.x] = null;
            this.x = newX;
            this.y = newY;
            Game.pieces[this.y][this.x] = new Pieces(this.x, this.y, this.pieceType, this.whitePiece);
        }
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
                    this.pawnDoubleMove = true;
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
                    colorTiles(xPos+xIncrement, yPos+yIncrement);
                }else if(Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece != this.whitePiece){
                    colorTiles(xPos+xIncrement, yPos+yIncrement);
                }
            }
        } catch (Exception e) {
        }
    }

    private void Jumping(int xIncrease, int yIncrease, int xPos, int yPos){
        try {
            if((xPos+xIncrease<Game.boardSize)&&(xPos+xIncrease>=0)&&(yPos+yIncrease<Game.boardSize)&&(yPos+yIncrease>=0)){
                if((Game.pieces[yPos+yIncrease][xPos+xIncrease] == null)||(Game.pieces[yPos+yIncrease][xPos+xIncrease].whitePiece != this.whitePiece)){
                    colorTiles(xPos+xIncrease, yPos+yIncrease);
                }
            } 
        } catch (Exception e) {
        }  
        try {
            if((Game.pieces[yPos+yIncrease][xPos+xIncrease*-1] == null)||(Game.pieces[yPos+yIncrease][xPos+xIncrease*-1].whitePiece != this.whitePiece)){
                colorTiles(xPos+xIncrease*-1, yPos+yIncrease);
            }
        } catch (Exception e) {
        }
    }


    private void TakeSideways(int yIncrease, int xIncrease){
        try {
            if((Game.pieces[this.y][this.x-xIncrease].whitePiece != this.whitePiece)&&
                (Game.pieces[this.y][this.x-xIncrease].pawnEnPassant)){
                colorTiles(this.x-xIncrease, this.y+yIncrease);
            }
        } catch (Exception e) {
        }
        try {
            if((Game.pieces[this.y][this.x+xIncrease].whitePiece != this.whitePiece)&&
                (Game.pieces[this.y][this.x+xIncrease].pawnEnPassant)){
                colorTiles(this.x+xIncrease, this.y+yIncrease);
            }
        } catch (Exception e) {
        }
        try {
            if(Game.pieces[this.y+yIncrease][this.x-xIncrease].whitePiece != this.whitePiece){
                colorTiles(this.x-xIncrease, this.y+yIncrease);
            }
        } catch (Exception e) {
        }
        try{
            if((Game.pieces[this.y+yIncrease][this.x+xIncrease].whitePiece != this.whitePiece)||
            ((Game.pieces[this.y][this.x+xIncrease].whitePiece != this.whitePiece)&&
            (Game.pieces[this.y][this.x+xIncrease].pawnEnPassant))){
                colorTiles(this.x+xIncrease, this.y+yIncrease);
            }
        } catch (Exception e) {
        }
    }
    private void PawnMovement(int yIncrease){
        if(this.pawnDoubleMove == false){
            if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                if(Game.pieces[this.y+yIncrease][this.x] == null){
                    colorTiles(this.x, this.y+yIncrease);
                }
            }
        }else if(this.pawnDoubleMove){
                if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                    if(Game.pieces[this.y+yIncrease][this.x] == null){
                        this.pawnDoubleMove = false;
                        PawnMovement(yIncrease*2);
                        colorTiles(this.x, this.y+yIncrease);
                    }
                }
        }
    }
    private void colorTiles(int xOfTile, int yOfTile){
        Game.tiles[yOfTile][xOfTile].colour = Color.red;
        Mouse_Input.firstClick = false;
    }
}