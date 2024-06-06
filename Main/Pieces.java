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
    int x;
    int y;
    Rectangle2D.Double rectangle;
    Pieces(int x,int y,piecEnum pieceType, boolean whitePiece){
        this.pieceType = pieceType;
        this.whitePiece = whitePiece;
        this.x = x;
        this.y = y;
        rectangle = new Rectangle.Double(x*Game.tileSize,y*Game.tileSize,Game.tileSize,Game.tileSize);
    }

    public void movePiece(int x, int y){
        Game.pieces[this.y][this.x] = null;
        this.x = x;
        this.y = y;
        Game.pieces[this.y][this.x] = new Pieces(this.x, this.y, this.pieceType, this.whitePiece);
    }

    /*public Dictionary getAvailableMoves(){
        Dictionary<Integer, Double> possibleMoves = new Hashtable<>();
        switch (pieceType) {
            case rook:
                for(int m = 0; m < Game.boardSize*2;){
                    for(int l = this.x-1; l >= 0; l--){
                        try {
                            if(Game.pieces[this.y][l] == null){
                                if(this.y < 10){
                                    possibleMoves.put(m,Double.parseDouble(l+".0"+this.y));
                                }else{
                                    possibleMoves.put(m,Double.parseDouble(l+"."+this.y));
                                }
                                m++;
                            }else if(Game.pieces[this.y][l].whitePiece != this.whitePiece){
                                if(this.y < 10){
                                    possibleMoves.put(m,Double.parseDouble(l+".0"+this.y));
                                }else{
                                    possibleMoves.put(m,Double.parseDouble(l+"."+this.y));
                                }  
                                m++;
                            }else{
                                l = 0;
                            }
                        }catch (Exception e) {
                        }
                    }
                    for(int r = this.x+1; r < Game.boardSize; r++){
                        try {
                            if(Game.pieces[this.y][r].whitePiece != this.whitePiece){
                                if(this.y < 10){
                                    possibleMoves.put(m,Double.parseDouble(r+".0"+this.y));
                                }else{
                                    possibleMoves.put(m,Double.parseDouble(r+"."+this.y));

                                }
                                m++;
                            }
                            r = Game.boardSize;
                        }catch (Exception e) {
                            if(this.y < 10){
                                possibleMoves.put(m,Double.parseDouble(r+".0"+this.y));
                            }else{
                                possibleMoves.put(m,Double.parseDouble(r+"."+this.y));

                            }
                            m++;
                        }
                    }
                    for(int u = this.y-1; u >= 0; u--){
                        try {
                            if(Game.pieces[u][this.x].whitePiece != this.whitePiece){
                                if(u < 10){
                                    possibleMoves.put(m,Double.parseDouble(this.x+".0"+u));
                                }else{
                                    possibleMoves.put(m,Double.parseDouble(this.x+"."+u));

                                }
                                m++;
                            }
                            u = 0;
                        }catch (Exception e) {
                            if(u < 10){
                                possibleMoves.put(m,Double.parseDouble(this.x+".0"+u));
                            }else{
                                possibleMoves.put(m,Double.parseDouble(this.x+"."+u));

                            }
                            m++;
                        }
                    }
                    for(int d = this.y+1; d < Game.boardSize; d++){
                        try {
                            if(Game.pieces[d][this.x].whitePiece != this.whitePiece){
                                if(d < 10){
                                    possibleMoves.put(m,Double.parseDouble(this.x+".0"+d));
                                }else{
                                    possibleMoves.put(m,Double.parseDouble(this.x+"."+d));

                                } 
                                m++;
                            }
                            d = Game.boardSize;
                        }catch (Exception e) {
                            if(d < 10){
                                possibleMoves.put(m,Double.parseDouble(this.x+".0"+d));
                            }else{
                                possibleMoves.put(m,Double.parseDouble(this.x+"."+d));

                            }
                            m++;
                        }
                    }
                    m = 32;
                }
            break;
            case knight:
                for(int m = 0; m < 8; m++){
                }
            break;
        }
        return possibleMoves;
    }*/
    public Boolean getAvailableMoves(){
            switch (pieceType) {
                case rook:
                        Pathing(1,0,this.x,this.y);
                        Pathing(-1,0,this.x,this.y);
                        Pathing(0,1,this.x,this.y);
                        Pathing(0,-1,this.x,this.y);
                    break;
            }
        return(true);
    }
    private void Pathing(int xIncrement, int yIncrement, int xPos, int yPos){
        try {
            if((xPos+xIncrement<Game.boardSize)&&(xPos+xIncrement>=0)&&(yPos+yIncrement<Game.boardSize)&&(yPos+yIncrement>=0)){
                if(Game.pieces[yPos+yIncrement][xPos+xIncrement] == null){
                    Pathing(xIncrement, yIncrement,xPos+xIncrement, yPos+yIncrement);
                    Game.tiles[yPos+yIncrement][xPos+xIncrement].colour = Color.red;
                    Mouse_Input.firstClick = false;
                }else if(Game.pieces[yPos+yIncrement][xPos+xIncrement].whitePiece != this.whitePiece){
                    Game.tiles[yPos+yIncrement][xPos+xIncrement].colour = Color.red;
                    Mouse_Input.firstClick = false;
                }
            }
        } catch (Exception e) {
        }
    }
}