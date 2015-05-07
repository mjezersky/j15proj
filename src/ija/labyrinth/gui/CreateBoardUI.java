package ija.labyrinth.gui;

import ija.labyrinth.game.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * IJA 2015 - Projekt Labyrinth
 * Autori:  Maroš Janota
 *          Matouš Jezerský
 *
 *
 * Hlavná trieda pre vytvorenie GUI novej alebo načítanej hry.
 * Zisťuje sa veľkosť hracej plochy, počet hráčov a ich mená, počet kariet v balíčku.
 * Po nastavení všetkých parametrov sa hra vykreslí.
 */
public class CreateBoardUI extends JPanel {

    private int boardSize;
    private int playersNum;
    private int cardsNum;
    private String[] playerNames;
    private Player[] p;
    private Player actualPlayer;
    private boolean madeMove;
    private boolean winnerSet = false;
    private boolean load = false;

    private static MazeBoard game;
    private Rock[] rock;
    private Rock freeRock;

    private BufferedImage C1, C2, C3, C4;
    private BufferedImage L1, L2;
    private BufferedImage F1, F2, F3, F4;
    private BufferedImage deckFree;
    private BufferedImage deckPlayer;
    private BufferedImage deckScore;
    private ImageIcon[] arrowIco;
    private JButton[] arrowBtn;

    private JTextPane scorePanel;
    private JTextField move =  new JTextField();

    /**
     * Vytvorí nový panel, ktorý bude obsahovať celú hru.
     * Spustí inicializáciu novej hry.
     * Po celkovom načítaní sa hra vykreslí na obrazovku.
     *
     * @param bs velkosť hracej plochy
     * @param pn počet hráčov
     * @param cn počet hracích kariet
     * @param names mená hráčov
     * @param gameIn obsahuje obsah hry pri load game, inak je to null
     */
    public CreateBoardUI(int bs, int pn, int cn, String[] names, MazeBoard gameIn) {
        setSize(1045, 700);
        setBackground(new Color(0, 0, 0, 0));

        load = false;
        this.boardSize = bs;
        this.playersNum = pn;
        this.playerNames = names;
        this.cardsNum = cn;

        if (gameIn == null){
            game = MazeBoard.createMazeBoard(boardSize);
            game.newGame();
            game.createPack(cardsNum);
            addPlayers();
        } else {
            game = gameIn;
            for(int i = 0; i < this.playersNum; i++){
                playerNames = new String[playersNum];
                playerNames[i] = game.getPlayer(i).getName();
                System.out.println(game.getPlayer(i).getName());
            }
            getPlayers();
            load = true;
        }

        GameData.initBuffer(5);

        getRock();
        getImages();
        createButtons();
        createButtonsArray();

        getPlayerOnTurn();
        createScorePanel();
        showWhatToDo(1);

        if(!load){GameData.store(game);}
        game.print();

        setKeys();
        writeHelp();

        setFocusable(true);
        requestFocusInWindow();
        setOpaque(false);
        setVisible(true);
        setLayout(null);
    }

    /**
     * Vykreslí celú hru: hraciu dosku, všetky kamene, voľný kameň, hráčov, aktuálnu kartu.
     * @param g graphics  potrebné pre vykreslenie obrázkov
     */
    @Override
    protected void paintComponent(Graphics g) {

        int blockSize = 0, maxSize = 0, xPoint = 0, yPoint = 0;
        if (this.boardSize == 5) {
            blockSize = 70;
            maxSize = 600;
            xPoint = 270;
            yPoint = 170;
        } else if (this.boardSize == 7) {
            blockSize = 70;
            maxSize = 650;
            xPoint = 198;
            yPoint = 98;
        } else if (this.boardSize == 9) {
            blockSize = 50;
            maxSize = 640;
            xPoint = 216;
            yPoint = 116;
        } else if (this.boardSize == 11) {
            blockSize = 50;
            maxSize = 690;
            xPoint = 164;
            yPoint = 64;
        }

        g.drawImage(deckFree, 820, 10, 160, 160, this);
        g.drawImage(this.freeRock.icon(), 865, 54, 70, 70, this);

        g.drawImage(deckFree, 820, 160, 160, 160, this);
        if(game.getPlayer(game.getTurn()).getCard() != null){
            g.drawImage(game.getPlayer(game.getTurn()).getCard().getCardIcon(), 865, 204, 70, 70, this);
        }

        g.drawImage(deckPlayer, 865, 335, 70, 70, this);
        g.drawImage(game.getPlayer(game.getTurn()).getIcon(), 865, 335, 70, 70, this);

        g.drawImage(deckScore, 833, 433, 140, 95, this);

        int i = 0;
        int x,y;
        for (int r = 1; r <= this.boardSize; r++) {
            for (int c = 1; c <= this.boardSize; c++) {

                i++;
                g.drawImage(this.rock[i].icon(), xPoint, yPoint, blockSize, blockSize, this);

                for(int pl = 0; pl < this.playersNum; pl++){
                    if (p[pl] != actualPlayer){
                        y = p[pl].getCol();
                        x = p[pl].getRow();

                        if (x == r && y == c){
                            g.drawImage(p[pl].getIcon(),xPoint,yPoint,blockSize,blockSize,this);
                        }
                    }
                }

                for(int pa = 0; pa < game.getPlayerCount(); pa++ ){
                    if(game.getPlayer(pa).getCard() != null){
                        if(game.getPlayer(pa).getCard().getLocation().row() == r &&
                                game.getPlayer(pa).getCard().getLocation().col() == c){
                            g.drawImage(game.getPlayer(pa).getCard().getCardIcon(), xPoint, yPoint, blockSize, blockSize, this );
                        }
                    }
                }


                for(int ca = 0; ca < game.getPack().getSize(); ca++){
                    if(game.getPlayer(ca).getCard() != null){
                        if(game.getPack().getCard(ca).getLocation().row() == r &&
                                game.getPack().getCard(ca).getLocation().col() == c){

                            g.drawImage(game.getPack().getCard(ca).getCardIcon(), xPoint, yPoint, blockSize, blockSize, this );
                        }
                    }
                }

                for(int pl = 0; pl < this.playersNum; pl++){
                    if (p[pl] == actualPlayer){
                        y = p[pl].getCol();
                        x = p[pl].getRow();

                        if (x == r && y == c){
                            g.drawImage(p[pl].getIcon(),xPoint,yPoint,blockSize,blockSize,this);
                        }
                    }
                }

                xPoint += blockSize + 2;
                if (xPoint > maxSize) {
                    if (this.boardSize == 5) { xPoint = 270; }
                    if (this.boardSize == 7) { xPoint = 198; }
                    if (this.boardSize == 9) { xPoint = 216; }
                    if (this.boardSize == 11) { xPoint = 164; }
                    yPoint += blockSize + 2;
                }
            }
        }
    }

