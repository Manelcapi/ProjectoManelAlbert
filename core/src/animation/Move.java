package animation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by cfgs on 11/05/17.
 */

public class Move extends ApplicationAdapter {

    private SpriteBatch bach;
    private TextureAtlas moveAtlas;
    private Animation animation;
    private float timePassed = 0;


    @Override
    public void create() {
        bach = new SpriteBatch();
        moveAtlas = new TextureAtlas(Gdx.files.internal("NINJA1.atlas"));
        animation = new Animation(1/30f,moveAtlas.getRegions());
    }

    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        bach.dispose();
        moveAtlas.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bach.begin();
        timePassed += Gdx.graphics.getDeltaTime();
        bach.draw((Texture) animation.getKeyFrame(timePassed, true), 300 , 500);
        bach.end();
    }
}


