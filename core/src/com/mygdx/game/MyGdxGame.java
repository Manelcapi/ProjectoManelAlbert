package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;

import Scenes.Hud;
import animation.Move;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import sprites.Player;

public class MyGdxGame extends ApplicationAdapter {
	public static final int V_WIDTH = 700;
	public static final int V_HEIGHT = 650;
	private MyGdxGame game;
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private Hud hud;
	private final float UPDATE_TIME = 1/60f;
	float timer;
	SpriteBatch batch;
	Player player;
	String id;
	Texture friendPlayer;
	Texture mainPlayer;
	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;
	private Socket socket;
	Map<String,Player> friendlyPlayers;

	@Override
	public void create () {
		batch = new SpriteBatch();
		friendPlayer = new Texture("ninja2.png");
		mainPlayer = new Texture("ninja1.png");
		backgroundTexture = new Texture("background.jpg");
		backgroundSprite =new Sprite(backgroundTexture);
		friendlyPlayers = new HashMap<String, Player>();
		gamecam = new OrthographicCamera();
		gamePort = new FillViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, gamecam);
		hud = new Hud(this.batch);
		connectSocket();
		configSocketEvents();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
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
	public void renderBackground() {
		backgroundSprite.draw(batch);
	}

	@Override
	public void render () {
		Gdx.gl.glActiveTexture(1);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput(Gdx.graphics.getDeltaTime());
		updateServer(Gdx.graphics.getDeltaTime());
		this.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		batch.begin();

		renderBackground();
		if(player != null){
			player.draw(batch);
		}
		for(Map.Entry<String,Player> entry : friendlyPlayers.entrySet()){
			entry.getValue().draw(batch);
		}
		batch.end();
	}

	public void handleInput(float dt) {
		if(player != null){
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				if(player.getX() > 30){
					player.setPosition(player.getX()+(-200*dt),player.getY());
				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				if(player.getX() < 600){
					player.setPosition(player.getX()+(+200*dt),player.getY());
				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
				if(player.getY() < 440){
					player.setPosition(player.getX(),player.getY()+(+200*dt));
				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				if(player.getY() > 30){
					player.setPosition(player.getX(),player.getY()+(-200*dt));
				}
			}
		}
	}

	@Override
	public void dispose () {
		super.dispose();
		mainPlayer.dispose();
		friendPlayer.dispose();

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
				player = new Player(mainPlayer);
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
					friendlyPlayers.put(playerId,new Player(friendPlayer));
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
						Player coopPlayer = new Player(friendPlayer);
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

}
