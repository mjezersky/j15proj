package ija.labyrinth.gui;

import ija.labyrinth.game.GameData;
import ija.labyrinth.game.MazeBoard;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.*;
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
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * IJA 2015 - Projekt Labyrinth
 * Autori:  Maroš Janota
 *          Matouš Jezerský
 *
 *
 * GUI pre hlavné menu hry.
 * Obsahuje tlačítka Nová hra, Načítať hru, O hre, Koniec hry.
 * Podľa vybratej voľby sa generuje nový obsah.
 */
public class GameUI extends JFrame implements WindowListener{

    private BufferedImage bgImage;
    private Container currentCont;

    private static MazeBoard game;

    final static String LOOKANDFEEL = "System";

    /**
     * Inicializácia hlavného okna s obsahom menu.
     */
    public GameUI(){
        mainWindow();
    }

    /**
     * Hlavné okno, ktoré obsahuje všetky tlačítka.
     * Je využívaný JFrame s nastaveným pozadím a velkosťou.
     * Okno sa nedá rozširovať manuálne.
     */
    public void mainWindow(){

        initLookAndFeel();
        JFrame.setDefaultLookAndFeelDecorated(true);

        this.setTitle("Labyrinth - IJA Projekt 2015");
        this.setSize(1050, 700);
        this.setResizable(false);
        this.setLocationRelativeTo(this);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        URL gifImg = getClass().getResource("/images/intro.gif");
        ImageIcon imageIcon = new ImageIcon(gifImg);
        JLabel label = new JLabel(imageIcon);
        this.setContentPane(label);


        this.addWindowListener(this);
        this.menuButtons();
        this.pack();
        this.setVisible(true);
    }

    /**
     * Výstražné okno, ktoré vyskočí pri pokuse o ukončení hry pomocou krížika.
     * Treba potvrdiť voľbu ukončnia.
     * Pri zrušní sa vráti do menu.
     * @param e WindowEvent čaká na stlačenie X
     */
    @Override
    public void windowClosing(WindowEvent e) {
        String[] options = new String[] {"Áno", "Nie"};
        int result = JOptionPane.showOptionDialog(null, "Naozaj chcete ukončiť hru?", "Exit", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (result == 0) {
            System.exit(0);
        } else {/* Vrati do hry*/ }
    }

    /**
     * Vytvorí novú hru podľa zadaných parametrov získaných od užívateľa.
     * Do aktuálneho okna sa pridá nový JPanel s obsahom hry.
     *
     * @param bs board size - veľkosť hracej plochy
     * @param pn player number - počet hráčov
     * @param cn card number - počet kariet v balíčku
     * @param playersNames - mená hráčov
     * @param gameIn - vstupná hra (iba pri načítaní uloženej hry, inak null)
     */
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

        CreateBoardUI newBoard = new CreateBoardUI(bs, pn, cn, playersNames, gameIn);
        this.add(newBoard);

        this.pack();
        this.setVisible(true);
    }

    /**
     * Vytvorí tlačítka v hlavnom menu a nastaví im akciu po stlačení.
     * Tlačítka majú nastavené 2 obrázky, jeden ako hlavný a druhý sa zmení po prejdení myšou.
     */
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

    /**
     * Zobrazí hlavné menu.
     * @param e ActionEvent
     */
    public void showMenu (ActionEvent e){
        this.mainWindow();
    }

    /**
     * Načíta triedu NewGameUI kde sa nachádza nastavenie novej hry, ktoré si zvolí užívateľ.
     * Podľa zadaných parametrov pošle žiadosť o vytvorenie novej hry.
     * @param e ActionEvent
     */
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

    /**
     * Zobrazí nové okno, kde si užívateľ zvolí súbor na načítanie uloženej hry.
     * Po načítaní sa pošle žiadosť na spustenie hry s aktuálnym obsahom.
     * @param e ActionEvent
     */
    private void showLoadGame (ActionEvent e){
        JFileChooser openFile = new JFileChooser();
        openFile.setDialogTitle("Načítaj hru");

        int selectFile = openFile.showOpenDialog(null);
        if (selectFile == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = openFile.getSelectedFile();  //Subor na nacitanie
            System.out.println("Open file: " + fileToOpen.getAbsolutePath());

            MazeBoard loadGame;
            loadGame = GameData.load(fileToOpen);

            if(loadGame != null){
                game = loadGame;
                int cardPackSize = 0;
                for(int i = 0; i < game.getPlayerCount(); i++){
                    cardPackSize += game.getPlayer(i).getScore()+1;
                }
                cardPackSize += game.getPack().getSize();
                System.out.println("Load OK");
                startNewGame(game.getSize(), game.getPlayerCount(), cardPackSize, null, game);
            }
        }
    }

    /**
     * Zobrazí informácie o hre.
     * @param e ActionEvent
     */
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
        this.setVisible(true);
    }

    /**
     * Funkcie potrebné pre WindowListener, ale v našom projekte sa nevyužívajú, no musia byť implementované pre správnu funkčnosť.
     * @param e WindowEvent
     */
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

    /**
     * Zistí podľa operačného systému Look And Feel (vzhľad).
     * Nastaví potrebnú tému, pre správne zobrazovanie.
     */
    private static void initLookAndFeel() {
        String lookAndFeel = null;

        if (LOOKANDFEEL != null) {
            if (LOOKANDFEEL.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();

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
                if (LOOKANDFEEL.equals("Metal")) {
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

    /**
     * Spustí vytvorenie hlavného menu s jeho obsahom.
     */
    public static void main(String[] args) {
        new GameUI().setVisible(true);
    }
}
