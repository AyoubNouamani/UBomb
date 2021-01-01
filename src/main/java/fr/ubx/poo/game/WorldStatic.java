package fr.ubx.poo.game;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static fr.ubx.poo.game.WorldEntity.*;


public class WorldStatic extends World {
    private static WorldEntity[][] mapEntities=
            {
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
            };

        public void creatWorld(){
            /*String path = ;
            try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
                Properties prop = new Properties();
                // load the configuration file
                prop.load(input);
                //initPlayerKey = Integer.parseInt(prop.getProperty("key", "0"));
            } catch (IOException ex) {
                System.err.println("Error loading configuration");
            }
            for (int x = 0; x < dimension.width; x++) {
                for (int y = 0; y < dimension.height; y++){

                }
            }*/
        }

    public WorldStatic() {
        super(mapEntities);
    }
}
