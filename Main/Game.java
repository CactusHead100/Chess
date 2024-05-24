package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import Main.Pieces.piecEnum;

public class Game extends JPanel{
    private Mouse_Input mouse_Input = new Mouse_Input(this);
    public static int tileSize = 40;
    public static final int boardSize = 16;
    int mouseX;
    int mouseY;
    public Board tiles[][] = new Board[boardSize][boardSize];
    public static Pieces pieces[][] = new Pieces[boardSize][boardSize];

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
                    tiles[y][x] = new Board(x*tileSize, y*tileSize, Color.white);
                }else{
                    tiles[y][x] = new Board(x*tileSize, y*tileSize, Color.black);
                }
            }
        }
    }

    private void CreatePieces() {
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){
                switch (x) {
                    case boardSize/2-4:
                        switch(y){
                            case boardSize/2-4:
                                pieces[y][x] = new Pieces((x)*tileSize, (y)*tileSize, piecEnum.rook, false);
                            break;
                            case boardSize/2+3:
                                pieces[y][x] = new Pieces((x)*tileSize, (y)*tileSize, piecEnum.rook, true);
                            break;
                        }
                    break;
                    case boardSize/2+3:
                        switch(y){
                            case boardSize/2-4:
                                pieces[y][x] = new Pieces((x)*tileSize, (y)*tileSize, piecEnum.rook, false);
                            break;
                            case boardSize/2+3:
                                pieces[y][x] = new Pieces((x)*tileSize, (y)*tileSize, piecEnum.rook, true);
                            break;
                        }
                    break;
                }
                    
                    
                }//else if((y == 0)&&((x == 1)||(x == 6))){
                   // pieces[y][x] = new Pieces(x, y, 2, getFocusTraversalKeysEnabled());
                //}
            }
        }

    public void MouseClicked(int mouseXpos,int mouseYpos){
        mouseX = mouseXpos/tileSize;
        mouseY = mouseYpos/tileSize;
        System.out.println(mouseY);
        try{
            if(pieces[mouseY][mouseX].getAvailableMoves() == 0){
                try{
                    tiles[mouseY][mouseX - 1].colour = Color.red;
                }catch (Exception e) {
                    System.out.println("fail");
                }
            }
        }catch (Exception e) {
            System.out.println("fail");
        }
        
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
                g2d.setColor(Color.blue);
                try {
                    g2d.fill(pieces[y][x].rectangle);
                    System.out.println("x:"+x+"y:"+y);
                } catch (Exception e) {
                }
            }
        } 
    }
}

