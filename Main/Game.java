package Main;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.*;
import Main.Pieces.piecEnum;

/**
 * Class game it controls the drawing of pieces creation of the board and and pieces and takes input from the mouselistner to call functions in other classes to make the game run
*/
public class Game extends JPanel{

    //constants things that will never change 
    public static final int tileSize = 80;//controls the tilesize so i can scale the tiles to ensure they fit in the window
    public static final int boardSize = 8;//controls how many tiles the board is made up of 
    public static final Double pixelSize = (double)tileSize/16;// this is used to determine the scale of the piices and is relitive to the tilesize
    public static final Color moveColour = new Color(255,75,75);//this is the colour that appears showing where you can move

    //for later use to find what the mouse is clicking on
    int mouseX;
    int mouseY;

    //these are objects from classes Board and Pieces and allow me to store and access lots of data easily
    public static Board tiles[][] = new Board[boardSize][boardSize];//this class stores the colour and size of the tiles
    public static Pieces pieces[][] = new Pieces[boardSize][boardSize];//this stores pretty much all the piece infomation

    //these control whos turn it is and and allow me easy access to some needed positions 
    private int[] movingPiece = new int[2];//allows me to store the old mouseX and mouseY so I know what piece is being moved
    boolean whitesMove = true;//controlls the turns
    public static Dictionary<String, Double> pieceXY = new Hashtable<>();// stores specific positions that i need to access frequently 

    //sets up the mouse listner allowing for mouse input and for us to use that to do things
    private Mouse_Input mouse_Input = new Mouse_Input(this);

    //note this was case sensitive as its a constructor
    public Game(){
        addMouseListener(mouse_Input);
        addMouseMotionListener(mouse_Input);
        createTiles();//calls to create the board 
        createPieces();//calls to create pieces and place them in the right spots
    }