    /**
     * Pridá potrebný počet hráčov podľa toho ako sa zadali pri vytváraní novej hry.
     * Každému hráčovi sa pridá meno, obrázok a presunieho na potrebné miesto.
     */
    private void addPlayers(){

        p = new Player[this.playersNum];
        for ( int i=0; i < this.playersNum; i++){
            game.addPlayer(playerNames[i]);
            this.p[i] = game.getPlayer(i);
            this.p[i].makeIcon();
            if(i == 1){ p[i].moveTo(1,this.boardSize);}
            if(i == 2){ p[i].moveTo(this.boardSize, 1);}
            if(i == 3){ p[i].moveTo(this.boardSize, this.boardSize);}
        }
    }

    /**
     * Získa potrebných hráčov z načítanej hry.
     * Každému pridá meno, obrázok a presunieho na potrebné miesto.
     */
    private void getPlayers(){

        p = new Player[this.playersNum];
        for ( int i=0; i < this.playersNum; i++){
            this.p[i] = game.getPlayer(i);
            this.p[i].makeIcon();
        }
    }

    /**
     * Získa aktuálneho háča, ktorý je na ťahu.
     */
    private void getPlayerOnTurn(){
        this.actualPlayer = game.nextTurn();
        this.madeMove = false;
    }

    /**
     * Presunie hráča na zadané políčko.
     * Po potvrdení ťahu urobí zálohu aktuálnej hry pre krok UNDO.
     * @param r číslo riadka
     * @param c číslo stĺpca
     */
    private void movePlayer(int r, int c){
        if(this.madeMove){
            showWhatToDo(2);
            if(!actualPlayer.isTakeCard()){
                if(this.actualPlayer.canMove(r, c)){
                    this.actualPlayer.moveTo(r, c);
                    repaint();
                }
            } else {
                GameData.store(game);
                if(!getScore()){
                    GameData.store(game);
                    getPlayerOnTurn();
                    showWhatToDo(1);
                    getRock();
                    repaint();
                    game.print();
                    actualPlayer.setTakeCard();
                }
            }
        }
    }

    /**
     * Ak je možné, tak posunie riadok alebo stĺpec.
     * Po presunutí prekreslí obrazovku.
     * @param r číslo riadku
     * @param c číslo stĺpca
     */
    private void moveLine(int r, int c){
        if(!this.madeMove){
            MazeField mf = game.get(r, c);
            System.out.print(mf+"**\n");
            game.shift(mf);

            showWhatToDo(2);
            getRock();
            repaint();
            this.madeMove = true;
        }
    }

    /**
     * Otočí voľný kameň.
     * Prekreslí aktuálnu obrazovku.
     */
    private void rotateFreeRock() {
        game.rotateFreeCard();
        getRock();
        repaint();
    }

    /**
     * Nastaví tlačítkam nad hracou plochou ich súradnice.
     * Využitie: hráč otestuje podľa súracníc, či sa môže na dané miesto presunúť.
     */
    private class ArrBtn {
        public int row;
        public int col;
        public JButton arrBtnNum;

        public ArrBtn(int row, int col, JButton arrBtnNum){
            this.row = row;
            this.col = col;
            this.arrBtnNum = arrBtnNum;
        }

