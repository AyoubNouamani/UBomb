package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.*;


public class Bomb extends GameObject {
    // peut etre ajouter direction et du coup getDirection
    public int time;
    private Direction[] tab = {Direction.N, Direction.S, Direction.E, Direction.W};
    //private int bombRange;

    public Bomb(Game game, Position position){
        super(game, position);
        //this.bombRange = game.getPlayer().getBombsRange();
        this.time = 4;
    }

    public void requestBomb(){
        if (game.getPlayer().getBombsValue() > 0){
            Position p = game.getPlayer().getPosition();
            Decor bomb = new Bomb4();
            game.getWorld().set(p, bomb);
            game.getPlayer().decreasBomb();
        }
    }

    private void explosionDecor(Direction t, Decor explosion){
        //on regarde s'il y'a pas un objet a pas detruire
        Position x = t.nextPosition(getPosition());
        if (game.getWorld().isEmpty(x)){
            game.getWorld().set(x, explosion);
        }else{    
            String object = game.getWorld().get(x).toString();
            if (object != "Tree" 
                && object != "Stone" 
                && object != "DoorNextClosed"
                && object != "DoorNextOpened"
                && object != "DoorPrevOpened")
                game.getWorld().set(x, explosion);
        }
    }

    public void Countdown(){
        time = time - 1;
        if(time == 3){
            Decor bomb = new Bomb3();
            game.getWorld().set(getPosition(), bomb);
        }   
        else if (time == 2){
            Decor bomb = new Bomb2();
            game.getWorld().set(getPosition(), bomb);
        }
        else if (time == 1){
            Decor bomb = new Bomb1();
            game.getWorld().set(getPosition(), bomb);
        }else if (time == 0){
            //creer explosion
            Position p = getPosition();
            game.getWorld().clear(p);
            Decor explosion = new Explosion();
            for (Direction x : tab){
                explosionDecor(x, explosion);
            }
        }else{
            erasseExplosion();
        }
    }

    public void erasseExplosion(){
        Position p = getPosition();
        for (Direction x : tab){
            Position y = x.nextPosition(p);    
            if (!game.getWorld().isEmpty(y)){
                String object = game.getWorld().get(y).toString();
                if (object == "Explosion"){
                    game.getWorld().clear(y);
                }
            }
        }
    }

}
