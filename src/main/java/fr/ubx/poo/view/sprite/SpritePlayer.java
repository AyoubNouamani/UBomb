/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

public class SpritePlayer extends SpriteGameObject {
    private final ColorAdjust effect = new ColorAdjust();
    

    public void SpriteColor(Sprite player){
        //effect.setContrast(-0.5);
    }

    public ColorAdjust getColorAdjust(){
        return effect;
    }

    public void SpriteErraseColor(Sprite player){

    }

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        updateImage();
    }

    @Override
    public void updateImage() {
        Player player = (Player) go;
        effect.setContrast(0.5);

        setImage(ImageFactory.getInstance().getPlayer(player.getDirection()));
    }
}