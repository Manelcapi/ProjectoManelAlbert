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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
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

import Connection.Connection;
import Scenes.Hud;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import sprites.Bullet;
import sprites.Enemy;
import sprites.Player;

import static java.lang.Math.sqrt;

/**
 * Created by cfgs on 15/05/17.
 */

public class PlayScreen implements Screen {

    private MyGdxGame game;
    private TextureAtlas atlas;
    private Animation walkRight;
    private Texture blank;
    private float life = 1;
    private float timePassed = 0;

    private Music music;
    private Music shootsound;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private boolean disparo;
    private final float UPDATE_TIME = 1 / 60f;
    private TmxMapLoader maploader;
    private int[] direcciones = {0, 1, -1, 90};
    private int direccion;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private float stateTimer;
    private TextureAtlas atlasExplosion;
    private TextureRegion region;
    private TextureRegion regionPlayers;

    float timer;
    SpriteBatch batch;
    Player player;
    String id;
    Texture bullet;
    Texture enemy;
    Map<String, Player> friendlyPlayers;
    ArrayList<Bullet> bulletsList = new ArrayList<Bullet>();
    ArrayList<Enemy> obtaculos = new ArrayList<Enemy>();
    private Socket socket;
    private float timeSpawn;

    public PlayScreen(MyGdxGame game) {
        this.game = game;
        //Creando conexion
        Connection conection = new Connection();

        //textura de la vida
        blank = new Texture("blank.png");

        atlas = new TextureAtlas("ninja1.pack");
        Array<TextureRegion> frames = new Array<TextureRegion>();
        stateTimer = 0;

        enemy = new Texture("limo1.png");
        bullet = new Texture("ataque.png");

        //Creacion jugadores
        batch = new SpriteBatch();
        friendlyPlayers = new HashMap<String, Player>();

        //Parametros para la camara

        atlasExplosion = new TextureAtlas("explosion.pack");

        gamecam = new OrthographicCamera();
        gamecam.position.x = 320;
        gamecam.position.y = 320;
        gamePort = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, gamecam);

