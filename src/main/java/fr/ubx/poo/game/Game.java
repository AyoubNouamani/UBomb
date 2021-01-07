/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

public class Game {

    private final World[] world;
    private final Player player;
    private final Monster monster;
    private final ArrayList<Monster> monster2;
    private final Monster [] monster3;
    private final String worldPath;
    public String nameLevels;
    public int initLevels;
    public int initPlayerLives;
    public int initPlayerKey;
    public int initNumberBomb;
    public int initRangeBomb;
    public int actualLevel = 1;


    public Game(String worldPath) throws IOException, Exception{
        this.worldPath = worldPath;
        loadConfig(worldPath);
        World[] allWorlds = new World[initLevels];
        for (int i = 0; i<initLevels; i++)
            allWorlds[i]=new WorldFromFile(worldPath, nameLevels, i+1);                        
        world = allWorlds;
        ArrayList<Position> pos = null;
        Position positionPlayer = null;
        Position positionMonster = null;
        
        pos = world[actualLevel ].findMonster2();
        monster3 = new Monster[pos.size()];
        monster2 = new ArrayList<>();
        System.out.println(pos.get(1));
        for (Position positions : pos) {
              monster2.add(new Monster(this,positions));
            
            }
        for (Monster mon2 : monster2) {
            for (Monster mon3 : monster3) {
                mon3=mon2;
            }
        }

        
        
        System.out.println(monster2.size()); 
        System.out.println(monster3.length);
        try {
            positionPlayer = world[actualLevel-1].findPlayer();
            player = new Player(this, positionPlayer);
        
           positionMonster = world[actualLevel+1].findMonster();
            monster = new Monster(this, positionMonster);
            
            
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    
    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            nameLevels = prop.getProperty("prefix");
            initLevels = Integer.parseInt(prop.getProperty("levels"));
            initPlayerLives = Integer.parseInt(prop.getProperty("lives"));
            initPlayerKey = Integer.parseInt(prop.getProperty("key"));
            initNumberBomb = Integer.parseInt(prop.getProperty("bombes"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return world[actualLevel-1];
    }

    public Player getPlayer() {
        return this.player;
    }
    public Monster getMonster(){
        return this.monster;
    }
    public ArrayList<Monster> getmonstertab() {
        return this.monster2;
    }

    public String getWorldPath(){
        return this.worldPath;
    }

}
