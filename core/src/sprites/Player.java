package sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import Screens.PlayScreen;

/**
 * Created by cfgs on 08/05/17.
 */

public class Player extends Sprite{

    public Vector2 previousPosition;
    private TextureRegion ninja1;
    private Rectangle hitbox;
    private float life = 1;

    public Player(PlayScreen screen){
        super(screen.getAtlas().findRegion("ninja1Down"));
        previousPosition = new Vector2(getX(),getY());
        ninja1 = new TextureRegion(getTexture(), 109, 2, 33,36);
        hitbox = new Rectangle(this.getX(),this.getY(),33,36);
        setRegion(ninja1);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getLife() {
        return life;
    }

    public void setLife(float life) {
        this.life = life;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
    public boolean hitMe(Rectangle hitbox) {
        return hitbox.overlaps(hitbox);
    }

    public void update(float dt){
    }


    public boolean hasMoved(){
        if(previousPosition.x != getX() || previousPosition.y != getY()){
            previousPosition.x = getX();
            previousPosition.y = getY();
            return true;
        }
        return false;

    }

    public void draw(Batch batch){
        super.draw(batch);
    }
}