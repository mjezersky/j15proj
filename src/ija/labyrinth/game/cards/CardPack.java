
package ija.labyrinth.game.cards;
import ija.labyrinth.game.MazeBoard;
import ija.labyrinth.game.MazeField;

import java.util.ArrayList;
import java.util.Collections;

public class CardPack {
    private int size;
    private ArrayList<TreasureCard> pack;
    private MazeBoard board;
    
    @Override
    public String toString() {
        // SS_card_*
        String tmp = Integer.toString(this.size);
        for (int i=0; i<this.pack.size(); i++) {
            tmp+=this.pack.get(i).toString();
        }
        return tmp;
    }
    
    public CardPack(int packSize, MazeBoard board) {
        this.pack = new ArrayList<>();
        this.size = packSize;
        this.board = board;
        
        ArrayList<MazeField> fieldList = new ArrayList<>();
        for (int r=1; r<=this.board.size(); r++) {
            for (int c=1; c<=this.board.size(); c++) {
                fieldList.add(this.board.get(r, c));
            }
        }
        Collections.shuffle(fieldList);
        
        for (int i = 0; i < packSize; i++) {
            TreasureCard newCard = new TreasureCard(i, fieldList.get(i));
            this.pack.add(newCard);
        }
        if (packSize > 0) Collections.shuffle(this.pack);
        fieldList.clear();
    }
        
    public boolean strConfig(String cfg) {
        if (cfg.length()<2) {
            System.err.println("Error - CardPack.strConfig: bad string");
            return false;
        }
        int cfgSize = Integer.valueOf(cfg.substring(0, 2));
        if (cfg.length()!=(cfgSize*6)+2) {
            System.err.println("Error - CardPack.strConfig: incomplete string");
            return false;
        }
        
        this.size = cfgSize;
        int id, row, col;
        TreasureCard newcard;
        this.pack.clear();
        
        for (int i=2; i<cfg.length(); i+=6) {
            id = Integer.valueOf(cfg.substring(i, i+2));
            row = Integer.valueOf(cfg.substring(i+2, i+4));
            col = Integer.valueOf(cfg.substring(i+4, i+6));
            newcard = new TreasureCard(id, this.board.get(row,col));
            this.pack.add(newcard);
        }
        
        return true;
    }
    
    public int getSize() { return this.size; }
    
    public TreasureCard takeCard() {
        
        if (this.size <= 0) {
            System.err.println("Error - CardPack.pop(): pack is empty");
            return null;
        }
        this.size--;
        TreasureCard card = this.pack.get(0);
        this.pack.remove(0);
        return card;
    }

}
