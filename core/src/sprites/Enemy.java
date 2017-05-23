package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import Tools.Colision;


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
    public Enemy(Texture texture){
        super(texture);
    }

    public Enemy (int x,int y ,float angle, Texture texture) {
        time = 2;
        speed = 10;
        hitbox = new Rectangle(x,y,40,40);
        this.texture = texture;
        a = angle;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void update(float delta) {
        hitbox.x += speed * (float)Math.sin(a)*delta;
        hitbox.y += speed * (float)Math.cos(a)*delta;
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

        batch.draw(texture,hitbox.x,hitbox.y,40,40);
    }
    public boolean hitMe(Rectangle hitbox) {
        return this.hitbox.overlaps(hitbox);
    }

    /*public void draw(SpriteBatch batch){
        batch.draw(texture,hitbox.x,hitbox.y,10,10);
    }*/

}