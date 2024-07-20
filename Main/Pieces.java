package Main;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.concurrent.CountDownLatch;
public class Pieces {
    public enum piecEnum{pawn,knight,bishop,rook,queen,king,enemyPiece}
    piecEnum pieceType;
    Pieces pinnedPieces[][] = new Pieces[18][2];
    boolean whitePiece;
    boolean pawnDoubleMove = false;
    boolean pawnEnPassant = false;
    boolean takingEnPassant = false;
    boolean notInCheck = true;
    int moveDirecion;
    int kingChecked = 0;
    String dictReference;
    int x;
    int y;
    Rectangle2D.Double[] rectangle;
    Pieces(int x,int y,piecEnum pieceType, boolean whitePiece){
        this.pieceType = pieceType;
        this.whitePiece = whitePiece;
        this.x = x;
        this.y = y;
            switch (pieceType) {
                case pawn:
                    spritingPieces(Sprites.pawn.length, Sprites.pawn);
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
                    spritingPieces(Sprites.knight.length, Sprites.knight);
                break;
                case knight:
                    spritingPieces(Sprites.knight.length, Sprites.knight);
                break;
                case bishop:
                    spritingPieces(Sprites.bishop.length, Sprites.bishop);
                break;
                case rook:
                spritingPieces(Sprites.rook.length, Sprites.rook);
                break;
                case queen:
                spritingPieces(Sprites.rook.length, Sprites.rook);
                break;
            }
    }
    public void spritingPieces(int length, int[][] spriteValues){
        rectangle = new Rectangle2D.Double[length];
        for(int i = 0; i < length; i++){
                rectangle[i] = new Rectangle2D.Double(this.x*Game.tileSize+Game.pixelSize*spriteValues[i][0],
                this.y*Game.tileSize+Game.pixelSize*spriteValues[i][1],Game.pixelSize*spriteValues[i][2],Game.pixelSize*spriteValues[i][3]);
        }
    }

