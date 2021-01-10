package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.*;

import java.util.List;

import fr.ubx.poo.model.go.character.Monster;


public class Bomb extends GameObject {
    // peut etre ajouter direction et du coup getDirection
    public int time;
    private int level;
    private Direction[] tab = {Direction.N, Direction.S, Direction.E, Direction.W};
    private int bombRange;

    public Bomb(Game game, Position position, int level){
        super(game, position);
        this.level = level - 1;
        this.bombRange = game.getPlayer().getBombsRange();
        this.time = 4;
    }

    public void requestBomb(){
        if (game.getPlayer().getBombsValue() > 0){
            Position p = game.getPlayer().getPosition();
            Decor bomb = new Bomb4();
            game.getWorld(level).set(p, bomb);
            game.getPlayer().decreasBomb();
        }
    }

    //regarde si a l'endroit de l'explosion s'y trouve un Monster ou un Player 
    private void explosionCharacter(Position pBomb){
        //Player
        if (game.getPlayer().getPosition().equals(pBomb)){
            game.getPlayer().decreasLive();
        }

        //Monster
        for (int i = 0; i < game.initLevels; i++){
            int x = 0;
            List<Monster> monsters = game.getWorld(level).getMonsterTab();
            for (Monster monster : monsters){
                if (monster.getPosition().equals(pBomb)){
                    monsters.remove(x);
                }
            }
        }

        //Explosion bomb
        for (Bomb bomb : game.getBombTab().get(game.getAcutualLevel()-1)){
            if (bomb.getPosition().equals(pBomb) && getPosition()!= bomb.getPosition()){
                if (bomb.time > 1){
                    bomb.time = 1;
                }
            }         
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
            if (game.getWorld(level).isEmpty(x)){
                game.getWorld(level).set(x, explosion);
            }else{    
                String object = game.getWorld(level).get(x).toString();
                //Si deux caisses se suivent l'explosion s'arrete sur la premiere
                if (object == "Box" && object2 == "Box"){
                    break;
                }
                else if (object != "Tree" 
                    && object != "Stone" 
                    && object != "DoorNextClosed"
                    && object != "DoorNextOpened"
                    && object != "DoorPrevOpened"
                    && object != "Key"
                    && object != "Princess"){
                        game.getWorld(level).set(x, explosion);
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
        World world = game.getWorld(level);
        if(time == 3){
            Decor bomb = new Bomb3();
            world.set(getPosition(), bomb);
        }   
        else if (time == 2){
            Decor bomb = new Bomb2();
            world.set(getPosition(), bomb);
        }
        else if (time == 1){
            Decor bomb = new Bomb1();
            world.set(getPosition(), bomb);
        }else if (time == 0){
            //creer explosion
            Position p = getPosition();
            world.clear(p);
            Decor explosion = new Explosion();
            world.set(p, explosion);
            for (Direction x : tab){
                explosionDecor(x, explosion);
            }
        }else if (time == -1){
            for (Direction x : tab){
                erasseExplosion(x);
            }
            game.getPlayer().increasBomb();
        }
    }

    public void erasseExplosion(Direction x){
        Position p = getPosition();
        World world = game.getWorld(level);
        world.clear(p);
        for (int i = 0; i<bombRange; i++){
            Position y = x.nextPosition(p);    
            if (!world.isEmpty(y)){
                String object = world.get(y).toString();
                if (object == "Explosion"){
                    world.clear(y);
                }
            }
            p = y;
        }
    }
}
