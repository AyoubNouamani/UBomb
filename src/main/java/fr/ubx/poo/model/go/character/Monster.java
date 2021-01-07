/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import java.util.Collection;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Monster extends GameObject implements Movable{
    Direction directionMonster;
    private boolean moveRequested = false;
    public int time = 0;

    public Monster(Game game, Position position){
        super(game, position);
        this.directionMonster = Direction.random();
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
        if (!game.getWorld().isEmpty(nextPos)){
            return false;
        }// monstre ne doit pas se marcher dessus
        /*for(int i =0; i<game.getmonstertab().size();i++){
            if(game.getmonstertab().get(i).getPosition().equals(game.getmonstertab().get(i+1).getPosition())){
                return false;
            }
        }*/

        return nextPos.inside(game.getWorld().dimension);
    }

    @Override
    public void doMove(Direction directionMonster) {
        // TODO Auto-generated method stub
        Position nextPos = directionMonster.nextPosition(getPosition());
        setPosition(nextPos);
        if (game.getPlayer().getPosition().equals(game.getMonster().getPosition())){
            game.getPlayer().decreaseLive();
        }//on perd trop de vie mdr
       /* for (Monster mon : game.getmonstertab()) {
            if(game.getPlayer().getPosition().equals(mon.getPosition())){
                game.getPlayer().decreaseLive();
            }
        }*/
    
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
