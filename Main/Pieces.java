package Main;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.concurrent.CountDownLatch;
public class Pieces {
    public enum piecEnum{pawn,knight,bishop,rook,queen,king,enemyPiece}
    piecEnum pieceType;
    piecEnum pinnedPieces[][] = new piecEnum[12][2];  
    boolean whitePiece;
    boolean pawnDoubleMove = false;
    boolean pawnEnPassant = false;
    boolean takingEnPassant = false;
    boolean notInCheck = true;
    int moveDirecion;
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
        this.x = newX;
        this.y = newY;
        Game.pieces[this.y][this.x] = new Pieces(this.x, this.y, this.pieceType, this.whitePiece);
        try {
            Game.pieceXY.put(dictReference, Game.pieceXY.put(dictReference,Double.parseDouble(Integer.toString(this.x)+"."+Integer.toString(this.y))));
        } catch (Exception e) {
        }
            
    }
    public void GetAvailableMoves(){
        if(this.whitePiece){
            checkChecker(piecEnum.king, (int)Math.floor(Game.pieceXY.get("wk")), (int)(Math.round((Game.pieceXY.get("wk")-(int)Math.floor(Game.pieceXY.get("wk")))*10)), 0, 1);
        }else{
            checkChecker(piecEnum.king, (int)Math.floor(Game.pieceXY.get("bk")), (int)(Math.round((Game.pieceXY.get("bk")-(int)Math.floor(Game.pieceXY.get("bk")))*10)), 0, 1);
        }
        if(notInCheck){
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
            }
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
    private void checkChecker(piecEnum checkedPiece, int checkX, int checkY, int doing, int increase){
        switch (checkedPiece) {
            case king:
                switch (doing) {
                    case 0:
                        if(checkY - increase>=0){
                            if(increase == 2){

                                try {
                                    if((Game.pieces[checkY-increase][checkX-1].whitePiece != this.whitePiece)&&(Game.pieces[checkY-increase][checkX-1].pieceType == piecEnum.knight)){ 
                                        pieceStoring(4, checkX-1, checkY-increase, true);
                                    }
                                } catch (Exception e) {
                                }
                                try {
                                    if((Game.pieces[checkY-increase][checkX+1].whitePiece != this.whitePiece)&&(Game.pieces[checkY-increase][checkX+1].pieceType == piecEnum.knight)){ 
                                        pieceStoring(5, checkX+1, checkY-increase, true);
                                    }
                                } catch (Exception e) {
                                }
                            }
                            if(Game.pieces[checkY-increase][checkX] != null){
                                if(Game.pieces[checkY-increase][checkX].whitePiece == this.whitePiece){
                                    pieceStoring(doing, checkX, checkY-increase, false);
                                }else if((Game.pieces[checkY-increase][checkX].whitePiece != this.whitePiece)&&((Game.pieces[checkY-increase][checkX].pieceType == piecEnum.rook)||(Game.pieces[checkY-increase][checkX].pieceType == piecEnum.queen))){ 
                                    pieceStoring(doing, checkX, checkY-increase, true);
                                }
                             }
                            checkChecker(piecEnum.king, checkX,checkY,doing,increase+1);
                        }else{
                            checkChecker(piecEnum.king, checkX,checkY,doing+1,1);
                        }
                    break;
                    case 1:
                        if(checkY + increase<Game.boardSize){
                            if(increase == 2){
                                try {
                                    if((Game.pieces[checkY+increase][checkX-1].whitePiece != this.whitePiece)&&(Game.pieces[checkY+increase][checkX-1].pieceType == piecEnum.knight)){ 
                                        pieceStoring(6, checkX-1, checkY-increase, true);
                                    }
                                } catch (Exception e) {
                                }
                                try {
                                    if((Game.pieces[checkY+increase][checkX+1].whitePiece != this.whitePiece)&&(Game.pieces[checkY+increase][checkX+1].pieceType == piecEnum.knight)){ 
                                        pieceStoring(7, checkX+1, checkY-increase, true);
                                    }
                                } catch (Exception e) {
                                }
                            }
                            if(Game.pieces[checkY+increase][checkX] != null){
                                if(Game.pieces[checkY+increase][checkX].whitePiece == this.whitePiece){
                                    pieceStoring(doing, checkX, checkY+increase, false);
                                }else if((Game.pieces[checkY+increase][checkX].whitePiece != this.whitePiece)&&((Game.pieces[checkY+increase][checkX].pieceType == piecEnum.rook)||(Game.pieces[checkY+increase][checkX].pieceType == piecEnum.queen))){ 
                                    pieceStoring(doing, checkX, checkY+increase, true);
                                }
                            }
                            checkChecker(piecEnum.king, checkX,checkY,doing,increase+1);
                        }else{
                            //checkChecker(piecEnum.king, checkX,checkY,doing++,1);
                        }
                    break;
                }
            break;
        }
    }

    private void pieceStoring(int storeSpot, int storeX, int storeY, boolean enemyPiece){
        if(enemyPiece){
            if(pinnedPieces[storeSpot][0] == null){
                pinnedPieces[storeSpot][0] = piecEnum.enemyPiece;
            }else if(pinnedPieces[storeSpot][1] == null){
                pinnedPieces[storeSpot][1] = piecEnum.enemyPiece;
            }
        }else{
            if(pinnedPieces[storeSpot][0] == null){
                pinnedPieces[storeSpot][0] = Game.pieces[storeY][storeX].pieceType;
            }else if(pinnedPieces[storeSpot][1] == null){
                pinnedPieces[storeSpot][1] = Game.pieces[storeY][storeX].pieceType;
            }
        }
        System.out.println(pinnedPieces[storeSpot][0]+","+pinnedPieces[storeSpot][1]);
    }
}