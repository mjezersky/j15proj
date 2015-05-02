package ija.board;

import ija.board.*;

public class Player {

    private int number;
    private MazeField location;
    private String name;

    private BufferedImage charIcon;
    private BufferedImage[] character;

    Player(String name, int number, MazeField location) {
        this.number = number;
        this.location = location;
        this.name = name;
    }

    public boolean canMoveTo(MazeField destination) {
        // TODO: kod hledani cesty k destination
        return true;
    }

    // Nahodne vybere zo zoznamu jednu postavicku
    private void makeIcon(){

        // Nacitam vsetky postavicky a zamiesam ich
        ArrayList<BufferedImage> char_pack = new ArrayList<>();
        char_pack.addAll(Arrays.asList(character).subList(1, 8 + 1));
        Collections.shuffle(char_pack);

        this.charIcon = char_pack.get(0);
        char_pack.remove(0);
    }

    private BufferedImage getIcon(){ return this.charIcon; }

    // Nacitam vsetky dostupne postavicky
    private void setImagesIcon(){

        try {
            this.character[1] = ImageIO.read(getClass().getResource("/images/chars/char01.png"));
            this.character[2] = ImageIO.read(getClass().getResource("/images/chars/char02.png"));
            this.character[3] = ImageIO.read(getClass().getResource("/images/chars/char03.png"));
            this.character[4] = ImageIO.read(getClass().getResource("/images/chars/char04.png"));
            this.character[5] = ImageIO.read(getClass().getResource("/images/chars/char05.png"));
            this.character[6] = ImageIO.read(getClass().getResource("/images/chars/char06.png"));
            this.character[7] = ImageIO.read(getClass().getResource("/images/chars/char07.png"));
            this.character[8] = ImageIO.read(getClass().getResource("/images/chars/char08.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
