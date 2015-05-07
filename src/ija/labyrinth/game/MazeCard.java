package ija.labyrinth.game;

/**
 * IJA 2015 - Projekt Labyrinth
 * Autori:  Maroš Janota
 *          Matouš Jezerský
 */
public class MazeCard {

    public static enum CANGO { LEFT, UP, RIGHT, DOWN; }

    private String cardtype;
    private MazeCard.CANGO[] directions;
    private int rotation; // doprava: 0 - 0 | 1 - 90 | 2 - 180 | 3 - 270

    public static MazeCard create(String type) {
        MazeCard newcard = new MazeCard();
        newcard.cardtype = type;
        newcard.rotation = 0;
        switch (type) {
            case "C":
                newcard.directions = new MazeCard.CANGO[2];
                newcard.directions[0] = MazeCard.CANGO.LEFT;
                newcard.directions[1] = MazeCard.CANGO.UP;
                break;
            case "L":
                newcard.directions = new MazeCard.CANGO[2];
                newcard.directions[0] = MazeCard.CANGO.LEFT;
                newcard.directions[1] = MazeCard.CANGO.RIGHT;
                break;
            case "F":
                newcard.directions = new MazeCard.CANGO[3];
                newcard.directions[0] = MazeCard.CANGO.LEFT;
                newcard.directions[1] = MazeCard.CANGO.RIGHT;
                newcard.directions[2] = MazeCard.CANGO.UP;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return newcard;
    }

    public boolean equals(MazeCard card) {
        return (this.cardtype.equals(card.getType()) && this.rotation == card.getRotation());
    }

    /**
     * Getter - typ karty
     * @return typ karty
     */
    public String getType() {
        return this.cardtype;
    }

    /**
     * Getter - rotace karty
     * @return rotace karty
     */
    public int getRotation() {
        return this.rotation;
    }

    /**
     * Otočí kartu doprava
     */
    public void turnRight() {
        this.rotation = (this.rotation+1)%4;
        for(int i = 0; i < this.directions.length; i++) {
            switch (this.directions[i]) {
                case LEFT:
                    this.directions[i] = MazeCard.CANGO.UP;
                    break;
                case RIGHT:
                    this.directions[i] = MazeCard.CANGO.DOWN;
                    break;
                case UP:
                    this.directions[i] = MazeCard.CANGO.RIGHT;
                    break;
                case DOWN:
                    this.directions[i] = MazeCard.CANGO.LEFT;
                    break;
            }
        }

    }

    /**
     * Zjistí, zda je možné přejít z karty zadaným směrem.
     * @param dir směr
     * @return je-li možné přejít zadaným směrem
     */
    public boolean canGo(MazeCard.CANGO dir) {
        for (MazeCard.CANGO available_dir : this.directions) {
            if (dir.equals(available_dir)) return true;
        }
        return false;
    }
}