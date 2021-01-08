/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Direction;


public class Monster extends GameObject implements Movable{
    Direction directionMonster;
    private boolean moveRequested = false;
    private int level;

    public Monster(Game game, Position position, int level){
        super(game, position);
        this.directionMonster = Direction.random();
        this.level = level;
    }

    public void requestMove(Direction directionMonster) {
        if (directionMonster != this.directionMonster) {
            this.directionMonster = directionMonster;
        }
        moveRequested = true;
    }

    public Direction getDirectionMonster() {
        return directionMonster;
    }

    @Override
    public boolean canMove(Direction directionMonster) {
        Position nextPos = directionMonster.nextPosition(getPosition());
        if (!game.getWorld(level).isEmpty(nextPos)){
            return false;
        }
        return nextPos.inside(game.getWorld().dimension);
    }


    @Override
    public void doMove(Direction directionMonster) {
        // TODO Auto-generated method stub
        Position nextPos = directionMonster.nextPosition(getPosition());
        setPosition(nextPos);
    }


    public void update(long now) {
        if (moveRequested) {
            if (canMove(directionMonster)) {
                doMove(directionMonster);
            }
        }
        moveRequested = false;
    }
}
