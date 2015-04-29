package ija.labyrinth.gui;

import ija.labyrinth.game.MazeBoard;
import ija.labyrinth.game.MazeCard;
import ija.labyrinth.game.MazeField;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


public class CreateBoardUI extends JPanel implements ActionListener{

    private int boardSize;
    private int xAxis = 80;
    private int yAxis = 80;

    private BufferedImage smile;
    private BufferedImage Lpath;
    private BufferedImage Ipath;
    private BufferedImage Tpath;
    private BufferedImage Arrow;
    private BufferedImage rotArrow;
    private JLabel ArrowIco;

    public CreateBoardUI(int Size){
        boardSize = Size;

        //Rock[] kamen = new Rock[boardSize*boardSize+1];


        this.setSize(900, 700);
        this.setBackground(new Color(57, 57, 57));

        this.getRockImage();

        //this.paintComponent(g);

        //this.initNewBoard();

        //this.setVisible(true);

        Timer t = new Timer(100, this);
        t.start();
    }

    @Override
        protected void paintComponent(Graphics g){

            int blockSize = 0, maxSize = 0;
            if (this.boardSize == 5 ){
                blockSize = 70;
                maxSize  = 370;
            } else if (this.boardSize == 7) {
                blockSize = 50;
                maxSize = 520;
            } else if (this.boardSize == 9){
                blockSize = 50;
                maxSize = 450;
            } else if (this.boardSize == 11){
                blockSize = 50;
                maxSize = 650;
            }


            int xPoint = 80;
            int yPoint = 80;
            for(int r = 1; r <= this.boardSize; r++) {
                for (int c = 1; c <= this.boardSize; c++) {

                    if(r == 1 && c == 1) g.drawImage(Lpath, xPoint, yPoint, blockSize, blockSize, this);

                    g.drawImage(Lpath, xPoint, yPoint, blockSize, blockSize, this);

                    xPoint += blockSize+2;
                    if (xPoint > maxSize) {
                        xPoint = 80;
                        yPoint += blockSize+2;
                    }
                    System.out.print(r+"-"+c+"  "+xPoint+"-"+yPoint+"\n");
                }
            }


        }

    public void actionPerformed(ActionEvent e){
       /* xAxis = xAxis +5;
        yAxis = yAxis +5;
        repaint();*/
    }

    private class Rock{
        public float x;
        public float y;
        BufferedImage path;

        public Rock(float x, float y, BufferedImage path) {
            this.x = x;
            this.y = y;
            this.path = path;
        }
    }




   /* private void initNewBoard(){
        this.setBackground(new Color(57, 57, 57));

        this.setSize(800, 600);

        this.setLayout(new GridBagLayout());
        this.gridConst = new GridBagConstraints();

        this.game = MazeBoard.createMazeBoard(boardSize);
        this.game.newGame();

        this.getRockImage();

        int i = 0;
        JButton[] arrBtn  = new JButton[25];
        for(int r = 0; r <= this.boardSize +1; r++) {
            for (int c = 0; c <= this.boardSize +1; c++) {
                if ((r == 0 || c == 0) || (r == this.boardSize +1 || c == this.boardSize +1)) {
                    if (((r == 0 && c == 0) || (r == this.boardSize +1 && c == 0 )) ||
                            (r == 0 && c == this.boardSize +1) || (r == this.boardSize +1 && c == this.boardSize +1)) continue;
                    if (r%2 == 0 && c%2 == 0){
                        this.gridConst.gridx = r;
                        this.gridConst.gridy = c;
                        this.gridConst.ipady = 1;
                        this.gridConst.ipadx = 1;
                        i++;
                        arrBtn[i] = new JButton();
                        System.out.print(r + "-" + c + " - ");
                        rotArrow = rotateArrow(r, c, 1);
                        arrBtn[i].setIcon(new ImageIcon(rotArrow));
                        rotArrow = rotateArrow(r, c, 2);
                        arrBtn[i].setRolloverIcon(new ImageIcon(rotArrow));
                        arrBtn[i].setSize(40, 40);
                        arrBtn[i].setBorderPainted(false);
                        arrBtn[i].setFocusPainted(false);
                        arrBtn[i].setContentAreaFilled(false);
                        this.add(arrBtn[i], this.gridConst);
                    }
                } else {
                    this.gridConst.gridx = r;
                    this.gridConst.gridy = c;
                    this.gridConst.ipady = 1;
                    this.gridConst.ipadx = 1;
                    if(r == 1 && c == 1) this.add(new JLabel(new ImageIcon(Lpath)), this.gridConst);
                    if(r == this.boardSize && c == this.boardSize)
                        this.add(new JLabel(new ImageIcon(Lpath)), this.gridConst);
                    if(r == 1 && c == this.boardSize)
                        this.add(new JLabel(new ImageIcon(Lpath)), this.gridConst);
                    if(r == this.boardSize && c == 1)
                        this.add(new JLabel(new ImageIcon(Lpath)), this.gridConst);
                    if(r%2 == 0)
                        this.add(new JLabel(new ImageIcon(Tpath)), this.gridConst);
                    if(c%2 == 0)
                        this.add(new JLabel(new ImageIcon(Ipath)), this.gridConst);
                    else
                        this.add(new JLabel(new ImageIcon(Tpath)), this.gridConst);

                }
            }
        }



        this.gridConst.gridx = boardSize +3;
        this.gridConst.gridy = 1;
        this.gridConst.ipady = 1;
        this.gridConst.ipadx = 1;
        this.gridConst.insets = new Insets(0,50,0,0);
        this.add(new JLabel(new ImageIcon(Tpath)), this.gridConst);

        this.gridConst.gridx = boardSize +3;
        this.gridConst.gridy = 2;
        this.gridConst.ipady = 1;
        this.gridConst.ipadx = 1;
        this.gridConst.insets = new Insets(0,50,0,0);
        this.add(new JButton("NewCard"), this.gridConst);

        //this.setLayout(null);
        //this.setVisible(true);
    }*/

