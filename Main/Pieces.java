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
    boolean takingEnPassant = false;
    int moveDirecion;
    String dictReference;
    int x;
    int y;
    Rectangle2D.Double rectangle;
    boolean sprite[][];
    Pieces(int x,int y,piecEnum pieceType, boolean whitePiece){
        this.pieceType = pieceType;
        this.whitePiece = whitePiece;
        this.x = x;
        this.y = y;
        rectangle = new Rectangle.Double(x*Game.tileSize+10,y*Game.tileSize+10,Game.tileSize-20,Game.tileSize-20);
            switch (pieceType) {
                case pawn:
                    if(this.whitePiece){
                        moveDirecion = -1;
                        dictReference = "wp"+Integer.toString(this.x);
                        Game.pieceXY.put(dictReference,Double.parseDouble(Integer.toString(this.x)+"."+Integer.toString(this.y)));
                    }else{
                        moveDirecion = 1;
                        dictReference = "bp"+Integer.toString(this.x);
                        Game.pieceXY.put(dictReference,Double.parseDouble(Integer.toString(this.x)+"."+Integer.toString(this.y)));
                    }
                break;
                case king:
                    if(this.whitePiece){
                        dictReference = "wk";
                        Game.pieceXY.put(dictReference,Double.parseDouble(Integer.toString(this.x)+"."+Integer.toString(this.y)));
                    }else{
                        dictReference = "bk";
                        Game.pieceXY.put(dictReference,Double.parseDouble(Integer.toString(this.x)+"."+Integer.toString(this.y)));
                    }
                break;
            }
            
        
    }

    public void ApplyGamerules(int newX, int newY){
        for(int i = 0; i<8; i++){
            try {
                Game.pieces[(int)(Math.round((Game.pieceXY.get("wp"+Integer.toString(i))-(int)Math.floor(Game.pieceXY.get("wp"+Integer.toString(i))))*10))][(int)Math.floor(Game.pieceXY.get("wp"+Integer.toString(i)))].pawnEnPassant = false;
                Game.pieces[(int)(Math.round((Game.pieceXY.get("bp"+Integer.toString(i))-(int)Math.floor(Game.pieceXY.get("bp"+Integer.toString(i))))*10))][(int)Math.floor(Game.pieceXY.get("bp"+Integer.toString(i)))].pawnEnPassant = false;
            } catch (Exception e) {
            }
        }
        switch (pieceType) {
            case pawn:
                if((this.y+2 == newY)||(this.y-2 == newY)){
                    MovePiece(newX, newY);
                    Game.pieces[this.y][this.x].pawnEnPassant = true;
                }else if(takingEnPassant){
                    MovePiece(newX, newY);
                    Game.pieces[newY+moveDirecion*-1][newX] = null;
                }else{
                    MovePiece(newX, newY);
                }
            break;
            case king:
                if(this.whitePiece){
                    Game.whiteKingXY[0] = newX;
                    Game.whiteKingXY[1] = newY;
                    MovePiece(newX, newY);
                }
            break;
            default:
                MovePiece(newX, newY);
            break;
        }
    }
    public void MovePiece(int newX, int newY){
        Game.pieces[this.y][this.x] = null;
        this.x = newX;
        this.y = newY;
        Game.pieces[this.y][this.x] = new Pieces(this.x, this.y, this.pieceType, this.whitePiece);
        try {
            Game.pieceXY.put(dictReference, Game.pieceXY.put(dictReference,Double.parseDouble(Integer.toString(this.x)+"."+Integer.toString(this.y))));
        } catch (Exception e) {
        }
            
    }
    public Boolean GetAvailableMoves(){
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
                        takingEnPassant = false;
                        if(((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3))){
                            this.pawnDoubleMove = true;
                        }
                        PawnMovement(this.moveDirecion);
                        TakeSideways(this.moveDirecion, 1);
                    break;
                }
        return(true);
    }
    private void Pathing(int xIncrement, int yIncrement, int xPos, int yPos){
        try {
            if((xPos+xIncrement<Game.boardSize)&&(xPos+xIncrement>=0)&&(yPos+yIncrement<Game.boardSize)&&(yPos+yIncrement>=0)){
                if(Game.pieces[yPos+yIncrement][xPos+xIncrement] == null){
                    Pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement);
                    ColourTiles(xPos+xIncrement, yPos+yIncrement);
                }else if(Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece != this.whitePiece){
                    ColourTiles(xPos+xIncrement, yPos+yIncrement);
                }
            }
        } catch (Exception e) {
        }
    }
    private void Jumping(int xIncrease, int yIncrease, int xPos, int yPos){
        try {
            if((xPos+xIncrease<Game.boardSize)&&(xPos+xIncrease>=0)&&(yPos+yIncrease<Game.boardSize)&&(yPos+yIncrease>=0)){
                if((Game.pieces[yPos+yIncrease][xPos+xIncrease] == null)||(Game.pieces[yPos+yIncrease][xPos+xIncrease].whitePiece != this.whitePiece)){
                    ColourTiles(xPos+xIncrease, yPos+yIncrease);
                }
            } 
        } catch (Exception e) {
        }  
        try {
            if((Game.pieces[yPos+yIncrease][xPos+xIncrease*-1] == null)||(Game.pieces[yPos+yIncrease][xPos+xIncrease*-1].whitePiece != this.whitePiece)){
                ColourTiles(xPos+xIncrease*-1, yPos+yIncrease);
            }
        } catch (Exception e) {
        }
    }
    private void TakeSideways(int yIncrease, int xIncrease){
        try {
            if((Game.pieces[this.y][this.x-xIncrease].whitePiece != this.whitePiece)&&
                (Game.pieces[this.y][this.x-xIncrease].pawnEnPassant)){
                ColourTiles(this.x-xIncrease, this.y+yIncrease);
                takingEnPassant = true;
            }
        } catch (Exception e) {
        }
        try {
            if((Game.pieces[this.y][this.x+xIncrease].whitePiece != this.whitePiece)&&
                (Game.pieces[this.y][this.x+xIncrease].pawnEnPassant)){
                ColourTiles(this.x+xIncrease, this.y+yIncrease);
                takingEnPassant = true;
            }
        } catch (Exception e) {
        }
        try {
            if(Game.pieces[this.y+yIncrease][this.x-xIncrease].whitePiece != this.whitePiece){
                ColourTiles(this.x-xIncrease, this.y+yIncrease);
            }
        } catch (Exception e) {
        }
        try{
            if((Game.pieces[this.y+yIncrease][this.x+xIncrease].whitePiece != this.whitePiece)||
            ((Game.pieces[this.y][this.x+xIncrease].whitePiece != this.whitePiece)&&
            (Game.pieces[this.y][this.x+xIncrease].pawnEnPassant))){
                ColourTiles(this.x+xIncrease, this.y+yIncrease);
            }
        } catch (Exception e) {
        }
    }
    private void PawnMovement(int yIncrease){
        if(this.pawnDoubleMove == false){
            if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                if(Game.pieces[this.y+yIncrease][this.x] == null){
                    ColourTiles(this.x, this.y+yIncrease);
                }
            }
        }else if(this.pawnDoubleMove){
                if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                    if(Game.pieces[this.y+yIncrease][this.x] == null){
                        this.pawnDoubleMove = false;
                        PawnMovement(yIncrease*2);
                        ColourTiles(this.x, this.y+yIncrease);
                    }
                }
        }
    }
    private void ColourTiles(int xOfTile, int yOfTile){
        Game.tiles[yOfTile][xOfTile].colour = Color.red;
        Mouse_Input.firstClick = false;
    }
    
}