/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.board;

import java.util.Collections;
import java.util.ArrayList;

/**
 *
 *
 */
public class MazeBoard {
    private int size;
    private MazeField[][] board;
    private Player[] players;
    private int currPlayers = 0;
    
    private static int maxPlayers = 3; // default max pocet hracu
    
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
        newboard.players = new Player[MazeBoard.maxPlayers];
        return newboard;
    }
    
    // prida hrace se jmenem
    public void addPlayer(String name) {
        if (this.currPlayers == MazeBoard.maxPlayers) {
            System.out.println("Max players reached!");
        }
        else {
            this.players[this.currPlayers] = new Player(name, this.currPlayers, this.board[0][0]);
            this.currPlayers++;
        }
    }
    
    public String strRepr() {
        // potreba implementace strRepr pro MazeField
        String mbStr = "0000"; // nuly jako zacatek freeCard 
        mbStr += freeCard.getType();
        mbStr += Integer.toString(this.freeCard.getRotation());
        for(int r = 0; r < this.size; r++) {
            for(int c = 0; c < this.size; c++) {
                mbStr += this.board[r][c].strRepr();
            }
        }
        return mbStr;
    }
    
    public void config(String cfgStr) {
        MazeCard newcard; // nova karta podle cfgStr rrccTR
        int r, c, rotation;
        
        r = Integer.parseInt(cfgStr.substring(0,2)) - 1; // -1 protoze r a c jsou indexovany od 1
        c = Integer.parseInt(cfgStr.substring(2,4)) - 1;
        newcard = MazeCard.create( String.valueOf(cfgStr.charAt(4)) );
        rotation = Character.getNumericValue( cfgStr.charAt(5) );
        for (int i = 0; i<rotation; i++) {
            newcard.turnRight(); // provedu rotaci karty
        }
        if (r == -1 && c == -1) this.freeCard = newcard;
        else this.board[r][c].PutCard(newcard);
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
                System.out.print(tmpcard.getType());
                if (c != this.size-1) System.out.print(" ");
            }
            System.out.print("\n");
        }
        System.out.print("Free card: ");
        System.out.println(this.freeCard.getType());
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
        for (int i=0; i<I_count; i++) {
            newcard = MazeCard.create(I_card);
            card_list.add(newcard);
        }
        for (int i=0; i<L_count; i++) {
            newcard = MazeCard.create(L_card);
            card_list.add(newcard);
        }
        for (int i=0; i<T_count; i++) {
            newcard = MazeCard.create(T_card);
            card_list.add(newcard);
        }
        // zamicham seznam karet
        Collections.shuffle(card_list);
        for (r=1; r<=this.size; r++) {
            for (c=1; c<=this.size; c++) {
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
