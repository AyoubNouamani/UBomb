package fr.ubx.poo.game;


import static fr.ubx.poo.game.WorldEntity.*;

import java.io.*; 


public class WorldStatic extends World {
    private static WorldEntity[][] mapEntities = creatWorld();
        /*{
                {Stone, Empty, Heart, Empty, Empty, Empty, Empty, Empty, Empty, Empty, BombRangeDec, Empty},
                {Player, Stone, Stone, Empty, Stone, Empty, Stone, Stone, Stone, Stone, Empty, Empty},
                {Empty, Empty, Empty, Empty, Stone, Box, Stone, Empty, Empty, Stone, Empty, Empty},
                {Empty, Empty, Empty, Empty, Stone, Box, Stone, Empty, Empty, Stone, Empty, Empty},
                {Empty, Box, Empty, Empty, Stone, Stone, Stone, Empty, Empty, Empty, Empty, Empty},
                {Empty, Empty, Empty, Empty, Empty, Empty, Empty, Key, Empty, Stone, Empty, Empty},
                {Empty, Tree, Empty, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
                {Empty, Empty, Box, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
                {Empty, Tree, Tree, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
                {Empty, Empty, Empty, Empty, Empty, Empty, BombRangeInc, Empty, Empty, Empty, Empty, Empty},
                {Stone, Stone, Stone, Empty, Stone, Empty, Box, Box, Stone, Stone, Box, Stone},
                {Empty, DoorNextClosed, Empty, Empty, Empty, Empty, Empty, Empty, Monster, Empty, Empty, Empty},
                {Empty, BombNumberDec, Empty, Empty, Empty, Empty, Empty, Empty, BombNumberInc, Empty, Empty, Princess}
        };*/
    
        

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

    public static WorldEntity[][] creatWorld() {
        WorldEntity[][] level1 = new WorldEntity[12][12];
        try {
            String t = "./src/main/resources/sample/level1.txt";
            FileReader reader = new FileReader(t);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            int x = 0;
            while ((line = bufferedReader.readLine()) != null) {
                for (int y = 0; y < line.length(); y++) {
                    String l = String.valueOf(line.charAt(y));
                    level1[x][y] = objectW(l);
                }
                x++;
            }
            reader.close();
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
        return level1;
    }

    public WorldStatic() throws Exception {
        super(mapEntities);
    }
}
