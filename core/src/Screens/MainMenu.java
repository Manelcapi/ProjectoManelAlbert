package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

/**
 * Created by Manelcapi on 23/05/2017.
 */

public class MainMenu implements Screen {
    private static final int PLAY_BUTTON_WIDTH = 100;
    private static final int PLAY_BUTTON_HEIGHT = 40;
    private static final int EXIT_BUTTON_WIDTH = 100;
    private static final int EXIT_BUTTON_HEIGHT = 40;
    private static final int PLAY_BUTTON_Y = 300;
    private static final int EXIT_BUTTON_Y = 200;
    private MyGdxGame game;
    private Texture playButtonPress;
    private Texture playButtonUnpres;
    private Texture exitButtonUnpres;
    private Texture exitButtonPress;
    SpriteBatch batch;
    public MainMenu (MyGdxGame game){
        this.game = game;
        playButtonPress = new Texture("PlayButtonPress.jpg");
        playButtonUnpres = new Texture("PlayButtonUnpresss.jpg");
        exitButtonUnpres = new Texture("ExitButtonUnress.jpg");
        exitButtonPress = new Texture("ExitButtonPress.jpg");
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        int x = MyGdxGame.V_WIDTH/2 - PLAY_BUTTON_HEIGHT/2;
        if(Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.V_HEIGHT - Gdx.input.getY() < PLAY_BUTTON_HEIGHT + PLAY_BUTTON_Y && MyGdxGame.V_HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y){
            game.batch.draw(playButtonPress,x,PLAY_BUTTON_Y ,PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if(Gdx.input.isTouched()){
                game.setScreen(new PlayScreen(game));
            }
        }else
            game.batch.draw(playButtonUnpres,x,PLAY_BUTTON_Y ,PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        int xExit = MyGdxGame.V_WIDTH/2 - EXIT_BUTTON_HEIGHT/2;
        if(Gdx.input.getX() < xExit + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.V_HEIGHT - Gdx.input.getY() < EXIT_BUTTON_HEIGHT + EXIT_BUTTON_Y && MyGdxGame.V_HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y){
            game.batch.draw(exitButtonPress,xExit,EXIT_BUTTON_Y ,PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
            if(Gdx.input.isTouched()){
                Gdx.app.exit();
            }
        }else
            game.batch.draw(exitButtonUnpres,xExit,EXIT_BUTTON_Y ,PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);



        game.batch.end();
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
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