    private void getRockImage(){

        switch (boardSize) {
            case 5:
            case 7:
                try {
                    this.smile = ImageIO.read(getClass().getResource("/images/smile_card.png"));
                    this.Lpath = ImageIO.read(getClass().getResource("/images/rock_L2.png"));
                    this.Ipath = ImageIO.read(getClass().getResource("/images/rock_I2.png"));
                    this.Tpath = ImageIO.read(getClass().getResource("/images/rock_T2.png"));
                    this.Arrow = ImageIO.read(getClass().getResource("/images/arrow_ico.png"));
                    this.ArrowIco = new JLabel(new ImageIcon(Arrow));
                } catch (IOException e) { e.printStackTrace(); }

                break;

            case 9:
            case 11:
                try {
                    this.Lpath = ImageIO.read(getClass().getResource("/images/rock_L3.png"));
                    this.Ipath = ImageIO.read(getClass().getResource("/images/rock_I3.png"));
                    this.Tpath = ImageIO.read(getClass().getResource("/images/rock_T3.png"));
                } catch (IOException e) { e.printStackTrace(); }

                break;
        }
    }

    private BufferedImage rotateArrow(int row, int col, int type){
        BufferedImage rotated;
        System.out.print("\n");

        if (row == 0){
            try {
                if(type == 1){
                    rotated = ImageIO.read(getClass().getResource("/images/arrows/arrow4_1_ico.png"));
                } else {
                    rotated = ImageIO.read(getClass().getResource("/images/arrows/arrow4_2_ico.png"));
                }
                return rotated;
            } catch (IOException e) { e.printStackTrace(); }
        }
        if (col == boardSize +1){
            try {
                if(type == 1){
                    rotated = ImageIO.read(getClass().getResource("/images/arrows/arrow3_1_ico.png"));
                } else {
                    rotated = ImageIO.read(getClass().getResource("/images/arrows/arrow3_2_ico.png"));
                }
                return rotated;
            } catch (IOException e) { e.printStackTrace(); }
        }
        if (row == boardSize +1){
            try {
                if(type == 1){
                    rotated = ImageIO.read(getClass().getResource("/images/arrows/arrow2_1_ico.png"));
                } else {
                    rotated = ImageIO.read(getClass().getResource("/images/arrows/arrow2_2_ico.png"));
                }
                return rotated;
            } catch (IOException e) { e.printStackTrace(); }
        }
        if (col == 0){
            try {
                if(type == 1){
                    rotated = ImageIO.read(getClass().getResource("/images/arrows/arrow1_1_ico.png"));
                } else {
                    rotated = ImageIO.read(getClass().getResource("/images/arrows/arrow1_2_ico.png"));
                }
                return rotated;
            } catch (IOException e) { e.printStackTrace(); }
        }

        rotated = Arrow;
        return rotated;
    }

   /* private void fillBoard() {

        MazeCard tmpcard;
        for(int r = 0; r < this.boardSize; r++) {
            for(int c = 0; c < this.boardSize; c++) {
                tmpcard = this.board[r][c].getCard();
                if (tmpcard == null) {
                    System.out.println("EMPTY");
                    return;
                }
                if (Objects.equals(tmpcard.getType(), "C")) System.out.print("L");
                if (Objects.equals(tmpcard.getType(), "L")) System.out.print("I");
                if (Objects.equals(tmpcard.getType(), "F")) System.out.print("T");

                if (c != this.boardSize -1) System.out.print(" ");
            }
            System.out.print("\n");
        }
        System.out.print("Free card: ");
        if (Objects.equals(this.freeCard.getType(), "C")) System.out.println("L");
        if (Objects.equals(this.freeCard.getType(), "L")) System.out.println("I");
        if (Objects.equals(this.freeCard.getType(), "F")) System.out.println("T");

    }
*/

}
