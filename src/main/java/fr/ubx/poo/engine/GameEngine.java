/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;



public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final Monster monster;
    private final ArrayList<Monster> monstertab;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private Sprite spriteMonster;
    private ArrayList<Sprite>  spriteMonstertab;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        this.monster = game.getMonster();
        this.monstertab = game.getmonstertab();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        spriteMonster = SpriteFactory.createMonster(layer, monster);
        // cree un tableau dynamique de spritemonster
       spriteMonstertab = new ArrayList<>();
       for (Monster monster : monstertab) {
            spriteMonstertab.add(SpriteFactory.createMonster(layer, monster));
        }

    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);
              
                // Do actions
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }
    
 
    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }

        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isBomb()){
            player.requestBomb();
        }
        if (input.isKey()){
            player.requestKey();
        }
        input.clear();
    }

    public void processMonster() {
        // effectuer un mvt tout les seconds
        Direction mons = fr.ubx.poo.game.Direction.random();

        if (mons == Direction.W ){
            monster.requestMove(Direction.W);
        }
        if (mons == Direction.E ){
            monster.requestMove(Direction.E);
        }
        if (mons == Direction.S ){
            monster.requestMove(Direction.S);
        }
        if (mons == Direction.N ){
            monster.requestMove(Direction.N);
        }                
    }

    public void processMonstertab() {
        //cette fonction sert presque a rien 
        // il faut que chaque monster soit random ****a modifier*****
        Direction mons = fr.ubx.poo.game.Direction.random();
        Direction mons1 = fr.ubx.poo.game.Direction.random();
        Direction mons2 = fr.ubx.poo.game.Direction.random();
        Direction mons3 = fr.ubx.poo.game.Direction.random();
        
        Direction [] directiontab = {mons,mons1,mons2,mons3};
        int size = monstertab.size();
        for (int i =0; i<size;i++) {
            for(int j=0; j<3;j++){

            if (directiontab[j] == Direction.W ){
                monstertab.get(i).requestMove(Direction.W);
            }
            if (directiontab[j]== Direction.E ){
                monstertab.get(i).requestMove(Direction.E);
            }
           
            if (directiontab[j]== Direction.S ){
                monstertab.get(i).requestMove(Direction.S);
            }
            if (directiontab[j]== Direction.N ){
                monstertab.get(i).requestMove(Direction.N);
            }  
            }
        }
       
        
        
    }


    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {
        player.update(now);
        monster.update(now);
        //tableau pour update
        for (Monster monsters : monstertab) {
            monsters.update(now);
        }

      //  for (Monster monster : monstertab) {
         int sec = Character.getNumericValue(String.valueOf(now).charAt(4));

            if (sec != monster.time){
                // Actualize monster 
                processMonster();
                processMonstertab();
                //Actualize bombs
                monster.time = sec;
            }
        
        
        // on supprime tous les decors et on le re-initilize 
        sprites.forEach(Sprite::remove); 
        sprites.clear();
        initialize(stage, game);
        
        //apres on regrde si le jouer a fini (gagné ou perdu)
        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
    }

    private void render() {
        sprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
        spriteMonster.render();
        // un tab pour render
        for (Sprite m : spriteMonstertab) {
            m.render();
        }
        
    }

    public void start() {
        gameLoop.start();
    }
}
