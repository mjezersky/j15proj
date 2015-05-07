package ija.labyrinth.game;

import ija.labyrinth.game.cards.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * IJA 2015 - Projekt Labyrinth
 * Autori:  Maroš Janota
 *          Matouš Jezerský
 */
public class Player {

    private int number;
    private int score;
    private MazeField location;
    private MazeBoard board;
    private String name;
    private TreasureCard currCard;
    private boolean takeCard = false;

    private BufferedImage charIcon;
    private BufferedImage[] character;

    Player(String name, int number, MazeField location, MazeBoard board) {
        this.number = number;
        this.location = location;
        this.name = name;
        this.board = board;
        this.currCard = null;
        this.score = 0;
        this.pullCard();

        setImagesIcon();
    }

    Player(String name, int number, MazeField location, MazeBoard board, TreasureCard initCard, int initScore) {
        this.number = number;
        this.location = location;
        this.name = name;
        this.board = board;
        this.currCard = initCard;
        this.score = initScore;

        setImagesIcon();
    }

    @Override
    public String toString() {
        // NNrrccSS_card_LL_name_
        String tmp = "";
        if (this.number < 10) tmp += "0";
        tmp += Integer.toString(this.number); // NN

        int row, col;
        row = this.location.row();
        if (row < 10) tmp += "0";
        tmp += Integer.toString(row); // rr
        col = this.location.col();
        if (col < 10) tmp += "0";
        tmp += Integer.toString(col); // cc

        if (this.score < 10) tmp += "0";
        tmp += Integer.toString(this.score); // SS

        tmp += this.currCard.toString(); // _card_

        int nameLen = this.name.length();
        if (nameLen < 10) tmp += "0";
        tmp += Integer.toString(nameLen); // LL

        tmp += this.name; // _name_

        return tmp;
    }


    public int getScore() { return this.score; }
    public TreasureCard getCard() { return this.currCard; }
    public String getName() { return this.name; }
    public int getNum() { return this.number; }
    public BufferedImage getIcon(){ return this.charIcon; }
    public boolean isTakeCard() { return this.takeCard; }
    public void setTakeCard() { this.takeCard = false; }

    private void pullCard() {
        if (this.board.getPack() == null) {
            System.out.println("Error - Player.pullCard: board.pack is null");
            return;
        }
        this.currCard = this.board.getPack().takeCard();
    }

    public int getRow() {
        if (this.location == null) {
            System.out.println("Error - Player.getRow: location is null");
            return -1;
        }
        return this.location.row();
    }


    public int getCol() {
        if (this.location == null) {
            System.out.println("Error - Player.getCol: location is null");
            return -1;
        }
        return this.location.col();
    }

    public void moveTo(int row, int col) {
        if (this.board == null) {
            System.out.println("Error - Player.place: board is null");
            return;
        }
        this.location = this.board.get(row, col);
        if (this.currCard != null) {
            if (this.location.equals(this.currCard.getLocation())) {
                this.score += 1;
                this.takeCard = true;
                this.pullCard();
            }
        }

    }

    private boolean isPathValid(MazeField target) {
        if (this.location.row() == target.row() && this.location.col() == target.col()) return true;

        MazeField tmpField, currField;
        ArrayList<MazeField> openList = new ArrayList<>();
        ArrayList<MazeField> closedList = new ArrayList<>();

        openList.add(this.location);
        while (!openList.isEmpty()) {
            currField = openList.get(0);
            openList.remove(0);
            if (currField.equals(target)) return true;
            if (!closedList.contains(currField)) {
                /* c ... currField
                X 1 X
                2 c 3
                X 4 X
                */
                // nejdrive zjistim jestli muzu na tu kartu a pak jestli vede cesta z te karty na aktualni (cesty jsou spojene)
                tmpField = this.board.get(currField.row()-1, currField.col()); // 1
                if (tmpField != null && !closedList.contains(tmpField) && currField.getCard().canGo(MazeCard.CANGO.UP)) {
                    if (tmpField.getCard().canGo(MazeCard.CANGO.DOWN)) openList.add(tmpField);
                }
                tmpField = this.board.get(currField.row(), currField.col()-1); // 2
                if (tmpField != null && !closedList.contains(tmpField) && currField.getCard().canGo(MazeCard.CANGO.LEFT)) {
                    if (tmpField.getCard().canGo(MazeCard.CANGO.RIGHT)) openList.add(tmpField);
                }
                tmpField = this.board.get(currField.row(), currField.col()+1); // 3
                if (tmpField != null && !closedList.contains(tmpField) && currField.getCard().canGo(MazeCard.CANGO.RIGHT)) {
                    if (tmpField.getCard().canGo(MazeCard.CANGO.LEFT)) openList.add(tmpField);
                }
                tmpField = this.board.get(currField.row()+1, currField.col()); // 4
                if (tmpField != null && !closedList.contains(tmpField) && currField.getCard().canGo(MazeCard.CANGO.DOWN)) {
                    if (tmpField.getCard().canGo(MazeCard.CANGO.UP)) openList.add(tmpField);
                }
                closedList.add(currField);
            }
        }
        return false;
    }

    public boolean canMove(int row, int col) {
        if (this.board == null) {
            System.out.println("Error - Player.canMove: board is null");
            return false;
        }
        if (this.location == null) {
            System.out.println("Error - Player.canMove: location is null");
            return false;
        }
        MazeField target;
        target = this.board.get(row, col);
        if (target == null) {
            System.out.println("Error - Player.canMove: target is null");
            return false;
        }
        return this.isPathValid(target);
    }

    public void makeIcon(){
        this.charIcon = this.character[this.number];
    }

    // Nacitam vsetky dostupne postavicky
    private void setImagesIcon(){

        character = new BufferedImage[4];
        try {
            this.character[0] = ImageIO.read(getClass().getResource("/images/chars/char01.png"));
            this.character[1] = ImageIO.read(getClass().getResource("/images/chars/char03.png"));
            this.character[2] = ImageIO.read(getClass().getResource("/images/chars/char05.png"));
            this.character[3] = ImageIO.read(getClass().getResource("/images/chars/char08.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
