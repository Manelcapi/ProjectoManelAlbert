package sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;

import Screens.PlayScreen;

/**
 * Created by cfgs on 19/05/17.
 */

public abstract class Enemy extends Sprite {

    protected PlayScreen screen;

    public Enemy(PlayScreen screen, float x, float y){
        this.screen = screen;
        setPosition(x,y);
    }

    protected abstract void defineEnemy();

}
