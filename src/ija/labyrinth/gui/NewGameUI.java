package ija.labyrinth.gui;

import ija.labyrinth.gui.GameUI;

import com.oracle.xmlns.internal.webservices.jaxws_databinding.JavaMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Maros Janota (xjanot01) on 3.5.2015.
 */
public class NewGameUI extends JPanel {

    private JTextField player1, player2, player3, player4;
    private JCheckBox five, seven, nine, eleven;
    private JCheckBox c12, c24;

    private int playersNum;
    private int boardSize;
    private int cardNum;

    private String[] playersNames;

    public NewGameUI(){

        setSize(1050, 700);

        JButton startGame = new JButton();
        startGame.setIcon(new ImageIcon(getClass().getResource("/images/spust_btn.png")));
        startGame.setRolloverIcon(new ImageIcon(getClass().getResource("/images/spust_btn2.png")));
        startGame.setBounds(750,550,210,60);
        startGame.setBorderPainted(false);
        startGame.setFocusPainted(false);
        startGame.setContentAreaFilled(false);
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkSettings();
            }
        });
        add(startGame);

        choosePlayers();
        chooseBoardSize();
        chooseCardNum();

        setFocusable(true);
        requestFocusInWindow();

        setOpaque(false);
        setVisible(true);
        setLayout(null);
    }

    public void checkSettings(){

        setPlayerNames();
        setBoardSize();
        setCardNum();

        if(this.playersNum < 2){
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Zadajte aspoň dvoch hráčov. ","Chyba",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            this.playersNum = 0;
        }

        else if (this.boardSize == 0){
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Vyberte len jednu velkosť hracej plochy.", "Chyba",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
        }

        else if (this.cardNum == 0){
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Vyberte len jeden počet kariet.", "Chyba",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
        }

        else {
            GameUI startIt = new GameUI();
            startIt.startNewGame(getBoardSize(), getPlayersNum(), getCardNum(), getPlayersNames());
        }
    }

    private void choosePlayers(){

        Font font = new Font("Arial", Font.PLAIN, 14);

        this.player1 = new JTextField();
        this.player2 = new JTextField();
        this.player3 = new JTextField();
        this.player4 = new JTextField();

        this.player1.setFont(font);
        this.player2.setFont(font);
        this.player3.setFont(font);
        this.player4.setFont(font);

        this.player1.setBounds(180,245,150,30);
        this.player2.setBounds(180,300,150,30);
        this.player3.setBounds(180,355,150,30);
        this.player4.setBounds(180,410,150,30);

        this.add(this.player1);
        this.add(this.player2);
        this.add(this.player3);
        this.add(this.player4);
    }

    // Zistim velkost dosky
    private void chooseBoardSize(){

        this.five = new JCheckBox();
        this.five.setBounds(558,180, 20,20);
        this.five.setOpaque(false);
        this.add(this.five);

        this.seven = new JCheckBox();
        this.seven.setBounds(662,180, 20,20);
        this.seven.setOpaque(false);
        this.add(this.seven);

        this.nine = new JCheckBox();
        this.nine.setBounds(761,180, 20,20);
        this.nine.setOpaque(false);
        this.add(this.nine);

        this.eleven = new JCheckBox();
        this.eleven.setBounds(855,180, 20,20);
        this.eleven.setOpaque(false);
        this.add(this.eleven);

    }

    // Zistim pocet kariet
    private void chooseCardNum(){

        this.c12 = new JCheckBox();
        this.c12.setBounds(609,412,20,20);
        this.c12.setOpaque(false);
        this.add(c12);

        this.c24 = new JCheckBox();
        this.c24.setBounds(712,412,20,20);
        this.c24.setOpaque(false);
        this.add(c24);

    }

    // Ziskam pocet a mena hracov
    private void setPlayerNames(){
        this.playersNum = 0;

        this.playersNames = new String[5];

        int i = 0;
        if (!this.player1.getText().equals("")){
            this.playersNames[i] = this.player1.getText();
            this.playersNum++;
            i++;
        }
        if (!this.player2.getText().equals("")){
            this.playersNames[i] = this.player1.getText();
            this.playersNum++;
            i++;
        }
        if (!this.player3.getText().equals("")){
            this.playersNames[i] = this.player1.getText();
            this.playersNum++;
            i++;
        }
        if (!this.player4.getText().equals("")){
            this.playersNames[i] = this.player1.getText();
            this.playersNum++;
            i++;
        }

    }


    private void setCardNum(){
        int num = 0;
        this.cardNum = 0;

        if(this.c12.isSelected()){
            this.cardNum = 12;
            num++;
        }

        if(this.c24.isSelected()){
            this.cardNum = 24;
            num++;
        }

        if(num > 1){
            this.cardNum = 0;
        }
    }

    private void setBoardSize() {
        int num = 0;
        this.boardSize = 0;

        if (this.five.isSelected()) {
            this.boardSize = 5;
            num++;
        }

        if (this.seven.isSelected()) {
            this.boardSize = 8;
            num++;
        }

        if (this.nine.isSelected()) {
            this.boardSize = 9;
            num++;
        }

        if (this.eleven.isSelected()) {
            this.boardSize = 11;
            num++;
        }

        if(num > 1){
            this.boardSize = 0;
        }
    }

    public String[] getPlayersNames() { return this.playersNames; }
    public int getPlayersNum(){ return this.playersNum; }
    public int getBoardSize() {return this.boardSize; }
    public int getCardNum() { return this.cardNum; }
}
