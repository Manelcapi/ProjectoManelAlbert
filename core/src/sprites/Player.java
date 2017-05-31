package sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private float life = 1;

    private TextureAtlas atlas;

    private Animation playerUp;
    private Animation playerDown;
    private Animation playerRight;
    private Animation playerLeft;

    public Player(PlayScreen screen){
        super(screen.getAtlas().findRegion("n1down0"));
        previousPosition = new Vector2(getX(),getY());
        currentState = State.DOWN;
        previousState = State.DOWN;
        ninja1 = new TextureRegion(getTexture(), 109, 2, 33,36);
        hitbox = new Rectangle(this.getX(),this.getY(),33,36);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        //Animacion hacia abajo
        frames.add(new TextureRegion(this.getTexture(), 109, 2, 33 , 36));
        frames.add(new TextureRegion(this.getTexture(), 144, 1, 32 , 37));
        frames.add(new TextureRegion(this.getTexture(), 212, 2, 32 , 37));
        playerDown = new Animation(0.1f, frames);
        playerDown.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        //Animacion hacia arriba
        frames.add(new TextureRegion(this.getTexture(), 73, 2, 34 , 36));
        frames.add(new TextureRegion(this.getTexture(), 178, 2, 32 , 36));
        frames.add(new TextureRegion(this.getTexture(), 314, 2, 32 , 36));
        playerUp = new Animation(0.1f, frames);
        playerUp.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        //Animacion hacia la derecha
        frames.add(new TextureRegion(this.getTexture(), 37, 2, 34 , 36));
        frames.add(new TextureRegion(this.getTexture(), 280, 2, 32 , 36));
        frames.add(new TextureRegion(this.getTexture(), 246, 2, 32 , 36));
        playerRight = new Animation(0.1f, frames);
        playerRight.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        //Animacion hacia la izquierda
        frames.add(new TextureRegion(this.getTexture(), 1, 2, 34 , 36));
        frames.add(new TextureRegion(this.getTexture(), 348, 3, 32 , 35));
        frames.add(new TextureRegion(this.getTexture(), 382, 3, 32 , 35));
        playerLeft = new Animation(0.1f, frames);
        playerLeft.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        setRegion(ninja1);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Animation getADown(){
        return playerDown;
    }

    public Animation getAUp(){
        return playerUp;
    }

    public Animation getARight(){
        return playerRight;
    }

    public Animation getALeft(){
        return playerLeft;
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
