
package ija.labyrinth.game.cards;
import ija.labyrinth.game.MazeField;


public class TreasureCard {
    private int identifier;
    private MazeField location;
    
    TreasureCard(int id, MazeField field) {
        this.identifier = id;
        this.location = field;
    }
    
    public boolean equals(TreasureCard tc) {
        return this.location.equals(tc.location);
    }
    
    public MazeField getLocation() { return this.location; }
}
