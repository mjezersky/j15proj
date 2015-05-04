package ija.labyrinth.game;

import ija.labyrinth.game.MazeField;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Player {

    private int number;
    private MazeField location;
    private MazeBoard board;
    private String name;

    private BufferedImage charIcon;
    private BufferedImage[] character;
    private ArrayList<BufferedImage> char_pack;

    Player(String name, int number, MazeField location, MazeBoard board) {
        this.number = number;
        this.location = location;
        this.name = name;
        this.board = board;

        setImagesIcon();
    }

    public String getName() { return this.name; }
    public int getNum() { return this.number; }
    public BufferedImage getIcon(){ return this.charIcon; }

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

    // Nahodne vybere zo zoznamu jednu postavicku
    public void makeIcon(){

        Collections.shuffle(this.char_pack);

        this.charIcon = this.char_pack.get(0);
        this.char_pack.remove(0);
    }

    // Nacitam vsetky dostupne postavicky
    private void setImagesIcon(){

        character = new BufferedImage[8];

        try {
            this.character[0] = ImageIO.read(getClass().getResource("/images/chars/char01.png"));
            this.character[1] = ImageIO.read(getClass().getResource("/images/chars/char02.png"));
            this.character[2] = ImageIO.read(getClass().getResource("/images/chars/char03.png"));
            this.character[3] = ImageIO.read(getClass().getResource("/images/chars/char04.png"));
            this.character[4] = ImageIO.read(getClass().getResource("/images/chars/char05.png"));
            this.character[5] = ImageIO.read(getClass().getResource("/images/chars/char06.png"));
            this.character[6] = ImageIO.read(getClass().getResource("/images/chars/char07.png"));
            this.character[7] = ImageIO.read(getClass().getResource("/images/chars/char08.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.char_pack = new ArrayList<>();
        this.char_pack.addAll(Arrays.asList(this.character).subList(0, 8));

    }


}
