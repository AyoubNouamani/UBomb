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
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private static int key;
    private int bombVal = 2;
    private int bombRange = 1;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        // mettre initbomb
    }

    public int getLives() {
        return lives;
    }

    public int getBombsValue() {
        return bombVal;
    }

    public int getBombsRange() {
        return bombRange;
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

    //poser bomb sur l'endroit du joeur
    public void requestBomb(){
        //requestBomb();
    }

   //ouvrir la porte si key > 0
    public void requestKey(){
        //remplacer la porte si on a une clé -> niveau suivant
        Position nextPos = direction.nextPosition(getPosition());
        if (key > 0
            && game.getWorld().get(nextPos).toString() == "DoorNextClosed"){
            key = key - 1;
            game.getWorld().clear(nextPos);
            Decor next = new DoorNextOpened();
            game.getWorld().set(nextPos, next);
            //System.out.println("Porte remplacé");
        }
   } 

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        //detecte si c'est pas null (espace vide sans decor)
        if (!game.getWorld().isEmpty(nextPos)){
            String object = game.getWorld().get(nextPos).toString();
            if (object == "Stone" || object == "Tree"){
                return false;
            }
            else if(object == "Box"){
                // after = position apres la boite -> deux cases apres le direction du joueur
                //on detecte si le mvt peut être effectué avec deux test
                    // - si y'a pas un objet apres
                    // - la caisse reste a l'interieur du monde
                Position after = direction.nextPosition(nextPos);
                if (after.inside(game.getWorld().dimension) 
                    && game.getWorld().isEmpty(after)){
                    game.getWorld().clear(nextPos);
                    Decor box = new Box();
                    game.getWorld().set(after, box);
                }else{
                    return false;
                }
            }
            // on clear chaque objet quand on passe dessus ensuite il faut aller dans gameEngine
            else if (object == "Key") {
                key = key + 1;
                game.getWorld().clear(nextPos);
            }
            else if (object == "BombRangeInc"){ 
                bombRange = bombRange + 1;
                game.getWorld().clear(nextPos);
            }
            else if (object == "BombRangeDec"){ 
                bombRange = bombRange - 1;
                game.getWorld().clear(nextPos);
            }
            else if (object == "Heart") {
                lives = lives + 1;
                game.getWorld().clear(nextPos);
            }
            else if (object == "BombNumberInc"){ 
                bombVal = bombVal + 1;
                game.getWorld().clear(nextPos);
            }
            else if (object == "BombNumberDec"){ 
                bombVal = bombVal - 1;
                game.getWorld().clear(nextPos);
            }
            else if (object == "DoorNextClosed"){
                return false;
            }
            else if (object == "DoorNextOpened"){
                // passer au niveau suivant
            }
            else if (object == "Princess") winner = true;
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
        if (lives == 0) return false;
        return alive;
    }

    public void decreasBomb(){
        bombVal = bombVal - 1;
    }
}
