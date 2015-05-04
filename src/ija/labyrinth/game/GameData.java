package ija.labyrinth.game;

import java.io.*;
import java.math.BigInteger;
import java.security.*;

public class GameData {

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

    public static boolean save(MazeBoard game, File filename) {
        System.out.println("saving");
        String saveStr = "";
        saveStr += game.toString();

        String hashtext = GameData.getHash(saveStr);
        System.out.println(hashtext);

        saveStr = hashtext+saveStr;

        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename)));
            writer.write(saveStr);
        } catch (IOException ex) {
            System.out.println("Error - GameData.save: cannot write to file");
            return false;
        } finally {
            try { if (writer!=null) writer.close();} catch (Exception ex) {}
        }
        return true;
    }

    public static MazeBoard load(File filename) {
        String loadStr = "";
        FileReader reader = null;
        File file = filename;
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
        String[] loadParts = loadStr.split("#");
        String boardString = loadParts[0];
        String packString = loadParts[1];

        int gSize;
        try { gSize = Integer.parseInt(boardString.substring(0,2)); }
        catch (NumberFormatException e) {
            System.out.println("Error - GameData.load: invalid save file");
            return null;
        }

        MazeBoard game = MazeBoard.createMazeBoard(gSize);
        for (int i=2; i<boardString.length(); i+=6) {
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


        return game;
    }
}
