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

import java.util.List;
import java.util.ArrayList;



public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private final List<Sprite> sprites = new ArrayList<>();
    private List<Sprite> spriteMonstertab;
    private boolean move = true;
    public int time = 0;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
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
        // cree un tableau dynamique de spritemonster
        spriteMonstertab = new ArrayList<>();
        game.getWorld().getMonsterTab().forEach((monster) -> spriteMonstertab.add(SpriteFactory.createMonster(layer, monster)));
        move = false;
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
            move = true;
        }

        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
            move = true;
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
            move = true;
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
            move = true;
        }
        if (input.isBomb()){
            player.requestBomb();
            move = true;
        }
        if (input.isKey()){
            player.requestKey();
            move = true;
        }
        input.clear();
    }

    public void processMonster(Monster monster) {
        // effectuer un mvt tout les seconds
        Direction mons = fr.ubx.poo.game.Direction.random();

        if (mons == Direction.W ){
            monster.requestMove(Direction.W);
        }
        else if (mons == Direction.E ){
            monster.requestMove(Direction.E);
        }
        else if (mons == Direction.S ){
            monster.requestMove(Direction.S);
        }
        else if (mons == Direction.N ){
            monster.requestMove(Direction.N);
        }                
    }

    private void loseLive(int sec){
        if (!player.invincible){
            for (Monster monster : game.getWorld().getMonsterTab()){
                if(monster.getPosition().equals(player.getPosition()) ){
                    player.decreasLive();
                }
            }

            player.inviTime = sec;
        }
    }
  
    private void update(long now) {
        //mvt monstres chaque second
        int sec = (int)((System.currentTimeMillis()/1000)%10);
        if (sec != time){            
            if (!game.getWorld().getMonsterTab().isEmpty()){
                for (int i = 0; i < game.initLevels; i++){
                    for (Monster monster : game.getWorld(i).getMonsterTab()){
                        processMonster(monster);
                        monster.update(now);
                    }
                }
            }

            if (sec - player.inviTime > 1 || sec - player.inviTime < 0){
                player.invincible = false;
            }

            //mise a jour des bombes possées
            game.bombCountdown();

            this.time = sec;
            move = true;
        }

        // on supprime tous les decors et on le re-initilize quand c'est necessaire
        //update sprites : mvt des monstres ou joueur
        loseLive(sec);
        if (move){
            player.update(now);
            sprites.forEach(Sprite::remove); 
            sprites.clear();
            initialize(stage, game);
        }
        
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
    
    private void render() {
        sprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
        // un tab pour render
        spriteMonstertab.forEach(Sprite::render);   
    }

    public void start() {
        gameLoop.start();
    }
}
