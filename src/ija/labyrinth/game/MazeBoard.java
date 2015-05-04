/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ija.labyrinth.game;

import ija.labyrinth.game.cards.CardPack;
import ija.labyrinth.game.MazeCard;
import ija.labyrinth.game.MazeField;

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
    private int turn;
    private CardPack pack;

    private static int maxPlayers = 4; // default max pocet hracu

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
    
    public void createPack(int packSize) { this.pack = new CardPack(packSize); }
    public CardPack getPack() { return this.pack; }

    // vraci hrace na tahu
    public Player nextTurn() {
        this.turn = (this.turn+1)%currPlayers;
        return this.players[this.turn];
    }

    // prida hrace se jmenem
    public void addPlayer(String name) {
        if (this.currPlayers == MazeBoard.maxPlayers) {
            System.out.println("Error - MazeBoard.addPlayer: max players reached!");
        }
        else {
            this.players[this.currPlayers] = new Player(name, this.currPlayers, this.board[0][0], this);
            this.currPlayers++;
        }
    }

    public Player getPlayer(int playerNo) {
        if (playerNo < 0 || playerNo > currPlayers-1) {
            System.out.println("Error - MazeBoard.getPlayerRow: player number not in range");
            return null;
        }
        return this.players[playerNo];
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
                System.out.print(tmpcard.getRotation());
                if (c != this.size-1) System.out.print(" ");
            }
            System.out.print("\n");
        }
        System.out.print("Free card: ");
        System.out.print(this.freeCard.getType());
        System.out.print(this.freeCard.getRotation());
        System.out.print("\n");
    }

    public void newGame() {
        turn = -1;

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
        this.board[this.size-1][0].PutCard(tmp);
        card_list.remove(0);

        tmp = card_list.get(0);
        tmp.turnRight();
        tmp.turnRight();
        tmp.turnRight();
        this.board[0][this.size-1].PutCard(tmp);
        card_list.remove(0);

        tmp = card_list.get(0);
        this.board[this.size-1][this.size-1].PutCard(tmp);
        card_list.remove(0);

        int n=0, k=0;
        boolean goOn = false;
        newcard = card_list.get(0);
        for (int i=1; i <= this.size; i++){
            for (int j=1; j <= this.size; j++){
                for (k=0; k < card_list.size(); k++){
                    if (card_list.get(k).getType() == T_card){
                        newcard = card_list.get(k);
                        goOn = true;
                        break;
                    }
                }
                if (goOn){
                    if (i == 1 && j%2 == 1){
                        tmp = get(i,j).getCard();
                        if(tmp != null) { continue; }
                        newcard.turnRight();
                        newcard.turnRight();
                        this.board[i-1][j-1].PutCard(newcard);
                        card_list.remove(k);
                    }
                    if (i == this.size && j%2 == 1){
                        tmp = get(i,j).getCard();
                        if(tmp != null) { continue; }
                        this.board[i-1][j-1].PutCard(newcard);
                        card_list.remove(k);
                    }
                    if (i%2 == 1 && j == 1){
                        tmp = get(i,j).getCard();
                        if(tmp != null) { continue; }
                        newcard.turnRight();
                        this.board[i-1][j-1].PutCard(newcard);
                        card_list.remove(k);
                    }
                    if (i%2 == 1 && j == this.size){
                        tmp = get(i,j).getCard();
                        if(tmp != null) { continue; }
                        newcard.turnRight();
                        newcard.turnRight();
                        newcard.turnRight();
                        this.board[i-1][j-1].PutCard(newcard);
                        card_list.remove(k);
                    }
                }
            }
        }

        // zamicham seznam karet
        Collections.shuffle(card_list);
        for (r=1; r<=this.size; r++) {
            for (c=1; c<=this.size; c++) {

                // Preskocime policke kde uz nieco je
                tmp = get(r,c).getCard();
                if(tmp != null) { continue; }

                // Ziskame kartu
                newcard = card_list.get(0);
                // Otocime ju nahodone  (1 - 4x)
                int randNum = 1 + (int)(Math.random() * ((5 - 1) + 1));
                for (int i = 0; i <= randNum; i++){
                    newcard.turnRight();
                }

                card_list.remove(0);
                this.board[r-1][c-1].PutCard(newcard);
            }
        }
        newcard = card_list.get(0);
        card_list.remove(0);
        // Otocime nahodne aj volnu kartu
        int randNum = 1 + (int)(Math.random() * ((5 - 1) + 1));
        for (int i = 0; i <= randNum; i++){
            newcard.turnRight();
        }
        this.freeCard = newcard;

    }

    public void rotateFreeCard() {
        MazeCard tmpCard = getFreeCard();
        tmpCard.turnRight();

        this.freeCard = tmpCard;
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
