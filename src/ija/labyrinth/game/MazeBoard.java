package ija.labyrinth.game;

import ija.labyrinth.game.cards.*;

import java.util.Collections;
import java.util.ArrayList;

/**
 *
 * @author Matouš Jezerský - xjezer01
 */

public class MazeBoard {
    private int size;
    private MazeField[][] board;
    private Player[] players;
    private int currPlayers = 0;
    private int turn;
    private CardPack pack;

    private static final int maxPlayers = 4; // default max pocet hracu

    private MazeCard freeCard = null;

    /**
     * Metoda vytvářející novou herní desku MazeBoard
     * @param n velikost strany čtvercové desky
     * @return nová deska
     */
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

    /**
     * Getter - aktuální tah (hráč na tahu)
     * @return číslo hráče, který je na tahu
     */
    public int getTurn() { return this.turn; }
    
    /**
     * Getter - aktuální balíček karet
     * @return balíček karet aktuální hry
     */
    public CardPack getPack() { return this.pack; }
    
    /**
     * Vytvoří a zinicializuje nový balicek karet. Používá se až po newGame.
     * @param packSize velikost balíčku
     */
    public void createPack(int packSize) { this.pack = new CardPack(packSize, this); }
    
    /**
     * Getter - velikost strany čtvercové herní desky
     * @return velikost herní desky
     */
    public int getSize() { return this.size; }
    
    /**
     * Getter - aktuální počet hráčů
     * @return aktuální počet hráčů
     */
    public int getPlayerCount() { return this.currPlayers; }


    /**
     * Nastaví tah na dalšího hráče
     * @return nový hráč na tahu
     */
    public Player nextTurn() {
        this.turn = (this.turn+1)%currPlayers;
        return this.players[this.turn];
    }

    /**
     * Vytvoří nového hráče a přídá ho do seznamu hráčů
     * @param name jméno hráče
     * @return nově vytvořený hráč
     */
    public Player addPlayer(String name) {
        Player newPlayer = null;
        if (name == null) {
            System.out.println("Error - MazeBoard.addPlayer: player name is null");
        }
        else if (this.currPlayers == MazeBoard.maxPlayers) {
            System.out.println("Error - MazeBoard.addPlayer: max players reached!");
        }
        else {
            newPlayer = new Player(name, this.currPlayers, this.board[0][0], this);
            this.players[this.currPlayers] = newPlayer;
            this.currPlayers++;
        }
        return newPlayer;
    }

    /**
     * Getter - hráč podle indexu v rozsahu 0 až počet_hráčů.
     * @param playerNo index hráče v seznamu hráču (indexováno od 0)
     * @return hráč na indexu playerNo
     */
    public Player getPlayer(int playerNo) {
        if (playerNo < 0 || playerNo > currPlayers-1) {
            System.out.println("Error - MazeBoard.getPlayerRow: player number not in range");
            return null;
        }
        return this.players[playerNo];
    }

    @Override
    public String toString() {
        String mbStr = "";
        if (this.turn >= 0 && this.turn<10) mbStr += "0";
        mbStr += Integer.toString(this.turn);
        if (this.size<10) mbStr += "0";
        mbStr += Integer.toString(this.size);
        mbStr += "0000"; // nuly jako zacatek freeCard 
        mbStr += freeCard.getType();
        mbStr += Integer.toString(this.freeCard.getRotation());
        for(int r = 0; r < this.size; r++) {
            for(int c = 0; c < this.size; c++) {
                mbStr += this.board[r][c].toString();
            }
        }
        mbStr += "#";
        mbStr += this.pack.toString();
        for (int i=0; i<this.currPlayers; i++) {
            mbStr += "#";
            mbStr += this.players[i].toString();
        }
        return mbStr;
    }

