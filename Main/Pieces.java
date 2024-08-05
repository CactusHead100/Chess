package Main;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.concurrent.CountDownLatch;
/**
 * this is the pieces class where all infomation about the pieces is stored and contains methods that allow pieces to move and take other pieces within the rules of chess
 */
public class Pieces {
    
    //I use an enum here as it creates less storage and these are the only values i need to chekc for
    public enum piecEnum{pawn,knight,bishop,rook,queen,king,enemyPiece}//enemy piece is used when checking for check and pins
    piecEnum pieceType;//this is a variable which i can set to one of the piecEnum values

    //these variables are for the pawn allowing it to do en-passant and move correctly
    boolean pawnEnPassant = false;//this shows if the pawn can be taken by en-passant or not
    boolean takingEnPassant = false;//this shows if the moving piece can take en-passant
    int moveDirecion;//this is set to either 1 or -1 depending on its colour as pawns can only move one way up the board

    //these store information used to move pieces and check for checks and pins
    boolean whitePiece;//this stores what type of piece 
    Pieces pinnedPieces[][] = new Pieces[18][2];//this stores an array of every piece that the king could potentially be taken by, and stores the second piece away from it if its in a a line away from it
    int kingChecked = 0;//tells us how many times the king is getting checked used to figure out what pieces can do when calculating moves
    String dictReference;//stores the dictionary reference used for king and pawns for easy access as otherwise id have to do aditional checks to find their positions this is needed in calculating piece moves and en-passant
    
    //stores position
    int x;
    int y;

    //an array of rectangles as i draw sprites through lots of rectangles so need an array
    Rectangle2D.Double[] rectangle;

    /**
     * intakes position in form x and y, the type of piece it is and its colour, returns nothing
     * constructor that sets the position piecetype and its colour also uses this infomation to
     * set the pawns movedirection and adds correct rectangle values to rectangle so that the piece draws as a sprite
     * this is done by calling the spriting pieces method
     */
    Pieces(int x,int y,piecEnum pieceType, boolean whitePiece){
        this.pieceType = pieceType;
        this.whitePiece = whitePiece;
        this.x = x;
        this.y = y;

        /**
         * gets the piecetype and depednding on the piece stores the rectangles which make up 
         * its image and sets any additional personal piece variables to the right thing
         */
        switch (pieceType) {
            case pawn:
                spritingPieces(Sprites.pawn.length, Sprites.pawn);//calls the spriting function and tells it what numbers to take from sprite class and turn into rectangles for drawingin the game class 
                moveDirecion = this.whitePiece? -1:1;//depedning on its colour sets it to be able to move up or down the board
            break;
            case king:
                spritingPieces(Sprites.king.length, Sprites.king);//same as pawn just king variable in sprite class as they have different sprites 
            break;
            case knight:

                //depedning on where the knight is it changes it sprite so that it always looks into the middle of the board
                if(this.x >= Game.boardSize/2){//dont use a ? type of if statement as would have to use it twice whereas this i just need one if else statement
                    spritingPieces(Sprites.rightKnight.length, Sprites.rightKnight);
                }else{
                    spritingPieces(Sprites.leftKnight.length, Sprites.leftKnight);
                }
            break;
            case bishop:
                spritingPieces(Sprites.bishop.length, Sprites.bishop);
            break;
            case rook:
                spritingPieces(Sprites.rook.length, Sprites.rook);
            break;
            case queen:
                spritingPieces(Sprites.queen.length, Sprites.queen);
            break;
        }
    }
    /**
     * intakes lengh of the 2D array and the 2D array, returns nothing
     * takes to inputs the lengh of the 2D array and the 2D array and adds rectangles unill 
     * piece sprie can be drawn (there are no longer any values in the array)
     */
    public void spritingPieces(int length, int[][] spriteValues){
        rectangle = new Rectangle2D.Double[length];//sets the length of the rectangle to the length of the 2D array as all we are doing is taking some rectangles represented in ints and turning them into resized rectangles 
        
        /**
         * as there are multiple rectangles this goes through every one that is written in 
         * sprites class and converts it to a rectangle.2D.double 
         */
        for(int i = 0; i < length; i++){

            //uses the constants from gameclass to ensure piece is correctly resized and fits within the square
            rectangle[i] = new Rectangle2D.Double(this.x*Game.tileSize+Game.pixelSize*spriteValues[i][0],
            this.y*Game.tileSize+Game.pixelSize*spriteValues[i][1],Game.pixelSize*spriteValues[i][2],Game.pixelSize*spriteValues[i][3]);
        }
    }
    
