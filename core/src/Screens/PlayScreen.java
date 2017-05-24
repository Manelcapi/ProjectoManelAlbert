package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import Connection.Connection;
import Scenes.Hud;
import io.socket.client.IO;
import io.socket.emitter.Emitter;
import io.socket.client.Socket;
import sprites.Bullet;
import sprites.Enemy;
import sprites.Player;

/**
 * Created by cfgs on 15/05/17.
 */

public class PlayScreen implements Screen{

    private MyGdxGame game;
    private TextureAtlas atlas;
    private Animation walkRight;
    private float timePassed = 0;

    private Music music;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private boolean disparo;
    private final float UPDATE_TIME = 1/60f;
    private TmxMapLoader maploader;
    private int[] direcciones = {0,1,-1,90};
    private int direccion;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    float timer;
    SpriteBatch batch;
    Player player;
    String id;
    Texture friendPlayer;
    Texture mainPlayer;
    Texture bullet;
    Texture enemy;
    Map<String, Player> friendlyPlayers;
    TiledMapTileLayer cell;
    ArrayList<Bullet> bulletsList = new ArrayList<Bullet>();
    ArrayList<Enemy> obtaculos = new ArrayList<Enemy>();
    private Socket socket;
    private float timeSpawn;

    public PlayScreen(MyGdxGame game) {
        //Creando conexion
        Connection conection = new Connection();


        atlas = new TextureAtlas("Ninja1.pack");
        bullet = new Texture("ataque.png");
        enemy = new Texture("ninja1.png");
        walkRight = new Animation(1 / 60f, atlas.getRegions());

        //Creacion jugadores
        batch = new SpriteBatch();
        friendlyPlayers = new HashMap<String, Player>();
        this.game = game;
        gamecam = new OrthographicCamera();
        gamecam.position.x = 320;
        gamecam.position.y = 320;
        gamePort = new FitViewport(MyGdxGame.V_WIDTH , MyGdxGame.V_HEIGHT ,gamecam);
        hud = new Hud(game.batch);
        maploader = new TmxMapLoader();
        map = maploader.load("map1.tmx");
        cell = ((TiledMapTileLayer) (map.getLayers().get(0)));

        renderer = new OrthogonalTiledMapRenderer(map, 1 );

        music = MyGdxGame.manager.get("audio/music/TheSunRises.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
        //player = new Player(world, this);

        renderer = new OrthogonalTiledMapRenderer(map);
        //estalecemos el socket.
        socket = conection.connectSocket();
        configSocketEvents();

    }


    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (player != null) {
            //boolean x = cell.getCell((int) player.getX() * 20 / MyGdxGame.V_WIDTH, (int) player.getY() * 20 / MyGdxGame.V_HEIGHT).getTile().getProperties().containsKey
            //("blocked");
            float previousPositionX = player.getX();
            float previousPositionY = player.getY();

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (player.getX() > 32) {
                    player.setPosition(player.getX() + (-200 * dt), player.getY());
                    direccion = 2;
                } else {
                    player.setPosition(previousPositionX, previousPositionY);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (player.getX() < 608) {
                    player.setPosition(player.getX() + (+200 * dt), player.getY());
                    direccion = 1;

                } else {
                    player.setPosition(previousPositionX, previousPositionY);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (player.getY() < 608) {
                    player.setPosition(player.getX(), player.getY() + (+200 * dt));
                    direccion = 0;
                } else {
                    player.setPosition(previousPositionX, previousPositionY);
                }


            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (player.getY() > 64) {
                    player.setPosition(player.getX(), player.getY() + (-200 * dt));
                    direccion = 3;
                } else {
                    player.setPosition(previousPositionX, previousPositionY);
                }
            }


            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                Gdx.app.log("X DISPARO", "DISPARO: " + player.getX());
                Gdx.app.log("Y DISPARO", "DISPARO: " + player.getY());
                bulletsList.add(new Bullet((int) player.getX(), (int) player.getY(), direcciones[direccion] * (float) (Math.PI / 2), bullet));
                disparo = true;
                //UPDATESERVER PARA ENVIAR EL DISPARO A LOS DEMAS CLIENTES
                updateServer(Gdx.graphics.getDeltaTime());

            }
            disparo = false;
        }
    }


    public void update(float dt) {
        if (player != null) {
            player.update(dt);
        }

        gamecam.update();
        renderer.setView(gamecam);


    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateServer(Gdx.graphics.getDeltaTime());
        handleInput(Gdx.graphics.getDeltaTime());
        hud.update(Gdx.graphics.getDeltaTime());
        checkHitsBullets();
        //REnder game map
        renderer.render();
        if(timeSpawn >=3){
            int randomNum = 10 + (int)(Math.random() * 500);
            obtaculos.add(new Enemy( randomNum, 500, direcciones[3] * (float) (Math.PI / 2), enemy));
            timeSpawn = 0;
        }
        else {
           timeSpawn += delta;
        }
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        batch.begin();

        if (player != null) {
            player.draw(batch);

        }
        //dibujar personajes amigos
        for (Map.Entry<String, Player> entry : friendlyPlayers.entrySet()) {
            entry.getValue().draw(batch);
        }
        for (Enemy b : obtaculos) {
            b.draw(batch);
        }
        for (Bullet b : bulletsList) {
            b.draw(batch);
            //b.render(batch);
        }
        batch.end();
        for (Bullet b : bulletsList) {
            b.update(Gdx.graphics.getDeltaTime());
        }
        for (Enemy b : obtaculos) {
            b.update(Gdx.graphics.getDeltaTime());
        }
        deleteBullets();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

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
        map.dispose();
        renderer.dispose();
        mainPlayer.dispose();
        friendPlayer.dispose();
        bullet.dispose();
        enemy.dispose();


    }

