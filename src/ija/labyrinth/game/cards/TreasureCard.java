
package ija.labyrinth.game.cards;
import ija.labyrinth.game.MazeField;

public class TreasureCard {
    private int identifier;
    private MazeField location;
    
    @Override
    public String toString() { 
        // IIrrcc
        String tmp = "";
        if (this.identifier<10) tmp += "0";
        tmp += Integer.toString(this.identifier);
        if (this.location.row()<10) tmp += "0";
        tmp += Integer.toString(this.location.row());
        if (this.location.col()<10) tmp += "0";
        tmp += Integer.toString(this.location.col());
        return tmp;
    }
    
    public TreasureCard(int id, MazeField field) {
        this.identifier = id;
        this.location = field;
    }
    
    public boolean equals(TreasureCard tc) {
        return this.location.equals(tc.location);
    }
    
    public int getId() { return this.identifier; }
    public MazeField getLocation() { return this.location; }
}
