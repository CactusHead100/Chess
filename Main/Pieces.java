package Main;

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

    public Dictionary getAvailableMoves(){
        Dictionary<Integer, Double> possibleMoves = new Hashtable<>();
        if(this.pieceType == piecEnum.rook){
            for(int m = 0; m < Game.boardSize*2;){
                for(int l = this.x-1; l >= 0; l--){
                    try {
                        if(Game.pieces[this.y][l].whitePiece == this.whitePiece){
                            if(this.y < 10){
                                possibleMoves.put(m,Double.parseDouble(l+".0"+this.y));
                            }else{
                                possibleMoves.put(m,Double.parseDouble(l+"."+this.y));
                            }  
                        }
                        l = 0;
                    }catch (Exception e) {
                        if(this.y < 10){
                            possibleMoves.put(m,Double.parseDouble(l+".0"+this.y));
                        }else{
                            possibleMoves.put(m,Double.parseDouble(l+"."+this.y));
                        }
                        m++;
                    }
                }
                for(int r = this.x+1; r < Game.boardSize; r++){
                    try {
                        if(Game.pieces[this.y][r].whitePiece == this.whitePiece){
                            if(this.y < 10){
                                possibleMoves.put(m,Double.parseDouble(r+".0"+this.y));
                            }else{
                                possibleMoves.put(m,Double.parseDouble(r+"."+this.y));
                                
                            }
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
                        if(Game.pieces[u][this.x].whitePiece == this.whitePiece){
                            if(u < 10){
                                possibleMoves.put(m,Double.parseDouble(this.x+".0"+u));
                            }else{
                                possibleMoves.put(m,Double.parseDouble(this.x+"."+u));
                                
                            }
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
                        if(Game.pieces[d][this.x].whitePiece == this.whitePiece){
                            if(d < 10){
                                possibleMoves.put(m,Double.parseDouble(this.x+".0"+d));
                            }else{
                                possibleMoves.put(m,Double.parseDouble(this.x+"."+d));
                                
                            } 
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
        }
        return possibleMoves;
    }
}
