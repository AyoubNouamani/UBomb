package fr.ubx.poo.game;

import static fr.ubx.poo.game.WorldEntity.*;

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
        String document = path + "/" + prefix + String.valueOf(level) + ".txt";
        //calculer hauteur et largeur du niveaux
        FileReader reader = new FileReader(document);
        BufferedReader bufferedReader = new BufferedReader(reader);
        int length = 0;
        int  width = 0;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            length = length + 1;
            width = line.length();
        }
        //creer le tableau du niveau 
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
        reader.close();
        return level1;
    }


    public WorldFromFile(String path, String prefix, int level) throws IOException, Exception {
        super(creatWorld(path, prefix, level));
    }
}