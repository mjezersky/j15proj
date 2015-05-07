package ija.labyrinth.game;

import java.io.*;
import java.math.BigInteger;
import java.security.*;

/**
 * 
 * @author Matouš Jezerský - xjezer01
 */
public class GameData {
    
    private static String[] saveStrBuffer;
    private static int ssbIndex = -1; // ukazatel na prvek pro undo
    private static int ssbSize = 0;
    private static int ssbMaxIndex = -1;
    
    /**
     * Vrací MD5 hash řetězce.
     * @param str zdrojový řetězec
     * @return hash řetězce str
     */
    private static String getHash(String str) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error - GameData.save: java.security no such algorithm MD5");
            return "";
        }
        m.reset();
        m.update(str.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        return hashtext;
    }
    
    /**
     * Inicializuje buffer pro undo/redo operace
     * @param bufferSize velikost bufferu (mak počet operací undo/redo)
     */
    public static void initBuffer(int bufferSize) {
        GameData.saveStrBuffer = new String[bufferSize];
        GameData.ssbSize = bufferSize;
        GameData.ssbIndex = -1;
    }
    
    /**
     *
     * @return je-li možné provést operaci undo 
     */
    public static boolean canUndo() { return GameData.ssbIndex > 0; }
    
    /**
     * 
     * @return je-li možné provést operaci redo
     */
    public static boolean canRedo() { return GameData.ssbIndex < GameData.ssbMaxIndex; }
    
    /**
     * Uloží stav hry (herní desky) pro operace undo/redo.
     * @param game herní deska
     */
    public static void store(MazeBoard game) {
        System.out.println("STORE");
        System.out.println(ssbIndex);
        System.out.println(ssbSize-1);
        if (GameData.ssbIndex < GameData.ssbSize-1) { // vlkadam
            GameData.ssbIndex++;
            GameData.ssbMaxIndex = GameData.ssbIndex; // virtualni konec bufferu bude na nove vlozenem prvku
            GameData.saveStrBuffer[GameData.ssbIndex] = game.toString();
        }
        else { // vkladam doprava a shiftuju
            for (int i=0; i<GameData.ssbSize-1; i++) {
                GameData.saveStrBuffer[i] = GameData.saveStrBuffer[i+1];
            }
            GameData.saveStrBuffer[GameData.ssbIndex] = game.toString(); 
        }
         
    }
    
    /**
     * Vrátí hru (herní desku) do předchozího stavu
     * @param game herní deska
     * @return upravená (je-li to možné) nebo neupravená herní deska
     */
    public static MazeBoard undo(MazeBoard game) {
        System.out.println("UNDO");
        MazeBoard nGame = GameData.undo();
        if (nGame == null) return game;
        System.out.println("OK");
        return nGame;
    }
    
    /**
     * Vrátí hru (herní desku) do předchozího stavu
     * @return původní herní deska nebo null
     */
    public static MazeBoard undo() {
        MazeBoard game;
        if (GameData.canUndo()) {
            GameData.ssbIndex--;
            game = GameData.loadStrConfig(GameData.saveStrBuffer[GameData.ssbIndex]);
            return game;
        }
        return null;
    }
    
    /**
     * Vrací operaci redo (posune hru/desku do následujícího stavu)
     * @param game herní deska
     * @return upravená (je-li to možné) nebo neupravená herní deska
     */
    public static MazeBoard redo(MazeBoard game) {
        System.out.println("REDO");
        MazeBoard nGame = GameData.redo();
        if (nGame == null) return game;
        System.out.println("OK");
        return nGame;
    }
    
    /**
     * Vrací operaci redo (posune hru/desku do následujícího stavu)
     * @return nová herní deska nebo null
     */
    public static MazeBoard redo(){
        if (GameData.canRedo()) {
            GameData.ssbIndex++;
            return GameData.loadStrConfig(GameData.saveStrBuffer[GameData.ssbIndex]);           
        }
        return null;
    }

    
    /**
     * Uloží aktuální stav hry (herní desky) do souboru
     * @param game herní deska
     * @param file cílový soubor
     * @return 
     */
    public static boolean save(MazeBoard game, File file) {
        System.out.println("saving");
        String saveStr = "";
        saveStr += game.toString();

        String hashtext = GameData.getHash(saveStr);

        saveStr = hashtext+saveStr;

        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file)));
            writer.write(saveStr);
        } catch (IOException ex) {
            System.out.println("Error - GameData.save: cannot write to file");
            return false;
        } finally {
            try { if (writer!=null) writer.close();} catch (Exception ex) {}
        }
        return true;
    }
    
    /**
     * Načte hru z konfiguračního řetězce.
     * @param loadStr konfigurační řetězec
     * @return načtená hra
     */
    private static MazeBoard loadStrConfig(String loadStr) {
        String[] loadParts = loadStr.split("#");
        String boardString = loadParts[0];
        String packString = loadParts[1];

        int currTurn;
        try { currTurn = Integer.parseInt(boardString.substring(0,2)); }
        catch (NumberFormatException e) {
            System.out.println("Error - GameData.load: invalid save file");
            return null;
        }
        
        int gSize;
        try { gSize = Integer.parseInt(boardString.substring(2,4)); }
        catch (NumberFormatException e) {
            System.out.println("Error - GameData.load: invalid save file");
            return null;
        }

        MazeBoard game = MazeBoard.createMazeBoard(gSize);
        for (int i=4; i<boardString.length(); i+=6) {
            if (!game.strConfigBoard(boardString.substring(i, i+6))) {
                System.out.println("Error - GameData.load: strConfigBoard bad data");
                return null;
            }
        }

        if (!game.strConfigPack(packString)) {
            System.out.println("Error - GameData.load: strConfigPack bad data");
            return null;
        }

        for (int i=2; i<loadParts.length; i++) {
            if (!game.strConfigPlayer(loadParts[i])) {
                System.out.println("Error - GameData.load: strConfigPlayer bad data");
                return null;
            }
        }
        
        for (int i=-1; i<currTurn; i++) { // turn defaultne zacina na -1
            game.nextTurn();
        }
        
        return game;
    }

    /**
     * Načte hru ze souboru
     * @param file soubor k načtení
     * @return 
     */
    public static MazeBoard load(File file) {
        String loadStr = "";
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            loadStr = new String(chars);
        } catch (IOException e) {
            System.out.println("Error - GameData.load: cannot read file");
            return null;
        } finally {
            try {if (reader!=null) reader.close();} catch (Exception ex) {}
        }

        System.out.println("loading");
        if ( (loadStr.length()<33) ) {
            System.out.println("Error - GameData.load: invalid save file");
            return null;
        }

        String hashtext = GameData.getHash(loadStr.substring(32));
        if (!loadStr.substring(0,32).equals(hashtext)) {
            System.out.println("Error - GameData.load: bad MD5 checksum");
            return null;
        }

        loadStr = loadStr.substring(32);
        return loadStrConfig(loadStr);

    }
    
    /**
     * Vypíše informace o bufferu
     */
    public static void printReport() {
        System.out.println(ssbSize);
        System.out.println(ssbIndex);
        System.out.println(ssbMaxIndex);
        for (int i=0; i<=ssbMaxIndex; i++) {
            System.out.println(GameData.saveStrBuffer[i]);
        }
    }
}
