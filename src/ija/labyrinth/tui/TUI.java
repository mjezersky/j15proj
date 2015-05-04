package ija.labyrinth.tui;

import ija.labyrinth.game.*;
import java.util.Scanner;
import java.io.*;

/**
 *
 * jednoduche TUI
 */


public class TUI {
    
    private static MazeBoard game;
    private static int gameSize;

    private static void save(String filename) {
        if (GameData.save(game, filename)) System.out.println("Save OK");
        else System.out.println("Save FAILED");
    }
    
    private static void load(String filename) {
        MazeBoard lg;
        lg = GameData.load(filename);
        if (lg!=null) {
            TUI.game = lg;
            System.out.println("Load OK");
        }
        else System.out.println("Load FAILED");
    }
    
    private static void test() {
        TUI.game.addPlayer("joza");
        TUI.game.addPlayer("pepa");
    }
    
    private static void test2() {
        Player plr;
        TUI.load("savegame.txt");
        TUI.game.addPlayer("joza");
        plr = TUI.game.getPlayer(0);
        plr.moveTo(2,2);
        System.out.print(plr.getNum());
        System.out.print(" ");
        System.out.print(plr.getName());
        System.out.print(" [");
        System.out.print(plr.getRow());
        System.out.print(",");
        System.out.print(plr.getCol());
        System.out.print("]\n");
        System.out.println("----------");
        System.out.println(plr.canMove(2, 2));
        System.out.println(plr.canMove(5, 5));
        System.out.println("----------");
        System.out.println(plr.canMove(plr.getRow()-1, plr.getCol()));
        System.out.println(plr.canMove(plr.getRow(), plr.getCol()-1));
        System.out.println(plr.canMove(plr.getRow(), plr.getCol()+1));
        System.out.println(plr.canMove(plr.getRow()+1, plr.getCol()));
        
    }
    
    private static void makeNewGame() {
        TUI.game.newGame();
        TUI.game.createPack(12);
    }
    
    private static void command(String cmd) {
        if (cmd.equals("n")) TUI.makeNewGame();
        else if (cmd.equals("test")) TUI.test();
        else if (cmd.equals("save")) TUI.save("savegame.txt");
        else if (cmd.equals("load")) TUI.load("savegame.txt");
        else if (cmd.equals("p")) TUI.game.print();
        else if (cmd.length()==3) {
            if (cmd.charAt(0)=='s') {
                    int r = Character.getNumericValue(cmd.charAt(1));
                    int c = Character.getNumericValue(cmd.charAt(2));
                    MazeField mf = TUI.game.get(r, c);
                    TUI.game.shift(mf);
                    
            }
            else if (cmd.charAt(0)=='r') {
                    int r = Character.getNumericValue(cmd.charAt(1));
                    int c = Character.getNumericValue(cmd.charAt(2));
                    MazeField mf = TUI.game.get(r, c);
                    if (mf == null) return;
                    MazeCard mc = mf.getCard();
                    if (mc == null) return;
                    mc.turnRight();
                    
            }
            else {
                System.out.print("Error: invalid command ");
                System.out.print(cmd);
                System.out.print("\n");
            }
        }
        else {
            System.out.print("Error: invalid command ");
            System.out.print(cmd);
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("TUI ready");
        String cmd = scan.next();
        TUI.gameSize = 7;
        
        TUI.game = MazeBoard.createMazeBoard(TUI.gameSize);
        while (!"q".equals(cmd)) {
            TUI.command(cmd);
            cmd = scan.next();
        }
    }
}
