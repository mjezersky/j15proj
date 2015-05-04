/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.labyrinth.game;

/**
 *
 *
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
        // potreba doplnit int rotation a metodu getRotation do MazeCard
        // rotace se taky musi byt (rot+1)%4 pri otoceni doprava
        String mfStr = ""; // rrccTR | row col Type Rotation
        if (this.row<10) mfStr += "0";
        mfStr += Integer.toString(this.row);
        if (this.col<10) mfStr += "0";
        mfStr += Integer.toString(this.col);
        mfStr += this.card.getType();
        mfStr += Integer.toString(this.card.getRotation());
        return mfStr;
    }
    public int row() { return this.row; }
    public int col() { return this.col; }
    public MazeCard getCard() { return this.card; }
    public void PutCard(MazeCard c) { this.card = c; }

}
