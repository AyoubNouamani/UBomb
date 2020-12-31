package fr.ubx.poo.model.go;

import java.util.ArrayList;
import java.util.List;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.*;


public class Bomb extends GameObject {
    Position p;
    public int time;
    private int bombRange;
    private final List<Bomb> bombs = new ArrayList<>();

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
        }else{
            Decor explosion = new Explosion();
            game.getWorld().set(getPosition(), explosion);
            

        }
    }


}
