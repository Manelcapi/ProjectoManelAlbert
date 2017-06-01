package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Connection.ConnectionBD;


/**
 * Created by cfgs on 30/05/17.
 */

public class Puntuacion implements Screen {

    private SpriteBatch batch;
    private MyGdxGame game;
    private String linea;
    private BitmapFont font;

    public Puntuacion(MyGdxGame game) {
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
        batch.begin();
        String index = "Top 10 Puntuaciones";
        GlyphLayout layout1 = new GlyphLayout(font, index);
        float fontA = (MyGdxGame.V_WIDTH / 2) + (- layout1.width) / 2;
        font.draw(batch, index, fontA, 500);
        font.setColor(Color.WHITE);

        try {
            Connection conexion = ConnectionBD.getConnection();
            Statement sentencia = conexion.createStatement();
            String sql = "SELECT * FROM puntuaciones order by puntuacion desc,tiempo desc";
            ResultSet resultat = sentencia.executeQuery(sql);
            int i =0;
            int y = 1;
            while (y < 11 && resultat.next()) {
                String nombre = resultat.getString(1);
                int puntuacion = resultat.getInt(2);
                int time = resultat.getInt(3);

                String puntos = y+".- Nombre: "+nombre + " - Puntuacion: " + puntuacion+" -Tiempo: "+time;
                GlyphLayout layout = new GlyphLayout(font, puntos);
                float fontX = (MyGdxGame.V_WIDTH / 2) + (- layout.width) / 2;
                float fontY = 470 -i + ( - layout.height) / 2;
                font.draw(batch, puntos, fontX, fontY);
                i = i +30;
                y++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        batch.end();


        if (Gdx.input.justTouched()) {
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
