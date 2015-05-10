package ija.labyrinth.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * IJA 2015 - Projekt Labyrinth
 * Autori:  Maroš Janota
 *          Matouš Jezerský
 *
 * Vytvorí GUI pre nastavenie novej hry užívateľom.
 * Ten si zvolí počet hráčov podľa zadaných mien, veľkosť hracej plochy a počet kariet.
 * Každému hráčovi bude pridelená ikona postavičky automaticky, tú si voliť nemôže.
 */
public class NewGameUI extends JPanel {

    private JTextField player1, player2, player3, player4;
    private JRadioButton c12, c24;
    private JRadioButton five, seven, nine, eleven;

    private int playersNum;
    private int boardSize;
    private int cardNum;

    private String[] playersNames;

    /**
     * Vytvorí nový panel ktorý sa vloží do hlavného okna.
     * Panel obsahuje celý obsah tohto menu.
     */
    public NewGameUI(){

        setSize(1050, 700);

        choosePlayers();
        chooseBoardSize();
        chooseCardNum();

        setFocusable(true);
        requestFocusInWindow();

        setOpaque(false);
        setVisible(true);
        setLayout(null);
    }

    /**
     * Vracia mená všetkých pridaných hráčov.
     * @return zoznam hráčov
     */
    public String[] getPlayersNames() { return this.playersNames; }

    /**
     * Vracia počet zadaných hráčov.
     * @return počet hráčov
     */
    public int getPlayersNum(){ return this.playersNum; }

    /**
     * Vracia velkosť hracej plochy.
     * @return velkosť hracej plochy
     */
    public int getBoardSize() {return this.boardSize; }

    /**
     * Vracia počet zvolených kariet.
     * @return počet kariet v balíčku
     */
    public int getCardNum() { return this.cardNum; }

    /**
     * Overuje, či boli všetky parametre správne.
     * Pri nájdeni chybz vypíše, presne čo je zle zadané.
     * @return true - ak je všetko OK; false - chyba
     */
    public boolean checkSettings(){

        setPlayerNames();
        setBoardSize();
        setCardNum();

        boolean lengthMax = false;
        for (int i = 0; i < this.playersNum; i++){
            String name = playersNames[i];
            if (name.length() > 8){
                lengthMax = true;
            }
        }

        if(this.playersNum < 2){
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Zadajte aspoň dvoch hráčov. ","Chyba",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            this.playersNum = 0;
        }
        else if (lengthMax == true){
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Meno hráča môže obsahovať maximálne 8 znakov.", "Chyba",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
        }
        else if (this.five.isSelected() && this.c24.isSelected()){
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Pre plochu 5x5 nie je možné zvoliť 24 kariet.\n" + "Zmente veľkosť balíčka na 12 kariet.", "Chyba",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
        }

        else { return true; }
        return false;
    }

    /**
     * Vytvorí textové polia, kde užívateľ zadá mená hráčov.
     * Prvé dve mená sú pridelené automaticky, ale po kliknutí na ne sa zmažu a užívateľ si môže zadať vlastné.
     */
    private void choosePlayers(){

        Font font = new Font("Arial", Font.PLAIN, 14);

        this.player1 = new JTextField("Player1");
        this.player2 = new JTextField("Player2");
        this.player3 = new JTextField();
        this.player4 = new JTextField();

        this.player1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                player1.setText("");
            }
        });
        this.player2.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                player2.setText("");
            }
        });

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

    /**
     * Vytvorí zaškrtávacie okienka pre výber veľkosti hracej plochy.
     * Musí byť zaškrtnuté vždy len jedno.
     */
    private void chooseBoardSize(){

        CheckboxGroup size = new CheckboxGroup();
        add(new Checkbox(null, true, size));

        this.five = new JRadioButton();
        this.five.setBounds(487,125, 20,20);
        this.five.setOpaque(false);
        this.five.setSelected(true);
        this.add(this.five);

        this.seven = new JRadioButton();
        this.seven.setBounds(591,125, 20,20);
        this.seven.setOpaque(false);
        this.add(this.seven);

        this.nine = new JRadioButton();
        this.nine.setBounds(692,125, 20,20);
        this.nine.setOpaque(false);
        this.add(this.nine);

        this.eleven = new JRadioButton();
        this.eleven.setBounds(784,125, 20,20);
        this.eleven.setOpaque(false);
        this.add(this.eleven);

        ButtonGroup boardBtns = new ButtonGroup();
        boardBtns.add(five);
        boardBtns.add(seven);
        boardBtns.add(nine);
        boardBtns.add(eleven);

    }

    /**
     * Vytvorí zaškrtávacie okienka pre výber počtu kariet v balíčku.
     * Musí byť zaškrtnuté vždy len jedno.
     */
    private void chooseCardNum(){

        this.c12 = new JRadioButton();
        this.c12.setBounds(567,336,20,20);
        this.c12.setOpaque(false);
        this.c12.setSelected(true);
        this.add(c12);

        this.c24 = new JRadioButton();
        this.c24.setBounds(670,336,20,20);
        this.c24.setOpaque(false);
        this.add(c24);

        ButtonGroup cardBtn = new ButtonGroup();
        cardBtn.add(c12);
        cardBtn.add(c24);
        ;
    }

    /**
     * Vytvorí hráčov podľa zadaných mien od užívateľa.
     * Tento zoznam sa dalej posiela do vytvorenia novej hry.
     */
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
            this.playersNames[i] = this.player2.getText();
            this.playersNum++;
            i++;
        }
        if (!this.player3.getText().equals("")){
            this.playersNames[i] = this.player3.getText();
            this.playersNum++;
            i++;
        }
        if (!this.player4.getText().equals("")){
            this.playersNames[i] = this.player4.getText();
            this.playersNum++;
            i++;
        }

    }

    /**
     * Nastaví sa veľkosť balíčka karit.
     * Táto veľkosť sa posiela do vytvorenia novej hry.
     */
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

    /**
     * Nastaví sa veľkosťhracej plochy.
     * Táto veľkosť sa posiela do vytvorenia novej hry.
     */
    private void setBoardSize() {
        int num = 0;
        this.boardSize = 0;

        if (this.five.isSelected()) {
            this.boardSize = 5;
            num++;
        }

        if (this.seven.isSelected()) {
            this.boardSize = 7;
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
}
