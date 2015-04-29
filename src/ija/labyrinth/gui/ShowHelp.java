package ija.labyrinth.gui;

import javax.swing.*;


/**
 * Created by Maros Janota (xjanot01) on 24.4.2015.
 */

public class ShowHelp extends JDialog{

    public ShowHelp(){

        String msg =     "                  Labyrinth\n"
                        + "School project for IJA 2015 - VUT FIT \n"
                        + "Authors:  Matouš Jezerský\n"
                        + "                 Maroš Janota\n"
                        + "\n"
                        + "Pri sputení novej hry je potrebné vybrať veľkosť hracej plochy,\n"
                        + "počet hracích kariet a počet hráčov. Následne sa vytvorí hracia\n"
                        + "plocha a hra sa spustí.\n"
                        + "\nCieľom hry je dostať sa s vašou figúrkou na kameň s obrázkom\n"
                        + "rovnakým ako je na zobrazenej karte. Karta sa započíta do získanych\n"
                        + "kariet a hráč si otočí ďalšiu kartu.\n"
                        + "Hra končí ak jeden z hráčov dosiahne taký počet kariet, ktorý\n"
                        + "odpovedá podielu K/P kde K je počet hracích kariet a P je počet\n"
                        + "hráčov";

        JOptionPane.showMessageDialog(new JFrame(), msg, "O hře", JOptionPane.PLAIN_MESSAGE);
    }

}
