/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import java.util.List;

public class Game {

    private final World[] world;
    private final Player player;
    private final String worldPath;
    public String nameLevels;
    public int initLevels;
    public int initPlayerLives;
    public int initPlayerKey;
    public int initNumberBomb;
    public int initRangeBomb;
    public int actualLevel = 1;
    public List<List<Monster>> monsterTab;
    public List<List<Bomb>> BombTab = new ArrayList<>();

    public Game(String worldPath){
        this.worldPath = worldPath;
        loadConfig(worldPath);

        //creation des niveaux

        World[] allWorlds = new World[initLevels];
        try {
            for (int i = 0; i<initLevels; i++)
                allWorlds[i] = new WorldFromFile(worldPath, nameLevels, i+1);
        }catch(Exception e){
            System.err.println("Position not found : " + e.getLocalizedMessage());
        }
        world = allWorlds;

        //creation Liste de tout les monstres de chaque niveaux  
        List<List<Monster>> monster = new ArrayList<>();
        try {
            for (int x = 0; x<initLevels; x++){
                List<Monster> m = new ArrayList<>();
                List<Position> position = WorldFromFile.findMonsters(worldPath, nameLevels, x+1);
                //Monster[] m = new Monster[position.size()];
                for(int y=0; y<position.size(); y++)
                    m.add(new Monster(this, position.get(y), x));
                monster.add(m);
            }
        }catch(Exception e){
            System.err.println("Position not found : " + e.getLocalizedMessage());
        }
        this.monsterTab = monster;

        //Liste des bombes
        for (int x = 0; x<initLevels; x++){
            List<Bomb> bomb = new ArrayList<>();
            BombTab.add(bomb);
        }

        
        //Recherche de la position du joueur
        Position positionPlayer = null;
        try {
            positionPlayer = world[actualLevel-1].findPlayer();
            player = new Player(this, positionPlayer);
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

    public void addBomb(Bomb bomb){
        BombTab.get(actualLevel-1).add(bomb);
    }

    public List<List<Bomb>> getBombTab(){
        return this.BombTab;
    }

    public World getWorld() {
        return world[actualLevel-1];
    }

    public World getWorld(int i) {
        return world[i];
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<List<Monster>> getMonsterTab() {
        return monsterTab;
    }

    public String getWorldPath(){
        return this.worldPath;
    }

}
