package ija;

import ija.board.MazeBoard;
import ija.board.MazeField;
import java.util.Scanner;
/**
 *
 * jednoduche TUI
 */


public class TUI {
    
    static MazeBoard game;

    private static void command(String cmd) {
        if (cmd.equals("n")) game.newGame();
        else if (cmd.equals("p")) game.print();
        else if (cmd.length()==3) {
            if (cmd.charAt(0)=='s') {
                    int r = Character.getNumericValue(cmd.charAt(1));
                    int c = Character.getNumericValue(cmd.charAt(2));
                    MazeField mf = game.get(r, c);
                    game.shift(mf);
                    
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
        game = MazeBoard.createMazeBoard(5);
        while (!"q".equals(cmd)) {
            command(cmd);
            cmd = scan.next();
        }
    }
}
