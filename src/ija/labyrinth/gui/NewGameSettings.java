package ija.labyrinth.gui;

import javax.smartcardio.Card;
import javax.swing.*;
import java.net.URL;
import java.util.Objects;

/**
 * Created by Maros Janota (xjanot01) on 25.4.2015.
 */
public class NewGameSettings extends JDialog{

    public int FieldSize;
    public String CardNum;
    public String PlayerNum;

    public NewGameSettings(){
        FieldSize = fieldSize();
        CardNum = cardNum();
        PlayerNum = playersNum();
    }

    private int fieldSize(){

        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/images/info_ico2.png"));

        String infoText = "Zvolte velkost hracieho pola:\n";
        Object[] possibilities = {"5x5","7x7","9x9","11x11"};
        String sizeField = (String) JOptionPane.showInputDialog(
                new JFrame(),
                infoText,
                "Nová hra",
                JOptionPane.PLAIN_MESSAGE,
                icon, possibilities, null);

        if (Objects.equals(sizeField, "5x5")) return 5;
        if (Objects.equals(sizeField, "7x7")) return 7;
        if (Objects.equals(sizeField, "9x9")) return 9;
        if (Objects.equals(sizeField, "11x11")) return 11;

        return 0;
    }

    private String cardNum(){

        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/images/card_ico2.png"));

        String infoText = "Zadajte pocet hracich kariet:\n";
        Object[] possibilities = {"12", "24"};
        String cardNumber = (String) JOptionPane.showInputDialog(
                new JFrame(),
                infoText,
                "Nová hra",
                JOptionPane.PLAIN_MESSAGE,
                icon, possibilities, null);

        //If a string was returned, say so.
        if ((cardNumber != null) && (cardNumber.length() > 0)) {
            return cardNumber;
        }

        //If you're here, the return value was null/empty.
        String err = "nullCard";
        return err;
    }


    private String playersNum(){

        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/images/player_ico2.png"));

        String infoText = "Zadajte pocet hracov:\n";
        Object[] possibilities = {"2", "3", "4"};
        String playerNumber = (String) JOptionPane.showInputDialog(
                new JFrame(),
                infoText,
                "Nová hra",
                JOptionPane.PLAIN_MESSAGE,
                icon, possibilities, null);

        if ((playerNumber != null) && (playerNumber.length() > 0)) {
            return playerNumber;
        }

        String err = "nullPlayer";
        return err;
    }

}
