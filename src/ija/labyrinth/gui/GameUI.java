package ija.labyrinth.gui;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameUI extends JFrame{

    private BufferedImage bgImage;
    private JButton MenuNewGame;
    private JButton MenuLoadGame;
    private JButton MenuHelp;
    private JButton MenuExitGame;
    private int BoardSize;

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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.menuButtons();
        this.pack();
        this.setVisible(true);
    }

    private void startNewGame(){

        this.currentCont = this.getContentPane();
        this.currentCont.removeAll();

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage_game.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CreateBoardUI newBoard = new CreateBoardUI(this.BoardSize);
        this.add(newBoard);

       /* CreateScoreUI newScore = new CreateScoreUI();
        this.add(newScore);*/

        this.pack();
        this.setVisible(true);
    }

    private void showMenuLoadGame(){

        this.currentCont = this.getContentPane();
        this.currentCont.removeAll();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
        //this.pack();
        this.setVisible(true);
    }

    private void menuButtons() {

        this.MenuNewGame = new JButton();
        this.MenuLoadGame = new JButton();
        this.MenuHelp = new JButton();
        this.MenuExitGame = new JButton();


        this.MenuNewGame.setIcon(new ImageIcon(getClass().getResource("/images/NovaHra_btn.png")));
        this.MenuNewGame.setRolloverIcon(new ImageIcon(getClass().getResource("/images/NovaHra_btn2.png")));
        this.MenuNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame(e);
            }
        });
        this.MenuNewGame.setBounds(100, 320, 210, 60);
        this.MenuNewGame.setBorderPainted(false);
        this.MenuNewGame.setFocusPainted(false);
        this.MenuNewGame.setContentAreaFilled(false);

        this.MenuLoadGame.setIcon(new ImageIcon(getClass().getResource("/images/NacistHru_btn.png")));
        this.MenuLoadGame.setRolloverIcon(new ImageIcon(getClass().getResource("/images/NacistHru_btn2.png")));
        this.MenuLoadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoadGame(e);
            }
        });
        this.MenuLoadGame.setBounds(100, 380, 210, 60);
        this.MenuLoadGame.setBorderPainted(false);
        this.MenuLoadGame.setFocusPainted(false);
        this.MenuLoadGame.setContentAreaFilled(false);


        this.MenuHelp.setIcon(new ImageIcon(getClass().getResource("/images/oHre_btn.png")));
        this.MenuHelp.setRolloverIcon(new ImageIcon(getClass().getResource("/images/oHre_btn2.png")));
        this.MenuHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAbout(e);
            }
        });
        this.MenuHelp.setBounds(100, 440, 210, 60);
        this.MenuHelp.setBorderPainted(false);
        this.MenuHelp.setFocusPainted(false);
        this.MenuHelp.setContentAreaFilled(false);


        this.MenuExitGame.setIcon(new ImageIcon(getClass().getResource("/images/KonecHry_btn.png")));
        this.MenuExitGame.setRolloverIcon(new ImageIcon(getClass().getResource("/images/KonecHry_btn2.png")));
        this.MenuExitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.MenuExitGame.setBounds(100, 500, 210, 60);
        this.MenuExitGame.setBorderPainted(false);
        this.MenuExitGame.setFocusPainted(false);
        this.MenuExitGame.setContentAreaFilled(false);

        this.add(MenuNewGame);
        this.add(MenuLoadGame);
        this.add(MenuHelp);
        this.add(MenuExitGame);

        this.setLayout(null);
    }


    private void showMenu (ActionEvent e){
        this.mainWindow();
    }

    private void newGame(ActionEvent e){
        NewGameSettings createNew = new NewGameSettings();

        System.out.print("Velkost hracej dosky je nastaveny na : " + createNew.FieldSize + "\n");
        System.out.print("Pocet kariet je nastaveny na: " + createNew.CardNum + "\n");
        System.out.print("Pocet hracov je nastaveny na: " + createNew.PlayerNum + "\n");

        this.BoardSize = createNew.FieldSize;
        this.startNewGame();
    }

    private void showLoadGame (ActionEvent e){
        this.showMenuLoadGame();
    }

    private void showAbout(ActionEvent e){
        this.showMenuHelp();
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
