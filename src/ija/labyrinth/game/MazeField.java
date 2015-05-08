package ija.labyrinth.game;

/**
 * IJA 2015 - Projekt Labyrinth
 * Autori:  Maroš Janota
 *          Matouš Jezerský
 */
public class MazeField {
    private int row;
    private int col;
    private MazeCard card;

    MazeField(int row, int col) {
        this.row = row;
        this.col = col;
        this.card = null;
    }

    public boolean equals(MazeField field) {
        return (this.row == field.row() && this.col == field.col() && this.card.equals(field.getCard()));
    }

    @Override
    public String toString() {
        String mfStr = ""; // rrccTR | row col Type Rotation
        if (this.row<10) mfStr += "0";
        mfStr += Integer.toString(this.row);
        if (this.col<10) mfStr += "0";
        mfStr += Integer.toString(this.col);
        mfStr += this.card.getType();
        mfStr += Integer.toString(this.card.getRotation());
        return mfStr;
    }

    /**
     * Getter - řádek, na kterém se pole nachází (indexováno od 1)
     * @return řádek, na kterém se pole nachází
     */
    public int row() { return this.row; }

    /**
     * Getter - sloupec, na kterém se pole nachází (indexováno od 1)
     * @return sloupec, na kterém se pole nachází
     */
    public int col() { return this.col; }

    /**
     * Vrací kámen na poli
     * @return kámen na poli
     */
    public MazeCard getCard() { return this.card; }

    /**
     * Nastaví kámen na poli.
     * @param c kámen
     */
    public void PutCard(MazeCard c) { this.card = c; }

}
