package Screens;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import Connection.ConnectionBD;

/**
 * Created by cfgs on 30/05/17.
 */

public class AddPuntiacion implements Input.TextInputListener,Screen {

    private MyGdxGame game;
    private String text;
    private int score;
    private int time;
    private boolean fin;
    public AddPuntiacion(MyGdxGame game, int score, int time) {
        this.game = game;
        this.score = score;
        this.time = time;
        fin = false;
    }

    @Override
    public void input(String text) {
        try {
            addPuntuacion(text);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fin = true;
    }

    @Override
    public void canceled() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            Gdx.input.getTextInput(this,"Introduzca su nombre","","Nombre");
        }
        if(fin){
            game.setScreen(new MainMenu(game));
        }
    }
    public void addPuntuacion(String text) throws SQLException {
        try {
            Connection conect = ConnectionBD.getConnection();
            Statement sentencia = conect.createStatement();

            String sql = "INSERT INTO puntuaciones (`nombre`, `puntuacion`,`tiempo`) VALUES ('"+text+"', "+score+","+time+")";
            sentencia.executeUpdate(sql);
            conect.close();
        }catch (SQLIntegrityConstraintViolationException e2) {
            String sql = "Update puntuaciones set puntuacion="+score+", tiempo= '"+time+"' where nombre = '"+text+"'";
            Connection conect = ConnectionBD.getConnection();
            Statement sentencia = conect.createStatement();
            sentencia.executeUpdate(sql);
            conect.close();
        }catch (SQLException e1) {

            e1.printStackTrace();
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
