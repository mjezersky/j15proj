package ija.labyrinth.tui;

import ija.labyrinth.game.MazeBoard;
import ija.labyrinth.game.MazeField;
import java.util.Scanner;
import java.io.*;

/**
 *
 * jednoduche TUI
 */


public class TUI {
    
    private static MazeBoard game;
    private static int gameSize;

    private static void save() {
        // potreba implementace metody strRepr v MazeBoard a jejich zavislosti
        // TODO: kontrola existence vytvorene hry
        System.out.println("saving");
        String saveStr = "";
        if (TUI.gameSize<10) saveStr += "0";
        saveStr += Integer.toString(TUI.gameSize);
        saveStr += TUI.game.strRepr();
        
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream("savegame.txt")));
            writer.write(saveStr);
        } catch (IOException ex) {
          System.out.println("Save error: cannot write to file");
        } finally {
           try { if (writer!=null) writer.close();} catch (Exception ex) {}
        }
    }
    
    private static void load() {
        // vyzaduje implementaci metody config v MazeBoard
        
        String loadStr = "";
        FileReader reader = null;
        File file = new File("savegame.txt");
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            loadStr = new String(chars);
        } catch (IOException e) {
            System.out.println("Load error: cannot read file");
        } finally {
            try {if (reader!=null) reader.close();} catch (Exception ex) {}
        }

        System.out.println("loading");
        if ( (loadStr.length()<2) ) {
            System.out.println("Load error: invalid save file");
            return;
        }
        
        int gSize;
        try { gSize = Integer.parseInt(loadStr.substring(0,2)); }
        catch (NumberFormatException e) {
            System.out.println("Load error: invalid save file");
            return;
        }
        
        if ( (loadStr.length()-2) != (gSize*gSize+1)*6 ) { // n*n + freeCard
            System.out.println("Load error: corrupted save file");
            return;
        }
        TUI.game = MazeBoard.createMazeBoard(gSize);
        TUI.gameSize = gSize;
        for (int i=2; i<loadStr.length(); i+=6) {
            TUI.game.config(loadStr.substring(i, i+6));
        }
    }
    
    private static void command(String cmd) {
        if (cmd.equals("n")) TUI.game.newGame();
        else if (cmd.equals("save")) TUI.save();
        else if (cmd.equals("load")) TUI.load();
        else if (cmd.equals("p")) TUI.game.print();
        else if (cmd.length()==3) {
            if (cmd.charAt(0)=='s') {
                    int r = Character.getNumericValue(cmd.charAt(1));
                    int c = Character.getNumericValue(cmd.charAt(2));
                    MazeField mf = TUI.game.get(r, c);
                    TUI.game.shift(mf);
                    
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
        TUI.gameSize = 5;
        
        TUI.game = MazeBoard.createMazeBoard(TUI.gameSize);
        while (!"q".equals(cmd)) {
            TUI.command(cmd);
            cmd = scan.next();
        }
    }
}
