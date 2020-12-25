/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 3;
    private int key = 0;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
    }

    public int getKey() {
        return key;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor v = game.getWorld().get(nextPos);
        //detecte si c'est pas null (espace vide sans decor)
        if (v != null){
            String object = v.toString();
            if (object == "Stone" 
            || object == "Tree" 
            || object == "Box"){
                return false;
            }
            else if (object == "Key") key = key + 1;
            else if (object == "Heart") lives = lives + 1;
            else if (object == "Princess") winner = true;
            else if (object == "Monster"){
                lives = lives - 1;
                minusLive();
            }
        }
        //detecte si le perosnnage sort de la map
        return nextPos.inside(game.getWorld().dimension);
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

    //False si jouer a 0 vies
    public void minusLive() {
        if (lives == 0){
            alive = false;
        }
    }

}
