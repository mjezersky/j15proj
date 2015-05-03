package ija.labyrinth.gui;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameUI extends JFrame implements WindowListener{

    private BufferedImage bgImage;
    private Container currentCont;

    public GameUI(){
        this.mainWindow();
        this.menuButtons();
    }

    private void mainWindow(){

        this.setTitle("Labyrinth - IJA Projekt 2015");
        this.setSize(1050, 700);
        this.setResizable(false);
        this.setLocationRelativeTo(this);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.addWindowListener(this);

        this.menuButtons();
        this.pack();
        this.setVisible(true);
    }




    @Override
    public void windowClosing(WindowEvent e) {
        int result = JOptionPane.showConfirmDialog(null, "Naozaj chcete ukoncit hru?","Exit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION){
            System.exit(0);
        }else{/* Vrati do hry*/ }
    }


    public void startNewGame(int bs, int pn, int cn, String[] playersNames){

        this.currentCont = this.getContentPane();
        this.currentCont.removeAll();

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage_game.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Velkost hracej dosky je nastaveny na : " + bs + "\n");
        System.out.print("Pocet kariet je nastaveny na: " + cn + "\n");
        System.out.print("Pocet hracov je nastaveny na: " + pn + "\n");

        CreateBoardUI newBoard = new CreateBoardUI(bs, pn, cn, playersNames);
        this.add(newBoard);

        this.pack();
        this.setVisible(true);
    }

    private void showNewGameSettings(){

        this.currentCont = this.getContentPane();
        this.currentCont.removeAll();

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage_newgame.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final NewGameUI newGame = new NewGameUI();
        this.add(newGame);

        JButton backBtn = new JButton();
        backBtn.setIcon(new ImageIcon(getClass().getResource("/images/navrat_btn.png")));
        backBtn.setRolloverIcon(new ImageIcon(getClass().getResource("/images/navrat2_btn.png")));
        backBtn.setBounds(100,550,210,60);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMenu(e);
            }
        });
        this.add(backBtn);



        this.add(backBtn);
        this.pack();
        this.setVisible(true);

    }

    private void showMenuLoadGame(){

        this.currentCont = this.getContentPane();
        this.currentCont.removeAll();

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage_empty.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton backBtn = new JButton();
        backBtn.setIcon(new ImageIcon(getClass().getResource("/images/navrat_btn.png")));
        backBtn.setRolloverIcon(new ImageIcon(getClass().getResource("/images/navrat2_btn.png")));
        backBtn.setBounds(100,550,210,60);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMenu(e);
            }
        });

        this.add(backBtn);
        this.pack();
        this.setVisible(true);
    }

    private void showMenuHelp(){

        this.currentCont = this.getContentPane();
        this.currentCont.removeAll();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage_help.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton backBtn = new JButton();
        backBtn.setIcon(new ImageIcon(getClass().getResource("/images/navrat_btn.png")));
        backBtn.setRolloverIcon(new ImageIcon(getClass().getResource("/images/navrat2_btn.png")));
        backBtn.setBounds(100, 550, 210, 60);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMenu(e);
            }
        });

        this.add(backBtn);
        //this.pack();
        this.setVisible(true);
    }

    private void menuButtons() {

        JButton menuNewGame = new JButton();
        JButton menuLoadGame = new JButton();
        JButton menuHelp = new JButton();
        JButton menuExitGame = new JButton();


        menuNewGame.setIcon(new ImageIcon(getClass().getResource("/images/NovaHra_btn.png")));
        menuNewGame.setRolloverIcon(new ImageIcon(getClass().getResource("/images/NovaHra_btn2.png")));
        menuNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame(e);
            }
        });
        menuNewGame.setBounds(100, 320, 210, 60);
        menuNewGame.setBorderPainted(false);
        menuNewGame.setFocusPainted(false);
        menuNewGame.setContentAreaFilled(false);

        menuLoadGame.setIcon(new ImageIcon(getClass().getResource("/images/NacistHru_btn.png")));
        menuLoadGame.setRolloverIcon(new ImageIcon(getClass().getResource("/images/NacistHru_btn2.png")));
        menuLoadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoadGame(e);
            }
        });
        menuLoadGame.setBounds(100, 380, 210, 60);
        menuLoadGame.setBorderPainted(false);
        menuLoadGame.setFocusPainted(false);
        menuLoadGame.setContentAreaFilled(false);


        menuHelp.setIcon(new ImageIcon(getClass().getResource("/images/oHre_btn.png")));
        menuHelp.setRolloverIcon(new ImageIcon(getClass().getResource("/images/oHre_btn2.png")));
        menuHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAbout(e);
            }
        });
        menuHelp.setBounds(100, 440, 210, 60);
        menuHelp.setBorderPainted(false);
        menuHelp.setFocusPainted(false);
        menuHelp.setContentAreaFilled(false);


        menuExitGame.setIcon(new ImageIcon(getClass().getResource("/images/KonecHry_btn.png")));
        menuExitGame.setRolloverIcon(new ImageIcon(getClass().getResource("/images/KonecHry_btn2.png")));
        menuExitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuExitGame.setBounds(100, 500, 210, 60);
        menuExitGame.setBorderPainted(false);
        menuExitGame.setFocusPainted(false);
        menuExitGame.setContentAreaFilled(false);

        this.add(menuNewGame);
        this.add(menuLoadGame);
        this.add(menuHelp);
        this.add(menuExitGame);

        this.setLayout(null);
    }


    private void showMenu (ActionEvent e){
        this.mainWindow();
    }

    private void newGame(ActionEvent e){
        this.showNewGameSettings();

    }

    private void showLoadGame (ActionEvent e){
        this.showMenuLoadGame();
    }

    private void showAbout(ActionEvent e){
        this.showMenuHelp();
    }

    // Nevyuzivaju sa, ale musia byt kvoli WindowListener
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public static void main(String[] args) {
        //Set Look and Feel
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}

        new GameUI().setVisible(true);
    }
}
