package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Tools.Colision;


/**
 * Created by cfgs on 11/05/17.
 */

public class Bullet extends Sprite{
    float a,time;
    Rectangle hitbox;
    int speed;

    Texture texture;

    float x, y;
    Colision rect;
    public boolean remove = false;
    public Bullet(Texture texture){
        super(texture);
    }

    public Bullet (int x,int y ,float angle) {
        time = 2;
        speed = 300;
        hitbox = new Rectangle(x,y,10,10);
        texture = new Texture("ataque.png");
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
        batch.draw(texture, x, y);
    }

    public boolean isDead(){
        if(time<0)
            return true;

        return false;
    }
    public void draw (SpriteBatch batch){

            batch.draw(texture,hitbox.x,hitbox.y,10,10);
    }

    /*public void draw(SpriteBatch batch){
        batch.draw(texture,hitbox.x,hitbox.y,10,10);
    }*/

}
