
package ija.labyrinth.game.cards;
import ija.labyrinth.game.MazeField;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * IJA 2015 - Projekt Labyrinth
 * Autori:  Maroš Janota
 *          Matouš Jezerský
 */
public class TreasureCard {
    private int identifier;
    private MazeField location;

    private BufferedImage cardIcon;
    private BufferedImage[] cardIco;

    @Override
    public String toString() {
        // IIrrcc
        String tmp = "";
        if (this.identifier<10) tmp += "0";
        tmp += Integer.toString(this.identifier);
        if (this.location.row()<10) tmp += "0";
        tmp += Integer.toString(this.location.row());
        if (this.location.col()<10) tmp += "0";
        tmp += Integer.toString(this.location.col());
        return tmp;
    }

    public TreasureCard(int id, MazeField field) {
        this.identifier = id;
        this.location = field;

        getImagesIcon();
        setCardIco();
    }

    public void moveTo(MazeField mf) {
        if (mf == null) {
            System.out.println("Error - TreasureCard.moveTo: target location is null");
            return;
        }
        this.location = mf;
    }

    public boolean equals(TreasureCard tc) {
        return this.location.equals(tc.location);
    }

    public MazeField getLocation() { return this.location; }

    public int getId() { return this.identifier; }


    public BufferedImage getCardIcon() { return this.cardIcon; }
    public void setCardIco(){
        this.cardIcon = this.cardIco[identifier];
    }
    private void getImagesIcon(){

        cardIco = new BufferedImage[24];

        try {
            this.cardIco[0] = ImageIO.read(getClass().getResource("/images/cards/card01.png"));
            this.cardIco[1] = ImageIO.read(getClass().getResource("/images/cards/card02.png"));
            this.cardIco[2] = ImageIO.read(getClass().getResource("/images/cards/card03.png"));
            this.cardIco[3] = ImageIO.read(getClass().getResource("/images/cards/card04.png"));
            this.cardIco[4] = ImageIO.read(getClass().getResource("/images/cards/card05.png"));
            this.cardIco[5] = ImageIO.read(getClass().getResource("/images/cards/card06.png"));
            this.cardIco[6] = ImageIO.read(getClass().getResource("/images/cards/card07.png"));
            this.cardIco[7] = ImageIO.read(getClass().getResource("/images/cards/card08.png"));
            this.cardIco[8] = ImageIO.read(getClass().getResource("/images/cards/card09.png"));
            this.cardIco[9] = ImageIO.read(getClass().getResource("/images/cards/card10.png"));
            this.cardIco[10] = ImageIO.read(getClass().getResource("/images/cards/card11.png"));
            this.cardIco[11] = ImageIO.read(getClass().getResource("/images/cards/card12.png"));
            this.cardIco[12] = ImageIO.read(getClass().getResource("/images/cards/card13.png"));
            this.cardIco[13] = ImageIO.read(getClass().getResource("/images/cards/card14.png"));
            this.cardIco[14] = ImageIO.read(getClass().getResource("/images/cards/card15.png"));
            this.cardIco[15] = ImageIO.read(getClass().getResource("/images/cards/card16.png"));
            this.cardIco[16] = ImageIO.read(getClass().getResource("/images/cards/card17.png"));
            this.cardIco[17] = ImageIO.read(getClass().getResource("/images/cards/card18.png"));
            this.cardIco[18] = ImageIO.read(getClass().getResource("/images/cards/card19.png"));
            this.cardIco[19] = ImageIO.read(getClass().getResource("/images/cards/card20.png"));
            this.cardIco[20] = ImageIO.read(getClass().getResource("/images/cards/card21.png"));
            this.cardIco[21] = ImageIO.read(getClass().getResource("/images/cards/card22.png"));
            this.cardIco[22] = ImageIO.read(getClass().getResource("/images/cards/card23.png"));
            this.cardIco[23] = ImageIO.read(getClass().getResource("/images/cards/card24.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
