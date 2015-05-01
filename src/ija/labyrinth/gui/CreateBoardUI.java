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
    private static MazeBoard game;
    private MazeCard tmpCard;
    private Rock[] rock;
    private Rock freeRock;

    private BufferedImage C1, C2, C3, C4;
    private BufferedImage L1, L2;
    private BufferedImage F1, F2, F3, F4;
    private BufferedImage deckFree;
    private ImageIcon AL1, AL2, AR1, AR2, AU1, AU2, AD1, AD2;
    private ImageIcon AL1s, AL2s, AR1s, AR2s, AU1s, AU2s, AD1s, AD2s;

    private JButton[] arrrowBtn;

    public CreateBoardUI(int Size) {
        this.boardSize = Size;

        this.game = MazeBoard.createMazeBoard(boardSize);
        this.game.newGame();
        this.game.print();

        setSize(1045, 700);
        setBackground(new Color(57, 57, 57));

        getRock();
        getImages();
        createButtons();

        JPanel coverLab = new JPanel();
        coverLab.setBackground(new Color(0x000000));
        coverLab.setBounds(0,0,50,50);
        this.add(coverLab);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "rotate");
        getActionMap().put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateFreeRock();
            }
        });

        setFocusable(true);
        requestFocusInWindow();

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

        g.drawImage(deckFree, 820, 50, 160, 160, this);
        g.drawImage(this.freeRock.icon(), 865, 94, 70, 70, this);

        g.drawImage(deckFree, 820, 200, 160, 160, this);

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

    private class Rock {
        public float x;
        public float y;
        public MazeCard type;
        public int rotation;
        public BufferedImage icon;

        public Rock(float x, float y, MazeCard type, int rotation) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.rotation = rotation;
        }

        public int rotation() { return this.rotation; }

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

        this.tmpCard = this.game.getFreeCard();
        this.freeRock = new Rock(0,0, this.tmpCard, this.tmpCard.getRotation());

        int i = 0;
        for (int r = 1; r <= this.boardSize; r++) {
            for (int c = 1; c <= this.boardSize; c++) {

                this.tmpCard = game.get(r, c).getCard();

                i++;
                this.rock[i] = new Rock(r, c, this.tmpCard, this.tmpCard.getRotation());

            }
        }
    }

    private void moveLine(int r, int c){

        MazeField mf = this.game.get(r, c);
        System.out.print(mf+"**\n");
        this.game.shift(mf);
        this.game.print();

        getRock();
        repaint();
    }

    public void rotateFreeRock() {
        this.game.rotateFreeCard();
        getRock();
        repaint();
    }


    private void createButtons() {
        int num =0;
        if (this.boardSize == 5) {
            num = 8;

            this.arrrowBtn = new JButton[num];

            this.arrrowBtn[0] = new JButton();
            this.arrrowBtn[0].setIcon(AD1);
            this.arrrowBtn[0].setRolloverIcon(AD2);
            this.arrrowBtn[0].setBounds(350, 115, 50, 50);
            this.arrrowBtn[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1,2);
                }
            });

            this.arrrowBtn[1] = new JButton();
            this.arrrowBtn[1].setIcon(AD1);
            this.arrrowBtn[1].setRolloverIcon(AD2);
            this.arrrowBtn[1].setBounds(495, 115, 50, 50);
            this.arrrowBtn[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 4);
                }
            });

            this.arrrowBtn[2] = new JButton();
            this.arrrowBtn[2].setIcon(AL1);
            this.arrrowBtn[2].setRolloverIcon(AL2);
            this.arrrowBtn[2].setBounds(632, 250, 50, 50);
            this.arrrowBtn[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 5);
                }
            });

            this.arrrowBtn[3] = new JButton();
            this.arrrowBtn[3].setIcon(AL1);
            this.arrrowBtn[3].setRolloverIcon(AL2);
            this.arrrowBtn[3].setBounds(632, 395, 50, 50);
            this.arrrowBtn[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4,5);
                }
            });

            this.arrrowBtn[4] = new JButton();
            this.arrrowBtn[4].setIcon(AU1);
            this.arrrowBtn[4].setRolloverIcon(AU2);
            this.arrrowBtn[4].setBounds(495, 533, 50, 50);
            this.arrrowBtn[4].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(5, 4);
                }
            });

            this.arrrowBtn[5] = new JButton();
            this.arrrowBtn[5].setIcon(AU1);
            this.arrrowBtn[5].setRolloverIcon(AU2);
            this.arrrowBtn[5].setBounds(350, 533, 50, 50);
            this.arrrowBtn[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(5, 2);
                }
            });

            this.arrrowBtn[6] = new JButton();
            this.arrrowBtn[6].setIcon(AR1);
            this.arrrowBtn[6].setRolloverIcon(AR2);
            this.arrrowBtn[6].setBounds(216, 395, 50, 50);
            this.arrrowBtn[6].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 1);
                }
            });

            this.arrrowBtn[7] = new JButton();
            this.arrrowBtn[7].setIcon(AR1);
            this.arrrowBtn[7].setRolloverIcon(AR2);
            this.arrrowBtn[7].setBounds(216, 250, 50, 50);
            this.arrrowBtn[7].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 1);
                }
            });
        }

        if (this.boardSize == 7) {
            num = 12;

            this.arrrowBtn = new JButton[num];

            this.arrrowBtn[0] = new JButton();
            this.arrrowBtn[0].setIcon(AD1);
            this.arrrowBtn[0].setRolloverIcon(AD2);
            this.arrrowBtn[0].setBounds(280, 40, 50, 50);
            this.arrrowBtn[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1,2);
                }
            });

            this.arrrowBtn[1] = new JButton();
            this.arrrowBtn[1].setIcon(AD1);
            this.arrrowBtn[1].setRolloverIcon(AD2);
            this.arrrowBtn[1].setBounds(423, 40, 50, 50);
            this.arrrowBtn[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 4);
                }
            });

            this.arrrowBtn[2] = new JButton();
            this.arrrowBtn[2].setIcon(AD1);
            this.arrrowBtn[2].setRolloverIcon(AD2);
            this.arrrowBtn[2].setBounds(568, 40, 50, 50);
            this.arrrowBtn[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 6);
                }
            });

            this.arrrowBtn[3] = new JButton();
            this.arrrowBtn[3].setIcon(AL1);
            this.arrrowBtn[3].setRolloverIcon(AL2);
            this.arrrowBtn[3].setBounds(705, 180, 50, 50);
            this.arrrowBtn[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2,7);
                }
            });

            this.arrrowBtn[4] = new JButton();
            this.arrrowBtn[4].setIcon(AL1);
            this.arrrowBtn[4].setRolloverIcon(AL2);
            this.arrrowBtn[4].setBounds(705, 325, 50, 50);
            this.arrrowBtn[4].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 7);
                }
            });

            this.arrrowBtn[5] = new JButton();
            this.arrrowBtn[5].setIcon(AL1);
            this.arrrowBtn[5].setRolloverIcon(AL2);
            this.arrrowBtn[5].setBounds(705, 467, 50, 50);
            this.arrrowBtn[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 7);
                }
            });

            this.arrrowBtn[6] = new JButton();
            this.arrrowBtn[6].setIcon(AU1);
            this.arrrowBtn[6].setRolloverIcon(AU2);
            this.arrrowBtn[6].setBounds(568, 605, 50, 50);
            this.arrrowBtn[6].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(7, 6);
                }
            });

            this.arrrowBtn[7] = new JButton();
            this.arrrowBtn[7].setIcon(AU1);
            this.arrrowBtn[7].setRolloverIcon(AU2);
            this.arrrowBtn[7].setBounds(423, 605, 50, 50);
            this.arrrowBtn[7].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(7, 4);
                }
            });

            this.arrrowBtn[8] = new JButton();
            this.arrrowBtn[8].setIcon(AU1);
            this.arrrowBtn[8].setRolloverIcon(AU2);
            this.arrrowBtn[8].setBounds(280, 605, 50, 50);
            this.arrrowBtn[8].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(7, 2);
                }
            });

            this.arrrowBtn[9] = new JButton();
            this.arrrowBtn[9].setIcon(AR1);
            this.arrrowBtn[9].setRolloverIcon(AR2);
            this.arrrowBtn[9].setBounds(143, 467, 50, 50);
            this.arrrowBtn[9].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 1);
                }
            });

            this.arrrowBtn[10] = new JButton();
            this.arrrowBtn[10].setIcon(AR1);
            this.arrrowBtn[10].setRolloverIcon(AR2);
            this.arrrowBtn[10].setBounds(143, 325, 50, 50);
            this.arrrowBtn[10].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 1);
                }
            });

            this.arrrowBtn[11] = new JButton();
            this.arrrowBtn[11].setIcon(AR1);
            this.arrrowBtn[11].setRolloverIcon(AR2);
            this.arrrowBtn[11].setBounds(143, 180, 50, 50);
            this.arrrowBtn[11].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 1);
                }
            });
        }

        if (this.boardSize == 9) {
            num = 16;

            this.arrrowBtn = new JButton[num];

            this.arrrowBtn[0] = new JButton();
            this.arrrowBtn[0].setIcon(AD1s);
            this.arrrowBtn[0].setRolloverIcon(AD2s);
            this.arrrowBtn[0].setBounds(278, 80, 30, 30);
            this.arrrowBtn[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1,2);
                }
            });

            this.arrrowBtn[1] = new JButton();
            this.arrrowBtn[1].setIcon(AD1s);
            this.arrrowBtn[1].setRolloverIcon(AD2s);
            this.arrrowBtn[1].setBounds(382, 80, 30, 30);
            this.arrrowBtn[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 4);
                }
            });

            this.arrrowBtn[2] = new JButton();
            this.arrrowBtn[2].setIcon(AD1s);
            this.arrrowBtn[2].setRolloverIcon(AD2s);
            this.arrrowBtn[2].setBounds(486, 80, 30, 30);
            this.arrrowBtn[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 6);
                }
            });

            this.arrrowBtn[3] = new JButton();
            this.arrrowBtn[3].setIcon(AD1s);
            this.arrrowBtn[3].setRolloverIcon(AD2s);
            this.arrrowBtn[3].setBounds(590, 80, 30, 30);
            this.arrrowBtn[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1,8);
                }
            });

            this.arrrowBtn[4] = new JButton();
            this.arrrowBtn[4].setIcon(AL1s);
            this.arrrowBtn[4].setRolloverIcon(AL2s);
            this.arrrowBtn[4].setBounds(687, 177, 30, 30);
            this.arrrowBtn[4].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 9);
                }
            });

            this.arrrowBtn[5] = new JButton();
            this.arrrowBtn[5].setIcon(AL1s);
            this.arrrowBtn[5].setRolloverIcon(AL2s);
            this.arrrowBtn[5].setBounds(687, 282, 30, 30);
            this.arrrowBtn[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 9);
                }
            });

            this.arrrowBtn[6] = new JButton();
            this.arrrowBtn[6].setIcon(AL1s);
            this.arrrowBtn[6].setRolloverIcon(AL2s);
            this.arrrowBtn[6].setBounds(687, 385, 30, 30);
            this.arrrowBtn[6].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 9);
                }
            });

            this.arrrowBtn[7] = new JButton();
            this.arrrowBtn[7].setIcon(AL1s);
            this.arrrowBtn[7].setRolloverIcon(AL2s);
            this.arrrowBtn[7].setBounds(687, 489, 30, 30);
            this.arrrowBtn[7].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(8, 9);
                }
            });

            this.arrrowBtn[8] = new JButton();
            this.arrrowBtn[8].setIcon(AU1s);
            this.arrrowBtn[8].setRolloverIcon(AU2s);
            this.arrrowBtn[8].setBounds(590, 590, 30, 30);
            this.arrrowBtn[8].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(9, 8);
                }
            });

            this.arrrowBtn[9] = new JButton();
            this.arrrowBtn[9].setIcon(AU1s);
            this.arrrowBtn[9].setRolloverIcon(AU1s);
            this.arrrowBtn[9].setBounds(486, 590, 30, 30);
            this.arrrowBtn[9].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(9, 6);
                }
            });

            this.arrrowBtn[10] = new JButton();
            this.arrrowBtn[10].setIcon(AU1s);
            this.arrrowBtn[10].setRolloverIcon(AU2s);
            this.arrrowBtn[10].setBounds(382, 590, 30, 30);
            this.arrrowBtn[10].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(9, 4);
                }
            });

            this.arrrowBtn[11] = new JButton();
            this.arrrowBtn[11].setIcon(AU1s);
            this.arrrowBtn[11].setRolloverIcon(AU2s);
            this.arrrowBtn[11].setBounds(278, 590, 30, 30);
            this.arrrowBtn[11].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(9, 2);
                }
            });

            this.arrrowBtn[12] = new JButton();
            this.arrrowBtn[12].setIcon(AR1s);
            this.arrrowBtn[12].setRolloverIcon(AR2s);
            this.arrrowBtn[12].setBounds(182, 489, 30, 30);
            this.arrrowBtn[12].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(8, 1);
                }
            });

            this.arrrowBtn[13] = new JButton();
            this.arrrowBtn[13].setIcon(AR1s);
            this.arrrowBtn[13].setRolloverIcon(AR2s);
            this.arrrowBtn[13].setBounds(182, 385, 30, 30);
            this.arrrowBtn[13].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 1);
                }
            });

            this.arrrowBtn[14] = new JButton();
            this.arrrowBtn[14].setIcon(AR1s);
            this.arrrowBtn[14].setRolloverIcon(AR2s);
            this.arrrowBtn[14].setBounds(182, 282, 30, 30);
            this.arrrowBtn[14].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 1);
                }
            });

            this.arrrowBtn[15] = new JButton();
            this.arrrowBtn[15].setIcon(AR1s);
            this.arrrowBtn[15].setRolloverIcon(AR2s);
            this.arrrowBtn[15].setBounds(182, 177, 30, 30);
            this.arrrowBtn[15].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 1);
                }
            });
        }

        if (this.boardSize == 11) {
            num = 20;

            this.arrrowBtn = new JButton[num];

            this.arrrowBtn[0] = new JButton();
            this.arrrowBtn[0].setIcon(AD1s);
            this.arrrowBtn[0].setRolloverIcon(AD2s);
            this.arrrowBtn[0].setBounds(227, 30, 30, 30);
            this.arrrowBtn[0].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1,2);
                }
            });

            this.arrrowBtn[1] = new JButton();
            this.arrrowBtn[1].setIcon(AD1s);
            this.arrrowBtn[1].setRolloverIcon(AD2s);
            this.arrrowBtn[1].setBounds(331, 30, 30, 30);
            this.arrrowBtn[1].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 4);
                }
            });

            this.arrrowBtn[2] = new JButton();
            this.arrrowBtn[2].setIcon(AD1s);
            this.arrrowBtn[2].setRolloverIcon(AD2s);
            this.arrrowBtn[2].setBounds(435, 30, 30, 30);
            this.arrrowBtn[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 6);
                }
            });

            this.arrrowBtn[3] = new JButton();
            this.arrrowBtn[3].setIcon(AD1s);
            this.arrrowBtn[3].setRolloverIcon(AD2s);
            this.arrrowBtn[3].setBounds(539, 30, 30, 30);
            this.arrrowBtn[3].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1,8);
                }
            });

            this.arrrowBtn[4] = new JButton();
            this.arrrowBtn[4].setIcon(AD1s);
            this.arrrowBtn[4].setRolloverIcon(AD2s);
            this.arrrowBtn[4].setBounds(642, 30, 30, 30);
            this.arrrowBtn[4].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(1, 10);
                }
            });

            this.arrrowBtn[5] = new JButton();
            this.arrrowBtn[5].setIcon(AL1s);
            this.arrrowBtn[5].setRolloverIcon(AL2s);
            this.arrrowBtn[5].setBounds(737, 125, 30, 30);
            this.arrrowBtn[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 11);
                }
            });

            this.arrrowBtn[6] = new JButton();
            this.arrrowBtn[6].setIcon(AL1s);
            this.arrrowBtn[6].setRolloverIcon(AL2s);
            this.arrrowBtn[6].setBounds(737, 229, 30, 30);
            this.arrrowBtn[6].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 11);
                }
            });

            this.arrrowBtn[7] = new JButton();
            this.arrrowBtn[7].setIcon(AL1s);
            this.arrrowBtn[7].setRolloverIcon(AL2s);
            this.arrrowBtn[7].setBounds(737, 333, 30, 30);
            this.arrrowBtn[7].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 11);
                }
            });

            this.arrrowBtn[8] = new JButton();
            this.arrrowBtn[8].setIcon(AL1s);
            this.arrrowBtn[8].setRolloverIcon(AL2s);
            this.arrrowBtn[8].setBounds(737, 435, 30, 30);
            this.arrrowBtn[8].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(8, 11);
                }
            });

            this.arrrowBtn[9] = new JButton();
            this.arrrowBtn[9].setIcon(AL1s);
            this.arrrowBtn[9].setRolloverIcon(AL2s);
            this.arrrowBtn[9].setBounds(737, 539, 30, 30);
            this.arrrowBtn[9].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(10, 11);
                }
            });

            this.arrrowBtn[10] = new JButton();
            this.arrrowBtn[10].setIcon(AU1s);
            this.arrrowBtn[10].setRolloverIcon(AU2s);
            this.arrrowBtn[10].setBounds(642, 639, 30, 30);
            this.arrrowBtn[10].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 10);
                }
            });

            this.arrrowBtn[11] = new JButton();
            this.arrrowBtn[11].setIcon(AU1s);
            this.arrrowBtn[11].setRolloverIcon(AU2s);
            this.arrrowBtn[11].setBounds(539, 639, 30, 30);
            this.arrrowBtn[11].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 8);
                }
            });

            this.arrrowBtn[12] = new JButton();
            this.arrrowBtn[12].setIcon(AU1s);
            this.arrrowBtn[12].setRolloverIcon(AU2s);
            this.arrrowBtn[12].setBounds(435, 639, 30, 30);
            this.arrrowBtn[12].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 6);
                }
            });

            this.arrrowBtn[13] = new JButton();
            this.arrrowBtn[13].setIcon(AU1s);
            this.arrrowBtn[13].setRolloverIcon(AU2s);
            this.arrrowBtn[13].setBounds(331, 639, 30, 30);
            this.arrrowBtn[13].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 4);
                }
            });

            this.arrrowBtn[14] = new JButton();
            this.arrrowBtn[14].setIcon(AU1s);
            this.arrrowBtn[14].setRolloverIcon(AU2s);
            this.arrrowBtn[14].setBounds(227, 639, 30, 30);
            this.arrrowBtn[14].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(11, 2);
                }
            });

            this.arrrowBtn[15] = new JButton();
            this.arrrowBtn[15].setIcon(AR1s);
            this.arrrowBtn[15].setRolloverIcon(AR2s);
            this.arrrowBtn[15].setBounds(130, 539, 30, 30);
            this.arrrowBtn[15].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(10, 1);
                }
            });

            this.arrrowBtn[16] = new JButton();
            this.arrrowBtn[16].setIcon(AR1s);
            this.arrrowBtn[16].setRolloverIcon(AR2s);
            this.arrrowBtn[16].setBounds(130, 435, 30, 30);
            this.arrrowBtn[16].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(8, 1);
                }
            });

            this.arrrowBtn[17] = new JButton();
            this.arrrowBtn[17].setIcon(AR1s);
            this.arrrowBtn[17].setRolloverIcon(AR2s);
            this.arrrowBtn[17].setBounds(130, 333, 30, 30);
            this.arrrowBtn[17].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(6, 1);
                }
            });

            this.arrrowBtn[18] = new JButton();
            this.arrrowBtn[18].setIcon(AR1s);
            this.arrrowBtn[18].setRolloverIcon(AR2s);
            this.arrrowBtn[18].setBounds(130, 229, 30, 30);
            this.arrrowBtn[18].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(4, 1);
                }
            });

            this.arrrowBtn[19] = new JButton();
            this.arrrowBtn[19].setIcon(AR1s);
            this.arrrowBtn[19].setRolloverIcon(AR2s);
            this.arrrowBtn[19].setBounds(130, 125, 30, 30);
            this.arrrowBtn[19].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveLine(2, 1);
                }
            });

        }

        for (int i = 0; i < num; i++){
            // this.arrrowBtn[i].setBackground(new Color(0x2E1200));
            // this.arrrowBtn[i].setFocusPainted(false);
            // this.arrrowBtn[i].setContentAreaFilled(false);
            // this.arrrowBtn[i].setOpaque(true);
            // this.arrrowBtn[i].setBorderPainted(false);
            this.add(arrrowBtn[i]);
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

            this.AL1 = new ImageIcon(getClass().getResource("/images/arrows/l_1_50.png"));
            this.AL2 = new ImageIcon(getClass().getResource("/images/arrows/l_2_50.png"));
            this.AR1 = new ImageIcon(getClass().getResource("/images/arrows/r_1_50.png"));
            this.AR2 = new ImageIcon(getClass().getResource("/images/arrows/r_2_50.png"));
            this.AU1 = new ImageIcon(getClass().getResource("/images/arrows/up_1_50.png"));
            this.AU2 = new ImageIcon(getClass().getResource("/images/arrows/up_2_50.png"));
            this.AD1 = new ImageIcon(getClass().getResource("/images/arrows/d_1_50.png"));
            this.AD2 = new ImageIcon(getClass().getResource("/images/arrows/d_2_50.png"));
            this.AL1s = new ImageIcon(getClass().getResource("/images/arrows/l_1_30.png"));
            this.AL2s = new ImageIcon(getClass().getResource("/images/arrows/l_2_30.png"));
            this.AR1s = new ImageIcon(getClass().getResource("/images/arrows/r_1_30.png"));
            this.AR2s = new ImageIcon(getClass().getResource("/images/arrows/r_2_30.png"));
            this.AU1s = new ImageIcon(getClass().getResource("/images/arrows/up_1_30.png"));
            this.AU2s = new ImageIcon(getClass().getResource("/images/arrows/up_2_30.png"));
            this.AD1s = new ImageIcon(getClass().getResource("/images/arrows/d_1_30.png"));
            this.AD2s = new ImageIcon(getClass().getResource("/images/arrows/d_2_30.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