    public void ApplyGamerules(int newX, int newY){
        //System.out.println('\u000c');
        for(int i = 0; i<8; i++){
            try {
                Game.pieces[(int)(Math.round((Game.pieceXY.get("wp"+Integer.toString(i))-(int)Math.floor(Game.pieceXY.get("wp"+Integer.toString(i))))*10))][(int)Math.floor(Game.pieceXY.get("wp"+Integer.toString(i)))].pawnEnPassant = false;
                Game.pieces[(int)(Math.round((Game.pieceXY.get("bp"+Integer.toString(i))-(int)Math.floor(Game.pieceXY.get("bp"+Integer.toString(i))))*10))][(int)Math.floor(Game.pieceXY.get("bp"+Integer.toString(i)))].pawnEnPassant = false;
            } catch (Exception e) {
            }
        }
        switch (pieceType) {
            case pawn:
            System.out.println(this.y);
                if((this.y+2 == newY)||(this.y-2 == newY)){
                    MovePiece(newX, newY);
                    Game.pieces[newY][newX].pawnEnPassant = true;
                }else if(takingEnPassant){
                    MovePiece(newX, newY);
                    Game.pieces[newY-moveDirecion][newX] = null;
                }else{
                    MovePiece(newX, newY);
                }
                Game.pieceXY.put(this.dictReference,Double.parseDouble(Integer.toString(this.x)+"."+Integer.toString(newY)));
            break;
            case king:
                Game.pieceXY.put(dictReference,Double.parseDouble(Integer.toString(newX)+"."+Integer.toString(newY)));
                MovePiece(newX, newY);
            break;
            default:
                MovePiece(newX, newY);
            break;
        }
    }
    public void MovePiece(int newX, int newY){
        Game.pieces[this.y][this.x] = null;
        Game.pieces[newY][newX] = new Pieces(newX, newY, this.pieceType, this.whitePiece);          
    }
    public void GetAvailableMoves(){
        for(int i = 0; i < 18; i++){
            pinnedPieces[i][0] = null;
            pinnedPieces[i][1] = null;
        }
        kingChecked = 0;
        System.out.println("start");
        for(int i = 0; i < 2; i++){
        Pathing(1,0,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,0);
        Pathing(-1,0,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,1);
        Pathing(0,1,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,2);
        Pathing(0,-1,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,3);
        Pathing(1,1,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,4);
        Pathing(-1,1,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,5);
        Pathing(1,-1,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,6);
        Pathing(-1,-1,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,7);
        Jumping(1,2,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,8);
        Jumping(2,1,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,9);
        Jumping(1,-2,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,10);
        Jumping(2,-1,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y,false,11);
        TakeSideways(this.whitePiece?-1:1,1, false, 16);     
        }
        ResolvingCheck();
    }
    private void normalMoves(){
        switch (pieceType) {
            case rook:
                Pathing(1,0,this.x,this.y,true,0);
                Pathing(-1,0,this.x,this.y,true,0);
                Pathing(0,1,this.x,this.y,true,0);
                Pathing(0,-1,this.x,this.y,true,0);
            break;
            case bishop:
                Pathing(1,1,this.x,this.y,true,0);
                Pathing(-1,1,this.x,this.y,true,0);
                Pathing(1,-1,this.x,this.y,true,0);
                Pathing(-1,-1,this.x,this.y,true,0);
            break;
            case queen:
                Pathing(1,0,this.x,this.y,true,0);
                Pathing(-1,0,this.x,this.y,true,0);
                Pathing(0,1,this.x,this.y,true,0);
                Pathing(0,-1,this.x,this.y,true,0);
                Pathing(1,1,this.x,this.y,true,0);
                Pathing(-1,1,this.x,this.y,true,0);
                Pathing(1,-1,this.x,this.y,true,0);
                Pathing(-1,-1,this.x,this.y,true,0);
            break;
            case knight:
                Jumping(1,2, this.x, this.y,true,0);
                Jumping(2,1, this.x, this.y,true,0);
                Jumping(1,-2, this.x, this.y,true,0);
                Jumping(2,-1, this.x, this.y,true,0);
            break;
            case king:
                Jumping(1,1, this.x, this.y,true,0);
                Jumping(0,1, this.x, this.y,true,0);
                Jumping(1,0, this.x, this.y,true,0);
                Jumping(0,-1, this.x, this.y,true,0);
                Jumping(1,-1, this.x, this.y,true,0);
            break;
            case pawn:
                takingEnPassant = false;
                if(((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3))){
                    this.pawnDoubleMove = true;
                }
                PawnMovement(this.moveDirecion);
                TakeSideways(this.moveDirecion, 1,true,0);
            break;
    }
    }
    private void Pathing(int xIncrement, int yIncrement, int xPos, int yPos, boolean movement, int storeSpot){
        try {
            if((xPos+xIncrement<Game.boardSize)&&(xPos+xIncrement>=0)&&(yPos+yIncrement<Game.boardSize)&&(yPos+yIncrement>=0)){
                if(movement){
                    if(Game.pieces[yPos+yIncrement][xPos+xIncrement] == null){
                        Pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement, movement, storeSpot);
                        ColourTiles(xPos+xIncrement, yPos+yIncrement);
                    }else if(Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece != this.whitePiece){
                        ColourTiles(xPos+xIncrement, yPos+yIncrement);
                    }
                }else if(Game.pieces[yPos+yIncrement][xPos+xIncrement] != null){
                    if(Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece == this.whitePiece){
                        pieceStoring(storeSpot, xPos+xIncrement, yPos+yIncrement,false);
                        if(pinnedPieces[storeSpot][1]== null){
                            Pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement, movement, storeSpot);
                        }
                    }else if((Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece != this.whitePiece)&&
                    (((xIncrement == 0)||(yIncrement == 0))&&((Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType == pieceType.rook)||(Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType == pieceType.queen)))||
                    ((((xIncrement == 1)||(xIncrement == -1))&&((yIncrement == 1)||(yIncrement == -1)))&&((Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType == pieceType.bishop)||(Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType == pieceType.queen)))){
                        pieceStoring(storeSpot, xPos+xIncrement, yPos+yIncrement,true);
                        if(pinnedPieces[storeSpot][1]== null){
                            Pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement, movement, storeSpot);
                        }
                    }
                }else{
                    Pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement, movement, storeSpot);
                }
            }
        } catch (Exception e) {
        }
    }
    private void Jumping(int xIncrease, int yIncrease, int xPos, int yPos, boolean movement, int storeSpot){
            if((xPos+xIncrease<Game.boardSize)&&(xPos+xIncrease>=0)&&(yPos+yIncrease<Game.boardSize)&&(yPos+yIncrease>=0)){
                if(movement){
                    if((Game.pieces[yPos+yIncrease][xPos+xIncrease] == null)||(Game.pieces[yPos+yIncrease][xPos+xIncrease].whitePiece != this.whitePiece)){
                        ColourTiles(xPos+xIncrease, yPos+yIncrease);
                    }
                }else if(Game.pieces[yPos+yIncrease][xPos+xIncrease] != null){
                    if((Game.pieces[yPos+yIncrease][xPos+xIncrease].whitePiece != this.whitePiece)&&(Game.pieces[yPos+yIncrease][xPos+xIncrease].pieceType == pieceType.knight)){
                        pieceStoring(storeSpot+4, xPos+xIncrease, yPos+yIncrease, true);
                    }else{
                        pieceStoring(storeSpot+4, xPos+xIncrease, yPos+yIncrease, false);
                    }
                }
            } 
            if((xPos-xIncrease<Game.boardSize)&&(xPos-xIncrease>=0)&&(yPos-yIncrease<Game.boardSize)&&(yPos-yIncrease>=0)){
                if(movement){
                    if((Game.pieces[yPos-yIncrease][xPos-xIncrease] == null)||(Game.pieces[yPos-yIncrease][xPos-xIncrease].whitePiece != this.whitePiece)){
                        ColourTiles(xPos-xIncrease, yPos-yIncrease);
                    }
                }else if(Game.pieces[yPos-yIncrease][xPos-xIncrease] != null){
                    if((Game.pieces[yPos-yIncrease][xPos-xIncrease].whitePiece != this.whitePiece)&&(Game.pieces[yPos-yIncrease][xPos-xIncrease].pieceType == pieceType.knight)){
                        pieceStoring(storeSpot+4, xPos-xIncrease, yPos-yIncrease, true);
                    }else{
                        pieceStoring(storeSpot+4, xPos-xIncrease, yPos-yIncrease, false);
                    }
                }
            } 
    }
    private void TakeSideways(int yIncrease, int xIncrease, boolean movement, int storeSpot){ 
        if(movement){
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
        }
        try {
            if(movement){
                if(Game.pieces[this.y+yIncrease][this.x-xIncrease].whitePiece != this.whitePiece){
                    ColourTiles(this.x-xIncrease, this.y+yIncrease);
                }
            }else if((Game.pieces[Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y+yIncrease][Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x-xIncrease].pieceType == pieceType.pawn)&&
            (Game.pieces[Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y+yIncrease][Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x-xIncrease].whitePiece != this.whitePiece)){
                pieceStoring(storeSpot, Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x-xIncrease,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y +yIncrease,true);
            }
        } catch (Exception e){
        }
        try{
            if(movement){
                if(Game.pieces[this.y+yIncrease][this.x+xIncrease].whitePiece != this.whitePiece){
                    ColourTiles(this.x+xIncrease, this.y+yIncrease);
                }
            }else if((Game.pieces[Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y+yIncrease][Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x+xIncrease].pieceType == pieceType.pawn)&&
            (Game.pieces[Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y+yIncrease][Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x+xIncrease].whitePiece != this.whitePiece)){
                pieceStoring(storeSpot, Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x+xIncrease,Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y +yIncrease,true);
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
        Game.tiles[yOfTile][xOfTile].colour = Game.moveColour;
        Mouse_Input.firstClick = false;
    }
    
    private void pieceStoring(int storeSpot, int storeX, int storeY, boolean enemyPiece){
        if(enemyPiece){
            if(pinnedPieces[storeSpot][0] == null){
                pinnedPieces[storeSpot][0] = new Pieces(storeX, storeY, piecEnum.enemyPiece, enemyPiece);
                kingChecked++;
            }else if(pinnedPieces[storeSpot][1] == null){
                pinnedPieces[storeSpot][1] = new Pieces(storeX, storeY, piecEnum.enemyPiece, enemyPiece);
            }
        }else{
            if(pinnedPieces[storeSpot][0] == null){
                pinnedPieces[storeSpot][0] = new Pieces(storeX, storeY, Game.pieces[storeY][storeX].pieceType, !enemyPiece);
            }else if(pinnedPieces[storeSpot][1] == null){
                pinnedPieces[storeSpot][1] = new Pieces(storeX, storeY, Game.pieces[storeY][storeX].pieceType, !enemyPiece);
            }
        }
        System.out.println("pinnedPieces["+storeSpot+"][0],    "+pinnedPieces[storeSpot][0].pieceType+":"+pinnedPieces[storeSpot][0].x+","+pinnedPieces[storeSpot][0].y+"    kingCheck: "+kingChecked);
    }
    private void ResolvingCheck(){
            switch (pieceType) {
                case rook:
                    if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){// didn't know howelse i could write this
                    }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){//specifically dont use for statement
                        if((notDoubleChecked())){
                            ColourTiles(pinnedPieces[0][1].x,pinnedPieces[0][1].y);
                        }
                    }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                        if((notDoubleChecked())){
                            ColourTiles(pinnedPieces[1][1].x,pinnedPieces[1][1].y);
                        }
                    }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                        if((notDoubleChecked())){
                            ColourTiles(pinnedPieces[2][1].x,pinnedPieces[2][1].y);
                        }
                    }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                        if((notDoubleChecked())){
                            ColourTiles(pinnedPieces[3][1].x,pinnedPieces[3][1].y);
                        }
                    }else if(kingChecked == 1){
                        if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x)&&(this.x <= pinnedPieces[0][0].x)){
                                checkVertical(pinnedPieces[0][0].y);
                            }else if((pinnedPieces[0][1] != null)&&(pinnedPieces[0][1].pieceType == piecEnum.rook)){
                                ColourTiles(pinnedPieces[0][0].x,pinnedPieces[0][0].y);
                            }
                        }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x)&&(this.x >= pinnedPieces[1][0].x)){
                                checkVertical(pinnedPieces[1][0].y);
                            }else if((pinnedPieces[1][1] != null)&&(pinnedPieces[1][1].pieceType == piecEnum.rook)){
                                ColourTiles(pinnedPieces[1][0].x,pinnedPieces[0][0].y);
                            }
                        }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == piecEnum.enemyPiece)){
                            if((this.y > Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y)&&(this.y <= pinnedPieces[2][0].y)){
                                checkHorizontal(pinnedPieces[2][0].x);
                            }else if((pinnedPieces[2][1] != null)&&(pinnedPieces[2][1].pieceType == piecEnum.rook)){
                                ColourTiles(pinnedPieces[2][0].x,pinnedPieces[2][0].y);
                            }
                        }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == piecEnum.enemyPiece)){
                            if((this.y < Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y)&&(this.y >= pinnedPieces[3][0].y)){
                                checkHorizontal(pinnedPieces[3][0].x);
                            }else if((pinnedPieces[3][1] != null)&&(pinnedPieces[3][1].pieceType == piecEnum.rook)){
                                ColourTiles(pinnedPieces[3][0].x,pinnedPieces[3][0].y);
                            }
                        }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x)&&(this.x <= pinnedPieces[4][0].x)){
                                checkVertical(pinnedPieces[4][0].y-(Math.max(pinnedPieces[4][0].x,this.x)-Math.min(pinnedPieces[4][0].x,this.x)));
                            }
                            if((this.y > Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y)&&(this.y <= pinnedPieces[4][0].y)){
                                checkHorizontal(pinnedPieces[4][0].x-(Math.max(pinnedPieces[4][0].y,this.y)-Math.min(pinnedPieces[4][0].y,this.y)));
                            }
                        }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x)&&(this.x >= pinnedPieces[5][0].x)){
                                checkVertical(pinnedPieces[5][0].y-(Math.max(pinnedPieces[5][0].x,this.x)-Math.min(pinnedPieces[5][0].x,this.x)));
                            }
                            if((this.y > Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y)&&(this.y <= pinnedPieces[5][0].y)){
                                checkHorizontal(pinnedPieces[5][0].x+(Math.max(pinnedPieces[5][0].y,this.y)-Math.min(pinnedPieces[5][0].y,this.y)));
                            }
                        }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x)&&(this.x <= pinnedPieces[6][0].x)){
                                checkVertical(pinnedPieces[6][0].y+(Math.max(pinnedPieces[6][0].x,this.x)-Math.min(pinnedPieces[6][0].x,this.x)));
                            }
                            if((this.y < Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y)&&(this.y >= pinnedPieces[6][0].y)){
                                checkHorizontal(pinnedPieces[6][0].x-(Math.max(pinnedPieces[6][0].y,this.y)-Math.min(pinnedPieces[6][0].y,this.y)));
                            }
                        }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].x)&&(this.x >= pinnedPieces[7][0].x)){
                                checkVertical(pinnedPieces[7][0].y+(Math.max(pinnedPieces[7][0].x,this.x)-Math.min(pinnedPieces[7][0].x,this.x)));
                            }
                            if((this.y < Game.pieces[(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))][(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))].y)&&(this.y >= pinnedPieces[7][0].y)){
                                checkHorizontal(pinnedPieces[7][0].x+(Math.max(pinnedPieces[7][0].y,this.y)-Math.min(pinnedPieces[7][0].y,this.y)));
                            }
                        }else for(int i = 8; i < 18; i++){
                            if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == piecEnum.enemyPiece)){
                                if(this.x == pinnedPieces[i][0].x){
                                    checkVertical(pinnedPieces[i][0].y);
                                }else if(this.y == pinnedPieces[i][0].y){
                                    checkHorizontal(pinnedPieces[i][0].x);
                                }
                            }
                        }
                    }else{
                        Pathing(1,0,this.x,this.y,true,0);
                        Pathing(-1,0,this.x,this.y,true,0);
                        Pathing(0,1,this.x,this.y,true,0);
                        Pathing(0,-1,this.x,this.y,true,0);
                    }
                break;
                default:
                    normalMoves();
                break;
            }
    }
    private void checkVertical(int enemyPosition){
        for(int upOrDown = this.y; upOrDown != enemyPosition;){
            upOrDown = upOrDown + (this.y > enemyPosition ? -1:+1);
            if(upOrDown == enemyPosition ){
                ColourTiles(this.x,enemyPosition);
            }else if(Game.pieces[upOrDown][this.x] != null){
                upOrDown = enemyPosition;
            }
        }
    }
    private void checkHorizontal(int enemyPosition){
        for(int leftOrRight = this.x; leftOrRight != enemyPosition;){
            leftOrRight = leftOrRight + (this.x > enemyPosition ? -1:+1);
            if(leftOrRight == enemyPosition ){
                ColourTiles(enemyPosition, this.y);
            }else if(Game.pieces[this.y][leftOrRight] != null){
                leftOrRight = enemyPosition;
            }
        }
    }
    private boolean notDoubleChecked(){
        for(int i = 0; i < 18; i++){
            if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == pieceType.enemyPiece)){
                return false;
            }
        }
        return true;
    }
}

