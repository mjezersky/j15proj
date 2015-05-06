package ija.labyrinth.gui;

import ija.labyrinth.game.GameData;
import ija.labyrinth.game.MazeBoard;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameUI extends JFrame implements WindowListener{

    private BufferedImage bgImage;
    private Container currentCont;

    private static MazeBoard game;
    private static int gameSize;

    final static String LOOKANDFEEL = "System";
    final static String THEME = "Test";

    public GameUI(){
        this.mainWindow();
    }

    public void mainWindow(){
        initLookAndFeel();
        JFrame.setDefaultLookAndFeelDecorated(true);

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


    public void startNewGame(int bs, int pn, int cn, String[] playersNames, MazeBoard gameIn){

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

        CreateBoardUI newBoard = new CreateBoardUI(bs, pn, cn, playersNames, gameIn);
        this.add(newBoard);

        this.pack();
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
                showNewGame(e);
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


    public void showMenu (ActionEvent e){
        this.mainWindow();
    }

    private void showNewGame(ActionEvent e){

        this.currentCont = this.getContentPane();
        this.currentCont.removeAll();

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage_newgame.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException ex) {
            ex.printStackTrace();
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
                if (newGame.checkSettings()){
                    startNewGame(newGame.getBoardSize(), newGame.getPlayersNum(),
                            newGame.getCardNum(), newGame.getPlayersNames(), null);
                }
            }
        });
        add(startGame);

        this.pack();
        this.setVisible(true);
    }

    private void showLoadGame (ActionEvent e){
        //this.showMenuLoadGame();

        JFileChooser openFile = new JFileChooser();
        openFile.setDialogTitle("Načítaj hru");

        int selectFile = openFile.showOpenDialog(null);
        if (selectFile == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = openFile.getSelectedFile();  //Subor na nacitanie
            System.out.println("Open file: " + fileToOpen.getAbsolutePath());

            MazeBoard loadGame;
            loadGame = GameData.load(fileToOpen);

            if(loadGame != null){

                GameUI.game = loadGame;

                String[] playerNames;
                playerNames = new String[2];
                playerNames[0] = "Maros";
                playerNames[1] = "Matous";

                this.startNewGame(game.getSize(), game.getPlayerCount(), game.getPack().getSize(), null, game);
            }
        }
    }

    private void showAbout(ActionEvent e){
        this.currentCont = this.getContentPane();
        this.currentCont.removeAll();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            bgImage = ImageIO.read(getClass().getResource("/images/bgImage_help.png"));
            JLabel bg = new JLabel(new ImageIcon(bgImage));
            this.setContentPane(bg);
        } catch (IOException ex) {
            ex.printStackTrace();
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

    private static void initLookAndFeel() {
        String lookAndFeel = null;

        if (LOOKANDFEEL != null) {
            if (LOOKANDFEEL.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
                //  an alternative way to set the Metal L&F is to replace the
                // previous line with:
                // lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";

            }

            else if (LOOKANDFEEL.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            }

            else if (LOOKANDFEEL.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }

            else if (LOOKANDFEEL.equals("GTK")) {
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            }

            else {
                System.err.println("Unexpected value of LOOKANDFEEL specified: "
                        + LOOKANDFEEL);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }

            try {


                UIManager.setLookAndFeel(lookAndFeel);

                // If L&F = "Metal", set the theme

                if (LOOKANDFEEL.equals("Metal")) {
                    if (THEME.equals("DefaultMetal"))
                        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    else
                        MetalLookAndFeel.setCurrentTheme(new OceanTheme());

                    UIManager.setLookAndFeel(new MetalLookAndFeel());
                }
            }

            catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class for specified look and feel:"
                        + lookAndFeel);
                System.err.println("Did you include the L&F library in the class path?");
                System.err.println("Using the default look and feel.");
            }

            catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel ("
                        + lookAndFeel
                        + ") on this platform.");
                System.err.println("Using the default look and feel.");
            }

            catch (Exception e) {
                System.err.println("Couldn't get specified look and feel ("
                        + lookAndFeel
                        + "), for some reason.");
                System.err.println("Using the default look and feel.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //Set Look and Feel
        /*try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}*/

        new GameUI().setVisible(true);
    }
}
