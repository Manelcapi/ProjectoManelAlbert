package sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import Screens.PlayScreen;

/**
 * Created by cfgs on 08/05/17.
 */

public class Player extends Sprite{

    public enum State{UP, DOWN, RIGHT, LEFT};
    public State currentState;
    public State previousState;
    public Vector2 previousPosition;
    private TextureRegion ninja1;
    private Rectangle hitbox;

    private Animation playerUp;
    private Animation playerDown;
    private Animation playerRight;
    private Animation playerLeft;

    public Player(PlayScreen screen){
        super(screen.getAtlas().findRegion("ninja1Down"));
        previousPosition = new Vector2(getX(),getY());
        currentState = State.DOWN;
        previousState = State.DOWN;
        ninja1 = new TextureRegion(getTexture(), 35, 2, 33,36);
        hitbox = new Rectangle(this.getX(),this.getY(),33,36);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++)
            frames.add(new TextureRegion(this.getTexture(), i * 35, 0, 32 , 37));
        playerDown = new Animation(0.1f, frames);
        playerDown.setPlayMode(Animation.PlayMode.LOOP);

        setRegion(ninja1);

    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Animation getADown(){
        return playerDown;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public void update(float dt){
    }

    public void definePlayer(){
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
