package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.*;

import fr.ubx.poo.model.go.character.Monster;


public class Bomb extends GameObject {
    // peut etre ajouter direction et du coup getDirection
    public int time;
    private Direction[] tab = {Direction.N, Direction.S, Direction.E, Direction.W};
    private int bombRange;

    public Bomb(Game game, Position position){
        super(game, position);
        this.bombRange = game.getPlayer().getBombsRange();
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

    //regarde si a l'endroit de l'explosion s'y trouve un Monster ou un Player 
    private void explosionCharacter(Position pBomb){
        for (int i = 0; i < game.initLevels; i++){
            int x = 0;
            for (Monster monster : game.getMonsterTab().get(game.actualLevel-1)){
                if (monster.getPosition().equals(pBomb)){
                    game.getMonsterTab().get(game.actualLevel-1).remove(x);
                }
            }
        }

        if (game.getPlayer().getPosition().equals(pBomb)){
            game.getPlayer().decreaseLiveBomb();
        }
    }

    private void explosionDecor(Direction t, Decor explosion){
        //on regarde s'il y'a pas un objet a pas detruire
        Position p = getPosition();
        explosionCharacter(p);
        String object2 = "X";
        for(int i=0; i < bombRange ; i++){
            Position x = t.nextPosition(p);
            explosionCharacter(x);
            if (game.getWorld().isEmpty(x)){
                game.getWorld().set(x, explosion);
            }else{    
                String object = game.getWorld().get(x).toString();
                //Si deux caisses se suivent l'explosion s'arrete sur le premiere
                if (object == "Box" && object2 == "Box"){
                    //game.getWorld().set(x, explosion);
                    break;
                }
                else if (object != "Tree" 
                    && object != "Stone" 
                    && object != "DoorNextClosed"
                    && object != "DoorNextOpened"
                    && object != "DoorPrevOpened"
                    && object != "Key"){
                        game.getWorld().set(x, explosion);
                }else{
                    break;
                }
                object2 = object;       
            }
            p = x;
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
            game.getWorld().set(p, explosion);
            for (Direction x : tab){
                explosionDecor(x, explosion);
            }
        }else{
            for (Direction x : tab){
                erasseExplosion(x);
            }
            game.getPlayer().increasBomb();
        }
    }

    public void erasseExplosion(Direction x){
        Position p = getPosition();
        game.getWorld().clear(p);
        for (int i = 0; i<bombRange; i++){
            Position y = x.nextPosition(p);    
            if (!game.getWorld().isEmpty(y)){
                String object = game.getWorld().get(y).toString();
                if (object == "Explosion"){
                    game.getWorld().clear(y);
                }
            }
            p = y;
        }
    }

}
