package ija.labyrinth.gui;

import ija.labyrinth.game.MazeBoard;
import ija.labyrinth.game.MazeCard;
import ija.labyrinth.game.MazeField;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CreateBoardUI extends JPanel {

    private int boardSize;
    private int playersNum;

    private int cardsNum;
    private static MazeBoard game;
    private ArrBtn[] arrBtn;
    private Rock[] rock;
    private Rock freeRock;

    private BufferedImage C1, C2, C3, C4;
    private BufferedImage L1, L2;
    private BufferedImage F1, F2, F3, F4;
    private BufferedImage deckFree;
    private ImageIcon[] arrowIco;
    private BufferedImage[] cardIco;
    private JButton[] arrowBtn;


    public CreateBoardUI(int bs, int pn, int cn, String[] names) {
        this.boardSize = bs;
        this.playersNum = pn;
        this.cardsNum = cn;
        String[] playerNames = names;

        game = MazeBoard.createMazeBoard(boardSize);
        game.newGame();
        game.print();

        setSize(1045, 700);
        setBackground(new Color(255, 255, 255));

        getRock();
        getImages();
        createButtons();
        createButtonsArray();
        setKeys();
        createScorePanel();
        writeHelp();

        JPanel coverLab = new JPanel();
        coverLab.setBackground(new Color(0x000000));
        coverLab.setBounds(0,0,70,70);
        this.add(coverLab);

        setFocusable(true);
        requestFocusInWindow();

        setOpaque(true);
        setVisible(true);
        setLayout(null);

    }

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

        g.drawImage(deckFree, 820, 30, 160, 160, this);
        g.drawImage(this.freeRock.icon(), 865, 74, 70, 70, this);

        g.drawImage(deckFree, 820, 180, 160, 160, this);
        g.drawImage(cardIco[5], 865, 224, 70, 70, this);

        BufferedImage charIco = null;
        try {
            charIco = ImageIO.read(getClass().getResource("/images/chars/char03.png"));
        } catch (IOException e) {e.printStackTrace();}

        g.drawImage(charIco, 865, 352, 70, 70, this);

        int i = 0;
        for (int r = 1; r <= this.boardSize; r++) {
            for (int c = 1; c <= this.boardSize; c++) {

                i++;
                g.drawImage(this.rock[i].icon(), xPoint, yPoint, blockSize, blockSize, this);

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

    // Vytvori akciu po stlaceni "neviditelneho tlacitka"
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
                    System.out.println("Stlacil si: " + r + "-" + c);
                }
            });
        }
    }

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

        public void rotate(int n) {

        }

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

    // Posunie cely stlpec alebo riadok a vlozi volny kamen
    private void moveLine(int r, int c){

        MazeField mf = game.get(r, c);
        System.out.print(mf+"**\n");
        game.shift(mf);
        game.print();

        getRock();
        repaint();
    }

    // Otoci volny kamen o 90*
    private void rotateFreeRock() {
        game.rotateFreeCard();
        getRock();
        repaint();
    }

    // Vypise hracov do Score:
    private void createScorePanel() {

        Font font = new Font("Verdana", Font.BOLD, 15);
        Font fontPlayer = new Font("Verdana", Font.BOLD, 12);

        JTextArea whoGo = new JTextArea("Hrac na tahu: ");
        whoGo.setFont(font);
        whoGo.setOpaque(false);
        whoGo.setEditable(false);
        whoGo.setBounds(833,330,140,23);
        whoGo.setForeground(new Color(0xD74E00));
        add(whoGo);

        JTextArea scoreP = new JTextArea("Score: ");
        scoreP.setFont(font);
        scoreP.setOpaque(false);
        scoreP.setEditable(false);
        scoreP.setBounds(833,430,140,23);
        scoreP.setForeground(new Color(0xD74E00));
        add(scoreP);

        JTextArea scorePanel = new JTextArea();
        scorePanel.setBounds(833, 453, 140, 80);
        scorePanel.setEditable(false);
        scorePanel.setOpaque(false);
        add(scorePanel);


        scorePanel.setForeground(new Color(0xFFFFFF));
        scorePanel.setFont(fontPlayer);

        if (this.playersNum == 2){
            scorePanel.append("  Hrac1\n");
            scorePanel.append("  Hrac2\n");
        }

        if (this.playersNum == 3){
            scorePanel.append("  Hrac1\n");
            scorePanel.append("  Hrac2\n");
            scorePanel.append("  Hrac3\n");
        }

        if (this.playersNum == 4){
            scorePanel.append("  Hrac1\n");
            scorePanel.append("  Hrac2\n");
            scorePanel.append("  Hrac3\n");
            scorePanel.append("  Hrac4\n");
        }

    }

    // Nastavi tlacitka
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
                int result = JOptionPane.showConfirmDialog(null, "Naozaj chcete ukoncit hru?","Exit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

                if(result == JOptionPane.YES_OPTION){
                    System.exit(0);
                }else{/* Vrati do hry*/ }
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "save");
        getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser saveFile = new JFileChooser();

                saveFile.showSaveDialog(null);
            }
        });
    }

    // Napise napovedu.
    private void writeHelp(){
        Font fontH = new Font("Arial", Font.BOLD, 14);

        JTextArea helpP = new JTextArea("NAPOVEDA: \n");
        helpP.setBounds(830, 565, 180, 100);
        helpP.setEditable(false);
        helpP.setFont(fontH);
        helpP.setOpaque(false);
        helpP.setForeground(new Color(0x4C4C4C));
        helpP.append("R - otoci volny kamen\n");
        helpP.append("S - ulozi hru\n");
        helpP.append("U - krok spat\n");
        helpP.append("Q - ukonci hru\n");
        this.add(helpP);
    }


    // Vytvori neviditelne tlacitka nad hracou plochou
    // Bude potrebne na pohyb panacika
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

        this.arrBtn = new ArrBtn[num];
        JButton[] arrayBtn = new JButton[num];
        int i = 0;
        for (int r = 1; r <= this.boardSize; r++) {
            for (int c = 1; c <= this.boardSize; c++) {


                arrayBtn[i] = new JButton();
                arrayBtn[i].setBounds(xPoint, yPoint, blockSize, blockSize);
                arrayBtn[i].setBorderPainted(false);
                arrayBtn[i].setOpaque(false);
                arrayBtn[i].setContentAreaFilled(false);

                this.arrBtn[i] = new ArrBtn(r, c, arrayBtn[i]);
                this.arrBtn[i].makeActionBtn();

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

    // Vytvori tlacitka okolo hracej plochy a nastavi ActionEvent
    private void createButtons() {
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
            //this.arrowBtn[i].setFocusPainted(false);
            this.arrowBtn[i].setBorderPainted(false);
            this.arrowBtn[i].setOpaque(true);
            this.arrowBtn[i].setContentAreaFilled(false);
            this.add(arrowBtn[i]);
        }

    }

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

            this.cardIco = new BufferedImage[27];
            this.cardIco[1] = ImageIO.read(getClass().getResource("/images/cards/card01.png"));
            this.cardIco[2] = ImageIO.read(getClass().getResource("/images/cards/card02.png"));
            this.cardIco[3] = ImageIO.read(getClass().getResource("/images/cards/card03.png"));
            this.cardIco[4] = ImageIO.read(getClass().getResource("/images/cards/card04.png"));
            this.cardIco[5] = ImageIO.read(getClass().getResource("/images/cards/card05.png"));
            this.cardIco[6] = ImageIO.read(getClass().getResource("/images/cards/card06.png"));
            this.cardIco[7] = ImageIO.read(getClass().getResource("/images/cards/card07.png"));
            this.cardIco[8] = ImageIO.read(getClass().getResource("/images/cards/card08.png"));
            this.cardIco[9] = ImageIO.read(getClass().getResource("/images/cards/card09.png"));
            this.cardIco[10] = ImageIO.read(getClass().getResource("/images/cards/card10.png"));
            this.cardIco[11] = ImageIO.read(getClass().getResource("/images/cards/card11.png"));
            this.cardIco[12] = ImageIO.read(getClass().getResource("/images/cards/card12.png"));
            this.cardIco[13] = ImageIO.read(getClass().getResource("/images/cards/card13.png"));
            this.cardIco[14] = ImageIO.read(getClass().getResource("/images/cards/card14.png"));
            this.cardIco[15] = ImageIO.read(getClass().getResource("/images/cards/card15.png"));
            this.cardIco[16] = ImageIO.read(getClass().getResource("/images/cards/card16.png"));
            this.cardIco[17] = ImageIO.read(getClass().getResource("/images/cards/card17.png"));
            this.cardIco[18] = ImageIO.read(getClass().getResource("/images/cards/card18.png"));
            this.cardIco[19] = ImageIO.read(getClass().getResource("/images/cards/card19.png"));
            this.cardIco[20] = ImageIO.read(getClass().getResource("/images/cards/card20.png"));
            this.cardIco[21] = ImageIO.read(getClass().getResource("/images/cards/card21.png"));
            this.cardIco[22] = ImageIO.read(getClass().getResource("/images/cards/card22.png"));
            this.cardIco[23] = ImageIO.read(getClass().getResource("/images/cards/card23.png"));
            this.cardIco[24] = ImageIO.read(getClass().getResource("/images/cards/card24.png"));
            this.cardIco[25] = ImageIO.read(getClass().getResource("/images/cards/card25.png"));
            this.cardIco[26] = ImageIO.read(getClass().getResource("/images/cards/card26.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

