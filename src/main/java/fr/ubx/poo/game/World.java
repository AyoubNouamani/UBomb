/*
 * Copyright (c) 2020. Laurent Réveillère
 */
package fr.ubx.poo.game;

import static fr.ubx.poo.game.WorldEntity.*;

import fr.ubx.poo.model.decor.Decor;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.Bomb;
import java.util.ArrayList;
import java.util.List;


public class World {
    public final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;
    public List<Monster> monsters = new ArrayList<>();
    public List<Bomb> bombes = new ArrayList<>();

    public World(WorldEntity[][] raw) throws Exception {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);        
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    // load the level in the file

    public Position getDoor(WorldEntity Door) throws PositionNotFoundException {
        Position p = null;
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == Door) {
                    p = new Position(x, y);
                }
            }
        }
        return p;
    }

    public Position findDoor(WorldEntity door) {
        Position p =null;
        try {
            p = getDoor(door);
        } catch (PositionNotFoundException e) {
            e.printStackTrace();
        }
        return p;
    }

    public void addBomb(Bomb bomb){
        this.bombes.add(bomb);
    }

    public List<Bomb> getListBomb(){
        return this.bombes;
    }

    public void setListMonster(List<Monster> list){
        this.monsters = list;
    }

    public List<Monster> getMonsterTab() {
        return monsters;
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
