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
    private final ArrayList<Monster> monsterTab;
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
        ArrayList<Position> AllPosMonster = null;
        Position positionPlayer = null;
        Position positionMonster = null;
        //les arraylist sont des tableaux dynamique
        // je trouve aucun moyen d'ajouter un element dans un tableau si on utilise un tableau simple comme int [] tab...
        // AllPosMonster contient tous les positions de tous les monstres 1 SEUL niveau
        AllPosMonster = world[actualLevel-1].findAllMonster();// actuellement le Niv 1
        // je cree un tableau dynamique vide de monster
        monsterTab = new ArrayList<>();
       // System.out.println(AllPosMonster.get(1)); <- permet d'avoir le 1er element
       // un foreach pour chaque position de monstre dans AllPosMonster je add un monstre dans le tableau monsterTab
        for (Position positions : AllPosMonster) {
              monsterTab.add(new Monster(this,positions));
            
            }

        // pour verifier la taille du tableau apres avoir eu tous les monstres, c'est en fonction du nombre de monstre que ta mis dans le fichier
        System.out.println(monsterTab.size()); 
     
        try {
            positionPlayer = world[actualLevel-1].findPlayer();
            player = new Player(this, positionPlayer);

           positionMonster = world[actualLevel-1].findMonster();
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
        return this.monsterTab;
    }

    public String getWorldPath(){
        return this.worldPath;
    }

}
