package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by cfgs on 15/05/17.
 */

public class Enemy extends Sprite{
    Vector2 previousPosition;
    public Enemy(Texture texture){
        super(texture);
        previousPosition = new Vector2(getX(),getY());

    }

}