    /**
     * intakes new position, returns nothing
     * is called from game class and moves the piece to its designated square
     */
    public void applyGameRules(int newX, int newY){
        
        /**
         * does this 8 times so every pawn on the board has this aplied
        */
        for(int i = 0; i<8; i++){
            try {//i use a try as when a piece is taken the info will be null so it stops any errors

                //sets the en-passant of eery piece to false as only the direct move after the pawn moves can you take en-passant
                Game.pieces[(int)(Math.round((Game.pieceXY.get("wp"+Integer.toString(i))-(int)Math.floor(Game.pieceXY.get("wp"+Integer.toString(i))))*10))][(int)Math.floor(Game.pieceXY.get("wp"+Integer.toString(i)))].pawnEnPassant = false;
                Game.pieces[(int)(Math.round((Game.pieceXY.get("bp"+Integer.toString(i))-(int)Math.floor(Game.pieceXY.get("bp"+Integer.toString(i))))*10))][(int)Math.floor(Game.pieceXY.get("bp"+Integer.toString(i)))].pawnEnPassant = false;
            }catch (Exception e) {
            }
        }
        /**
         * gets the piecetype as some pieces have specific checks and variables that need changing
         */
        switch (pieceType) {
            case pawn:
                if((this.y+2 == newY)||(this.y-2 == newY)){//checks if its doublemoved and if it has allows it to be taken en-passant
                    movePiece(newX, newY);//calls movePiece which moves the piece its in a seperate method as i call it a lot
                    Game.pieces[newY][newX].pawnEnPassant = true;////allows it to be taken en-passant 
                    Game.pieces[newY][newX].dictReference = this.dictReference;//makes sure that the dict reference of the new pawn is the same as this one so can still access its info easily
                    Game.pieceXY.put(this.dictReference,Double.parseDouble(Integer.toString(newX)+"."+Integer.toString(newY)));//updates the stored position to its current one 
                }else if(takingEnPassant){
                    movePiece(newX, newY);
                    Game.pieces[newY-moveDirecion][newX] = null;//changes the position behind its new one to false it has just taken the pawn on that space
                    Game.pieces[newY][newX].dictReference = this.dictReference;
                    Game.pieceXY.put(this.dictReference,Double.parseDouble(Integer.toString(newX)+"."+Integer.toString(newY)));
                }else if(newY == (this.whitePiece ? 0:Game.boardSize-1)){// checks if it got to the end of the board
                    Game.pieces[this.y][this.x] = null;//delets itself 
                    Game.pieces[newY][newX] = new Pieces(newX, newY, pieceType.queen, this.whitePiece);//promotes to a queen
                }else{
                    movePiece(newX, newY);
                    Game.pieces[newY][newX].dictReference = this.dictReference;
                    Game.pieceXY.put(this.dictReference,Double.parseDouble(Integer.toString(newX)+"."+Integer.toString(newY)));
                }   
            break;
            case king:
                Game.pieceXY.put(this.whitePiece?"wk":"bk",Double.parseDouble(Integer.toString(newX)+"."+Integer.toString(newY)));// updates the stored postion to its current position 
                movePiece(newX, newY);
            break;
            default:
                movePiece(newX, newY);
            break;
        }
    }
    /**
     * intakes new position of piece, returns nothing
     * changes its old one to null and new one to a copy of its infomation and by doing so moves the piece
     */
    public void movePiece(int newX, int newY){
        Game.pieces[this.y][this.x] = null;
        Game.pieces[newY][newX] = new Pieces(newX, newY, this.pieceType, this.whitePiece);          
    }
    /**
     * inatkes and returns nothing
     * called from game and calls pathing and other methods to check for checks to see if
     * pieces can move and resets variables so these checks run off a fresh slate each time
     */
    public void getAvailableMoves(){

        //resets the pinned pieces variable so old infomation doesn't interfere with any new stuff being added and checked for
        for(int i = 0; i < 18; i++){
            pinnedPieces[i][0] = null;
            pinnedPieces[i][1] = null;
        }
        kingChecked = 0;//resets the amount of checkson the king so we can recalculate for this new move
        
        /**
         * repeats 2 times so that we can check for pinned pieces as if the second piece is an 
         * enemy and the first piece is this piece it will be pinned or have restricted moves
         */
        for(int i = 0; i < 2; i++){

            //calls all the movement options with the kings position and sets pinned pieces to the outcome
            pathing(1,0,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,0);
            pathing(-1,0,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,1);
            pathing(0,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,2);
            pathing(0,-1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,3);
            pathing(1,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,4);
            pathing(-1,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,5);
            pathing(1,-1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,6);
            pathing(-1,-1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,7);
        }
        //same here
        jumping(1,2,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,8);
        jumping(2,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,9);
        jumping(1,-2,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,10);
        jumping(2,-1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)),false,11);
        takeSideways(this.whitePiece?-1:1,1,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")), (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)), false, 16);     
        

        //calls to resolve the values that have been added to pinnedPieces variable this checks for checks and pins and calculates the piece moves
        resolvingCheck();
    }
    /**
     * intakes x and y, the increase of which they will move(x and y increments) the storing spot for 
     * piece storing and if its storing this data or if its going to colour a square, returns nothing
     * a recursive function that takes a the position and either colours squares so that you can move or 
     * stores data by calling pieceStoring which is used to determine moves
     */
    private void pathing(int xIncrement, int yIncrement, int xPos, int yPos, boolean movement, int storeSpot){
        try {
            if((xPos+xIncrement<Game.boardSize)&&(xPos+xIncrement>=0)&&(yPos+yIncrement<Game.boardSize)&&(yPos+yIncrement>=0)){//checks that its move is within the gameboard 
                if(movement){//checksif is moving or storing a piece
                    if(Game.pieces[yPos+yIncrement][xPos+xIncrement] == null){//calls itself again if the square is null and only stops if it hits another piece or the edge of teh board
                        pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement, movement, storeSpot);
                        colourTiles(xPos+xIncrement, yPos+yIncrement);//colours the tile as if its null this piece can move there 
                    }else if(Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece != this.whitePiece){// if the piece is an enemy piece colours the tile so that it can take the piece if wanted
                        colourTiles(xPos+xIncrement, yPos+yIncrement);
                    }
                }else if(Game.pieces[yPos+yIncrement][xPos+xIncrement] != null){
                    if(Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece == this.whitePiece){
                        if(Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType != pieceType.king){//makes sure that the king cant block itself from check
                            pieceStoring(storeSpot, xPos+xIncrement, yPos+yIncrement,false);//stores the piece data for later use to confirm king is safe and that the piece can move
                        }
                        if(pinnedPieces[storeSpot][1]== null){//if the second spot is not filled calls to do checks again, this allows pieces to be pinned as we want to check if they are inbetween the enemy piece and king
                            pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement, movement, storeSpot);
                        }
                    }else if((Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece != this.whitePiece)&&//checks for rooks along lines and bishops the diagonals and queens both
                    (((xIncrement == 0)||(yIncrement == 0))&&((Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType == pieceType.rook)||(Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType == pieceType.queen)))||
                    ((((xIncrement == 1)||(xIncrement == -1))&&((yIncrement == 1)||(yIncrement == -1)))&&((Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType == pieceType.bishop)||(Game.pieces[yPos+yIncrement][xPos+xIncrement].pieceType == pieceType.queen)))){
                        pieceStoring(storeSpot, xPos+xIncrement, yPos+yIncrement,true);
                        if(pinnedPieces[storeSpot][1]== null){//even though we have the enemy piece we check again just incase its a our piece that can protect the king
                            pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement, movement, storeSpot);
                        }
                    }
                }else{//if the checked square is null it checks again as it only cares about storing pieces
                    pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement, movement, storeSpot);
                }
            }
        } catch (Exception e) {
        }
    }
    
    /** intakes
     * intakes x and y, the increase of which they will move(x and y increments) the storing spot for 
     * piece storing and if its storing this data or if its going to colour a square, return nothing
     * gets given position and increases and checks for null or enemy pieces on both sides positiove and negative as all
     * jumping pieces have a mirrored move pattern (currently just the knight but this is flexable so could use others)
     */
    private void jumping(int xIncrease, int yIncrease, int xPos, int yPos, boolean movement, int storeSpot){
            if((xPos+xIncrease<Game.boardSize)&&(xPos+xIncrease>=0)&&(yPos+yIncrease<Game.boardSize)&&(yPos+yIncrease>=0)){
                if(movement){
                    if((Game.pieces[yPos+yIncrease][xPos+xIncrease] == null)||(Game.pieces[yPos+yIncrease][xPos+xIncrease].whitePiece != this.whitePiece)){//if the square is empty or contains a enemy piece this piece can move there
                        colourTiles(xPos+xIncrease, yPos+yIncrease);
                    }
                }else if(Game.pieces[yPos+yIncrease][xPos+xIncrease] != null){
                    if((Game.pieces[yPos+yIncrease][xPos+xIncrease].whitePiece != this.whitePiece)&&(Game.pieces[yPos+yIncrease][xPos+xIncrease].pieceType == pieceType.knight)){//checks for enemy knight otherwise puts whatever there
                        pieceStoring(storeSpot+4, xPos+xIncrease, yPos+yIncrease, true);
                    }else{
                        pieceStoring(storeSpot+4, xPos+xIncrease, yPos+yIncrease, false);
                    }
                }
            } 

            //same thing just mirrorred
            if((xPos-xIncrease<Game.boardSize)&&(xPos-xIncrease>=0)&&(yPos-yIncrease<Game.boardSize)&&(yPos-yIncrease>=0)){
                if(movement){
                    if((Game.pieces[yPos-yIncrease][xPos-xIncrease] == null)||(Game.pieces[yPos-yIncrease][xPos-xIncrease].whitePiece != this.whitePiece)){
                        colourTiles(xPos-xIncrease, yPos-yIncrease);
                    }
                }else if(Game.pieces[yPos-yIncrease][xPos-xIncrease] != null){
                    if((Game.pieces[yPos-yIncrease][xPos-xIncrease].whitePiece != this.whitePiece)&&(Game.pieces[yPos-yIncrease][xPos-xIncrease].pieceType == pieceType.knight)){
                        pieceStoring(storeSpot+5, xPos-xIncrease, yPos-yIncrease, true);
                    }else{
                        pieceStoring(storeSpot+5, xPos-xIncrease, yPos-yIncrease, false);
                    }
                }
            } 
    }

    /**
     * intakes x and y, the increase of which they will move(x and y increase) the storing spot for 
     * piece storing and if its storing this data or if its going to colour a square, returns nothing
     * checks if it can do en-passant otherwise checks for enemy piece diagonally in front of it
     * (front being relative to where its moving) and either stores or highlights squares depending on outcome
     */
    private void takeSideways(int yIncrease, int xIncrease, int xPos, int yPos, boolean movement, int storeSpot){ 
        if(movement){
            try {
                /**
                 * checks if it can take en-passant and if it can it colours tiles otherwise nothing
                 */
                if((Game.pieces[this.y][this.x-xIncrease].whitePiece != this.whitePiece)&&
                    (Game.pieces[this.y][this.x-xIncrease].pawnEnPassant)){
                    colourTiles(this.x-xIncrease, this.y+yIncrease);
                    takingEnPassant = true;//allows us to know that its taking en-passant if this pieced is moved
                }
            } catch (Exception e) {
            }
            try {

                //same here just opposite x direction
                if((Game.pieces[this.y][this.x+xIncrease].whitePiece != this.whitePiece)&&
                    (Game.pieces[this.y][this.x+xIncrease].pawnEnPassant)){
                    colourTiles(this.x+xIncrease, this.y+yIncrease);
                    takingEnPassant = true;
                }
            } catch (Exception e) {
            }
        }
        try {
            if(movement){
                
                //normal taking, just checks if enemy piecce as pawns can only take diagonaly 
                if(Game.pieces[this.y+yIncrease][this.x-xIncrease].whitePiece != this.whitePiece){
                    colourTiles(this.x-xIncrease, this.y+yIncrease);
                }
            }else if((Game.pieces[yPos+yIncrease][xPos-xIncrease].pieceType == pieceType.pawn)&&
            (Game.pieces[yPos+yIncrease][xPos-xIncrease].whitePiece != this.whitePiece)){//checks if the piece is a pawn and an enemy piecce if it is stores it as a enemy piece
                pieceStoring(storeSpot, (xPos-xIncrease),(yPos+yIncrease),true);
            }
        } catch (Exception e){
        }
        try{

            //same here just opposite x direction
            if(movement){
                if(Game.pieces[this.y+yIncrease][this.x+xIncrease].whitePiece != this.whitePiece){
                    colourTiles(this.x+xIncrease, this.y+yIncrease);
                }
            }else if((Game.pieces[yPos+yIncrease][xPos+xIncrease].pieceType == pieceType.pawn)&&
            (Game.pieces[yPos+yIncrease][xPos+xIncrease].whitePiece != this.whitePiece)){
                pieceStoring(storeSpot+1,xPos+xIncrease,yPos+yIncrease,true);
            }
        } catch (Exception e) {
        }
    }

    /**
     * intakes y increase in which it uses to check for double and single moves, returns nothing
     * uses recursion to check for next move if it can double move
     */ 
    private void pawnMovement(int yIncrease){

        //check if it's in position to possibly do a double move using maths to determine its starting position without regards of boardsize in order to make it flexible
        if((yIncrease == (this.whitePiece?-1:1))&&(((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3)))){
            if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                if(Game.pieces[this.y+yIncrease][this.x] == null){
                    pawnMovement(yIncrease+(this.whitePiece?-1:1));//if it can do a double move uses recursion to check agian the square in front of where it would've origonally moved
                    colourTiles(this.x, this.y+yIncrease);
                }
            }
        }else{
            if((this.y+yIncrease<Game.boardSize)&&(this.y+yIncrease>=0)){
                if(Game.pieces[this.y+yIncrease][this.x] == null){//checks if square is null as pawn can only take diagonally and if it is calls colourTiles to show it can move there
                    colourTiles(this.x, this.y+yIncrease);
                }
            }
        }
    }
    /**
     * intakes x and y position and colours that square, returns nothing
     * by making the tile the move colour it shows the computer 
     * and person where the piece can move 
     */
    private void colourTiles(int xOfTile, int yOfTile){
        Game.tiles[yOfTile][xOfTile].colour = Game.moveColour;
        Mouse_Input.firstClick = false;//sets firstclick to false as it has shown where the piece can move so next check will be to see where the player wants the piece moved
    }
    
    /**
     * intakes where to store the piece which represents what directiont the piece is in, the 
     * position being storex and storey, and if its an enemy piece, returns nothing
     * uses data given to store it in an array of pieces inside the class pieces which it usesw
     * to later figure out if there are any restrictions to moves
     */
    private void pieceStoring(int storeSpot, int storeX, int storeY, boolean enemyPiece){//storespot represents what direction the piece is, storex and storey the position and enemy piece is if its a enemy piece or not
        if(enemyPiece){
            if(pinnedPieces[storeSpot][0] == null){//check if its empty as don't want to overide any data as its exposed to show the closest piece
                pinnedPieces[storeSpot][0] = new Pieces(storeX, storeY, piecEnum.enemyPiece, enemyPiece);
                kingChecked++;//adds a checkon the king used to determine moves
            }else if(pinnedPieces[storeSpot][1] == null){
                pinnedPieces[storeSpot][1] = new Pieces(storeX, storeY, piecEnum.enemyPiece, enemyPiece);
            }
        }else{
            if(pinnedPieces[storeSpot][0] == null){
                pinnedPieces[storeSpot][0] = new Pieces(storeX, storeY, Game.pieces[storeY][storeX].pieceType, !enemyPiece);//!enemypiece means that the piece is set to whatever piece type the this piece is
            }else if(pinnedPieces[storeSpot][1] == null){
                pinnedPieces[storeSpot][1] = new Pieces(storeX, storeY, Game.pieces[storeY][storeX].pieceType, !enemyPiece);//gets the piecetype by finding its type on the gameboard
            }
        }  
    }
    /**
     * intakes and returns nothing
     * goes through and checks for pins checks and where this piece can intercept or take the 
     * attacking piece or if it can just move normally
     * note i wrote most of this while i was sick
     */
    private void resolvingCheck(){
        switch (pieceType) {
            /**
             * for each piece i check if its pinned first then if it has any only one move 
             * options the check for intercepts then call to check what moves it can do if all
             *  other checks return false
             */
            case rook:        
                
                /**
                 * i dont use for statement as the end checks rely on the start checks being false to happen
                 * these checks the diagonals for pins
                 */
                if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[4][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){
                }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                
                /**
                 * these check if the rook is infront of the attacking piece but in line with it and
                 * the king, if it is its only move is to take the piece
                 * */
                }else if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){//makes sure that it can only move if the king is not being doubolechecked
                        colourTiles(pinnedPieces[0][1].x,pinnedPieces[0][1].y);
                    }
                }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[1][1].x,pinnedPieces[1][1].y);
                    }
                }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[2][1].x,pinnedPieces[2][1].y);
                    }
                }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[3][1].x,pinnedPieces[3][1].y);
                    }

                //checks if king is in check
                }else if(kingChecked == 1){

                    //if it is then checks what direction the check comes from 
                    if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == piecEnum.enemyPiece)){
                        
                        //checks if the rook is between the king and piece so can block or take the piece
                        if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[0][0].x)){
                            checkVertical(pinnedPieces[0][0].y);//calls a method that checks if the rook can block the check
                        }else if((pinnedPieces[0][1] != null)&&((pinnedPieces[0][1].x == this.x)&&(pinnedPieces[0][1].y == this.y))){//this checks that if the rook is in line with the king and checking piece that it's only move is to take it
                            colourTiles(pinnedPieces[0][0].x,pinnedPieces[0][0].y);
                        }

                    //this is the same just in a different direction 
                    }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == piecEnum.enemyPiece)){
                        if((this.x < (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x >= pinnedPieces[1][0].x)){
                            checkVertical(pinnedPieces[1][0].y);
                        }else if((pinnedPieces[1][1] != null)&&((pinnedPieces[1][1].x == this.x)&&(pinnedPieces[1][1].y == this.y))){
                            colourTiles(pinnedPieces[1][0].x,pinnedPieces[1][0].y);
                        }
                    }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == piecEnum.enemyPiece)){
                        if((this.y > (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y <= pinnedPieces[2][0].y)){
                            checkHorizontal(pinnedPieces[2][0].x);
                        }else if((pinnedPieces[2][1] != null)&&((pinnedPieces[2][1].x == this.x)&&(pinnedPieces[2][1].y == this.y))){
                            colourTiles(pinnedPieces[2][0].x,pinnedPieces[2][0].y);
                        }
                    }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == piecEnum.enemyPiece)){
                        if((this.y < (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y >= pinnedPieces[3][0].y)){
                            checkHorizontal(pinnedPieces[3][0].x);
                        }else if((pinnedPieces[3][1] != null)&&((pinnedPieces[3][1].x == this.x)&&(pinnedPieces[3][1].y == this.y))){
                            colourTiles(pinnedPieces[3][0].x,pinnedPieces[3][0].y);
                        }

                    
                    //checks if the check is coming from a diagonal
                    }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == piecEnum.enemyPiece)){

                        //checks if the rook is between the checking piece and king
                        if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[4][0].x)){
                            checkVertical(pinnedPieces[4][0].y-(Math.max(pinnedPieces[4][0].x,this.x)-Math.min(pinnedPieces[4][0].x,this.x)));//if it is calculates the intercept square and calls method to see if it can block the check
                        }
                        
                        //same thing as above but for y
                        if((this.y > (int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))&&(this.y <= pinnedPieces[4][0].y)){
                            checkHorizontal(pinnedPieces[4][0].x-(Math.max(pinnedPieces[4][0].y,this.y)-Math.min(pinnedPieces[4][0].y,this.y)));
                        }

                    //again same as above just for a different direction
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
                    /**
                     * use a for loop here as i have done all the if checks i've needed to and 
                     * the result is the same for all of these beacuse nothing else relies on the outcome
                     * checks if the rook can take the checking piece
                     */
                    }else for(int i = 8; i < 18; i++){
                        if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == piecEnum.enemyPiece)){
                            if(this.x == pinnedPieces[i][0].x){
                                checkVertical(pinnedPieces[i][0].y);
                            }else if(this.y == pinnedPieces[i][0].y){
                                checkHorizontal(pinnedPieces[i][0].x);
                            }
                        }
                    }

                /**
                 * does normal moves if nothing else is true
                 */
                }else if(kingChecked == 0){
                    pathing(1,0,this.x,this.y,true,0);
                    pathing(-1,0,this.x,this.y,true,0);
                    pathing(0,1,this.x,this.y,true,0);
                    pathing(0,-1,this.x,this.y,true,0);
                }
            break;

            /**
             * very similar to rook just for diagonals
             */
            case bishop:
                try{
                    /**
                     * same as rook but is pinned if its inbetween king and rook or queen in a straight line
                     * as it only moves in diagonals
                     */
                    if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){// didn't know howelse i could write this
                    }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                    }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                    
                    /**
                     * same as rook if its between the queen or bishopit can only take
                     */
                    }else if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[4][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){//specifically dont use for statement
                        if(kingChecked != 2){
                            colourTiles(pinnedPieces[4][1].x,pinnedPieces[4][1].y);
                        }
                    }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            colourTiles(pinnedPieces[5][1].x,pinnedPieces[5][1].y);
                        }
                    }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            colourTiles(pinnedPieces[6][1].x,pinnedPieces[6][1].y);
                        }
                    }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                        if(kingChecked != 2){
                            colourTiles(pinnedPieces[7][1].x,pinnedPieces[7][1].y);
                        }
                    }else if(kingChecked == 1){
                        /**
                         * finds the intercept point for every square between the checking piece and the king
                         * and if then calls check diagonals to see if it can go there
                         */
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
                            //same thing but for diagonals
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
                            /**
                             * same as rook checks if it can take the enemy piece and uses for loop as the statements 
                             * dont rely on the outcome unlike the ones above
                             */
                        }else for(int i = 8; i < 18; i++){
                            if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == piecEnum.enemyPiece)){
                                if(Math.max(this.x,pinnedPieces[i][0].x)-Math.min(this.x,pinnedPieces[i][0].x) == Math.max(this.y, pinnedPieces[i][0].y)-Math.min(this.y,pinnedPieces[i][0].y)){
                                    checkDiagonals(pinnedPieces[i][0].x, pinnedPieces[i][0].y, pinnedPieces[i][0].x, pinnedPieces[i][0].y, this.x > pinnedPieces[i][0].x ? 1:-1, this.y > pinnedPieces[i][0].y ? 1:-1);
                                }
                            }
                        }
                        /**
                         * otherwise does normal moves
                         */
                    }else if(kingChecked == 0){
                        pathing(1,1,this.x,this.y,true,0);
                        pathing(-1,1,this.x,this.y,true,0);
                        pathing(1,-1,this.x,this.y,true,0);
                        pathing(-1,-1,this.x,this.y,true,0);
                    }  
                } catch (Exception e) {
                }
            break;
            case knight:
                /**
                 * checks if any of these are checks and if they are the piece will be pinned
                 */
                if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){// didn't know howelse i could write this
                }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                }else if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[4][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){// didn't know howelse i could write this
                }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                /**
                 * if the king is in check goes through and checks if the knight can block or take the enemy piece
                 */
                }else if(kingChecked == 1){
                    /**
                     * checks if it can block line checks
                     */
                    if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == piecEnum.enemyPiece)){
                        for(int i = 0; i < Math.max(pinnedPieces[0][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))
                        -Math.min(pinnedPieces[0][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                            if(((Math.max(this.x,pinnedPieces[0][0].x-i)-Math.min(this.x,pinnedPieces[0][0].x-i) == 2)&&
                            (Math.max(this.y,pinnedPieces[0][0].y)-Math.min(this.y,pinnedPieces[0][0].y) == 1))||
                            ((Math.max(this.x,pinnedPieces[0][0].x-i)-Math.min(this.x,pinnedPieces[0][0].x-i) == 1)&&
                            (Math.max(this.y,pinnedPieces[0][0].y)-Math.min(this.y,pinnedPieces[0][0].y) == 2))){
                                colourTiles(pinnedPieces[0][0].x-i, pinnedPieces[0][0].y);
                            }
                        };
                    }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == piecEnum.enemyPiece)){
                        for(int i = 0; i < Math.max(pinnedPieces[1][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))
                        -Math.min(pinnedPieces[1][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                            if(((Math.max(this.x,pinnedPieces[1][0].x+i)-Math.min(this.x,pinnedPieces[1][0].x+i) == 2)&&
                            (Math.max(this.y,pinnedPieces[1][0].y)-Math.min(this.y,pinnedPieces[1][0].y) == 1))||
                            ((Math.max(this.x,pinnedPieces[1][0].x+i)-Math.min(this.x,pinnedPieces[1][0].x+i) == 1)&&
                            (Math.max(this.y,pinnedPieces[1][0].y)-Math.min(this.y,pinnedPieces[1][0].y) == 2))){
                                colourTiles(pinnedPieces[1][0].x+i, pinnedPieces[1][0].y);
                            }
                        };
                    }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == piecEnum.enemyPiece)){
                        for(int i = 0; i < Math.max(pinnedPieces[2][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                        -Math.min(pinnedPieces[2][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                            if(((Math.max(this.x,pinnedPieces[2][0].x)-Math.min(this.x,pinnedPieces[2][0].x) == 2)&&
                            (Math.max(this.y,pinnedPieces[2][0].y-i)-Math.min(this.y,pinnedPieces[2][0].y-i) == 1))||
                            ((Math.max(this.x,pinnedPieces[2][0].x)-Math.min(this.x,pinnedPieces[2][0].x) == 1)&&
                            (Math.max(this.y,pinnedPieces[2][0].y-i)-Math.min(this.y,pinnedPieces[2][0].y-i) == 2))){
                                colourTiles(pinnedPieces[2][0].x, pinnedPieces[2][0].y-i);
                            }
                        };
                    }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == piecEnum.enemyPiece)){
                        for(int i = 0; i < Math.max(pinnedPieces[3][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                        -Math.min(pinnedPieces[3][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                            if(((Math.max(this.x,pinnedPieces[3][0].x)-Math.min(this.x,pinnedPieces[3][0].x) == 2)&&
                            (Math.max(this.y,pinnedPieces[3][0].y+i)-Math.min(this.y,pinnedPieces[3][0].y+i) == 1))||
                            ((Math.max(this.x,pinnedPieces[3][0].x)-Math.min(this.x,pinnedPieces[3][0].x) == 1)&&
                            (Math.max(this.y,pinnedPieces[3][0].y+i)-Math.min(this.y,pinnedPieces[3][0].y+i) == 2))){
                                colourTiles(pinnedPieces[3][0].x, pinnedPieces[3][0].y+i);
                            }
                        };
                        /**
                         * checks if it can block diagonal checks
                         */
                    }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == piecEnum.enemyPiece)){
                        for(int i = 0; i < Math.max(pinnedPieces[4][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                        -Math.min(pinnedPieces[4][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                            if(((Math.max(this.x,pinnedPieces[4][0].x-i)-Math.min(this.x,pinnedPieces[4][0].x-i) == 2)&&
                            (Math.max(this.y,pinnedPieces[4][0].y-i)-Math.min(this.y,pinnedPieces[4][0].y-i) == 1))||
                            ((Math.max(this.x,pinnedPieces[4][0].x-i)-Math.min(this.x,pinnedPieces[4][0].x-i) == 1)&&
                            (Math.max(this.y,pinnedPieces[4][0].y-i)-Math.min(this.y,pinnedPieces[4][0].y-i) == 2))){
                                colourTiles(pinnedPieces[4][0].x-i, pinnedPieces[4][0].y-i);
                            }
                        };
                    }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][0].pieceType == piecEnum.enemyPiece)){
                        for(int i = 0; i < Math.max(pinnedPieces[5][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                        -Math.min(pinnedPieces[5][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                            if(((Math.max(this.x,pinnedPieces[5][0].x+i)-Math.min(this.x,pinnedPieces[5][0].x+i) == 2)&&
                            (Math.max(this.y,pinnedPieces[5][0].y-i)-Math.min(this.y,pinnedPieces[5][0].y-i) == 1))||
                            ((Math.max(this.x,pinnedPieces[5][0].x+i)-Math.min(this.x,pinnedPieces[5][0].x+i) == 1)&&
                            (Math.max(this.y,pinnedPieces[5][0].y-i)-Math.min(this.y,pinnedPieces[5][0].y-i) == 2))){
                                colourTiles(pinnedPieces[5][0].x+i, pinnedPieces[5][0].y-i);
                            }
                        };
                    }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][0].pieceType == piecEnum.enemyPiece)){
                        for(int i = 0; i < Math.max(pinnedPieces[6][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                        -Math.min(pinnedPieces[6][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                            if(((Math.max(this.x,pinnedPieces[6][0].x-i)-Math.min(this.x,pinnedPieces[6][0].x-i) == 2)&&
                            (Math.max(this.y,pinnedPieces[6][0].y+i)-Math.min(this.y,pinnedPieces[6][0].y+i) == 1))||
                            ((Math.max(this.x,pinnedPieces[6][0].x-i)-Math.min(this.x,pinnedPieces[6][0].x-i) == 1)&&
                            (Math.max(this.y,pinnedPieces[6][0].y+i)-Math.min(this.y,pinnedPieces[6][0].y+i) == 2))){
                                colourTiles(pinnedPieces[6][0].x-i, pinnedPieces[6][0].y+i);
                            }
                        };
                    }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == piecEnum.enemyPiece)){
                        for(int i = 0; i < Math.max(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10)))
                        -Math.min(pinnedPieces[7][0].y,(int)(Math.round((Game.pieceXY.get(this.whitePiece?"wk":"bk")-(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))*10))); i++){
                            if(((Math.max(this.x,pinnedPieces[7][0].x+i)-Math.min(this.x,pinnedPieces[7][0].x+i) == 2)&&
                            (Math.max(this.y,pinnedPieces[7][0].y+i)-Math.min(this.y,pinnedPieces[7][0].y+i) == 1))||
                            ((Math.max(this.x,pinnedPieces[7][0].x+i)-Math.min(this.x,pinnedPieces[7][0].x+i) == 1)&&
                            (Math.max(this.y,pinnedPieces[7][0].y+i)-Math.min(this.y,pinnedPieces[7][0].y+i) == 2))){
                                colourTiles(pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i);
                            }
                        };
                    /**
                     * checks if knight can take the checking piece
                     */
                    }else for(int i = 8; i < 18;i++){
                    if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == piecEnum.enemyPiece)){
                            if(((Math.max(this.x,pinnedPieces[i][0].x)-Math.min(this.x,pinnedPieces[i][0].x) == 2)&&
                            (Math.max(this.y,pinnedPieces[i][0].y)-Math.min(this.y,pinnedPieces[i][0].y) == 1))||
                            ((Math.max(this.x,pinnedPieces[i][0].x)-Math.min(this.x,pinnedPieces[i][0].x) == 1)&&
                            (Math.max(this.y,pinnedPieces[i][0].y)-Math.min(this.y,pinnedPieces[i][0].y) == 2))){
                                colourTiles(pinnedPieces[i][0].x, pinnedPieces[i][0].y);
                            }
                        };
                    }
                /**
                 * otherwise checks its normal moves
                 */
                }else if(kingChecked == 0){
                    jumping(1,2, this.x, this.y,true,0);
                    jumping(2,1, this.x, this.y,true,0);
                    jumping(1,-2, this.x, this.y,true,0);
                    jumping(2,-1, this.x, this.y,true,0);
                }
            break;
            case pawn:
                /**
                 * cant block horizontal checks
                 */
                if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null)&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[0][0].x)&&(this.y == pinnedPieces[0][0].y)){
                }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null)&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[1][0].x)&&(this.y == pinnedPieces[1][0].y)){
                    /**
                     * allows movement if not double checked as it will still be blocking check
                     */
                }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null)&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[2][0].x)&&(this.y == pinnedPieces[2][0].y)){
                    if(kingChecked != 2){
                        pawnMovement(this.moveDirecion);
                    }
                }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null)&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[3][0].x)&&(this.y == pinnedPieces[3][0].y)){
                    if(kingChecked != 2){
                        pawnMovement(this.moveDirecion);
                    }
                    /**
                     * checks if it can take the piece thats pinning it diagonnaly
                     */
                }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null)&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[4][0].x)&&(this.y == pinnedPieces[4][0].y)){
                    if((kingChecked != 2)&&(this.whitePiece == false)&&(Math.max(this.y,pinnedPieces[4][1].y)-Math.min(this.y,pinnedPieces[4][1].y) == 1)){
                        colourTiles(pinnedPieces[4][1].x, pinnedPieces[4][1].y);
                    }
                }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null)&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[5][0].x)&&(this.y == pinnedPieces[5][0].y)){
                    if((kingChecked != 2)&&(this.whitePiece == false)&&(Math.max(this.y,pinnedPieces[5][1].y)-Math.min(this.y,pinnedPieces[5][1].y) == 1)){
                        colourTiles(pinnedPieces[5][1].x, pinnedPieces[5][1].y);
                    }
                }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null)&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[6][0].x)&&(this.y == pinnedPieces[6][0].y)){
                    if((kingChecked != 2)&&(this.whitePiece == true)&&(Math.max(this.y,pinnedPieces[6][1].y)-Math.min(this.y,pinnedPieces[6][1].y) == 1)){
                        colourTiles(pinnedPieces[6][1].x, pinnedPieces[6][1].y);
                    }
                }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null)&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)&&(this.x == pinnedPieces[7][0].x)&&(this.y == pinnedPieces[7][0].y)){
                    if((kingChecked != 2)&&(this.whitePiece == true)&&(Math.max(this.y,pinnedPieces[7][1].y)-Math.min(this.y,pinnedPieces[7][1].y) == 1)){
                        colourTiles(pinnedPieces[7][1].x, pinnedPieces[7][1].y);
                    }
                }else if(kingChecked == 1){
                    /**
                     * checks if it can block check via normal or double move if possible or can take the checking piece
                     */
                    if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == pieceType.enemyPiece)){
                        if((this.y + moveDirecion == pinnedPieces[0][0].y)&&(this.x + (this.x > pinnedPieces[0][0].x ? -1:+1) == pinnedPieces[0][0].x)){
                            colourTiles(pinnedPieces[0][0].x, pinnedPieces[0][0].y);
                        }
                        if((this.x > Math.min((int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),pinnedPieces[0][0].x))&&(this.x < Math.max((int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),pinnedPieces[0][0].x))&&(Game.pieces[this.y + moveDirecion][this.x] == null)){
                            if(this.y + moveDirecion == pinnedPieces[0][0].y){
                                colourTiles(this.x,pinnedPieces[0][0].y);
                            }else if((((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3)))&&(Game.pieces[this.y + moveDirecion*2][this.x] == null)&&(this.y + moveDirecion*2 == pinnedPieces[0][0].y)){
                                colourTiles(this.x,pinnedPieces[0][0].y);
                            }
                        }
                    }else if((pinnedPieces[1][0] != null)&&(pinnedPieces[1][0].pieceType == pieceType.enemyPiece)){
                        if((this.y + moveDirecion == pinnedPieces[1][0].y)&&(this.x + (this.x > pinnedPieces[1][0].x ? -1:+1) == pinnedPieces[1][0].x)){
                            colourTiles(pinnedPieces[1][0].x, pinnedPieces[1][0].y);
                        }
                        if((this.x > Math.min((int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),pinnedPieces[1][0].x))&&(this.x < Math.max((int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")),pinnedPieces[1][0].x))&&(Game.pieces[this.y + moveDirecion][this.x] == null)){
                            if(this.y + moveDirecion == pinnedPieces[1][0].y){
                                colourTiles(this.x,pinnedPieces[1][0].y);
                            }else if((((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3)))&&(Game.pieces[this.y + moveDirecion*2][this.x] == null)&&(this.y + moveDirecion*2 == pinnedPieces[1][0].y)){
                                colourTiles(this.x,pinnedPieces[1][0].y);
                            }
                        }
                    }else if((pinnedPieces[2][0] != null)&&(pinnedPieces[2][0].pieceType == pieceType.enemyPiece)){
                        if((this.y + moveDirecion == pinnedPieces[2][0].y)&&(this.x + (this.x > pinnedPieces[2][0].x ? -1:+1) == pinnedPieces[2][0].x)){
                            colourTiles(pinnedPieces[2][0].x, pinnedPieces[2][0].y);
                        }
                    }else if((pinnedPieces[3][0] != null)&&(pinnedPieces[3][0].pieceType == pieceType.enemyPiece)){
                        if((this.y + moveDirecion == pinnedPieces[3][0].y)&&(this.x + (this.x > pinnedPieces[3][0].x ? -1:+1) == pinnedPieces[3][0].x)){
                            colourTiles(pinnedPieces[3][0].x, pinnedPieces[3][0].y);
                        }
                        /**
                         * checks if it can take checking piece otherwise calls check for pawn which calculates if the pawn can blocj 
                         * the diagonal check by moving
                         */
                    }else if((pinnedPieces[4][0] != null)&&(pinnedPieces[4][0].pieceType == pieceType.enemyPiece)){
                        if((this.y + moveDirecion == pinnedPieces[4][0].y)&&(this.x + (this.x > pinnedPieces[4][0].x ? -1:+1) == pinnedPieces[4][0].x)){
                            colourTiles(pinnedPieces[4][0].x, pinnedPieces[4][0].y);
                        }else for(int i = 0; i < Math.max(pinnedPieces[4][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))-Math.min(pinnedPieces[4][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                            checkForPawn(pinnedPieces[4][0].x-i, pinnedPieces[4][0].y-i);
                        }
                    }else if((pinnedPieces[5][0] != null)&&(pinnedPieces[5][0].pieceType == pieceType.enemyPiece)){
                        if((this.y + moveDirecion == pinnedPieces[5][0].y)&&(this.x + (this.x > pinnedPieces[5][0].x ? -1:+1) == pinnedPieces[5][0].x)){
                            colourTiles(pinnedPieces[5][0].x, pinnedPieces[5][0].y);
                        }for(int i = 0; i < Math.max(pinnedPieces[5][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))-Math.min(pinnedPieces[5][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                            checkForPawn(pinnedPieces[5][0].x+i, pinnedPieces[5][0].y-i);
                        }
                    }else if((pinnedPieces[6][0] != null)&&(pinnedPieces[6][0].pieceType == pieceType.enemyPiece)){
                        if((this.y + moveDirecion == pinnedPieces[6][0].y)&&(this.x + (this.x > pinnedPieces[6][0].x ? -1:+1) == pinnedPieces[6][0].x)){
                            colourTiles(pinnedPieces[6][0].x, pinnedPieces[6][0].y);
                        }for(int i = 0; i < Math.max(pinnedPieces[6][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))-Math.min(pinnedPieces[6][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                            checkForPawn(pinnedPieces[6][0].x-i, pinnedPieces[6][0].y+i);
                        }
                    }else if((pinnedPieces[7][0] != null)&&(pinnedPieces[7][0].pieceType == pieceType.enemyPiece)){
                        if((this.y + moveDirecion == pinnedPieces[7][0].y)&&(this.x + (this.x > pinnedPieces[7][0].x ? -1:+1) == pinnedPieces[7][0].x)){
                            colourTiles(pinnedPieces[7][0].x, pinnedPieces[7][0].y);
                        }for(int i = 0; i < Math.max(pinnedPieces[7][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))-Math.min(pinnedPieces[7][0].x,(int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk"))); i++){
                            checkForPawn(pinnedPieces[7][0].x+i, pinnedPieces[7][0].y+i);
                        }
                        /**
                         * otherwise checks if it can take checking piece
                         */
                    }else for(int i = 8; i < 18; i++){
                        try {
                            if((this.y + moveDirecion == pinnedPieces[i][0].y)&&(this.x + (this.x > pinnedPieces[i][0].x ? -1:+1) == pinnedPieces[i][0].x)){
                                colourTiles(pinnedPieces[i][0].x, pinnedPieces[i][0].y);
                            }
                        } catch (Exception e) {
                        }
                    }
                    /**
                     * if none of these are true it does it's normal moves
                     */
                }else if(kingChecked == 0){
                    takingEnPassant = false;
                    pawnMovement(this.moveDirecion);
                    takeSideways(this.moveDirecion, 1,0,0,true,0);
                }
            break;
            /**
             * rook and bishop combined so checks for diagonal and line blocks and takes for pieces uses same code as bishop and rook
             */
            case queen:
                if(((pinnedPieces[0][0] != null)&&(pinnedPieces[0][1] != null))&&((pinnedPieces[0][0].x == this.x)&&(pinnedPieces[0][0].y == this.y))&&(pinnedPieces[0][1].pieceType == pieceType.enemyPiece)){//specifically dont use for statement
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[0][1].x,pinnedPieces[0][1].y);
                    }
                }else if(((pinnedPieces[1][0] != null)&&(pinnedPieces[1][1] != null))&&((pinnedPieces[1][0].x == this.x)&&(pinnedPieces[1][0].y == this.y))&&(pinnedPieces[1][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[1][1].x,pinnedPieces[1][1].y);
                    }
                }else if(((pinnedPieces[2][0] != null)&&(pinnedPieces[2][1] != null))&&((pinnedPieces[2][0].x == this.x)&&(pinnedPieces[2][0].y == this.y))&&(pinnedPieces[2][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[2][1].x,pinnedPieces[2][1].y);
                    }
                }else if(((pinnedPieces[3][0] != null)&&(pinnedPieces[3][1] != null))&&((pinnedPieces[3][0].x == this.x)&&(pinnedPieces[3][0].y == this.y))&&(pinnedPieces[3][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[3][1].x,pinnedPieces[3][1].y);
                    }
                }else if(((pinnedPieces[4][0] != null)&&(pinnedPieces[4][1] != null))&&((pinnedPieces[4][0].x == this.x)&&(pinnedPieces[4][0].y == this.y))&&(pinnedPieces[4][1].pieceType == pieceType.enemyPiece)){//specifically dont use for statement
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[4][1].x,pinnedPieces[4][1].y);
                    }
                }else if(((pinnedPieces[5][0] != null)&&(pinnedPieces[5][1] != null))&&((pinnedPieces[5][0].x == this.x)&&(pinnedPieces[5][0].y == this.y))&&(pinnedPieces[5][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[5][1].x,pinnedPieces[5][1].y);
                    }
                }else if(((pinnedPieces[6][0] != null)&&(pinnedPieces[6][1] != null))&&((pinnedPieces[6][0].x == this.x)&&(pinnedPieces[6][0].y == this.y))&&(pinnedPieces[6][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[6][1].x,pinnedPieces[6][1].y);
                    }
                }else if(((pinnedPieces[7][0] != null)&&(pinnedPieces[7][1] != null))&&((pinnedPieces[7][0].x == this.x)&&(pinnedPieces[7][0].y == this.y))&&(pinnedPieces[7][1].pieceType == pieceType.enemyPiece)){
                    if(kingChecked != 2){
                        colourTiles(pinnedPieces[7][1].x,pinnedPieces[7][1].y);
                    }
                    /**
                     * still just rook and queen checks combined so it it checks if it can block on either lines or diagonal 
                     */
                }else if(kingChecked == 1){
                    if((pinnedPieces[0][0] != null)&&(pinnedPieces[0][0].pieceType == piecEnum.enemyPiece)){
                        if((this.x > (int)Math.floor(Game.pieceXY.get(this.whitePiece?"wk":"bk")))&&(this.x <= pinnedPieces[0][0].x)){
                            checkVertical(pinnedPieces[0][0].y);
                        }else if((pinnedPieces[0][1] != null)&&((pinnedPieces[0][1].x == this.x)&&(pinnedPieces[0][1].y == this.y))){
                            colourTiles(pinnedPieces[0][0].x,pinnedPieces[0][0].y);
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
                            colourTiles(pinnedPieces[1][0].x,pinnedPieces[1][0].y);
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
                            colourTiles(pinnedPieces[2][0].x,pinnedPieces[2][0].y);
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
                            colourTiles(pinnedPieces[3][0].x,pinnedPieces[3][0].y);
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
                    /**
                     * otherwise does its normal moves which is just both rook and bishop moves
                     */
                }else if(kingChecked == 0){
                    pathing(1,0,this.x,this.y,true,0);
                    pathing(-1,0,this.x,this.y,true,0);
                    pathing(0,1,this.x,this.y,true,0);
                    pathing(0,-1,this.x,this.y,true,0);
                    pathing(1,1,this.x,this.y,true,0);
                    pathing(-1,1,this.x,this.y,true,0);
                    pathing(1,-1,this.x,this.y,true,0);
                    pathing(-1,-1,this.x,this.y,true,0);
                }
            break;
            /**
             * checks every square for check and if no check is targeting that square allows the king to move to it
             * uses safeSquare method to check for checks kinda a simplified version of all the checks above
             */
            case king:
                try {
                    if(((Game.pieces[this.y-1][this.x] == null)||(Game.pieces[this.y-1][this.x].whitePiece != this.whitePiece))&&(safeSquare(this.x, this.y-1))){
                        colourTiles(this.x, this.y-1);
                    }
                } catch (Exception e) {
                }
                try {
                if(((Game.pieces[this.y+1][this.x] == null)||(Game.pieces[this.y+1][this.x].whitePiece != this.whitePiece))&&(safeSquare(this.x, this.y+1))){
                    colourTiles(this.x, this.y+1);
                }
                }catch(Exception e){

                }
                try {
                    if(((Game.pieces[this.y-1][this.x+1] == null)||(Game.pieces[this.y-1][this.x+1].whitePiece != this.whitePiece))&&(safeSquare(this.x+1, this.y-1))){
                        colourTiles(this.x+1, this.y-1);
                    } 
                } catch (Exception e) {
                }
                try {
                    if(((Game.pieces[this.y+1][this.x+1] == null)||(Game.pieces[this.y+1][this.x+1].whitePiece != this.whitePiece))&&(safeSquare(this.x+1, this.y+1))){
                        colourTiles(this.x+1, this.y+1);
                    }
                } catch (Exception e) {
                }
                try {
                    if(((Game.pieces[this.y-1][this.x-1] == null)||(Game.pieces[this.y-1][this.x-1].whitePiece != this.whitePiece))&&(safeSquare(this.x-1, this.y-1))){
                        colourTiles(this.x-1, this.y-1);
                    }
                } catch (Exception e) {
                }
                try {
                    if(((Game.pieces[this.y+1][this.x-1] == null)||(Game.pieces[this.y+1][this.x-1].whitePiece != this.whitePiece))&&(safeSquare(this.x-1, this.y+1))){
                        colourTiles(this.x-1, this.y+1);
                    }
                } catch (Exception e) {
                }
                try {
                    if(((Game.pieces[this.y][this.x+1] == null)||(Game.pieces[this.y][this.x+1].whitePiece != this.whitePiece))&&(safeSquare(this.x+1, this.y))){
                        colourTiles(this.x+1, this.y);
                    }
                } catch (Exception e) {
                }
                try {
                    if(((Game.pieces[this.y][this.x-1] == null)||(Game.pieces[this.y][this.x-1].whitePiece != this.whitePiece))&&(safeSquare(this.x-1, this.y))){
                        colourTiles(this.x-1, this.y);
                    }
                } catch (Exception e) {
                }
            break;
        }
    }
    /**
     * intakes position in form of checkX and checkY, returns true or false
     * resets pinned pieces and redoes the checks except this time only does each time once as only looking for if the 
     * square is safe to move to, safe squares are squares that dont have any enemy piece checking them
     */
    private boolean safeSquare(int checkX, int checkY){
        for(int i = 0; i < 18; i++){
            pinnedPieces[i][0] = null;
            pinnedPieces[i][1] = null;
        }
        /**
         * same as above except only does each one once
         */
        pathing(1,0,checkX,checkY,false,0);
        pathing(-1,0,checkX,checkY,false,1);
        pathing(0,1,checkX,checkY,false,2);
        pathing(0,-1,checkX,checkY,false,3);
        pathing(1,1,checkX,checkY,false,4);
        pathing(-1,1,checkX,checkY,false,5);
        pathing(1,-1,checkX,checkY,false,6);
        pathing(-1,-1,checkX,checkY,false,7);
        jumping(1,2,checkX,checkY,false,8);
        jumping(2,1,checkX,checkY,false,9);
        jumping(1,-2,checkX,checkY,false,10);
        jumping(2,-1,checkX,checkY,false,11);
        takeSideways(this.whitePiece?-1:1,1, checkX,checkY,false, 16);
        /**
         * checks if there are any checks for the square and if there are it returns false meaning that the king cant move there
         */
        for(int i = 0; i<18; i++){
            if((pinnedPieces[i][0] != null)&&(pinnedPieces[i][0].pieceType == pieceType.enemyPiece)){
                return false;
            }
        }
        /**
         * checks for a king 1 tile away as if there is the other kings radius will hit this kings radius allowing it to take the other king
         * this makes it so the kings can never get too close to each other
         */
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
    /**
     * intakes position of enemy y in form enemyPosition, returns nothing
     * checks linearly towards the the enemy y and if there is a fellow same cloured piece nothing happens but if there
     * is and enemy piece or the y reaches the enemy y colours a tile there 
     */
    private void checkVertical(int enemyPosition){
        for(int upOrDown = this.y; upOrDown != enemyPosition;){
            upOrDown = upOrDown + (this.y > enemyPosition ? -1:+1);
            if(upOrDown == enemyPosition ){
                colourTiles(this.x,enemyPosition);
            }else if(Game.pieces[upOrDown][this.x] != null){
                upOrDown = enemyPosition;
            }
        }
    }
    /**
     * intakes position of enemy x in form enemyPosition, returns nothing
     * does the same things as method above but horizontally
     */
    private void checkHorizontal(int enemyPosition){
        for(int leftOrRight = this.x; leftOrRight != enemyPosition;){
            leftOrRight = leftOrRight + (this.x > enemyPosition ? -1:+1);
            if(leftOrRight == enemyPosition ){
                colourTiles(enemyPosition, this.y);
            }else if(Game.pieces[this.y][leftOrRight] != null){
                leftOrRight = enemyPosition;
            }
        }
    }     

    /**
     * intakes x and y position whch are the of the enemy piece, check x and check y which are where its checking and xincrement 
     * and yincrement which are the direction its checking, returns nothing
     * checks from given position for this piece and if it doesn't find it checks along the next path of the enemy piece as it 
     * gets called again until its checked along the line of the enemy piece.
     */
    private void checkDiagonals(int targetX, int targetY, int checkX, int checkY, int xIncrement, int yIncrement){
        if((checkY+yIncrement < Game.boardSize)&&(checkY+yIncrement >= 0)&&(checkX+xIncrement < Game.boardSize)&&(checkX+xIncrement >= 0)){
            if(Game.pieces[checkY+yIncrement][checkX+xIncrement] == null){
                checkDiagonals(targetX, targetY, checkX+xIncrement, checkY+yIncrement, xIncrement, yIncrement);
            }else if((Game.pieces[checkY+yIncrement][checkX+xIncrement].x == this.x)&&(Game.pieces[checkY+yIncrement][checkX+xIncrement].y == this.y)){
                colourTiles(targetX, targetY);
            }
        }
    }
    /**
     * intakes x and y position in form targetX and targetY, returns nothing
     * checks if it moves once if it can block the check otherwise checks if it can move twice and if it can checks if 
     * it can block the check using a double move
     */
    private void checkForPawn(int targetX, int targetY){
        if((this.x == targetX)&&(Game.pieces[this.y + moveDirecion][this.x] == null)){
            if(this.y + moveDirecion == targetY){
                colourTiles(targetX,targetY);
            }else if((((this.whitePiece)&&(this.y == Game.boardSize/2+2))||((this.whitePiece == false)&&(this.y == Game.boardSize/2+-3)))&&(this.y + moveDirecion * 2 == targetY)&&(Game.pieces[this.y + moveDirecion*2][this.x] == null)){
                colourTiles(targetX,targetY);
            }
        }
    }
}