    /**
     * Metoda pro konfiguraci herní desky podle konfiguračního řetězce - nastavuje jedno pole MazeField
     * @param cfg konfigurační řetězec pro 1 MazeField
     * @return úspěšná konfigurace - true / neúspěšná - false
     */
    public boolean strConfigBoard(String cfg) {
        if (cfg.length()!=6) {
            System.out.println("Error - MazeBoard.strConfigBoard: bad string");
            return false;
        }
        MazeCard newcard; // nova karta podle cfgStr rrccTR
        int r, c, rotation;

        r = Integer.parseInt(cfg.substring(0,2)) - 1; // -1 protoze r a c jsou indexovany od 1
        c = Integer.parseInt(cfg.substring(2,4)) - 1;
        newcard = MazeCard.create(String.valueOf(cfg.charAt(4)));
        rotation = Character.getNumericValue(cfg.charAt(5));
        for (int i = 0; i<rotation; i++) {
            newcard.turnRight(); // provedu rotaci karty
        }
        if (r == -1 && c == -1) this.freeCard = newcard;
        else this.board[r][c].PutCard(newcard);
        return true;
    }

    /**
     * Metoda pro konfiguraci herní desky podle konfiguračního řetězce - nastavuje balíček karet
     * @param cfg konfigurační řetězec pro balíček karet CardPack
     * @return úspěšná konfigurace - true / neúspěšná - false
     */
    public boolean strConfigPack(String cfg) {
        this.pack = new CardPack(0, this);
        return this.pack.strConfig(cfg);
    }

    /**
     * Metoda pro konfiguraci herní desky podle konfiguračního řetězce - nastavuje hráče
     * @param cfg konfigurační řetězec pro 1 hráče Player
     * @return úspěšná konfigurace - true / neúspěšná - false
     */
    public boolean strConfigPlayer(String cfg) {
        TreasureCard card;
        int num, row, col, nameLen, score;
        String name, cardStr;

        cardStr = cfg.substring(8, 14);
        num = Integer.valueOf(cardStr.substring(0, 2));
        row = Integer.valueOf(cardStr.substring(2, 4));
        col = Integer.valueOf(cardStr.substring(4, 6));
        card = new TreasureCard(num, this.get(row,col));

        num = Integer.valueOf(cfg.substring(0, 2));
        row = Integer.valueOf(cfg.substring(2, 4));
        col = Integer.valueOf(cfg.substring(4, 6));
        score = Integer.valueOf(cfg.substring(6, 8));
        nameLen = Integer.valueOf(cfg.substring(14, 16));
        name = cfg.substring(16, 16+nameLen);

        this.players[currPlayers] = new Player(name, num, this.get(row, col), this, card, score);
        this.currPlayers++;

        return true;
    }

