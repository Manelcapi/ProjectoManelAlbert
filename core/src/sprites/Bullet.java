package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import Tools.Colision;


/**
 * Created by cfgs on 11/05/17.
 */

public class Bullet extends Sprite{
    private float a,time;
    private Rectangle hitbox;
    private int speed;
    private String idPlayer;

    private Texture texture;

    private float x, y;
    Colision rect;
    public boolean remove = false;
    public Bullet(Texture texture){
        super(texture);
    }

    public Bullet (int x,int y ,float angle, Texture texture,String idPlayer) {
        time = 2;
        speed = 300;
        hitbox = new Rectangle(x,y,10,10);
        this.texture = texture;
        this.idPlayer = idPlayer;
        a = angle;
    }
    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
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

    /*public boolean isDead(){
        if(time<0)
            return true;
        return false;
    }*/
    public void draw (SpriteBatch batch){

        batch.draw(texture,hitbox.x,hitbox.y,10,10);
    }

    /*public void draw(SpriteBatch batch){
        batch.draw(texture,hitbox.x,hitbox.y,10,10);
    }*/

}