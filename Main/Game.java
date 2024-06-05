package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Dictionary;
import java.util.Hashtable;

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
    private int[] movingPiece = new int[2];

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
                                pieces[y][x] = new Pieces(x, y, piecEnum.rook, false);
                            break;
                            case boardSize/2+3:
                                pieces[y][x] = new Pieces(x, y, piecEnum.rook, true);
                            break;
                        }
                    break;
                    case boardSize/2+3:
                        switch(y){
                            case boardSize/2-4:
                                pieces[y][x] = new Pieces(x, y, piecEnum.rook, false);
                            break;
                            case boardSize/2+3:
                                pieces[y][x] = new Pieces(x, y, piecEnum.rook, true);
                            break;
                        }
                    break;
                }
                    
                    
                }
            }
        }

    public void MouseClicked(int mouseXpos,int mouseYpos){
        mouseX = mouseXpos/tileSize;
        mouseY = mouseYpos/tileSize;
        movingPiece[0] = mouseX;
        movingPiece[1] = mouseY;

        try{
            @SuppressWarnings("unchecked")
            Dictionary<Integer, Double> possibleMoves = pieces[mouseY][mouseX].getAvailableMoves();
            System.out.println(possibleMoves.toString()+"\n"+possibleMoves.size());
            for(int i = 0; i < possibleMoves.size(); i++){
                int x = (int)Math.floor(possibleMoves.get(i));
                int y = (int)(Math.round((possibleMoves.get(i)-x)*100));
                tiles[y][x].colour = Color.red;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        repaint();
    }

    public void MouseReleased(int mouseXpos,int mouseYpos){
        mouseX = mouseXpos/tileSize;
        mouseY = mouseYpos/tileSize;
        if(tiles[mouseY][mouseX].colour== Color.RED){
            pieces[movingPiece[1]][movingPiece[0]].movePiece(mouseX,mouseY);
            repaint();
        }
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
                g2d.setColor(Color.blue);
                try {
                    g2d.fill(pieces[y][x].rectangle);
                } catch (Exception e) {
                }
            }
        } 
    }
}

