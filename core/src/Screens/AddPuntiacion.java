package Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by cfgs on 30/05/17.
 */

public class AddPuntiacion implements Input.TextInputListener,Screen {

    private MyGdxGame game;
    private String text;
    private int score;
    private boolean fin;
    public AddPuntiacion(MyGdxGame game, int score) {
        this.game = game;
        this.score = score;
        fin = false;
    }

    @Override
    public void input(String text) {

        try {
            URL                url;
            URLConnection      urlConn;
            DataOutputStream   dos;
            DataInputStream    dis;

            url = new URL("http://192.168.2.248/score.txt");
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");
            dos = new DataOutputStream (urlConn.getOutputStream());
            dos.writeUTF(text);
            dos.flush();
            dos.close();


            /*URL                url;
            URLConnection      urlConn;
            DataOutputStream dos;
            DataInputStream dis;
            url = new URL("http://192.168.2.248/score.txt");
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");
            dos = new DataOutputStream (urlConn.getOutputStream());
            dos.writeBytes(text +" : " +score);
            dos.flush();
            dos.close();





            /*URL url = new URL("http://192.168.2.248/score.txt");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(text + " : " + score);
            out.close();*/

            fin = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
