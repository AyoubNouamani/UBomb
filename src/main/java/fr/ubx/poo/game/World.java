/*
 * Copyright (c) 2020. Laurent Réveillère
 */
package fr.ubx.poo.game;



import static fr.ubx.poo.game.WorldEntity.*;


import fr.ubx.poo.model.decor.Decor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public class World {
    public final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;

    public World(WorldEntity[][] raw) throws Exception {
        this.raw = raw;
        //this.raw = creatWorld();
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);        
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }
    
    public Position findMonster() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                  //  System.out.println("position ("+x+","+y+")");
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Monster");
    }
    public ArrayList<Position> findMonster2() {
        ArrayList<Position> tab = new ArrayList<>();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    System.out.println("position ("+x+","+y+")");
                    tab.add(new Position(x,y));
            
                }
            }
        }
        return tab;
    }

    // load the level in the file
    
    public Position findDoorPrevOpened() throws PositionNotFoundException {
        Position p = null;
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorPrevOpened) {
                    p = new Position(x, y);
                }
            }
        }
        return p;
    }

    public Position getDoorNext() throws PositionNotFoundException {
        Position p = null;
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorNextClosed) {
                    p = new Position(x, y);
                }
            }
        }
        return p;
    }

    public Position findDoorPrev() {
        try {
            return findDoorPrevOpened();
        } catch (PositionNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Position findDoorNext() {
        try {
            return getDoorNext();
        } catch (PositionNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Decor get(Position position) {
        return grid.get(position);
    }
    

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }

    public void clear(Position position) {
        grid.remove(position);
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        return (position.x >= 0 
        && position.x < dimension.width 
        && position.y >= 0 
        && position.y < dimension.height); // to update
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }
}
