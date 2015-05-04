
package ija.labyrinth.game.cards;


import java.util.ArrayList;
import java.util.Collections;

public class CardPack {
    private int size;
    private ArrayList<TreasureCard> pack;
    
    public CardPack(int packSize) {
        if (packSize != 12 && packSize != 24) {
            System.err.println("Error - CardPack: invalid size (not 12 or 24)");
            return;
        }
        this.pack = new ArrayList<>();
        this.size = packSize;
        for (int i = 0; i < packSize; i++) {
            TreasureCard newCard = new TreasureCard(i, null);
            this.pack.add(newCard);
        }
        Collections.shuffle(this.pack);
    }
    
    public int getSize() { return this.size; }
    
    public TreasureCard takeCard() {
        
        if (this.size <= 0) {
            System.err.println("Error - CardPack.pop(): pack is empty");
            return null;
        }
        TreasureCard card = this.pack.get(0);
        this.pack.remove(0);
        return card;
    }

}