        hud = new Hud(game.batch);
        maploader = new TmxMapLoader();
        //cargamos el mapa
        map = maploader.load("map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);

        //carga de la musica del juego
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


    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TextureAtlas getAtlasEx() {
        return atlasExplosion;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (player != null) {
            float previousPositionX = player.getX();
            float previousPositionY = player.getY();

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (player.getX() > 32) {
                    stateTimer += dt;
                    region = (TextureRegion) player.getALeft().getKeyFrame(stateTimer);
                    player.setRegion(region);
                    player.setPosition(player.getX() + (-200 * dt), player.getY());
                    direccion = 2;
                } else {
                    player.setPosition(previousPositionX, previousPositionY);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (player.getX() < 580) {
                    stateTimer += dt;
                    region = (TextureRegion) player.getARight().getKeyFrame(stateTimer);
                    player.setRegion(region);
                    player.setPosition(player.getX() + (+200 * dt), player.getY());
                    direccion = 1;

                } else {
                    player.setPosition(previousPositionX, previousPositionY);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (player.getY() < 608) {
                    stateTimer += dt;
                    region = (TextureRegion) player.getAUp().getKeyFrame(stateTimer);
                    player.setRegion(region);
                    player.setPosition(player.getX(), player.getY() + (+200 * dt));
                    direccion = 0;
                } else {
                    player.setPosition(previousPositionX, previousPositionY);
                }

            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (player.getY() > 50) {
                    stateTimer += dt;
                    region = (TextureRegion) player.getADown().getKeyFrame(stateTimer);
                    player.setRegion(region);
                    player.setPosition(player.getX(), player.getY() + (-200 * dt));
                    direccion = 3;
                } else {
                    player.setPosition(previousPositionX, previousPositionY);
                }
            }


            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                Gdx.app.log("X DISPARO", "DISPARO: " + player.getX());
                Gdx.app.log("Y DISPARO", "DISPARO: " + player.getY());
                bulletsList.add(new Bullet((int) player.getX(), (int) player.getY(), direcciones[direccion] * (float) (Math.PI / 2), bullet, id));
                shootsound = MyGdxGame.manager.get("audio/efects/Shoot.mp3", Music.class);
                shootsound.setVolume(0.3f);
                shootsound.play();
                disparo = true;
                //UPDATESERVER PARA ENVIAR EL DISPARO A LOS DEMAS CLIENTES
                updateServer(Gdx.graphics.getDeltaTime());

            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
                game.setScreen(new MainMenu(game));
                music.stop();
                socket.disconnect();
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateServer(Gdx.graphics.getDeltaTime());
        handleInput(Gdx.graphics.getDeltaTime());
        hud.update(Gdx.graphics.getDeltaTime());
        checkHitsBullets();
        checkHitsEnemy();
        if (!friendlyPlayers.isEmpty()) {
            checkHitsWIthFrendy();
        }
        //REnder game map
        renderer.render();
        hud.stage.draw();
        batch.begin();

        if (player != null) {
            player.draw(batch);

        }
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        //linea de vida

        batch.draw(blank, 0, 0, Gdx.graphics.getWidth() * life, 5);

        //dibujar personajes amigos
        for (Map.Entry<String, Player> entry : friendlyPlayers.entrySet()) {
            entry.getValue().draw(batch);
        }
        for (Enemy b : obtaculos) {
            b.draw(batch);
        }
        for (Bullet b : bulletsList) {
            b.draw(batch);
        }
        batch.end();
        for (Bullet b : bulletsList) {
            b.update(Gdx.graphics.getDeltaTime());
        }
        for (Enemy b : obtaculos) {
            if (friendlyPlayers.isEmpty()) {
                b.update(Gdx.graphics.getDeltaTime(), player.getX(), player.getY());
            } else {

                for (Map.Entry<String, Player> entry : friendlyPlayers.entrySet()) {
                    float xjugador = player.getX() - b.getX();
                    float yjugador = player.getY() - b.getY();
                    float xOtroJuagador = entry.getValue().getX() - b.getX();
                    float yOtroJuagador = entry.getValue().getY() - b.getY();
                    float hypOtroJugador = (float) sqrt(xOtroJuagador * xOtroJuagador + yOtroJuagador * yOtroJuagador);
                    float hypJugadorPrincipal = (float) sqrt(xjugador * xjugador + yjugador * yjugador);
                    if (hypJugadorPrincipal - hypOtroJugador > 0)
                        b.update(Gdx.graphics.getDeltaTime(), entry.getValue().getX(), entry.getValue().getY());
                    else if (hypJugadorPrincipal - hypOtroJugador < 0)
                        b.update(Gdx.graphics.getDeltaTime(), player.getX(), player.getY());
                }
            }

        }
        deleteBullets();
        if (player != null && player.getLife() < 0) {
            music.stop();
            game.setScreen(new GameOverScreen(game, hud.getScore(),hud.getTime()));
            socket.disconnect();
            dispose();

        }
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
                data.put("direction", direccion);
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
                int randomNum = 10 + (int) (Math.random() * 500);

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
                    int direction = data.getInt("direction");
                    //Texture texutraStep = new Texture(texture);
                    Gdx.app.log("SocketID", "move: " + direction);
                    if (friendlyPlayers.get(playerId) != null) {
                        stateTimer += Gdx.graphics.getDeltaTime();
                        if (direction == 0) {
                            regionPlayers = (TextureRegion) friendlyPlayers.get(playerId).getAUp().getKeyFrame(stateTimer);
                            friendlyPlayers.get(playerId).setRegion(regionPlayers);
                        } else if (direction == 1) {
                            regionPlayers = (TextureRegion) friendlyPlayers.get(playerId).getARight().getKeyFrame(stateTimer);
                            friendlyPlayers.get(playerId).setRegion(regionPlayers);

                        } else if (direction == 2) {
                            regionPlayers = (TextureRegion) friendlyPlayers.get(playerId).getALeft().getKeyFrame(stateTimer);
                            friendlyPlayers.get(playerId).setRegion(regionPlayers);

                        } else {
                            regionPlayers = (TextureRegion) friendlyPlayers.get(playerId).getADown().getKeyFrame(stateTimer);
                            friendlyPlayers.get(playerId).setRegion(regionPlayers);

                        }
                        friendlyPlayers.get(playerId).setPosition(x.floatValue(), y.floatValue());
                        Rectangle hitbox = new Rectangle(x.floatValue(), y.floatValue(), 33, 36);
                        friendlyPlayers.get(playerId).setHitbox(hitbox);
                        //friendlyPlayers.get(playerId).setTexture(texutraStep);
                    }

                } catch (JSONException e) {

                }
            }
        }).on("addEnemy", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    int x = data.getInt("x");
                    int y = data.getInt("y");
                    Gdx.app.log("ADD Enemy", "Nuevo Enemigo ");
                    obtaculos.add(new Enemy((int) x, (int) y, direcciones[3] * (float) (Math.PI / 2), enemy));
                } catch (JSONException e) {
                    Gdx.app.log("SocketID", "Error estableciendo nuevo jugador");
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
                        bulletsList.add(new Bullet((int) x, (int) y, direcciones[direction] * (float) (Math.PI / 2), bullet, playerId));
                        music = MyGdxGame.manager.get("audio/efects/Shoot.mp3", Music.class);
                        music.setVolume(0.3f);
                        music.play();
                    }

                } catch (JSONException e) {

                }
            }
        });
    }

    //Chekea si la vala a dado a un enemigo
    public void checkHitsBullets() {

        TextureRegion region;

        Iterator<Enemy> iterEnemy = obtaculos.iterator();
        while (iterEnemy.hasNext()) {
            Enemy enemy = iterEnemy.next();
            Iterator<Bullet> iterBul = bulletsList.iterator();
            while (iterBul.hasNext()) {
                Bullet b = iterBul.next();
                if (enemy.hitMe(b.getHitbox())) {
                    if (b.getIdPlayer() == id) {
                        hud.addScore(10);
                    }
                    iterBul.remove();
                    iterEnemy.remove();
                }
            }

        }
    }
    public void checkHitsEnemy() {
        Iterator<Enemy> iterEnemy = obtaculos.iterator();
        while (iterEnemy.hasNext()) {
            Enemy enemy = iterEnemy.next();
            Rectangle hitboxPlayer = new Rectangle(player.getX(), player.getY(), 33, 36);
            if (hitboxPlayer.overlaps(enemy.getHitbox())) {
                player.setLife(player.getLife() - (float) 0.1);
                life = player.getLife();
                iterEnemy.remove();
                Gdx.app.log("TOCADO", "-vida");
            }
        }
    }

    private void checkHitsWIthFrendy() {
        Iterator<Enemy> iterEnemy = obtaculos.iterator();
        while (iterEnemy.hasNext()) {
            Enemy enemy = iterEnemy.next();
            for (Map.Entry<String, Player> entry : friendlyPlayers.entrySet()) {
                Rectangle hitboxPlayerFrendly = new Rectangle(entry.getValue().getX(), entry.getValue().getY(), 33, 36);
                if (hitboxPlayerFrendly.overlaps(enemy.getHitbox())) {
                    iterEnemy.remove();
                }
            }
        }
    }

    //borra las balas que sobresalen del mapa
    public void deleteBullets() {
        Iterator<Bullet> iterBul = bulletsList.iterator();
        while (iterBul.hasNext()) {
            Bullet b = iterBul.next();
            if (b.getHitbox().getX() > 650 || b.getHitbox().getX() < 0 || b.getHitbox().getY() > 650 || b.getHitbox().getY() < 0) {
                iterBul.remove();
            }
        }
    }
}