    public void updateServer(float dt) {
        timer += dt;
        if (timer > UPDATE_TIME && player != null && player.hasMoved()) {
            JSONObject data = new JSONObject();
            try {
                data.put("x", player.getX());
                data.put("y", player.getY());
                String path = ((FileTextureData) player.getTexture().getTextureData()).getFileHandle().path();
                data.put("texture", path);
                socket.emit("playerMoved", data);

            } catch (JSONException e) {
                Gdx.app.log("SOCKET.IO", "Error enviando datos de recarga");
            }
        }
        if (timer > UPDATE_TIME && player != null && disparo) {
            JSONObject data = new JSONObject();
            try {
                data.put("x", player.getX());
                data.put("y", player.getY());
                data.put("direction", direccion);
                socket.emit("playerShoot", data);

            } catch (JSONException e) {
                Gdx.app.log("SOCKET.IO", "Error enviando datos de recarga");
            }
        }
    }


    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
                player = new Player(PlayScreen.this);
                int randomNum = 10 + (int)(Math.random() * 500);

                player.setPosition(randomNum, 60);
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    id = data.getString("id");
                    Gdx.app.log("SocketID", "My ID: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketID", "Error Estableciendo ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    String playerId = data.getString("id");
                    Gdx.app.log("SocketID", "Nuevo jugador Conectado: " + id);
                    friendlyPlayers.put(playerId, new Player(PlayScreen.this));
                } catch (JSONException e) {
                    Gdx.app.log("SocketID", "Error estableciendo nuevo jugador");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    id = data.getString("id");
                    friendlyPlayers.remove(id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketID", "Error desconectando jugador");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    String texture = data.getString("texture");
                    //Texture texutraStep = new Texture(texture);
                    Gdx.app.log("SocketID", "move: " + texture);
                    if (friendlyPlayers.get(playerId) != null) {
                        friendlyPlayers.get(playerId).setPosition(x.floatValue(), y.floatValue());
                        Rectangle hitbox = new Rectangle(x.floatValue(), y.floatValue(), 33, 36);
                        friendlyPlayers.get(playerId).setHitbox(hitbox);
                        //friendlyPlayers.get(playerId).setTexture(texutraStep);
                    }

                } catch (JSONException e) {

                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    for (int i = 0; i < objects.length(); i++) {
                        Player coopPlayer = new Player(PlayScreen.this);
                        Vector2 position = new Vector2();
                        position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                        coopPlayer.setPosition(position.x, position.y);
                        coopPlayer.setHitbox(new Rectangle(position.x, position.y, 33, 36));
                        friendlyPlayers.put(objects.getJSONObject(i).getString("id"), coopPlayer);
                    }
                } catch (JSONException e) {

                }
            }
        }).on("playerShoot", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    double x = data.getDouble("x");
                    double y = data.getDouble("y");
                    int direction = data.getInt("direction");
                    //Texture texutraStep = new Texture(texture);
                    Gdx.app.log("SocketID", "shoot : " + direction + " posx; " + x + " posy: " + y);
                    if (friendlyPlayers.get(playerId) != null) {
                        bulletsList.add(new Bullet((int) x, (int) y, direcciones[direction] * (float) (Math.PI / 2), bullet));
                    }

                } catch (JSONException e) {

                }
            }
        });
    }

    private Array<Rectangle> getTiles(int startX, int startY, int endX, int endY) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        Array<Rectangle> rectangles = new Array<Rectangle>();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = new Rectangle(x * 32, y * 32, 32, 32);
                    rectangles.add(rect);
                }
            }
        }
        return rectangles;
    }

    //Chekea si la vala a dado a un enemigo
    public void checkHitsBullets() {

        Iterator<Enemy> iterEnemy = obtaculos.iterator();
        while (iterEnemy.hasNext()) {
            Enemy enemy = iterEnemy.next();
            Iterator<Bullet> iterBul = bulletsList.iterator();
            while (iterBul.hasNext()) {
                Bullet b = iterBul.next();

                if (enemy.hitMe(b.getHitbox())) {

                    iterBul.remove();
                    iterEnemy.remove();
                    hud.addScore(10);
                }
            }

        }
    }

    //borra las balas que sobresalen del mapa
    public void deleteBullets() {

        Iterator<Bullet> iterBul = bulletsList.iterator();
        while (iterBul.hasNext()) {
            Bullet b = iterBul.next();
            if(b.getHitbox().getX() > 650 || b.getHitbox().getX() < 0 || b.getHitbox().getY()> 650 || b.getHitbox().getY()< 0){
                iterBul.remove();
            }
        }


    }

}
