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
        for(int i = 0; i < 2; i++){
        Pathing(1,0,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,0);
        Pathing(-1,0,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,1);
        Pathing(0,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,2);
        Pathing(0,-1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,3);
        Pathing(1,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,4);
        Pathing(-1,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,5);
        Pathing(1,-1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,6);
        Pathing(-1,-1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,7);
        Jumping(1,2,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,8);
        Jumping(2,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,9);
        Jumping(1,-2,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,10);
        Jumping(2,-1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,11);
        TakeSideways(this.whitePiece?-1:1,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")), (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)), false, 16);     
        }
        ResolvingCheck();
    }
    private void normalMoves(){
        switch (pieceType) {
            /*case rook:
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
            break;*/
            /*case queen:
                Pathing(1,0,this.x,this.y,true,0);
                Pathing(-1,0,this.x,this.y,true,0);
                Pathing(0,1,this.x,this.y,true,0);
                Pathing(0,-1,this.x,this.y,true,0);
                Pathing(1,1,this.x,this.y,true,0);
                Pathing(-1,1,this.x,this.y,true,0);
                Pathing(1,-1,this.x,this.y,true,0);
                Pathing(-1,-1,this.x,this.y,true,0);
            break;*/
            /*case knight:
                Jumping(1,2, this.x, this.y,true,0);
                Jumping(2,1, this.x, this.y,true,0);
                Jumping(1,-2, this.x, this.y,true,0);
                Jumping(2,-1, this.x, this.y,true,0);
            break;*/
            case king:
                Jumping(1,1, this.x, this.y,true,0);
                Jumping(0,1, this.x, this.y,true,0);
                Jumping(1,0, this.x, this.y,true,0);
                Jumping(0,-1, this.x, this.y,true,0);
                Jumping(1,-1, this.x, this.y,true,0);
            break;
            /*case pawn:
                takingEnPassant = false;
                PawnMovement(this.moveDirecion);
                TakeSideways(this.moveDirecion, 1,true,0);
            break;*/
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
    private void TakeSideways(int yIncrease, int xIncrease, int xPos, int yPos, boolean movement, int storeSpot){ 
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
            }else if((Game.pieces[/*(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))*/yPos+yIncrease][/*(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))*/xPos-xIncrease].pieceType == pieceType.pawn)&&
            (Game.pieces[/*(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))*/yPos+yIncrease][/*(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))*/xPos-xIncrease].whitePiece != this.whitePiece)){
                pieceStoring(storeSpot, /*(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))*/(xPos-xIncrease),/*(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))*/(yPos+yIncrease),true);
                System.out.println("x: "+(xPos-xIncrease)+", y: "+(yPos+yIncrease)+", storespot: "+storeSpot);
            }
        } catch (Exception e){
        }
        try{
            if(movement){
                if(Game.pieces[this.y+yIncrease][this.x+xIncrease].whitePiece != this.whitePiece){
                    ColourTiles(this.x+xIncrease, this.y+yIncrease);
                }
            }else if((Game.pieces[/*(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))*/yPos+yIncrease][/*(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))*/xPos+xIncrease].pieceType == pieceType.pawn)&&
            (Game.pieces[/*(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))*/yPos+yIncrease][/*(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))*/xPos+xIncrease].whitePiece != this.whitePiece)){
                pieceStoring(storeSpot+1,/*(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))*/xPos+xIncrease,/*(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))*/yPos+yIncrease,true);
            }
        } catch (Exception e) {
        }
    }
    private void PawnMovement(int yIncrease){
        System.out.println(yIncrease);
        if((yIncrease == (this.whitePiece?-1:1))&&(((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3)))){
            if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                if(Game.pieces[this.y+yIncrease][this.x] == null){
                    PawnMovement(yIncrease+(this.whitePiece?-1:1));
                    ColourTiles(this.x, this.y+yIncrease);
                }
            }
        }else{
            if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                if(Game.pieces[this.y+yIncrease][this.x] == null){
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
        //System.out.println("pinnedPieces["+storeSpot+"][0],    "+pinnedPieces[storeSpot][0].pieceType+":"+pinnedPieces[storeSpot][0].x+","+pinnedPieces[storeSpot][0].y+"    kingCheck: "+kingChecked);
    }
    private void ResolvingCheck(){
            switch (pieceType) {
                case rook:                        
                    if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[4][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){// didn't know howelse i could write this
                    }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){//specifically dont use for statement
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[0][1].x,pinnedPieces[0][1].y);
                        }
                    }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[1][1].x,pinnedPieces[1][1].y);
                        }
                    }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[2][1].x,pinnedPieces[2][1].y);
                        }
                    }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[3][1].x,pinnedPieces[3][1].y);
                        }
                    }else if(kingChecked == 1){
                        if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[0][0].x)){
                                checkVertical(pinnedPieces[0][0].y);
                            }else if((pinnedPieces[0][1] != null)&&((pinnedPieces[0][1].x == this.x)&&(pinnedPieces[0][1].y == this.y))){
                                ColourTiles(pinnedPieces[0][0].x,pinnedPieces[0][0].y);
                            }
                        }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x >= pinnedPieces[1][0].x)){
                                checkVertical(pinnedPieces[1][0].y);
                            }else if((pinnedPieces[1][1] != null)&&((pinnedPieces[1][1].x == this.x)&&(pinnedPieces[1][1].y == this.y))){
                                ColourTiles(pinnedPieces[1][0].x,pinnedPieces[1][0].y);
                            }
                        }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == piecEnum.enemyPiece)){
                            if((this.y > (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y <= pinnedPieces[2][0].y)){
                                checkHorizontal(pinnedPieces[2][0].x);
                            }else if((pinnedPieces[2][1] != null)&&((pinnedPieces[2][1].x == this.x)&&(pinnedPieces[2][1].y == this.y))){
                                ColourTiles(pinnedPieces[2][0].x,pinnedPieces[2][0].y);
                            }
                        }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == piecEnum.enemyPiece)){
                            if((this.y < (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y >= pinnedPieces[3][0].y)){
                                checkHorizontal(pinnedPieces[3][0].x);
                            }else if((pinnedPieces[3][1] != null)&&((pinnedPieces[3][1].x == this.x)&&(pinnedPieces[3][1].y == this.y))){
                                ColourTiles(pinnedPieces[3][0].x,pinnedPieces[3][0].y);
                            }
                        }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[4][0].x)){
                                checkVertical(pinnedPieces[4][0].y-(Math.max(pinnedPieces[4][0].x,this.x)-Math.min(pinnedPieces[4][0].x,this.x)));
                            }
                            if((this.y > (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y <= pinnedPieces[4][0].y)){
                                checkHorizontal(pinnedPieces[4][0].x-(Math.max(pinnedPieces[4][0].y,this.y)-Math.min(pinnedPieces[4][0].y,this.y)));
                            }
                        }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x >= pinnedPieces[5][0].x)){
                                checkVertical(pinnedPieces[5][0].y-(Math.max(pinnedPieces[5][0].x,this.x)-Math.min(pinnedPieces[5][0].x,this.x)));
                            }
                            if((this.y > (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y <= pinnedPieces[5][0].y)){
                                checkHorizontal(pinnedPieces[5][0].x+(Math.max(pinnedPieces[5][0].y,this.y)-Math.min(pinnedPieces[5][0].y,this.y)));
                            }
                        }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[6][0].x)){
                                checkVertical(pinnedPieces[6][0].y+(Math.max(pinnedPieces[6][0].x,this.x)-Math.min(pinnedPieces[6][0].x,this.x)));
                            }
                            if((this.y < (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y >= pinnedPieces[6][0].y)){
                                checkHorizontal(pinnedPieces[6][0].x-(Math.max(pinnedPieces[6][0].y,this.y)-Math.min(pinnedPieces[6][0].y,this.y)));
                            }
                        }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x >= pinnedPieces[7][0].x)){
                                checkVertical(pinnedPieces[7][0].y+(Math.max(pinnedPieces[7][0].x,this.x)-Math.min(pinnedPieces[7][0].x,this.x)));
                            }
                            if((this.y < (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y >= pinnedPieces[7][0].y)){
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
                    }else if(kingChecked == 0){
                        Pathing(1,0,this.x,this.y,true,0);
                        Pathing(-1,0,this.x,this.y,true,0);
                        Pathing(0,1,this.x,this.y,true,0);
                        Pathing(0,-1,this.x,this.y,true,0);
                    }
                break;
                case bishop:
                    try{
                        if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){// didn't know howelse i could write this
                        }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                        }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                        }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                        }else if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[4][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){//specifically dont use for statement
                            if(kingChecked != 2){
                                ColourTiles(pinnedPieces[4][1].x,pinnedPieces[4][1].y);
                            }
                        }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                            if(kingChecked != 2){
                                ColourTiles(pinnedPieces[5][1].x,pinnedPieces[5][1].y);
                            }
                        }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                            if(kingChecked != 2){
                                ColourTiles(pinnedPieces[6][1].x,pinnedPieces[6][1].y);
                            }
                        }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                            if(kingChecked != 2){
                                ColourTiles(pinnedPieces[7][1].x,pinnedPieces[7][1].y);
                            }
                        }else if(kingChecked == 1){
                            //not sure if i should ase a for statement as then i would still have to repeat checks
                            if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == piecEnum.enemyPiece)){
                                for(int i = 0; i < Math.max(pinnedPieces[0][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))
                                -Math.min(pinnedPieces[0][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                    if(Math.max(this.x,pinnedPieces[0][0].x-i)-Math.min(this.x,pinnedPieces[0][0].x-i) == Math.max(this.y, pinnedPieces[0][0].y)-Math.min(this.y,pinnedPieces[0][0].y)){
                                        checkDiagonals(pinnedPieces[0][0].x-i, pinnedPieces[0][0].y, pinnedPieces[0][0].x-i, pinnedPieces[0][0].y, this.x > pinnedPieces[0][0].x-i ? 1:-1, this.y > pinnedPieces[0][0].y ? 1:-1);
                                    }
                                }
                            }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == piecEnum.enemyPiece)){
                                for(int i = 0; i < Math.max(pinnedPieces[1][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))
                                -Math.min(pinnedPieces[1][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                    if(Math.max(this.x,pinnedPieces[1][0].x+i)-Math.min(this.x,pinnedPieces[1][0].x+i) == Math.max(this.y, pinnedPieces[1][0].y)-Math.min(this.y,pinnedPieces[1][0].y)){
                                        checkDiagonals(pinnedPieces[1][0].x+i, pinnedPieces[1][0].y, pinnedPieces[1][0].x+i, pinnedPieces[1][0].y, this.x > pinnedPieces[1][0].x+i ? 1:-1, this.y > pinnedPieces[1][0].y ? 1:-1);
                                    }
                                }
                            }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == piecEnum.enemyPiece)){
                                for(int i = 0; i < Math.max(pinnedPieces[2][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[2][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[2][0].x)-Math.min(this.x,pinnedPieces[2][0].x) == Math.max(this.y, pinnedPieces[2][0].y-i)-Math.min(this.y,pinnedPieces[2][0].y-i)){
                                        checkDiagonals(pinnedPieces[2][0].x, pinnedPieces[2][0].y-i, pinnedPieces[2][0].x, pinnedPieces[2][0].y-i, this.x > pinnedPieces[2][0].x ? 1:-1, this.y > pinnedPieces[2][0].y-i ? 1:-1);
                                    }
                                }
                            }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == piecEnum.enemyPiece)){
                                for(int i = 0; i < Math.max(pinnedPieces[3][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[3][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[3][0].x)-Math.min(this.x,pinnedPieces[3][0].x) == Math.max(this.y, pinnedPieces[3][0].y+i)-Math.min(this.y,pinnedPieces[3][0].y+i)){
                                        checkDiagonals(pinnedPieces[3][0].x, pinnedPieces[3][0].y+i, pinnedPieces[3][0].x, pinnedPieces[3][0].y+i, this.x > pinnedPieces[3][0].x ? 1:-1, this.y > pinnedPieces[3][0].y+i ? 1:-1);
                                    }
                                }
                            }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == piecEnum.enemyPiece)){
                                for(int i = 0; i < Math.max(pinnedPieces[4][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[4][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[4][0].x-i)-Math.min(this.x,pinnedPieces[4][0].x-i) == Math.max(this.y, pinnedPieces[4][0].y-i)-Math.min(this.y,pinnedPieces[4][0].y-i)){
                                        checkDiagonals(pinnedPieces[4][0].x-i, pinnedPieces[4][0].y-i, pinnedPieces[4][0].x-i, pinnedPieces[4][0].y-i, this.x > pinnedPieces[4][0].x-i ? 1:-1, this.y > pinnedPieces[4][0].y-i ? 1:-1);
                                    }
                                }
                            }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][0].pieceType == piecEnum.enemyPiece)){
                                for(int i = 0; i < Math.max(pinnedPieces[5][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[5][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[5][0].x+i)-Math.min(this.x,pinnedPieces[5][0].x+i) == Math.max(this.y, pinnedPieces[5][0].y-i)-Math.min(this.y,pinnedPieces[5][0].y-i)){
                                        checkDiagonals(pinnedPieces[5][0].x+i, pinnedPieces[5][0].y-i, pinnedPieces[5][0].x+i, pinnedPieces[5][0].y-i, this.x > pinnedPieces[5][0].x+i ? 1:-1, this.y > pinnedPieces[5][0].y-i ? 1:-1);
                                    }
                                }
                            }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][0].pieceType == piecEnum.enemyPiece)){
                                for(int i = 0; i < Math.max(pinnedPieces[6][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[6][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[6][0].x-i)-Math.min(this.x,pinnedPieces[6][0].x-i) == Math.max(this.y, pinnedPieces[6][0].y+i)-Math.min(this.y,pinnedPieces[6][0].y+i)){
                                        checkDiagonals(pinnedPieces[6][0].x-i, pinnedPieces[6][0].y+i, pinnedPieces[6][0].x-i, pinnedPieces[6][0].y+i, this.x > pinnedPieces[6][0].x-i ? 1:-1, this.y > pinnedPieces[6][0].y+i ? 1:-1);
                                    }
                                }
                            }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == piecEnum.enemyPiece)){
                                for(int i = 0; i < Math.max(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[7][0].x+i)-Math.min(this.x,pinnedPieces[7][0].x+i) == Math.max(this.y, pinnedPieces[7][0].y+i)-Math.min(this.y,pinnedPieces[7][0].y+i)){
                                        checkDiagonals(pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i, pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i, this.x > pinnedPieces[7][0].x+i ? 1:-1, this.y > pinnedPieces[7][0].y+i ? 1:-1);
                                    }
                                }
                            }else for(int i = 8; i < 18; i++){
                                if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == piecEnum.enemyPiece)){
                                    if(Math.max(this.x,pinnedPieces[i][0].x)-Math.min(this.x,pinnedPieces[i][0].x) == Math.max(this.y, pinnedPieces[i][0].y)-Math.min(this.y,pinnedPieces[i][0].y)){
                                        checkDiagonals(pinnedPieces[i][0].x, pinnedPieces[i][0].y, pinnedPieces[i][0].x, pinnedPieces[i][0].y, this.x > pinnedPieces[i][0].x ? 1:-1, this.y > pinnedPieces[i][0].y ? 1:-1);
                                    }
                                }
                            }
                        }else if(kingChecked == 0){
                            Pathing(1,1,this.x,this.y,true,0);
                            Pathing(-1,1,this.x,this.y,true,0);
                            Pathing(1,-1,this.x,this.y,true,0);
                            Pathing(-1,-1,this.x,this.y,true,0);
                        }  
                    } catch (Exception e) {
                    }
                break;
                case knight:
                    if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){// didn't know howelse i could write this
                    }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[4][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){// didn't know howelse i could write this
                    }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                    }else if(kingChecked == 1){
                        if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[0][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))
                            -Math.min(pinnedPieces[0][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                if(((Math.max(this.x,pinnedPieces[0][0].x-i)-Math.min(this.x,pinnedPieces[0][0].x-i) == 2)&&
                                (Math.max(this.y,pinnedPieces[0][0].y)-Math.min(this.y,pinnedPieces[0][0].y) == 1))||
                                ((Math.max(this.x,pinnedPieces[0][0].x-i)-Math.min(this.x,pinnedPieces[0][0].x-i) == 1)&&
                                (Math.max(this.y,pinnedPieces[0][0].y)-Math.min(this.y,pinnedPieces[0][0].y) == 2))){
                                    ColourTiles(pinnedPieces[0][0].x-i, pinnedPieces[0][0].y);
                                }
                            };
                        }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[1][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))
                            -Math.min(pinnedPieces[1][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                if(((Math.max(this.x,pinnedPieces[1][0].x+i)-Math.min(this.x,pinnedPieces[1][0].x+i) == 2)&&
                                (Math.max(this.y,pinnedPieces[1][0].y)-Math.min(this.y,pinnedPieces[1][0].y) == 1))||
                                ((Math.max(this.x,pinnedPieces[1][0].x+i)-Math.min(this.x,pinnedPieces[1][0].x+i) == 1)&&
                                (Math.max(this.y,pinnedPieces[1][0].y)-Math.min(this.y,pinnedPieces[1][0].y) == 2))){
                                    ColourTiles(pinnedPieces[1][0].x+i, pinnedPieces[1][0].y);
                                }
                            };
                        }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[2][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                            -Math.min(pinnedPieces[2][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                if(((Math.max(this.x,pinnedPieces[2][0].x)-Math.min(this.x,pinnedPieces[2][0].x) == 2)&&
                                (Math.max(this.y,pinnedPieces[2][0].y-i)-Math.min(this.y,pinnedPieces[2][0].y-i) == 1))||
                                ((Math.max(this.x,pinnedPieces[2][0].x)-Math.min(this.x,pinnedPieces[2][0].x) == 1)&&
                                (Math.max(this.y,pinnedPieces[2][0].y-i)-Math.min(this.y,pinnedPieces[2][0].y-i) == 2))){
                                    ColourTiles(pinnedPieces[2][0].x, pinnedPieces[2][0].y-i);
                                }
                            };
                        }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[3][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                            -Math.min(pinnedPieces[3][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                if(((Math.max(this.x,pinnedPieces[3][0].x)-Math.min(this.x,pinnedPieces[3][0].x) == 2)&&
                                (Math.max(this.y,pinnedPieces[3][0].y+i)-Math.min(this.y,pinnedPieces[3][0].y+i) == 1))||
                                ((Math.max(this.x,pinnedPieces[3][0].x)-Math.min(this.x,pinnedPieces[3][0].x) == 1)&&
                                (Math.max(this.y,pinnedPieces[3][0].y+i)-Math.min(this.y,pinnedPieces[3][0].y+i) == 2))){
                                    ColourTiles(pinnedPieces[3][0].x, pinnedPieces[3][0].y+i);
                                }
                            };
                        }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[4][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                            -Math.min(pinnedPieces[4][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                if(((Math.max(this.x,pinnedPieces[4][0].x-i)-Math.min(this.x,pinnedPieces[4][0].x-i) == 2)&&
                                (Math.max(this.y,pinnedPieces[4][0].y-i)-Math.min(this.y,pinnedPieces[4][0].y-i) == 1))||
                                ((Math.max(this.x,pinnedPieces[4][0].x-i)-Math.min(this.x,pinnedPieces[4][0].x-i) == 1)&&
                                (Math.max(this.y,pinnedPieces[4][0].y-i)-Math.min(this.y,pinnedPieces[4][0].y-i) == 2))){
                                    ColourTiles(pinnedPieces[4][0].x-i, pinnedPieces[4][0].y-i);
                                }
                            };
                        }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[5][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                            -Math.min(pinnedPieces[5][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                if(((Math.max(this.x,pinnedPieces[5][0].x+i)-Math.min(this.x,pinnedPieces[5][0].x+i) == 2)&&
                                (Math.max(this.y,pinnedPieces[5][0].y-i)-Math.min(this.y,pinnedPieces[5][0].y-i) == 1))||
                                ((Math.max(this.x,pinnedPieces[5][0].x+i)-Math.min(this.x,pinnedPieces[5][0].x+i) == 1)&&
                                (Math.max(this.y,pinnedPieces[5][0].y-i)-Math.min(this.y,pinnedPieces[5][0].y-i) == 2))){
                                    ColourTiles(pinnedPieces[5][0].x+i, pinnedPieces[5][0].y-i);
                                }
                            };
                        }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[6][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                            -Math.min(pinnedPieces[6][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                if(((Math.max(this.x,pinnedPieces[6][0].x-i)-Math.min(this.x,pinnedPieces[6][0].x-i) == 2)&&
                                (Math.max(this.y,pinnedPieces[6][0].y+i)-Math.min(this.y,pinnedPieces[6][0].y+i) == 1))||
                                ((Math.max(this.x,pinnedPieces[6][0].x-i)-Math.min(this.x,pinnedPieces[6][0].x-i) == 1)&&
                                (Math.max(this.y,pinnedPieces[6][0].y+i)-Math.min(this.y,pinnedPieces[6][0].y+i) == 2))){
                                    ColourTiles(pinnedPieces[6][0].x-i, pinnedPieces[6][0].y+i);
                                }
                            };
                        }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                            -Math.min(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                if(((Math.max(this.x,pinnedPieces[7][0].x+i)-Math.min(this.x,pinnedPieces[7][0].x+i) == 2)&&
                                (Math.max(this.y,pinnedPieces[7][0].y+i)-Math.min(this.y,pinnedPieces[7][0].y+i) == 1))||
                                ((Math.max(this.x,pinnedPieces[7][0].x+i)-Math.min(this.x,pinnedPieces[7][0].x+i) == 1)&&
                                (Math.max(this.y,pinnedPieces[7][0].y+i)-Math.min(this.y,pinnedPieces[7][0].y+i) == 2))){
                                    ColourTiles(pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i);
                                }
                            };
                        }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == piecEnum.enemyPiece)){
                            for(int i = 0; i < Math.max(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                            -Math.min(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                if(((Math.max(this.x,pinnedPieces[7][0].x+i)-Math.min(this.x,pinnedPieces[7][0].x+i) == 2)&&
                                (Math.max(this.y,pinnedPieces[7][0].y+i)-Math.min(this.y,pinnedPieces[7][0].y+i) == 1))||
                                ((Math.max(this.x,pinnedPieces[7][0].x+i)-Math.min(this.x,pinnedPieces[7][0].x+i) == 1)&&
                                (Math.max(this.y,pinnedPieces[7][0].y+i)-Math.min(this.y,pinnedPieces[7][0].y+i) == 2))){
                                    ColourTiles(pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i);
                                }
                            };
                        }else for(int i = 8; i < 18;i++){
                        if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == piecEnum.enemyPiece)){
                                if(((Math.max(this.x,pinnedPieces[i][0].x)-Math.min(this.x,pinnedPieces[i][0].x) == 2)&&
                                (Math.max(this.y,pinnedPieces[i][0].y)-Math.min(this.y,pinnedPieces[i][0].y) == 1))||
                                ((Math.max(this.x,pinnedPieces[i][0].x)-Math.min(this.x,pinnedPieces[i][0].x) == 1)&&
                                (Math.max(this.y,pinnedPieces[i][0].y)-Math.min(this.y,pinnedPieces[i][0].y) == 2))){
                                    ColourTiles(pinnedPieces[i][0].x, pinnedPieces[i][0].y);
                                }
                            };
                        }
                    }else if(kingChecked == 0){
                        Jumping(1,2, this.x, this.y,true,0);
                        Jumping(2,1, this.x, this.y,true,0);
                        Jumping(1,-2, this.x, this.y,true,0);
                        Jumping(2,-1, this.x, this.y,true,0);
                    }
                break;
                case pawn:
                    if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null)&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[0][0].x)&&(this.y == pinnedPieces[0][0].y)){
                    }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null)&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[1][0].x)&&(this.y == pinnedPieces[1][0].y)){
                    }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null)&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[2][0].x)&&(this.y == pinnedPieces[2][0].y)){
                        if((kingChecked != 2)&&(this.whitePiece == false)){
                            PawnMovement(this.moveDirecion);
                        }
                    }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null)&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[3][0].x)&&(this.y == pinnedPieces[3][0].y)){
                        if((kingChecked != 2)&&(this.whitePiece == true)){
                            PawnMovement(this.moveDirecion);
                        }
                    }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null)&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[4][0].x)&&(this.y == pinnedPieces[4][0].y)){
                        if((kingChecked != 2)&&(this.whitePiece == false)&&(Math.max(this.y,pinnedPieces[4][1].y)-Math.min(this.y,pinnedPieces[4][1].y) == 1)){
                            ColourTiles(pinnedPieces[4][1].x, pinnedPieces[4][1].y);
                        }
                    }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null)&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[5][0].x)&&(this.y == pinnedPieces[5][0].y)){
                        if((kingChecked != 2)&&(this.whitePiece == false)&&(Math.max(this.y,pinnedPieces[5][1].y)-Math.min(this.y,pinnedPieces[5][1].y) == 1)){
                            ColourTiles(pinnedPieces[5][1].x, pinnedPieces[5][1].y);
                        }
                    }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null)&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[6][0].x)&&(this.y == pinnedPieces[6][0].y)){
                        if((kingChecked != 2)&&(this.whitePiece == true)&&(Math.max(this.y,pinnedPieces[6][1].y)-Math.min(this.y,pinnedPieces[6][1].y) == 1)){
                            ColourTiles(pinnedPieces[6][1].x, pinnedPieces[6][1].y);
                        }
                    }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null)&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[7][0].x)&&(this.y == pinnedPieces[7][0].y)){
                        if((kingChecked != 2)&&(this.whitePiece == true)&&(Math.max(this.y,pinnedPieces[7][1].y)-Math.min(this.y,pinnedPieces[7][1].y) == 1)){
                            ColourTiles(pinnedPieces[7][1].x, pinnedPieces[7][1].y);
                        }
                    }else if(kingChecked == 1){
                        if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == pieceType.enemyPiece)){
                            if((this.y + moveDirecion == pinnedPieces[0][0].y)&&(this.x + (this.x > pinnedPieces[0][0].x ? -1:+1) == pinnedPieces[0][0].x)){
                                ColourTiles(pinnedPieces[0][0].x, pinnedPieces[0][0].y);
                            }
                            if((this.x > Math.min((int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),pinnedPieces[0][0].x))&&(this.x < Math.max((int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),pinnedPieces[0][0].x))&&(Game.pieces[this.y + moveDirecion][this.x] == null)){
                                if(this.y + moveDirecion == pinnedPieces[0][0].y){
                                    ColourTiles(this.x,pinnedPieces[0][0].y);
                                }else if((((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3)))&&(Game.pieces[this.y + moveDirecion*2][this.x] == null)&&(this.y + moveDirecion*2 == pinnedPieces[0][0].y)){
                                    ColourTiles(this.x,pinnedPieces[0][0].y);
                                }
                            }
                        }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == pieceType.enemyPiece)){
                            if((this.y + moveDirecion == pinnedPieces[1][0].y)&&(this.x + (this.x > pinnedPieces[1][0].x ? -1:+1) == pinnedPieces[1][0].x)){
                                ColourTiles(pinnedPieces[1][0].x, pinnedPieces[1][0].y);
                            }
                            if((this.x > Math.min((int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),pinnedPieces[1][0].x))&&(this.x < Math.max((int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),pinnedPieces[1][0].x))&&(Game.pieces[this.y + moveDirecion][this.x] == null)){
                                if(this.y + moveDirecion == pinnedPieces[1][0].y){
                                    ColourTiles(this.x,pinnedPieces[1][0].y);
                                }else if((((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3)))&&(Game.pieces[this.y + moveDirecion*2][this.x] == null)&&(this.y + moveDirecion*2 == pinnedPieces[1][0].y)){
                                    ColourTiles(this.x,pinnedPieces[1][0].y);
                                }
                            }
                        }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == pieceType.enemyPiece)){
                            if((this.y + moveDirecion == pinnedPieces[2][0].y)&&(this.x + (this.x > pinnedPieces[2][0].x ? -1:+1) == pinnedPieces[2][0].x)){
                                ColourTiles(pinnedPieces[2][0].x, pinnedPieces[2][0].y);
                            }
                        }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == pieceType.enemyPiece)){
                            if((this.y + moveDirecion == pinnedPieces[3][0].y)&&(this.x + (this.x > pinnedPieces[3][0].x ? -1:+1) == pinnedPieces[3][0].x)){
                                ColourTiles(pinnedPieces[3][0].x, pinnedPieces[3][0].y);
                            }
                        }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == pieceType.enemyPiece)){
                            if((this.y + moveDirecion == pinnedPieces[4][0].y)&&(this.x + (this.x > pinnedPieces[4][0].x ? -1:+1) == pinnedPieces[4][0].x)){
                                ColourTiles(pinnedPieces[4][0].x, pinnedPieces[4][0].y);
                            }else for(int i = 0; i < Math.max(pinnedPieces[4][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))-Math.min(pinnedPieces[4][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                checkForPawn(pinnedPieces[4][0].x-i, pinnedPieces[4][0].y-i);
                            }
                        }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][0].pieceType == pieceType.enemyPiece)){
                            if((this.y + moveDirecion == pinnedPieces[5][0].y)&&(this.x + (this.x > pinnedPieces[5][0].x ? -1:+1) == pinnedPieces[5][0].x)){
                                ColourTiles(pinnedPieces[5][0].x, pinnedPieces[5][0].y);
                            }for(int i = 0; i < Math.max(pinnedPieces[5][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))-Math.min(pinnedPieces[5][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                checkForPawn(pinnedPieces[5][0].x+i, pinnedPieces[5][0].y-i);
                            }
                        }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][0].pieceType == pieceType.enemyPiece)){
                            if((this.y + moveDirecion == pinnedPieces[6][0].y)&&(this.x + (this.x > pinnedPieces[6][0].x ? -1:+1) == pinnedPieces[6][0].x)){
                                ColourTiles(pinnedPieces[6][0].x, pinnedPieces[6][0].y);
                            }for(int i = 0; i < Math.max(pinnedPieces[6][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))-Math.min(pinnedPieces[6][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                checkForPawn(pinnedPieces[6][0].x-i, pinnedPieces[6][0].y+i);
                            }
                        }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == pieceType.enemyPiece)){
                            if((this.y + moveDirecion == pinnedPieces[7][0].y)&&(this.x + (this.x > pinnedPieces[7][0].x ? -1:+1) == pinnedPieces[7][0].x)){
                                ColourTiles(pinnedPieces[7][0].x, pinnedPieces[7][0].y);
                            }for(int i = 0; i < Math.max(pinnedPieces[7][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))-Math.min(pinnedPieces[7][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                checkForPawn(pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i);
                            }
                        }else for(int i = 8; i < 18; i++){
                            try {
                                if((this.y + moveDirecion == pinnedPieces[i][0].y)&&(this.x + (this.x > pinnedPieces[i][0].x ? -1:+1) == pinnedPieces[i][0].x)){
                                    ColourTiles(pinnedPieces[i][0].x, pinnedPieces[i][0].y);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }else if(kingChecked == 0){
                        takingEnPassant = false;
                        PawnMovement(this.moveDirecion);
                        TakeSideways(this.moveDirecion, 1,0,0,true,0);
                    }
                break;
                case queen:
                    if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){//specifically dont use for statement
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[0][1].x,pinnedPieces[0][1].y);
                        }
                    }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[1][1].x,pinnedPieces[1][1].y);
                        }
                    }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[2][1].x,pinnedPieces[2][1].y);
                        }
                    }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[3][1].x,pinnedPieces[3][1].y);
                        }
                    }else if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[4][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){//specifically dont use for statement
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[4][1].x,pinnedPieces[4][1].y);
                        }
                    }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[5][1].x,pinnedPieces[5][1].y);
                        }
                    }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[6][1].x,pinnedPieces[6][1].y);
                        }
                    }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            ColourTiles(pinnedPieces[7][1].x,pinnedPieces[7][1].y);
                        }
                    }else if(kingChecked == 1){
                        if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[0][0].x)){
                                checkVertical(pinnedPieces[0][0].y);
                            }else if((pinnedPieces[0][1] != null)&&((pinnedPieces[0][1].x == this.x)&&(pinnedPieces[0][1].y == this.y))){
                                ColourTiles(pinnedPieces[0][0].x,pinnedPieces[0][0].y);
                            }
                            for(int i = 0; i < Math.max(pinnedPieces[0][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))
                                -Math.min(pinnedPieces[0][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                    if(Math.max(this.x,pinnedPieces[0][0].x-i)-Math.min(this.x,pinnedPieces[0][0].x-i) == Math.max(this.y, pinnedPieces[0][0].y)-Math.min(this.y,pinnedPieces[0][0].y)){
                                        checkDiagonals(pinnedPieces[0][0].x-i, pinnedPieces[0][0].y, pinnedPieces[0][0].x-i, pinnedPieces[0][0].y, this.x > pinnedPieces[0][0].x-i ? 1:-1, this.y > pinnedPieces[0][0].y ? 1:-1);
                                    }
                                }
                        }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x >= pinnedPieces[1][0].x)){
                                checkVertical(pinnedPieces[1][0].y);
                            }else if((pinnedPieces[1][1] != null)&&((pinnedPieces[1][1].x == this.x)&&(pinnedPieces[1][1].y == this.y))){
                                ColourTiles(pinnedPieces[1][0].x,pinnedPieces[1][0].y);
                            }
                            for(int i = 0; i < Math.max(pinnedPieces[1][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))
                                -Math.min(pinnedPieces[1][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                                    if(Math.max(this.x,pinnedPieces[1][0].x+i)-Math.min(this.x,pinnedPieces[1][0].x+i) == Math.max(this.y, pinnedPieces[1][0].y)-Math.min(this.y,pinnedPieces[1][0].y)){
                                        checkDiagonals(pinnedPieces[1][0].x+i, pinnedPieces[1][0].y, pinnedPieces[1][0].x+i, pinnedPieces[1][0].y, this.x > pinnedPieces[1][0].x+i ? 1:-1, this.y > pinnedPieces[1][0].y ? 1:-1);
                                    }
                                }
                        }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == piecEnum.enemyPiece)){
                            if((this.y > (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y <= pinnedPieces[2][0].y)){
                                checkHorizontal(pinnedPieces[2][0].x);
                            }else if((pinnedPieces[2][1] != null)&&((pinnedPieces[2][1].x == this.x)&&(pinnedPieces[2][1].y == this.y))){
                                ColourTiles(pinnedPieces[2][0].x,pinnedPieces[2][0].y);
                            }
                            for(int i = 0; i < Math.max(pinnedPieces[2][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[2][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[2][0].x)-Math.min(this.x,pinnedPieces[2][0].x) == Math.max(this.y, pinnedPieces[2][0].y-i)-Math.min(this.y,pinnedPieces[2][0].y-i)){
                                        checkDiagonals(pinnedPieces[2][0].x, pinnedPieces[2][0].y-i, pinnedPieces[2][0].x, pinnedPieces[2][0].y-i, this.x > pinnedPieces[2][0].x ? 1:-1, this.y > pinnedPieces[2][0].y-i ? 1:-1);
                                    }
                                }
                        }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == piecEnum.enemyPiece)){
                            if((this.y < (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y >= pinnedPieces[3][0].y)){
                                checkHorizontal(pinnedPieces[3][0].x);
                            }else if((pinnedPieces[3][1] != null)&&((pinnedPieces[3][1].x == this.x)&&(pinnedPieces[3][1].y == this.y))){
                                ColourTiles(pinnedPieces[3][0].x,pinnedPieces[3][0].y);
                            }
                            for(int i = 0; i < Math.max(pinnedPieces[3][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[3][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[3][0].x)-Math.min(this.x,pinnedPieces[3][0].x) == Math.max(this.y, pinnedPieces[3][0].y+i)-Math.min(this.y,pinnedPieces[3][0].y+i)){
                                        checkDiagonals(pinnedPieces[3][0].x, pinnedPieces[3][0].y+i, pinnedPieces[3][0].x, pinnedPieces[3][0].y+i, this.x > pinnedPieces[3][0].x ? 1:-1, this.y > pinnedPieces[3][0].y+i ? 1:-1);
                                    }
                                }
                        }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[4][0].x)){
                                checkVertical(pinnedPieces[4][0].y-(Math.max(pinnedPieces[4][0].x,this.x)-Math.min(pinnedPieces[4][0].x,this.x)));
                            }
                            if((this.y > (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y <= pinnedPieces[4][0].y)){
                                checkHorizontal(pinnedPieces[4][0].x-(Math.max(pinnedPieces[4][0].y,this.y)-Math.min(pinnedPieces[4][0].y,this.y)));
                            }
                            for(int i = 0; i < Math.max(pinnedPieces[4][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[4][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[4][0].x-i)-Math.min(this.x,pinnedPieces[4][0].x-i) == Math.max(this.y, pinnedPieces[4][0].y-i)-Math.min(this.y,pinnedPieces[4][0].y-i)){
                                        checkDiagonals(pinnedPieces[4][0].x-i, pinnedPieces[4][0].y-i, pinnedPieces[4][0].x-i, pinnedPieces[4][0].y-i, this.x > pinnedPieces[4][0].x-i ? 1:-1, this.y > pinnedPieces[4][0].y-i ? 1:-1);
                                    }
                                }
                        }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x >= pinnedPieces[5][0].x)){
                                checkVertical(pinnedPieces[5][0].y-(Math.max(pinnedPieces[5][0].x,this.x)-Math.min(pinnedPieces[5][0].x,this.x)));
                            }
                            if((this.y > (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y <= pinnedPieces[5][0].y)){
                                checkHorizontal(pinnedPieces[5][0].x+(Math.max(pinnedPieces[5][0].y,this.y)-Math.min(pinnedPieces[5][0].y,this.y)));
                            }
                            for(int i = 0; i < Math.max(pinnedPieces[5][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[5][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[5][0].x+i)-Math.min(this.x,pinnedPieces[5][0].x+i) == Math.max(this.y, pinnedPieces[5][0].y-i)-Math.min(this.y,pinnedPieces[5][0].y-i)){
                                        checkDiagonals(pinnedPieces[5][0].x+i, pinnedPieces[5][0].y-i, pinnedPieces[5][0].x+i, pinnedPieces[5][0].y-i, this.x > pinnedPieces[5][0].x+i ? 1:-1, this.y > pinnedPieces[5][0].y-i ? 1:-1);
                                    }
                                }
                        }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[6][0].x)){
                                checkVertical(pinnedPieces[6][0].y+(Math.max(pinnedPieces[6][0].x,this.x)-Math.min(pinnedPieces[6][0].x,this.x)));
                            }
                            if((this.y < (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y >= pinnedPieces[6][0].y)){
                                checkHorizontal(pinnedPieces[6][0].x-(Math.max(pinnedPieces[6][0].y,this.y)-Math.min(pinnedPieces[6][0].y,this.y)));
                            }
                            for(int i = 0; i < Math.max(pinnedPieces[6][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[6][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[6][0].x-i)-Math.min(this.x,pinnedPieces[6][0].x-i) == Math.max(this.y, pinnedPieces[6][0].y+i)-Math.min(this.y,pinnedPieces[6][0].y+i)){
                                        checkDiagonals(pinnedPieces[6][0].x-i, pinnedPieces[6][0].y+i, pinnedPieces[6][0].x-i, pinnedPieces[6][0].y+i, this.x > pinnedPieces[6][0].x-i ? 1:-1, this.y > pinnedPieces[6][0].y+i ? 1:-1);
                                    }
                                }
                        }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == piecEnum.enemyPiece)){
                            if((this.x < (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x >= pinnedPieces[7][0].x)){
                                checkVertical(pinnedPieces[7][0].y+(Math.max(pinnedPieces[7][0].x,this.x)-Math.min(pinnedPieces[7][0].x,this.x)));
                            }
                            if((this.y < (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y >= pinnedPieces[7][0].y)){
                                checkHorizontal(pinnedPieces[7][0].x+(Math.max(pinnedPieces[7][0].y,this.y)-Math.min(pinnedPieces[7][0].y,this.y)));
                            }
                            for(int i = 0; i < Math.max(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                                -Math.min(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                                    if(Math.max(this.x,pinnedPieces[7][0].x+i)-Math.min(this.x,pinnedPieces[7][0].x+i) == Math.max(this.y, pinnedPieces[7][0].y+i)-Math.min(this.y,pinnedPieces[7][0].y+i)){
                                        checkDiagonals(pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i, pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i, this.x > pinnedPieces[7][0].x+i ? 1:-1, this.y > pinnedPieces[7][0].y+i ? 1:-1);
                                    }
                                }
                        }else for(int i = 8; i < 18; i++){
                            if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == piecEnum.enemyPiece)){
                                if(this.x == pinnedPieces[i][0].x){
                                    checkVertical(pinnedPieces[i][0].y);
                                }else if(this.y == pinnedPieces[i][0].y){
                                    checkHorizontal(pinnedPieces[i][0].x);
                                }
                            }
                            if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == piecEnum.enemyPiece)){
                                if(Math.max(this.x,pinnedPieces[i][0].x)-Math.min(this.x,pinnedPieces[i][0].x) == Math.max(this.y, pinnedPieces[i][0].y)-Math.min(this.y,pinnedPieces[i][0].y)){
                                    checkDiagonals(pinnedPieces[i][0].x, pinnedPieces[i][0].y, pinnedPieces[i][0].x, pinnedPieces[i][0].y, this.x > pinnedPieces[i][0].x ? 1:-1, this.y > pinnedPieces[i][0].y ? 1:-1);
                                }
                            }
                        }
                    }else if(kingChecked == 0){
                        Pathing(1,0,this.x,this.y,true,0);
                        Pathing(-1,0,this.x,this.y,true,0);
                        Pathing(0,1,this.x,this.y,true,0);
                        Pathing(0,-1,this.x,this.y,true,0);
                        Pathing(1,1,this.x,this.y,true,0);
                        Pathing(-1,1,this.x,this.y,true,0);
                        Pathing(1,-1,this.x,this.y,true,0);
                        Pathing(-1,-1,this.x,this.y,true,0);
                    }
                        
                break;
                case king:
                    try {
                        if(((Game.pieces[this.y-1][this.x] == null)||(Game.pieces[this.y-1][this.x].whitePiece != this.whitePiece))&&(safeSquare(this.x, this.y-1))){
                            ColourTiles(this.x, this.y-1);
                        }
                    } catch (Exception e) {
                    }
                    try {
                    if(((Game.pieces[this.y+1][this.x] == null)||(Game.pieces[this.y+1][this.x].whitePiece != this.whitePiece))&&(safeSquare(this.x, this.y+1))){
                        ColourTiles(this.x, this.y+1);
                    }
                    }catch(Exception e){

                    }
                    try {
                        if(((Game.pieces[this.y-1][this.x+1] == null)||(Game.pieces[this.y-1][this.x+1].whitePiece != this.whitePiece))&&(safeSquare(this.x+1, this.y-1))){
                            ColourTiles(this.x+1, this.y-1);
                        } 
                    } catch (Exception e) {
                    }
                    try {
                        if(((Game.pieces[this.y+1][this.x+1] == null)||(Game.pieces[this.y+1][this.x+1].whitePiece != this.whitePiece))&&(safeSquare(this.x+1, this.y+1))){
                            ColourTiles(this.x+1, this.y+1);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if(((Game.pieces[this.y-1][this.x-1] == null)||(Game.pieces[this.y-1][this.x-1].whitePiece != this.whitePiece))&&(safeSquare(this.x-1, this.y-1))){
                            ColourTiles(this.x-1, this.y-1);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if(((Game.pieces[this.y+1][this.x-1] == null)||(Game.pieces[this.y+1][this.x-1].whitePiece != this.whitePiece))&&(safeSquare(this.x-1, this.y+1))){
                            ColourTiles(this.x-1, this.y+1);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if(((Game.pieces[this.y][this.x+1] == null)||(Game.pieces[this.y][this.x+1].whitePiece != this.whitePiece))&&(safeSquare(this.x+1, this.y))){
                            ColourTiles(this.x+1, this.y);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if(((Game.pieces[this.y][this.x-1] == null)||(Game.pieces[this.y][this.x-1].whitePiece != this.whitePiece))&&(safeSquare(this.x-1, this.y))){
                            ColourTiles(this.x-1, this.y);
                        }
                    } catch (Exception e) {
                    }
                break;
            }
    }
    private boolean safeSquare(int checkX, int checkY){
        for(int i = 0; i < 18; i++){
            pinnedPieces[i][0] = null;
            pinnedPieces[i][1] = null;
        }
        Pathing(1,0,checkX,checkY,false,0);
        Pathing(-1,0,checkX,checkY,false,1);
        Pathing(0,1,checkX,checkY,false,2);
        Pathing(0,-1,checkX,checkY,false,3);
        Pathing(1,1,checkX,checkY,false,4);
        Pathing(-1,1,checkX,checkY,false,5);
        Pathing(1,-1,checkX,checkY,false,6);
        Pathing(-1,-1,checkX,checkY,false,7);
        Jumping(1,2,checkX,checkY,false,8);
        Jumping(2,1,checkX,checkY,false,9);
        Jumping(1,-2,checkX,checkY,false,10);
        Jumping(2,-1,checkX,checkY,false,11);
        TakeSideways(this.whitePiece?-1:1,1, checkX,checkY,false, 16);
        for(int i = 0; i<18; i++){
            if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == pieceType.enemyPiece)){
                System.out.println(pinnedPieces[i][0].pieceType+": "+pinnedPieces[i][0].x+", "+pinnedPieces[i][0].y+" . Storespot: "+i);
                return false;
            }
        }
        try{
            if((Game.pieces[checkY+1][checkX] != null)&&(Game.pieces[checkY+1][checkX].pieceType == pieceType.king)&&(Game.pieces[checkY+1][checkX].whitePiece != this.whitePiece)){
                return false;
            }
        }catch(Exception e){
        }
        try{
            if((Game.pieces[checkY-1][checkX] != null)&&(Game.pieces[checkY-1][checkX].pieceType == pieceType.king)&&(Game.pieces[checkY-1][checkX].whitePiece != this.whitePiece)){
                return false;
            }
        }catch(Exception e){
        }
        try{
            if((Game.pieces[checkY+1][checkX+1] != null)&&(Game.pieces[checkY+1][checkX+1].pieceType == pieceType.king)&&(Game.pieces[checkY+1][checkX+1].whitePiece != this.whitePiece)){
                return false;
            }
        }catch(Exception e){
        }
        try{
            if((Game.pieces[checkY+1][checkX-1] != null)&&(Game.pieces[checkY+1][checkX-1].pieceType == pieceType.king)&&(Game.pieces[checkY+1][checkX-1].whitePiece != this.whitePiece)){
                return false;
            }
        }catch(Exception e){
        }
        try{
            if((Game.pieces[checkY-1][checkX+1] != null)&&(Game.pieces[checkY-1][checkX+1].pieceType == pieceType.king)&&(Game.pieces[checkY-1][checkX+1].whitePiece != this.whitePiece)){
                return false;
            }
        }catch(Exception e){
        }
        try{
            if((Game.pieces[checkY-1][checkX-1] != null)&&(Game.pieces[checkY-1][checkX-1].pieceType == pieceType.king)&&(Game.pieces[checkY-1][checkX-1].whitePiece != this.whitePiece)){
                return false;
            }
        }catch(Exception e){
        }
        try{
            if((Game.pieces[checkY][checkX+1] != null)&&(Game.pieces[checkY][checkX+1].pieceType == pieceType.king)&&(Game.pieces[checkY][checkX+1].whitePiece != this.whitePiece)){
                return false;
            }
        }catch(Exception e){
        }
        try{
            if((Game.pieces[checkY][checkX-1] != null)&&(Game.pieces[checkY][checkX-1].pieceType == pieceType.king)&&(Game.pieces[checkY][checkX-1].whitePiece != this.whitePiece)){
                return false;
            }
        }catch(Exception e){
        }
        return true;
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
    private void checkDiagonals(int targetX, int targetY, int checkX, int checkY, int xIncrement, int yIncrement){
        if((checkY+yIncrement < Game.boardSize)&&(checkY+yIncrement >= 0)&&(checkX+xIncrement < Game.boardSize)&&(checkX+xIncrement >= 0)){
            if(Game.pieces[checkY+yIncrement][checkX+xIncrement] == null){
                checkDiagonals(targetX, targetY, checkX+xIncrement, checkY+yIncrement, xIncrement, yIncrement);
            }else if((Game.pieces[checkY+yIncrement][checkX+xIncrement].x == this.x)&&(Game.pieces[checkY+yIncrement][checkX+xIncrement].y == this.y)){
                ColourTiles(targetX, targetY);
            }
        }
    }
    private void checkForPawn(int targetX, int targetY){
        if((this.x == targetX)&&(Game.pieces[this.y + moveDirecion][this.x] == null)){
            if(this.y + moveDirecion == targetY){
                ColourTiles(targetX,targetY);
            }else if((((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3)))&&(this.y + moveDirecion * 2 == targetY)&&(Game.pieces[this.y + moveDirecion*2][this.x] == null)){
                ColourTiles(targetX,targetY);
            }
        }
    }
}