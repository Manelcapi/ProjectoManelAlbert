package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import Screens.PlayScreen;
import Tools.Colision;

import static java.lang.Math.sqrt;


/**
 * Created by cfgs on 15/05/17.
 */

public class Enemy extends Sprite{
    float a,time;
    Rectangle hitbox;
    int speed;
    Texture texture;
    float x, y;
    Colision rect;
    public boolean remove = false;

    private Animation explosion;

    public Enemy(Texture texture){
        super(texture);
    }

    public Enemy (int x, int y , float angle, Texture texture, PlayScreen screen) {
        super(screen.getAtlasEx().findRegion("Ex0"));
        this.texture = texture;
        time = 2;
        speed = 1;
        hitbox = new Rectangle(x,y,30,30);
        a = angle;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        int i = 0;
        //Animacion de explosion
        for (i = 0;i < 6; i++) {
            frames.add(new TextureRegion(screen.getAtlasEx().getRegions().get(i)));
        }
        explosion = new Animation(0.1f, frames);
        //explosion.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

    }

    public Animation getExplosion(){
        return explosion;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void update(float delta , float x, float y) {
        float xe = x - hitbox.x;
        float ye = y - hitbox.y;
        float hyp = (float) sqrt(xe*xe + ye*ye);
        xe /= hyp;
        ye /= hyp;
        hitbox.x += speed * xe;//(float)Math.sin(a)*delta;
        hitbox.y += speed * ye;//(float)Math.cos(a)*delta;
        time -= delta;
    }


    public void render (SpriteBatch batch) {
        batch.draw(texture, 33, 36);
    }

    /*public boolean isDead(){
        if(time<0)
            return true;

        return false;
    }*/
    public void draw (SpriteBatch batch){
        super.draw(batch);
        batch.draw(texture,hitbox.x,hitbox.y,30,30);
    }

    public boolean hitMe(Rectangle hitbox) {
        return this.hitbox.overlaps(hitbox);
    }

    /*public void draw(SpriteBatch batch){
        batch.draw(texture,hitbox.x,hitbox.y,10,10);
    }*/

}
