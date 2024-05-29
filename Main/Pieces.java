package Main;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Dictionary;
import java.util.Hashtable;


public class Pieces {
   // int pieceType;
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

    public Dictionary getAvailableMoves(){
        Dictionary<Integer, Double> possibleMoves = new Hashtable<>();
        if(this.pieceType == piecEnum.rook){
            for(int m = 0; m < Game.boardSize*2;){
                for(int l = this.x-1; l >= 0; l--){
                    try {
                        System.out.println(Game.pieces[this.y][l].pieceType);
                        l = 0;
                    }catch (Exception e) {
                        if(y < 10){
                            possibleMoves.put(m,Double.parseDouble(l+".0"+this.y));
                        }else{
                            possibleMoves.put(m,Double.parseDouble(l+"."+this.y));
                        }
                        m++;
                    }
                }
                for(int u = this.y-1; u >= 0; u--){
                    try {
                        System.out.println(Game.pieces[u][this.x].pieceType);
                        u = 0;
                    }catch (Exception e) {
                        if(u < 10){
                            possibleMoves.put(m,Double.parseDouble(this.x+".0"+u));
                            System.out.println(this.x+".0"+u);
                        }else{
                            possibleMoves.put(m,Double.parseDouble(this.x+"."+u));
                            
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
