package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by cfgs on 30/05/17.
 */

public class Puntuacion implements Screen {

    private SpriteBatch batch;
    private MyGdxGame game;
    private String linea;
    private BitmapFont font;

    public Puntuacion (MyGdxGame game) {
        this.game = game;
        font = new BitmapFont();
        batch = new SpriteBatch();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        File archivo = new File("Score.txt");
        FileReader fr = null;
        batch.begin();
        font.draw(batch,"PUNTUACIONES",320-12,500);
        font.setColor(Color.WHITE);

        try {
            URL url = new URL("http://192.168.2.248/score.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            int x = 0;
            while ((linea = in.readLine()) != null) {
                font.draw(batch,linea,(MyGdxGame.V_HEIGHT/2),(MyGdxGame.V_WIDTH/2)-x);
                x -= 30;
            }
            in.close();

        batch.end();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Gdx.input.justTouched()){
            game.setScreen(new MainMenu(game));
        }
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
