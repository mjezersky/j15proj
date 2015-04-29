/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.board;

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
   
    public int row() { return this.row; }
    public int col() { return this.col; }
    public MazeCard getCard() { return this.card; }
    public void PutCard(MazeCard c) { this.card = c; }

}
