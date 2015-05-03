package ija.labyrinth.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Maros Janota (xjanot01) on 27.4.2015.
 */
public class CreateScoreUI extends JTextArea {

    private JTextArea scorePanel;
    private int playersNum;

    public CreateScoreUI(int pn){
        this.playersNum = pn;

        Font font = new Font("Verdana", Font.BOLD, 14);
        this.scorePanel = new JTextArea("Score: ");
        this.scorePanel.setBounds(830,385,140,140);
        this.scorePanel.setEditable(false);
        this.scorePanel.setFont(font);
        this.scorePanel.setForeground(Color.RED);
        add(this.scorePanel);
    }

    private void getPlayers(){

        Font fontPlayer = new Font("Verdana", Font.PLAIN, 12);

        if (this.playersNum == 2){
            this.scorePanel.append("Player1");
            this.scorePanel.append("Player2");
            this.scorePanel.setForeground(Color.BLACK);
        }
    }
}