        public int getRow(){ return this.row; }
        public int getCol(){ return this.col; }
        public void makeActionBtn(){
            JButton test = this.arrBtnNum;

            test.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int r = getRow();
                    int c = getCol();
                    movePlayer(r, c);
                }
            });
        }
    }


    /**
     * Vytvorí kameň ako objekt kvôli lepšiemu spracovaniu.
     * Ukladá si jeho polohu, typ a smer otočenia.
     */
    private class Rock {
        public float x;
        public float y;
        public MazeCard type;
        public int rotation;

        public Rock(float x, float y, MazeCard type, int rotation) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.rotation = rotation;
        }

        public MazeCard type() { return this.type; }
        public float x() { return this.x; }
        public float y() { return this.y;  }

        public BufferedImage icon() {
            switch (this.type.getType()) {
                case "L":
                    if (this.rotation == 0) { return L1; }
                    if (this.rotation == 1) { return L2; }
                    if (this.rotation == 2) { return L1; }
                    if (this.rotation == 3) { return L2; }

                case "C":
                    if (this.rotation == 0) { return C1; }
                    if (this.rotation == 1) { return C2; }
                    if (this.rotation == 2) { return C3; }
                    if (this.rotation == 3) { return C4; }

                case "F":
                    if (this.rotation == 0) { return F1; }
                    if (this.rotation == 1) { return F2; }
                    if (this.rotation == 2) { return F3; }
                    if (this.rotation == 3) { return F4; }
            }
            return L1; // Nemoze nastat - nahodne zvoleny obrazok
        }
    }

    /**
     * Získa informácie o vytvorených kameňoch.
     * Každý kameň sa vytvorí ako objekt.
     * Uloží sa jeho tvar, poloha a smer otočenia.
     */
    private void getRock() {

        this.rock = new Rock[this.boardSize * this.boardSize + 1];

        MazeCard tmpCard = game.getFreeCard();
        this.freeRock = new Rock(0,0, tmpCard, tmpCard.getRotation());

        int i = 0;
        for (int r = 1; r <= this.boardSize; r++) {
            for (int c = 1; c <= this.boardSize; c++) {

                tmpCard = game.get(r, c).getCard();

                i++;
                this.rock[i] = new Rock(r, c, tmpCard, tmpCard.getRotation());
            }
        }
    }

    /**
     * Vypíše skóre a meno hráča.
     * Aktuálny hráč je vypísaný inou farbou.
     * Overuje či hráč nedosiahol požadované skóre.
     */
    private boolean getScore(){

        boolean isWinner = false;

        SimpleAttributeSet go = new SimpleAttributeSet();
        StyleConstants.setFontFamily(go, "Verdana");
        StyleConstants.setForeground(go, new Color(0xFDA820));

        SimpleAttributeSet goNo = new SimpleAttributeSet();
        StyleConstants.setFontFamily(goNo, "Verdana");
        StyleConstants.setForeground(goNo, new Color(0xFFFFFF));

        Player winner = null;
        boolean end = false;
        this.scorePanel.setText("");
        for(int i=0; i < this.playersNum; i++ ){
            int score = this.p[i].getScore();
            int scoreToWin = this.cardsNum/this.playersNum;
            if(p[i].getName() == this.actualPlayer.getName()){
                System.out.print("ide "+actualPlayer.getName());
                int size = this.scorePanel.getDocument().getLength();
                try {
                    this.scorePanel.getDocument().insertString(size,"  "+score+"/"+scoreToWin+" "+this.p[i].getName()+"\n",go);
                } catch (Exception e) {}
            } else {
                int size = this.scorePanel.getDocument().getLength();
                try {
                    this.scorePanel.getDocument().insertString(size,"  "+score+"/"+scoreToWin+" "+this.p[i].getName()+"\n",goNo);
                } catch (Exception e) {}
            }
            System.out.println(score+" "+this.cardsNum+" "+this.playersNum);
            if(score == this.cardsNum/this.playersNum){
                end = true;
                winner = this.actualPlayer;
            }
        }
        if (end){
            gameOver(winner);
            return isWinner = true;
        }

        return isWinner;
    }

    /**
     * Nastaví funkcie na konkrétne tlačítka
     * R - otočí voľný kameň
     * Q - ukonči hru
     * U - krok vzad
     * I - krok vpred
     * P - uloží aktuálnu hru do zvoleného súboru
     * W/A/S/D - posun hráča
     * E - ukonči ťah
     */
    private void setKeys() {

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rotate");
        getActionMap().put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateFreeRock();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "quit");
        getActionMap().put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Naozaj chcete ukoncit hru?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {/* Vrati do hry*/ }
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "save");
        getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser saveFile = new JFileChooser();
                saveFile.setDialogTitle("Uložiť hru");

                int selectFile = saveFile.showSaveDialog(null);
                if (selectFile == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = saveFile.getSelectedFile();  //Subor kde sa bude ukladat
                    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                    saveGameSettings(fileToSave);
                }

            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0), "undo");
        getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                undoMove();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "redo");
        getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                redoMove();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "win");
        getActionMap().put("win", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                gameOver(actualPlayer);
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "go_up");
        getActionMap().put("go_up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                playerGoUp();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "go_down");
        getActionMap().put("go_down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                playerGoDown();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "go_left");
        getActionMap().put("go_left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                playerGoLeft();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "go_right");
        getActionMap().put("go_right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                playerGoRight();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "takeOk");
        getActionMap().put("takeOk", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                endTurn();
            }
        });
    }

    /**
     * Uloží hru do zvoleného súboru.
     * @param save súbor kde sa hra uloží
     */
    private void saveGameSettings(File save){
        if (GameData.save(game, save)) System.out.println("Save OK");
        else System.out.println("Save FAILED");
    }

    /**
     * Urobí krok vzad podľa zálohovanej hry.
     * Načíta rozloženie kameňov, hráčov a kariet.
     * Prekreslí aktuálnu obrazovku za novú.
     */
    private void undoMove(){
        game = GameData.undo(game);
        getPlayers();
        getRock();
        this.actualPlayer = game.getPlayer(game.getTurn());
        getScore();
        repaint();
        game.print();
    }

    /**
     * Urobí krok vpred zo zálohovanej hry.
     * Načíta rozloženie kameňov, hráčov a kariet.
     * Prekreslí aktuálnu obrazovku za novú.
     */
    private void redoMove(){
        game = GameData.redo(game);
        getRock();
        getPlayers();
        this.actualPlayer = game.getPlayer(game.getTurn());
        getScore();
        repaint();
        game.print();
    }

    /**
     * Presunie hráča hore, ak je to možné.
     */
    private void playerGoUp(){
        for(int pl = 0; pl < this.playersNum; pl++){
            if (p[pl] == actualPlayer){
                int y = p[pl].getCol();
                int x = p[pl].getRow();
                movePlayer(x - 1, y);
            }
        }
    }

    /**
     * Presunie hráča dolu, ak je to možné.
     */
    private void playerGoDown(){
        for(int pl = 0; pl < this.playersNum; pl++){
            if (p[pl] == actualPlayer){
                int y = p[pl].getCol();
                int x = p[pl].getRow();
                movePlayer(x+1, y);
            }
        }
    }

    /**
     * Presunie hráča do ľava, ak je to možné.
     */
    private void playerGoLeft(){
        for(int pl = 0; pl < this.playersNum; pl++){
            if (p[pl] == actualPlayer){
                int y = p[pl].getCol();
                int x = p[pl].getRow();
                movePlayer(x, y-1);
            }
        }
    }

    /**
     * Presunie hráča do ľava, ak je to možné.
     */
    private void playerGoRight(){
        for(int pl = 0; pl < this.playersNum; pl++){
            if (p[pl] == actualPlayer){
                int y = p[pl].getCol();
                int x = p[pl].getRow();
                movePlayer(x, y + 1);
            }
        }
    }

    /**
     * Ukončí ťah aktuálneho hráča.
     */
    private void endTurn(){
        if(!getScore()){
            GameData.store(game);
            getPlayerOnTurn();
            getRock();
            repaint();
            game.print();
            showWhatToDo(1);
            actualPlayer.setTakeCard();
        }
    }

    /**
     * V rohu obrazovky vypíše, čo ma aktuálny hráč urobiť.
     * @param doWhat čo treba urobiť 1 - rozloženie; 2 - posun hráča
     */
    private void showWhatToDo(int doWhat){
        Font font = new Font("Verdana", Font.BOLD, 14);
        this.move.setText("");
        if (doWhat == 1){
            this.move.setText("Krok: zmeň rozloženie bludiska.");
        } else if (doWhat == 2){
            this.move.setText("Krok: posuň postavičku. (WASD/myš)");
        }
        this.move.setBounds(5,1,400,25);
        this.move.setFont(font);
        this.move.setForeground(new Color(0xFFFFFF));
        this.move.setBackground(new Color(0x000000));
        this.move.setEditable(false);
        this.move.setBorder(null);
        this.add(this.move);
    }

    /**
     * Otvorí nové okno s obrázkom a menom výhercu.
     * @param winner meno výhercu
     */
    private void gameOver(Player winner){
        if(!this.winnerSet){
            this.winnerSet = true;
            ImageIcon winIco = new ImageIcon(winner.getIcon());
            Object[] options = {"OK"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Víťaz je : " + winner.getName(), "Koniec hry",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.QUESTION_MESSAGE, winIco,
                    options, options[0]);

            JFrame top = (JFrame) getTopLevelAncestor();
            System.out.print(top);
            top.dispose();

            GameUI newgame = new GameUI();
            newgame.mainWindow();
        }
    }

    /**
     * Vytvorí text Score a Hráč na ťahu.
     * Pripraví panel na vypisovanie skóre.
     */
    private void createScorePanel() {

        Font font = new Font("Verdana", Font.BOLD, 15);
        Font fontPlayer = new Font("Verdana", Font.BOLD, 14);

        JTextArea whoGo = new JTextArea("Hráč na ťahu: ");
        whoGo.setFont(font);
        whoGo.setOpaque(false);
        whoGo.setEditable(false);
        whoGo.setBounds(833, 305, 140, 23);
        whoGo.setForeground(new Color(0xD74E00));
        this.add(whoGo);

        JTextArea scoreP = new JTextArea("Score: ");
        scoreP.setFont(font);
        scoreP.setOpaque(false);
        scoreP.setEditable(false);
        scoreP.setBounds(833, 407, 140, 23);
        scoreP.setForeground(new Color(0xD74E00));
        this.add(scoreP);

        this.scorePanel = new JTextPane();
        this.scorePanel.setBounds(833, 435, 140, 90);
        this.scorePanel.setEditable(false);
        this.scorePanel.setOpaque(false);
        this.add(this.scorePanel);


        this.scorePanel.setForeground(new Color(0xFFFFFF));
        this.scorePanel.setFont(fontPlayer);

        getScore();
    }

    /**
     * Ukáže nápovedu k hre v dolnom rohu hry.
     * Obsahuje ovládanie hry.
     */
    private void writeHelp(){
        Font fontH = new Font("Arial", Font.BOLD, 14);

        JTextArea helpP = new JTextArea("NÁPOVEDA: \n");
        helpP.setBounds(830, 535, 210, 150);
        helpP.setEditable(false);
        helpP.setFont(fontH);
        helpP.setOpaque(false);
        helpP.setForeground(new Color(0x4C4C4C));
        helpP.append("R - otočí voľný kameň\n");
        helpP.append("E - ukončí ťah\n");
        helpP.append("P - uloží hru\n");
        helpP.append("U - krok späť\n");
        helpP.append(" I - krok vpred\n");
        helpP.append("Q - ukončí hru\n");
        this.add(helpP);
    }

    /**
     * Vytvorí tlačítka nad hraciu plochu, ktoré slúžia na presúvanie postavičiek.
     * Po kliknutí sa hráč skusí presunúť na dané miesto, ak je to možné.
     */
    private void createButtonsArray(){

        int blockSize = 0, maxSize = 0, xPoint = 0, yPoint = 0, num = 0;
        if (this.boardSize == 5) {
            blockSize = 70;
            maxSize = 600;
            xPoint = 270;
            yPoint = 170;
            num = 25;
        } else if (this.boardSize == 7) {
            blockSize = 70;
            maxSize = 650;
            xPoint = 198;
            yPoint = 98;
            num = 49;
        } else if (this.boardSize == 9) {
            blockSize = 50;
            maxSize = 640;
            xPoint = 216;
            yPoint = 116;
            num = 81;
        } else if (this.boardSize == 11) {
            blockSize = 50;
            maxSize = 690;
            xPoint = 164;
            yPoint = 64;
            num = 121;
        }

        ArrBtn[] arrBtn = new ArrBtn[num];
        JButton[] arrayBtn = new JButton[num];
        int i = 0;
        for (int r = 1; r <= this.boardSize; r++) {
            for (int c = 1; c <= this.boardSize; c++) {


                arrayBtn[i] = new JButton();
                arrayBtn[i].setBounds(xPoint, yPoint, blockSize, blockSize);
                arrayBtn[i].setBorderPainted(false);
                arrayBtn[i].setOpaque(false);
                arrayBtn[i].setContentAreaFilled(false);

                arrBtn[i] = new ArrBtn(r, c, arrayBtn[i]);
                arrBtn[i].makeActionBtn();

                this.add(arrayBtn[i]);
                i++;

                xPoint += blockSize + 2;
                if (xPoint > maxSize) {
                    if (this.boardSize == 5) { xPoint = 270; }
                    if (this.boardSize == 7) { xPoint = 198; }
                    if (this.boardSize == 9) { xPoint = 216; }
                    if (this.boardSize == 11) { xPoint = 164; }
                    yPoint += blockSize + 2;
                }
            }
        }
    }

    /**
     * Vytvorí tlačítka okolo hracej plochy a nastaví ActionEvent pre každe jedno.
     * Po kliknutí na šípku sa pounie riadok alebo stĺpec v hre.
     */
    private void createButtons() {
        final JButton rotateFree = new JButton();
        rotateFree.setBounds(865, 54, 70, 70);
        rotateFree.setBorderPainted(false);
        rotateFree.setOpaque(false);
        rotateFree.setContentAreaFilled(false);
        rotateFree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateFreeRock();
            }
        });
        this.add(rotateFree);

        final JButton endTurn = new JButton();
        endTurn.setBounds(865, 335, 70, 70);
        endTurn.setBorderPainted(false);
        endTurn.setOpaque(false);
        endTurn.setContentAreaFilled(false);
        endTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurn();
            }
        });
        this.add(endTurn);


        int num =0;
        if (this.boardSize == 5) {
            num = 8;

            this.arrowBtn = new JButton[num];

            this.arrowBtn[0] = new JButton();
            this.arrowBtn[0].setIcon(arrowIco[7]);
            this.arrowBtn[0].setRolloverIcon(arrowIco[8]);
            this.arrowBtn[0].setBounds(350, 115, 50, 50);
            this.arrowBtn[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 2);
                }
            });

            this.arrowBtn[1] = new JButton();
            this.arrowBtn[1].setIcon(arrowIco[7]);
            this.arrowBtn[1].setRolloverIcon(arrowIco[8]);
            this.arrowBtn[1].setBounds(495, 115, 50, 50);
            this.arrowBtn[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 4);
                }
            });

            this.arrowBtn[2] = new JButton();
            this.arrowBtn[2].setIcon(arrowIco[1]);
            this.arrowBtn[2].setRolloverIcon(arrowIco[2]);
            this.arrowBtn[2].setBounds(632, 250, 50, 50);
            this.arrowBtn[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 5);
                }
            });

            this.arrowBtn[3] = new JButton();
            this.arrowBtn[3].setIcon(arrowIco[1]);
            this.arrowBtn[3].setRolloverIcon(arrowIco[2]);
            this.arrowBtn[3].setBounds(632, 395, 50, 50);
            this.arrowBtn[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4,5);
                }
            });

            this.arrowBtn[4] = new JButton();
            this.arrowBtn[4].setIcon(arrowIco[5]);
            this.arrowBtn[4].setRolloverIcon(arrowIco[6]);
            this.arrowBtn[4].setBounds(495, 533, 50, 50);
            this.arrowBtn[4].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(5, 4);
                }
            });

            this.arrowBtn[5] = new JButton();
            this.arrowBtn[5].setIcon(arrowIco[5]);
            this.arrowBtn[5].setRolloverIcon(arrowIco[6]);
            this.arrowBtn[5].setBounds(350, 533, 50, 50);
            this.arrowBtn[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(5, 2);
                }
            });

            this.arrowBtn[6] = new JButton();
            this.arrowBtn[6].setIcon(arrowIco[3]);
            this.arrowBtn[6].setRolloverIcon(arrowIco[4]);
            this.arrowBtn[6].setBounds(216, 395, 50, 50);
            this.arrowBtn[6].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 1);
                }
            });

            this.arrowBtn[7] = new JButton();
            this.arrowBtn[7].setIcon(arrowIco[3]);
            this.arrowBtn[7].setRolloverIcon(arrowIco[4]);
            this.arrowBtn[7].setBounds(216, 250, 50, 50);
            this.arrowBtn[7].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 1);
                }
            });
        }

        if (this.boardSize == 7) {
            num = 12;

            this.arrowBtn = new JButton[num];

            this.arrowBtn[0] = new JButton();
            this.arrowBtn[0].setIcon(arrowIco[7]);
            this.arrowBtn[0].setRolloverIcon(arrowIco[8]);
            this.arrowBtn[0].setBounds(280, 40, 50, 50);
            this.arrowBtn[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 2);
                }
            });

            this.arrowBtn[1] = new JButton();
            this.arrowBtn[1].setIcon(arrowIco[7]);
            this.arrowBtn[1].setRolloverIcon(arrowIco[8]);
            this.arrowBtn[1].setBounds(423, 40, 50, 50);
            this.arrowBtn[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 4);
                }
            });

            this.arrowBtn[2] = new JButton();
            this.arrowBtn[2].setIcon(arrowIco[7]);
            this.arrowBtn[2].setRolloverIcon(arrowIco[8]);
            this.arrowBtn[2].setBounds(568, 40, 50, 50);
            this.arrowBtn[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 6);
                }
            });

            this.arrowBtn[3] = new JButton();
            this.arrowBtn[3].setIcon(arrowIco[1]);
            this.arrowBtn[3].setRolloverIcon(arrowIco[2]);
            this.arrowBtn[3].setBounds(705, 180, 50, 50);
            this.arrowBtn[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 7);
                }
            });

            this.arrowBtn[4] = new JButton();
            this.arrowBtn[4].setIcon(arrowIco[1]);
            this.arrowBtn[4].setRolloverIcon(arrowIco[2]);
            this.arrowBtn[4].setBounds(705, 325, 50, 50);
            this.arrowBtn[4].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 7);
                }
            });

            this.arrowBtn[5] = new JButton();
            this.arrowBtn[5].setIcon(arrowIco[1]);
            this.arrowBtn[5].setRolloverIcon(arrowIco[2]);
            this.arrowBtn[5].setBounds(705, 467, 50, 50);
            this.arrowBtn[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 7);
                }
            });

            this.arrowBtn[6] = new JButton();
            this.arrowBtn[6].setIcon(arrowIco[5]);
            this.arrowBtn[6].setRolloverIcon(arrowIco[6]);
            this.arrowBtn[6].setBounds(568, 605, 50, 50);
            this.arrowBtn[6].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(7, 6);
                }
            });

            this.arrowBtn[7] = new JButton();
            this.arrowBtn[7].setIcon(arrowIco[5]);
            this.arrowBtn[7].setRolloverIcon(arrowIco[6]);
            this.arrowBtn[7].setBounds(423, 605, 50, 50);
            this.arrowBtn[7].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(7, 4);
                }
            });

            this.arrowBtn[8] = new JButton();
            this.arrowBtn[8].setIcon(arrowIco[5]);
            this.arrowBtn[8].setRolloverIcon(arrowIco[6]);
            this.arrowBtn[8].setBounds(280, 605, 50, 50);
            this.arrowBtn[8].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(7, 2);
                }
            });

            this.arrowBtn[9] = new JButton();
            this.arrowBtn[9].setIcon(arrowIco[3]);
            this.arrowBtn[9].setRolloverIcon(arrowIco[4]);
            this.arrowBtn[9].setBounds(143, 467, 50, 50);
            this.arrowBtn[9].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 1);
                }
            });

            this.arrowBtn[10] = new JButton();
            this.arrowBtn[10].setIcon(arrowIco[3]);
            this.arrowBtn[10].setRolloverIcon(arrowIco[4]);
            this.arrowBtn[10].setBounds(143, 325, 50, 50);
            this.arrowBtn[10].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 1);
                }
            });

            this.arrowBtn[11] = new JButton();
            this.arrowBtn[11].setIcon(arrowIco[3]);
            this.arrowBtn[11].setRolloverIcon(arrowIco[4]);
            this.arrowBtn[11].setBounds(143, 180, 50, 50);
            this.arrowBtn[11].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 1);
                }
            });
        }

        if (this.boardSize == 9) {
            num = 16;

            this.arrowBtn = new JButton[num];

            this.arrowBtn[0] = new JButton();
            this.arrowBtn[0].setIcon(arrowIco[15]);
            this.arrowBtn[0].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[0].setBounds(278, 80, 30, 30);
            this.arrowBtn[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1,2);
                }
            });

            this.arrowBtn[1] = new JButton();
            this.arrowBtn[1].setIcon(arrowIco[15]);
            this.arrowBtn[1].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[1].setBounds(382, 80, 30, 30);
            this.arrowBtn[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 4);
                }
            });

            this.arrowBtn[2] = new JButton();
            this.arrowBtn[2].setIcon(arrowIco[15]);
            this.arrowBtn[2].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[2].setBounds(486, 80, 30, 30);
            this.arrowBtn[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 6);
                }
            });

            this.arrowBtn[3] = new JButton();
            this.arrowBtn[3].setIcon(arrowIco[15]);
            this.arrowBtn[3].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[3].setBounds(590, 80, 30, 30);
            this.arrowBtn[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1,8);
                }
            });

            this.arrowBtn[4] = new JButton();
            this.arrowBtn[4].setIcon(arrowIco[9]);
            this.arrowBtn[4].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[4].setBounds(687, 177, 30, 30);
            this.arrowBtn[4].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 9);
                }
            });

            this.arrowBtn[5] = new JButton();
            this.arrowBtn[5].setIcon(arrowIco[9]);
            this.arrowBtn[5].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[5].setBounds(687, 282, 30, 30);
            this.arrowBtn[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 9);
                }
            });

            this.arrowBtn[6] = new JButton();
            this.arrowBtn[6].setIcon(arrowIco[9]);
            this.arrowBtn[6].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[6].setBounds(687, 385, 30, 30);
            this.arrowBtn[6].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 9);
                }
            });

            this.arrowBtn[7] = new JButton();
            this.arrowBtn[7].setIcon(arrowIco[9]);
            this.arrowBtn[7].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[7].setBounds(687, 489, 30, 30);
            this.arrowBtn[7].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(8, 9);
                }
            });

            this.arrowBtn[8] = new JButton();
            this.arrowBtn[8].setIcon(arrowIco[13]);
            this.arrowBtn[8].setRolloverIcon(arrowIco[14]);
            this.arrowBtn[8].setBounds(590, 590, 30, 30);
            this.arrowBtn[8].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(9, 8);
                }
            });

            this.arrowBtn[9] = new JButton();
            this.arrowBtn[9].setIcon(arrowIco[13]);
            this.arrowBtn[9].setRolloverIcon(arrowIco[13]);
            this.arrowBtn[9].setBounds(486, 590, 30, 30);
            this.arrowBtn[9].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(9, 6);
                }
            });

            this.arrowBtn[10] = new JButton();
            this.arrowBtn[10].setIcon(arrowIco[13]);
            this.arrowBtn[10].setRolloverIcon(arrowIco[14]);
            this.arrowBtn[10].setBounds(382, 590, 30, 30);
            this.arrowBtn[10].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(9, 4);
                }
            });

            this.arrowBtn[11] = new JButton();
            this.arrowBtn[11].setIcon(arrowIco[13]);
            this.arrowBtn[11].setRolloverIcon(arrowIco[14]);
            this.arrowBtn[11].setBounds(278, 590, 30, 30);
            this.arrowBtn[11].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(9, 2);
                }
            });

            this.arrowBtn[12] = new JButton();
            this.arrowBtn[12].setIcon(arrowIco[11]);
            this.arrowBtn[12].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[12].setBounds(182, 489, 30, 30);
            this.arrowBtn[12].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(8, 1);
                }
            });

            this.arrowBtn[13] = new JButton();
            this.arrowBtn[13].setIcon(arrowIco[11]);
            this.arrowBtn[13].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[13].setBounds(182, 385, 30, 30);
            this.arrowBtn[13].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 1);
                }
            });

            this.arrowBtn[14] = new JButton();
            this.arrowBtn[14].setIcon(arrowIco[11]);
            this.arrowBtn[14].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[14].setBounds(182, 282, 30, 30);
            this.arrowBtn[14].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 1);
                }
            });

            this.arrowBtn[15] = new JButton();
            this.arrowBtn[15].setIcon(arrowIco[11]);
            this.arrowBtn[15].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[15].setBounds(182, 177, 30, 30);
            this.arrowBtn[15].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 1);
                }
            });
        }

        if (this.boardSize == 11) {
            num = 20;

            this.arrowBtn = new JButton[num];

            this.arrowBtn[0] = new JButton();
            this.arrowBtn[0].setIcon(arrowIco[15]);
            this.arrowBtn[0].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[0].setBounds(227, 30, 30, 30);
            this.arrowBtn[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 2);
                }
            });

            this.arrowBtn[1] = new JButton();
            this.arrowBtn[1].setIcon(arrowIco[15]);
            this.arrowBtn[1].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[1].setBounds(331, 30, 30, 30);
            this.arrowBtn[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 4);
                }
            });

            this.arrowBtn[2] = new JButton();
            this.arrowBtn[2].setIcon(arrowIco[15]);
            this.arrowBtn[2].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[2].setBounds(435, 30, 30, 30);
            this.arrowBtn[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 6);
                }
            });

            this.arrowBtn[3] = new JButton();
            this.arrowBtn[3].setIcon(arrowIco[15]);
            this.arrowBtn[3].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[3].setBounds(539, 30, 30, 30);
            this.arrowBtn[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 8);
                }
            });

            this.arrowBtn[4] = new JButton();
            this.arrowBtn[4].setIcon(arrowIco[15]);
            this.arrowBtn[4].setRolloverIcon(arrowIco[16]);
            this.arrowBtn[4].setBounds(642, 30, 30, 30);
            this.arrowBtn[4].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 10);
                }
            });

            this.arrowBtn[5] = new JButton();
            this.arrowBtn[5].setIcon(arrowIco[9]);
            this.arrowBtn[5].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[5].setBounds(737, 125, 30, 30);
            this.arrowBtn[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 11);
                }
            });

            this.arrowBtn[6] = new JButton();
            this.arrowBtn[6].setIcon(arrowIco[9]);
            this.arrowBtn[6].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[6].setBounds(737, 229, 30, 30);
            this.arrowBtn[6].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 11);
                }
            });

            this.arrowBtn[7] = new JButton();
            this.arrowBtn[7].setIcon(arrowIco[9]);
            this.arrowBtn[7].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[7].setBounds(737, 333, 30, 30);
            this.arrowBtn[7].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 11);
                }
            });

            this.arrowBtn[8] = new JButton();
            this.arrowBtn[8].setIcon(arrowIco[9]);
            this.arrowBtn[8].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[8].setBounds(737, 435, 30, 30);
            this.arrowBtn[8].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(8, 11);
                }
            });

            this.arrowBtn[9] = new JButton();
            this.arrowBtn[9].setIcon(arrowIco[9]);
            this.arrowBtn[9].setRolloverIcon(arrowIco[10]);
            this.arrowBtn[9].setBounds(737, 539, 30, 30);
            this.arrowBtn[9].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(10, 11);
                }
            });

            this.arrowBtn[10] = new JButton();
            this.arrowBtn[10].setIcon(arrowIco[13]);
            this.arrowBtn[10].setRolloverIcon(arrowIco[14]);
            this.arrowBtn[10].setBounds(642, 639, 30, 30);
            this.arrowBtn[10].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 10);
                }
            });

            this.arrowBtn[11] = new JButton();
            this.arrowBtn[11].setIcon(arrowIco[13]);
            this.arrowBtn[11].setRolloverIcon(arrowIco[14]);
            this.arrowBtn[11].setBounds(539, 639, 30, 30);
            this.arrowBtn[11].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 8);
                }
            });

            this.arrowBtn[12] = new JButton();
            this.arrowBtn[12].setIcon(arrowIco[13]);
            this.arrowBtn[12].setRolloverIcon(arrowIco[14]);
            this.arrowBtn[12].setBounds(435, 639, 30, 30);
            this.arrowBtn[12].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 6);
                }
            });

            this.arrowBtn[13] = new JButton();
            this.arrowBtn[13].setIcon(arrowIco[13]);
            this.arrowBtn[13].setRolloverIcon(arrowIco[14]);
            this.arrowBtn[13].setBounds(331, 639, 30, 30);
            this.arrowBtn[13].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 4);
                }
            });

            this.arrowBtn[14] = new JButton();
            this.arrowBtn[14].setIcon(arrowIco[13]);
            this.arrowBtn[14].setRolloverIcon(arrowIco[14]);
            this.arrowBtn[14].setBounds(227, 639, 30, 30);
            this.arrowBtn[14].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 2);
                }
            });

            this.arrowBtn[15] = new JButton();
            this.arrowBtn[15].setIcon(arrowIco[11]);
            this.arrowBtn[15].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[15].setBounds(130, 539, 30, 30);
            this.arrowBtn[15].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(10, 1);
                }
            });

            this.arrowBtn[16] = new JButton();
            this.arrowBtn[16].setIcon(arrowIco[11]);
            this.arrowBtn[16].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[16].setBounds(130, 435, 30, 30);
            this.arrowBtn[16].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(8, 1);
                }
            });

            this.arrowBtn[17] = new JButton();
            this.arrowBtn[17].setIcon(arrowIco[11]);
            this.arrowBtn[17].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[17].setBounds(130, 333, 30, 30);
            this.arrowBtn[17].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 1);
                }
            });

            this.arrowBtn[18] = new JButton();
            this.arrowBtn[18].setIcon(arrowIco[11]);
            this.arrowBtn[18].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[18].setBounds(130, 229, 30, 30);
            this.arrowBtn[18].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 1);
                }
            });

            this.arrowBtn[19] = new JButton();
            this.arrowBtn[19].setIcon(arrowIco[11]);
            this.arrowBtn[19].setRolloverIcon(arrowIco[12]);
            this.arrowBtn[19].setBounds(130, 125, 30, 30);
            this.arrowBtn[19].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 1);
                }
            });
        }

        for (int i = 0; i < num; i++){
            this.arrowBtn[i].setOpaque(false);
            this.arrowBtn[i].setBorderPainted(false);
            this.arrowBtn[i].setContentAreaFilled(false);
            this.add(arrowBtn[i]);
        }
    }

    /**
     * Načítanie potrebných obrázkov zo súbora.
     * Obsahuje ikony políčok a šípiek.
     */
    private void getImages() {
        try {
            this.C1 = ImageIO.read(getClass().getResource("/images/rocks/C_1_70.png"));
            this.C2 = ImageIO.read(getClass().getResource("/images/rocks/C_2_70.png"));
            this.C3 = ImageIO.read(getClass().getResource("/images/rocks/C_3_70.png"));
            this.C4 = ImageIO.read(getClass().getResource("/images/rocks/C_4_70.png"));

            this.L1 = ImageIO.read(getClass().getResource("/images/rocks/L_1_70.png"));
            this.L2 = ImageIO.read(getClass().getResource("/images/rocks/L_2_70.png"));

            this.F1 = ImageIO.read(getClass().getResource("/images/rocks/F_1_70.png"));
            this.F2 = ImageIO.read(getClass().getResource("/images/rocks/F_2_70.png"));
            this.F3 = ImageIO.read(getClass().getResource("/images/rocks/F_3_70.png"));
            this.F4 = ImageIO.read(getClass().getResource("/images/rocks/F_4_70.png"));

            this.deckFree = ImageIO.read(getClass().getResource("/images/deckFree.png"));
            this.deckPlayer = ImageIO.read(getClass().getResource("/images/playerDeck.png"));
            this.deckScore = ImageIO.read(getClass().getResource("/images/scoreDeck.png"));

            this.arrowIco = new ImageIcon[17];
            this.arrowIco[1] = new ImageIcon(getClass().getResource("/images/arrows/l_1_50.png"));
            this.arrowIco[2] = new ImageIcon(getClass().getResource("/images/arrows/l_2_50.png"));
            this.arrowIco[3] = new ImageIcon(getClass().getResource("/images/arrows/r_1_50.png"));
            this.arrowIco[4] = new ImageIcon(getClass().getResource("/images/arrows/r_2_50.png"));
            this.arrowIco[5] = new ImageIcon(getClass().getResource("/images/arrows/up_1_50.png"));
            this.arrowIco[6] = new ImageIcon(getClass().getResource("/images/arrows/up_2_50.png"));
            this.arrowIco[7] = new ImageIcon(getClass().getResource("/images/arrows/d_1_50.png"));
            this.arrowIco[8] = new ImageIcon(getClass().getResource("/images/arrows/d_2_50.png"));
            this.arrowIco[9] = new ImageIcon(getClass().getResource("/images/arrows/l_1_30.png"));
            this.arrowIco[10] = new ImageIcon(getClass().getResource("/images/arrows/l_2_30.png"));
            this.arrowIco[11] = new ImageIcon(getClass().getResource("/images/arrows/r_1_30.png"));
            this.arrowIco[12] = new ImageIcon(getClass().getResource("/images/arrows/r_2_30.png"));
            this.arrowIco[13] = new ImageIcon(getClass().getResource("/images/arrows/up_1_30.png"));
            this.arrowIco[14] = new ImageIcon(getClass().getResource("/images/arrows/up_2_30.png"));
            this.arrowIco[15] = new ImageIcon(getClass().getResource("/images/arrows/d_1_30.png"));
            this.arrowIco[16] = new ImageIcon(getClass().getResource("/images/arrows/d_2_30.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

