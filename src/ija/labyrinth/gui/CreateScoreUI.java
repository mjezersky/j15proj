package ija.labyrinth.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Maros Janota (xjanot01) on 27.4.2015.
 */
public class CreateScoreUI extends JTextArea {

    private JTextArea scorePanel;
    private int playersNum;

    public CreateScoreUI(int pn, String[] playerName){

        this.playersNum = pn;
        String[] playerNames = playerName;

        Font font = new Font("Verdana", Font.BOLD, 15);
        Font fontPlayer = new Font("Verdana", Font.BOLD, 12);

        JTextArea whoGo = new JTextArea("Hrac na tahu: ");
        whoGo.setFont(font);
        whoGo.setOpaque(false);
        whoGo.setEditable(false);
        whoGo.setBounds(833,330,140,23);
        whoGo.setForeground(new Color(0xD74E00));
        this.add(whoGo);

        JTextArea scoreP = new JTextArea("Score: ");
        scoreP.setFont(font);
        scoreP.setOpaque(false);
        scoreP.setEditable(false);
        scoreP.setBounds(833,430,140,23);
        scoreP.setForeground(new Color(0xD74E00));
        this.add(scoreP);

        JTextArea scorePanel = new JTextArea();
        scorePanel.setBounds(833, 453, 140, 80);
        scorePanel.setEditable(false);
        scorePanel.setOpaque(false);
        this.add(scorePanel);


        scorePanel.setForeground(new Color(0xFFFFFF));
        scorePanel.setFont(fontPlayer);

        for(int i=0; i < this.playersNum; i++ ){
            scorePanel.append("  "+ playerNames[i]+"\n");
        }

        this.setVisible(true);

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
