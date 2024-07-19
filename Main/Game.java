package Main;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.*;
import Main.Pieces.piecEnum;

public class Game extends JPanel{
    private Mouse_Input mouse_Input = new Mouse_Input(this);
    public static final int tileSize = 80;
    public static final int boardSize = 8;
    public static final int pixelSize = tileSize/16;
    public static final Color moveColour = new Color(255,75,75);
    int mouseX;
    int mouseY;
    public static Board tiles[][] = new Board[boardSize][boardSize];
    public static Pieces pieces[][] = new Pieces[boardSize][boardSize];
    private int[] movingPiece = new int[2];
    public static Dictionary<String, Double> pieceXY = new Hashtable<>();
    public Game(){
        addMouseListener(mouse_Input);
        addMouseMotionListener(mouse_Input);
        CreateTiles();
        CreatePieces();
    }

    //Function creates Board objects into the 2D array tile
    private void CreateTiles(){
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){
                if(((y%2 == 0)&&(x%2==0))||((x%2==1)&&(y%2 == 1))){
                    tiles[y][x] = new Board(x*tileSize, y*tileSize, new Color(200,150,100));
                }else{
                    tiles[y][x] = new Board(x*tileSize, y*tileSize, new Color(150,75,0));
                }
            }
        }
    }

    private void CreatePieces() {
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){
                switch (y) {
                    case boardSize/2-4:
                        PlacePiece(x,y,false);
                    break;
                    case boardSize/2+3:
                        PlacePiece(x,y,true);
                    break;
                    case boardSize/2-3:
                        if((x>=boardSize/2-4)&&(x<=boardSize/2+3)){
                            pieces[y][x] = new Pieces(x, y, piecEnum.pawn, false);
                        }
                    break;
                    case boardSize/2+2:
                        if((x>=boardSize/2-4)&&(x<=boardSize/2+3)){
                            pieces[y][x] = new Pieces(x, y, piecEnum.pawn, true);
                        }
                    break;
                }                    
                }
            }
        }

        public void PlacePiece(int x,int y,boolean colourOfPiece){
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
                break;
            }
        }

    public void MouseClicked(int mouseXpos,int mouseYpos){
        mouseX = mouseXpos/tileSize;
        mouseY = mouseYpos/tileSize;
        movingPiece[0] = mouseX;
        movingPiece[1] = mouseY;
        try {
            pieces[mouseY][mouseX].GetAvailableMoves();
        } catch (Exception e) {
        }
        repaint();
    }

    public void MouseReleased(int mouseXpos,int mouseYpos){
        mouseX = mouseXpos/tileSize;
        mouseY = mouseYpos/tileSize;
        if(tiles[mouseY][mouseX].colour == moveColour){
            pieces[movingPiece[1]][movingPiece[0]].ApplyGamerules(mouseX, mouseY);
        }
        mouse_Input.firstClick = true;
        CreateTiles();
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //Draws board based off infomation in said Board objuect in tiles 2D array
        g2d.clearRect(0,0,boardSize*tileSize,boardSize*tileSize);
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){
                g2d.setColor(tiles[y][x].colour);
                g2d.fill(tiles[y][x].tile);
            }
        } 
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){                
                try {
                    if(pieces[y][x].whitePiece){
                        g2d.setColor(new Color(245,230,180));
                        //g2d.setColor(Color.white);
                    }else{
                        g2d.setColor(new Color(40,30,30));
                        //g2d.setColor(Color.black);
                    }
                    for(int i = 0; i < pieces[y][x].rectangle.length; i++){ 
                        g2d.fill(pieces[y][x].rectangle[i]);
                    }
                } catch (Exception e) {
                }
            }
        } 
        
    }
}