    /**
     * intakes and returns nothing 
     * goes through board and creates new board which contains infomation about the position 
     * and colour of the tile
     * in short function creates Board objects into the 2D array tile
    */
    public void createTiles(){

        //goes through the board and 
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){

                //this checks if its on an even or odd square and depending on that changes the colour of the tile
                if(((y%2 == 0)&&(x%2==0))||((x%2==1)&&(y%2 == 1))){

                    //adds the board objects into the array for later drawing, note:colours were chosen through trialling
                    tiles[y][x] = new Board(x*tileSize, y*tileSize, new Color(200,150,100));
                }else{
                    tiles[y][x] = new Board(x*tileSize, y*tileSize, new Color(150,75,0));
                }
            }
        }
    }

    /**
     * intakes and returns nothing 
     * goes through the board and sets each piece to null
     *  in short clears all pieces off the board allowing for a new game to be played
    */
    public void clearBoard(){
        int boardY;
        for(int boardX = 0; boardX < boardSize; boardX++){
            for(boardY = 0; boardY < boardSize; boardY++){
                pieces[boardY][boardX] = null;
            }
            boardY = 0;
        }
    }

    /**
     * intakes and returns nothing 
     * goes through the y positions of the board and then calls another function that places the 
     * pieces if at correct y
     * this allows for future adjustments if i want to do them
    */
    public void createPieces() {
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){
                switch (y) {
                    //for all pieces not pawns it places them in a seperate function depending on their x
                    case boardSize/2-4:
                        placePiece(x,y,false);
                    break;
                    case boardSize/2+3:
                        placePiece(x,y,true);
                    break;
                    //as all pawns have the same y are all the same peice so only need some x checks to confirm they are in the center of the board
                    case boardSize/2-3:
                        if((x>=boardSize/2-4)&&(x<=boardSize/2+3)){
                            pieces[y][x] = new Pieces(x, y, piecEnum.pawn, false);
                            String dictReference = "bp"+Integer.toString(x);
                            pieceXY.put(dictReference,Double.parseDouble(Integer.toString(x)+"."+Integer.toString(y)));
                            pieces[y][x].dictReference = dictReference;
                        }
                    break;

                    /**
                     * checks if in the right place on board then creates a piece object it stores its x and y in a dictionary used to calculate en-pasant
                     * then a variable in the piece class to the dict reference its done in this order setting the dictionary reference or key value of its data by initializing 
                     * didn't work this was because of the way i did it around its x position would could change when taking the piece causing errors and this hasn't failed yet
                     * note: same as above just different piece colour 
                    */
                    case boardSize/2+2:
                        if((x>=boardSize/2-4)&&(x<=boardSize/2+3)){
                            pieces[y][x] = new Pieces(x, y, piecEnum.pawn, true);
                            String dictReference = "wp"+Integer.toString(x);
                            pieceXY.put(dictReference,Double.parseDouble(Integer.toString(x)+"."+Integer.toString(y)));
                            pieces[y][x].dictReference = dictReference;
                        }
                    break;
                }                    
                }
            }
        }

        /**
         * intakes x and y and the colour of piece it uses this information to create piece objects returns nothing
         * checks the x position of the pieces and then creates a piece object of corosponding type
         * note that these methods allow for flexible boardsize placing 
        */
        public void placePiece(int x,int y,boolean colourOfPiece){
            switch (x) {
                case boardSize/2-4:
                    pieces[y][x] = new Pieces(x, y, piecEnum.rook, colourOfPiece);
                break;
                case boardSize/2+3:
                    pieces[y][x] = new Pieces(x, y, piecEnum.rook, colourOfPiece);
                break;
                case boardSize/2-3:
                    pieces[y][x] = new Pieces(x, y, piecEnum.knight, colourOfPiece);
                break;
                case boardSize/2+2:
                    pieces[y][x] = new Pieces(x, y, piecEnum.knight, colourOfPiece);
                break;
                case boardSize/2-2:
                    pieces[y][x] = new Pieces(x, y, piecEnum.bishop, colourOfPiece);
                break;
                case boardSize/2+1:
                    pieces[y][x] = new Pieces(x, y, piecEnum.bishop, colourOfPiece);
                break;
                case boardSize/2-1:
                    pieces[y][x] = new Pieces(x, y, piecEnum.queen, colourOfPiece);
                break;
                case boardSize/2:
                    pieces[y][x] = new Pieces(x, y, piecEnum.king, colourOfPiece);

                    //stores king x and y along with creating the piece so all pieces can access it easily for cecking for check and whatnot
                    pieceXY.put(colourOfPiece?"wk":"bk",Double.parseDouble(Integer.toString(x)+"."+Integer.toString(y)));
                break;
            }
        }
    /**
     * intakes mouse position in form of x and y and uses that to find what tile is being clicked 
     * and checks the moves of the piece on that tile if there is one, returns nothing
     * this is called from the mouse listener when there is a specific click it converts the mouse positions 
     * into ones that work related to the board then stores these for later and checks for pieces on the tile 
     * and gets its possible moves if their is a piece on the tile
    */
    public void mouseClicked(int mouseXpos,int mouseYpos){

        //converts the given variables to ones that work on the chessboard
        mouseX = mouseXpos/tileSize;
        mouseY = mouseYpos/tileSize;

        //stores this for later if they move the piece
        movingPiece[0] = mouseX;
        movingPiece[1] = mouseY;
        try {

            //gets the piece to show its possible moves if it's their turn 
            if(whitesMove == pieces[mouseY][mouseX].whitePiece){
                pieces[mouseY][mouseX].getAvailableMoves();
            }
        } catch (Exception e) {
        }
        repaint();
    }

    /**
     * intakes mouse position if form x and y, returns nothing
     * also called from when the mouselistner when it hears a specific click, converts mouse position 
     * for use on the tile grid then checks if tile is can be moved to by its colour and if so calls 
     * applyGamerules which moves the piece, then changes firstclick to true meaning that the next click
     * will check for where the piece can move calls create tiles to reset all tilecolour so that no wrong moves are made
     * then calls repaint to draw this
    */
    public void mouseReleased(int mouseXpos,int mouseYpos){
        mouseX = mouseXpos/tileSize;
        mouseY = mouseYpos/tileSize;

        //pieces show what moves they can do by changing the colour of the tiles on the board this just confirms its an available move
        if(tiles[mouseY][mouseX].colour == moveColour){
            pieces[movingPiece[1]][movingPiece[0]].applyGameRules(mouseX, mouseY);

            //changes the turn to opposing colour
            whitesMove = !whitesMove;
        }

        //this is used to check if its getting moves or trying to move the piece
        mouse_Input.firstClick = true;
        
        //resets the tiles so that none are of the moving colour
        createTiles();
        //paints everything
        repaint();
    }
    /**
     * gets passed Graphics g returns nothing
     * method that draws squares on the canvasa depending on what is inputted
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //clears the canvas 
        g2d.clearRect(0,0,boardSize*tileSize,boardSize*tileSize);

        //draws board based off infomation in said Board objuect in tiles 2D array
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){
                g2d.setColor(tiles[y][x].colour);
                g2d.fill(tiles[y][x].tile);
            }
        } 

        //checks all squares on the board
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){ 

                //uses a try as not all squares have pieces on them so are null and return error             
                try {

                    /**
                     * sets the colour and depending on the colour of piece 
                     * the ? is and if and the : seperates two values that it could be so if the 
                     * statement is true the value is whatever is on the left of the : else right
                    */
                    g2d.setColor(pieces[y][x].whitePiece?new Color(245,230,180):new Color(40,30,30));
                    
                    //goes through and draws all the rectangles making up the piece as i couldn't figure how to get drawing images working with blueJ
                    for(int i = 0; i < pieces[y][x].rectangle.length; i++){ 
                        g2d.fill(pieces[y][x].rectangle[i]);
                    }
                } catch (Exception e) {
                }
            }
        } 
        
    }
}

