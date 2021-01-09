package fr.ubx.poo.game;

import static fr.ubx.poo.game.WorldEntity.*;

import java.util.ArrayList;
import java.util.List;

import java.io.*; 


public class WorldFromFile extends World {       

    public static WorldEntity objectW(String o) {
        switch (o) {
            case "S":
                return Stone;
            case "T":
                return Tree;
            case "B":
                return Box;
            case "H":
                return Heart;
            case "K":
                return Key;
            case "W":
                return Princess;
            case "+":
                return BombNumberInc;
            case "-":
                return BombNumberDec;
            case ">":
                return BombRangeInc;
            case "<":
                return BombRangeDec;
            case "n":
                return DoorNextClosed;
            case "N":
                return DoorNextOpened;
            case "V":
                return DoorPrevOpened;
            case "P":
                return Player;
            case "M":
                return Monster;
            case "_":
                return Empty;
            default:
                return null;
        }
    }
    
    
    public static WorldEntity[][] creatWorld(String path, String prefix, int level) throws IOException {
        //Lien vers le fichier
        String document = path + "/" + prefix + String.valueOf(level) + ".txt";

        //calculer hauteur et largeur du niveaux
        FileReader reader = new FileReader(document);
        BufferedReader bufferedReader = new BufferedReader(reader);
        int length = 0;
        int  width = 0;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            length++;
            width = line.length();
        }
        reader.close();

        //creer le tableau du niveau avec les Entity
        WorldEntity[][] level1 = new WorldEntity[length][width];
        FileReader reader2 = new FileReader(document);
        BufferedReader fd = new BufferedReader(reader2);
        int x = 0;
        while ((line = fd.readLine()) != null) {
            for (int y = 0; y < line.length(); y++) {
                String l = String.valueOf(line.charAt(y));
                level1[x][y] = objectW(l);
            }
            x++;
        }
        fd.close();
        return level1;
    }

    public static List<Position> findMonsters(String path, String prefix, int level) throws Exception {
        //Map<Integer, Position> m = new Hashtable<>();
        List<Position> position = new ArrayList<>();
        String document = path + "/" + prefix + String.valueOf(level) + ".txt";
        //creer le tableau du niveau 
        FileReader reader = new FileReader(document);
        BufferedReader fd = new BufferedReader(reader);
        String line1;
        int x = 0;
        String letter = "M";
        while ((line1 = fd.readLine()) != null) {
            for (int y = 0; y < line1.length(); y++) {
                String l = String.valueOf(line1.charAt(y));
                if (l.equals(letter)){
                    Position p = new Position(y,x);
                    position.add(p);                     
                }
            }
            x++;
        }
        fd.close();
        return position;
    }

    public WorldFromFile(String path, String prefix, int level) throws Exception {
        super(creatWorld(path, prefix, level));
    }
}