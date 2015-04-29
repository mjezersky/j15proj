/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.labyrinth.game;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 *
 */
public class MazeBoard {
    private int size;
    private MazeField[][] board;
    
    private MazeCard freeCard = null;
    
    public static MazeBoard createMazeBoard(int n) {
        MazeBoard newboard = new MazeBoard();
        newboard.size = n;
        newboard.board = new MazeField[n][n];
        for(int r = 0; r < n; r++) {
            for(int c = 0; c < n; c++) {
                newboard.board[r][c] = new MazeField(r+1, c+1);
            }
        }
        return newboard;
    }
    
    public void print() {
        MazeCard tmpcard;
        for(int r = 0; r < this.size; r++) {
            for(int c = 0; c < this.size; c++) {
                tmpcard = this.board[r][c].getCard();
                if (tmpcard == null) {
                    System.out.println("EMPTY");
                    return;
                }
                if (Objects.equals(tmpcard.getType(), "C")) System.out.print("L");
                if (Objects.equals(tmpcard.getType(), "L")) System.out.print("I");
                if (Objects.equals(tmpcard.getType(), "F")) System.out.print("T");

                if (c != this.size-1) System.out.print(" ");
            }
            System.out.print("\n");
        }
        System.out.print("Free card: ");
        if (Objects.equals(this.freeCard.getType(), "C")) System.out.println("L");
        if (Objects.equals(this.freeCard.getType(), "L")) System.out.println("I");
        if (Objects.equals(this.freeCard.getType(), "F")) System.out.println("T");

    }
    
    public void newGame() {
        int I_count, L_count, T_count, card_count, r, c;
        String I_card = "L";
        String L_card = "C";
        String T_card = "F";
        MazeCard newcard;
        
        card_count = (this.size*this.size)+1;
        I_count = L_count = T_count = card_count/3; // +1 kvuli volne karte
        if (card_count%3 == 1) I_count += 1;
        if (card_count%3 == 2) {
            I_count += 1;
            L_count += 1;
        }

        // vlozeni nahodnych karet na zbyvajici policka
        // naplnim seznam novych karet
        ArrayList<MazeCard> card_list = new ArrayList<>();

        for (int i=0; i<L_count; i++) {
            newcard = MazeCard.create(L_card);
            card_list.add(newcard);
        }
        for (int i=0; i<I_count; i++) {
            newcard = MazeCard.create(I_card);
            card_list.add(newcard);
        }
        for (int i=0; i<T_count; i++) {
            newcard = MazeCard.create(T_card);
            card_list.add(newcard);
        }

        // Vlozime L do vsetkych rohov a nastavime aj smer
        MazeCard tmp = card_list.get(0);
        tmp.turnRight();
        tmp.turnRight();
        this.board[0][0].PutCard(tmp);
        card_list.remove(0);

        tmp = card_list.get(0);
        tmp.turnRight();
        tmp.turnRight();
        tmp.turnRight();
        this.board[this.size-1][0].PutCard(tmp);
        card_list.remove(0);

        this.board[0][this.size-1].PutCard(card_list.get(0));
        card_list.remove(0);

        tmp = card_list.get(0);
        tmp.turnRight();
        this.board[this.size-1][this.size-1].PutCard(tmp);
        card_list.remove(0);

        // zamicham seznam karet
        Collections.shuffle(card_list);
        for (r=1; r<=this.size; r++) {
            for (c=1; c<=this.size; c++) {

                // Preskocime rohy, tam je uz vlozene L
                if(r == 1 && c == 1) continue;
                if(r == 1 && c == this.size) continue;
                if(r == this.size && c == 1) continue;
                if(r == this.size && c == this.size) continue;

                newcard = card_list.get(0);
                card_list.remove(0);
                this.board[r-1][c-1].PutCard(newcard);
            }
        }

        newcard = card_list.get(0);
        card_list.remove(0);
        this.freeCard = newcard;
    }

    public MazeField get(int r, int c) {
        if (r<1 || c<1 || r>this.size || c>this.size) return null;
        return this.board[r-1][c-1];
    }
            
    public MazeCard getFreeCard() { return this.freeCard; }

    public void shift(MazeField mf) {
        if (mf == null) return;
        MazeCard tmpcard;
        if (mf.row()==1 && mf.col()%2==0) {
            tmpcard = this.board[this.size-1][mf.col()-1].getCard();
            for (int i=this.size-1; i>0; i--) {
                this.board[i][mf.col()-1].PutCard(this.board[i-1][mf.col()-1].getCard());
            }
            this.board[0][mf.col()-1].PutCard(this.freeCard);
            this.freeCard = tmpcard;
        }
        else if (mf.row()==this.size && mf.col()%2==0) {
            tmpcard = this.board[0][mf.col()-1].getCard();
            for (int i=1; i<this.size; i++) {
                this.board[i-1][mf.col()-1].PutCard(this.board[i][mf.col()-1].getCard());
            }
            this.board[this.size-1][mf.col()-1].PutCard(this.freeCard);
            this.freeCard = tmpcard;
        }
        else if (mf.row()%2==0 && mf.col()==1) {
            tmpcard = this.board[mf.row()-1][this.size-1].getCard();
            for (int i=this.size-1; i>0; i--) {
                this.board[mf.row()-1][i].PutCard(this.board[mf.row()-1][i-1].getCard());
            }
            this.board[mf.row()-1][0].PutCard(this.freeCard);
            this.freeCard = tmpcard;
        }
        else if (mf.row()%2==0 && mf.col()==this.size) {
            tmpcard = this.board[mf.row()-1][0].getCard();
            for (int i=1; i<this.size; i++) {
                this.board[mf.row()-1][i-1].PutCard(this.board[mf.row()-1][i].getCard());
            }
            this.board[mf.row()-1][this.size-1].PutCard(this.freeCard);
            this.freeCard = tmpcard;
        }
    }
}