    /**
     * Vypíše informace o herní desce
     */
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
        System.out.print(" Turn: ");
        System.out.print(this.getTurn()); 
        System.out.print("\n");
        System.out.println("Players:");
        for (int i=0; i<this.currPlayers; i++) {
            System.out.print("#");
            System.out.print(this.players[i].getNum());
            System.out.print(" ");
            System.out.print(this.players[i].getName());
            System.out.print(" ");
            System.out.print(this.players[i].getRow()); System.out.print(":"); System.out.print(this.players[i].getCol());
            System.out.print(" score: "); System.out.print(this.players[i].getScore());
            System.out.print(" C:");
            System.out.print(this.players[i].getCard().toString());
            System.out.print("\n");
        }
        if (this.pack != null) {
            System.out.print("Cards: ");
            System.out.print(this.pack.toString());
            System.out.print("\n");
        }
        else System.out.println("Cards: NULL");
    }

    /**
     * Vytvoří novou hru, rozmístí políčka na herní desku
     */
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

        int k;
        boolean goOn = false;
        newcard = card_list.get(0);
        for (int i=1; i <= this.size; i++){
            for (int j=1; j <= this.size; j++){
                for (k=0; k < card_list.size(); k++){
                    if (card_list.get(k).getType().equals(T_card)){
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

    /**
     * Rotuje kartu freeCard jednou doprava
     */
    public void rotateFreeCard() {
        MazeCard tmpCard = getFreeCard();
        tmpCard.turnRight();

        this.freeCard = tmpCard;
    }

    /**
     * Getter - políčko MazeField na zadaných souřadnicích (indexováno od 1)
     * @param r řádek
     * @param c sloupec
     * @return políčko MazeField na souřadnicích r c
     */
    public MazeField get(int r, int c) {
        if (r<1 || c<1 || r>this.size || c>this.size) return null;
        return this.board[r-1][c-1];
    }

    /**
     * Getter - volná karta
     * @return volná karta MazeCard
     */
    public MazeCard getFreeCard() { return this.freeCard; }

    /**
     * Metoda pro aktualizaci herní desky po posunu políček. Aktualizuje pozice hráčů a pokladu podle umístění vloženáho kamene
     * @param row řádek vloženého kamene
     * @param col sloupec vloženého kamene
     * @param offset posun - záporny doleva/nahoru , kladný doprava/dolů
     * @param modRow přepinac modifikace řádku: true - posouvá se sloupec nahoru/dolů, false - posouvá se řádek doleva/doprava
     */
    private void shiftUpdate(int row, int col, int offset, boolean modRow) {
        int pRow, pCol, cRow, cCol;

        for (int i=0; i<this.currPlayers; i++) {
            pRow = this.players[i].getRow();
            pCol = this.players[i].getCol();
            cRow = this.players[i].getCard().getLocation().row();
            cCol = this.players[i].getCard().getLocation().col();

            if (modRow && pCol == col) {
                pRow += offset;
                if (pRow < 1) pRow = this.size;
                else if (pRow > this.size) pRow = 1;
                this.players[i].moveTo(pRow, col);
            }
            else if (!modRow && pRow == row) {
                pCol += offset;
                if (pCol < 1) pCol = this.size;
                else if (pCol > this.size) pCol = 1;
                this.players[i].moveTo(row, pCol);
            }

            if (modRow && cCol == col) {
                cRow += offset;
                if (cRow < 1) cRow = this.size;
                else if (cRow > this.size) cRow = 1;
                this.players[i].getCard().moveTo(this.get(cRow, col));
            }
            else if (!modRow && cRow == row) {
                cCol += offset;
                if (cCol < 1) cCol = this.size;
                else if (cCol > this.size) cCol = 1;
                this.players[i].getCard().moveTo(this.get(row, cCol));
            }

        }
        for (int i=0; i<this.getPack().getSize()-1; i++) {
            cRow = this.getPack().getCard(i).getLocation().row();
            cCol = this.getPack().getCard(i).getLocation().col();
            
            if (modRow && cCol == col) {
                cRow += offset;
                if (cRow < 1) cRow = this.size;
                else if (cRow > this.size) cRow = 1;
                this.getPack().getCard(i).moveTo(this.get(cRow, col));
            }
            else if (!modRow && cRow == row) {
                cCol += offset;
                if (cCol < 1) cCol = this.size;
                else if (cCol > this.size) cCol = 1;
                this.getPack().getCard(i).moveTo(this.get(row, cCol));
            }
            
        }
        
    }

    /**
     * Posun kamenů
     * @param mf místo vložení nového kamene
     */
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
            this.shiftUpdate(mf.row(), mf.col(), 1, true);
        }
        else if (mf.row()==this.size && mf.col()%2==0) {
            tmpcard = this.board[0][mf.col()-1].getCard();
            for (int i=1; i<this.size; i++) {
                this.board[i-1][mf.col()-1].PutCard(this.board[i][mf.col()-1].getCard());
            }
            this.board[this.size-1][mf.col()-1].PutCard(this.freeCard);
            this.freeCard = tmpcard;
            this.shiftUpdate(mf.row(), mf.col(), -1, true);
        }
        else if (mf.row()%2==0 && mf.col()==1) {
            tmpcard = this.board[mf.row()-1][this.size-1].getCard();
            for (int i=this.size-1; i>0; i--) {
                this.board[mf.row()-1][i].PutCard(this.board[mf.row()-1][i-1].getCard());
            }
            this.board[mf.row()-1][0].PutCard(this.freeCard);
            this.freeCard = tmpcard;
            this.shiftUpdate(mf.row(), mf.col(), 1, false);
        }
        else if (mf.row()%2==0 && mf.col()==this.size) {
            tmpcard = this.board[mf.row()-1][0].getCard();
            for (int i=1; i<this.size; i++) {
                this.board[mf.row()-1][i-1].PutCard(this.board[mf.row()-1][i].getCard());
            }
            this.board[mf.row()-1][this.size-1].PutCard(this.freeCard);
            this.freeCard = tmpcard;
            this.shiftUpdate(mf.row(), mf.col(), -1, false);
        }
    }
}
