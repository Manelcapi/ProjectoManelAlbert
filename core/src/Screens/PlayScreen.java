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
import java.util.Map;

import Scenes.Hud;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import sprites.Bullet;
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
    private final float UPDATE_TIME = 1/60f;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    float timer;
    SpriteBatch batch;
    Player player;
    String id;
    Texture friendPlayer;
    Texture mainPlayer;
    Map<String, Player> friendlyPlayers;
    TiledMapTileLayer cell ;
    ArrayList<Bullet> bulletsList = new ArrayList<Bullet>();
    ArrayList<Rectangle> obtaculos = new ArrayList<Rectangle>();
    private Socket socket;

    public PlayScreen(MyGdxGame game){

        atlas = new TextureAtlas("Ninja1.pack");
        walkRight = new Animation(1/60f,atlas.getRegions());

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
        int tileSize = (int) cell.getTileWidth();
        int mapWidth = cell.getWidth() * tileSize;
        int mapHeight = cell.getHeight() * tileSize;

        renderer = new OrthogonalTiledMapRenderer(map, 1 );

        music = MyGdxGame.manager.get("audio/music/TheSunRises.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
        getRectangles();
        //player = new Player(world, this);

        connectSocket();
        configSocketEvents();

    }

    private void getRectangles() {
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if(player != null){
            boolean x = cell.getCell((int)player.getX()*20/MyGdxGame.V_WIDTH,(int)player.getY()*20/MyGdxGame.V_HEIGHT).getTile().getProperties().containsKey
                    ("blocked");
            float previousPositionX = player.getX();
            float previousPositionY = player.getY();

            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                if(player.getX()>32){
                    player.setPosition(player.getX()+(-100*dt),player.getY());
                }else{
                    player.setPosition(previousPositionX,previousPositionY);
                    Gdx.app.log("Toque izquierda","data");
                }

            }if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){

                Array<Rectangle> tiles = getTiles((int)(player.getX()/32 + 1), (int)(player.getY()/32 - 1),
                        (int)(player.getX()/32 + 1), (int)(player.getY()/32 + 1));

                for (Rectangle tile : tiles) {

                    if(player.getHitbox().overlaps(tile)) {
                        Gdx.app.log("overlap right","data");
                        player.setPosition(tile.x - player.getWidth() , player.getY());
                    }
                }

                if(player.getX()< 589){
                    player.setPosition(player.getX()+(+100*dt),player.getY());
                }else{
                    player.setPosition(previousPositionX,previousPositionY);
                    Gdx.app.log("Toque derecha","data");
                }

            }if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                if(player.getY()< 448){
                    player.setPosition(player.getX(),player.getY()+(+100*dt));
                }else{
                    player.setPosition(previousPositionX,previousPositionY);
                    Gdx.app.log("Toque arriba","data");
                }

            }if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                if(player.getY()>50){
                    player.setPosition(player.getX(),player.getY()+(-100*dt));
                }else{
                    player.setPosition(previousPositionX,previousPositionY);
                    Gdx.app.log("Toque abajo","data");
                }
            }
        }
    }


    public void update(float dt){

        handleInput(dt);
        if(player != null){
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
        updateServer(delta);
        handleInput(delta);

        //REnder game map
        renderer.render();

         game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        batch.begin();

        if(player != null){
            player.draw(batch);

        }
        for(Map.Entry<String, Player> entry : friendlyPlayers.entrySet()){
            entry.getValue().draw(batch);
        }
        for (Bullet b : bulletsList){
            b.draw(batch);
        }
        batch.end();
        for (Bullet b : bulletsList){
            b.update(Gdx.graphics.getDeltaTime());
        }

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);

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
        hud.dispose();
        mainPlayer.dispose();
        friendPlayer.dispose();

    }

    public void updateServer(float dt){
        timer += dt;
        if(timer > UPDATE_TIME && player != null && player.hasMoved()){
            JSONObject data = new JSONObject();
            try {
                data.put("x",player.getX());
                data.put("y",player.getY());
                String path = ((FileTextureData)player.getTexture().getTextureData()).getFileHandle().path();
                data.put("texture",path);
                socket.emit("playerMoved",data);

            }catch (JSONException e){
                Gdx.app.log("SOCKET.IO","Error enviando datos de recarga");
            }
        }
    }
    public void connectSocket(){
        try {
            socket = IO.socket("http://192.168.2.248:8080");
            socket.connect();
        }catch (Exception e){
            System.out.print(e);
        }
    }


    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO","Connected");
                player = new Player(PlayScreen.this);
                player.setPosition(200,200);
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    id = data.getString("id");
                    Gdx.app.log("SocketID","My ID: "+id);
                }catch (JSONException e){
                    Gdx.app.log("SocketID","Error Estableciendo ID");
                }
            }
        }).on("newPlayer" ,new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    String playerId = data.getString("id");
                    Gdx.app.log("SocketID", "Nuevo jugador Conectado: " + id);
                    friendlyPlayers.put(playerId,new Player(PlayScreen.this));
                } catch (JSONException e) {
                    Gdx.app.log("SocketID", "Error estableciendo nuevo jugador");
                }
            }
        }).on("playerDisconnected" ,new Emitter.Listener(){
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
        }).on("playerMoved" ,new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    String texture = data.getString("texture");
                    //Texture texutraStep = new Texture(texture);
                    Gdx.app.log("SocketID","move: "+texture);
                    if(friendlyPlayers.get(playerId)!=null){
                        friendlyPlayers.get(playerId).setPosition(x.floatValue(),y.floatValue());
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
                    for(int i = 0; i < objects.length(); i++){
                        Player coopPlayer = new Player(PlayScreen.this);
                        Vector2 position = new Vector2();
                        position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                        coopPlayer.setPosition(position.x, position.y);

                        friendlyPlayers.put(objects.getJSONObject(i).getString("id"), coopPlayer);
                    }
                } catch(JSONException e){

                }
            }
        });
    }

    private Array<Rectangle> getTiles(int startX, int startY, int endX, int endY) {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        Array<Rectangle> rectangles = new Array<Rectangle>();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if(cell != null) {
                    Rectangle rect = new Rectangle(x*32, y*32, 32, 32);
                    rectangles.add(rect);
                }
            }
        }
        return rectangles;
    }
}
