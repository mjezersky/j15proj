package ija.board;

import ija.board.*;

public class Player {
    private int number;
    private MazeField location;
    private String name;
    
    Player(String name, int number, MazeField location) {
        this.number = number;
        this.location = location;
        this.name = name;
    }
        
    public boolean canMoveTo(MazeField destination) {
        // TODO: kod hledani cesty k destination
        return true;
    }
}
