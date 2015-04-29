package ija.labyrinth.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Maros Janota (xjanot01) on 27.4.2015.
 */
public class CreateScoreUI extends JPanel{

    public CreateScoreUI(){

        this.setSize(200,700);
        this.setLocation(850,0);
        this.setBackground(new Color(0,0,0));

        JButton testBtn = new JButton();
        testBtn.setIcon(new ImageIcon(getClass().getResource("/images/rock_I2.png")));
        /*testBtn.setSize(80,80);
        testBtn.setBorderPainted(false);
        testBtn.setFocusPainted(false);
        testBtn.setContentAreaFilled(false);*/
        this.add(testBtn);

    }
}
