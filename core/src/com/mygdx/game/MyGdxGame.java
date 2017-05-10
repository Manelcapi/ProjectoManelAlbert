package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import sprites.Player;

public class MyGdxGame extends ApplicationAdapter {
	private final float UPDATE_TIME = 1/60f;
	float timer;
	SpriteBatch batch;
	Player player;
	String id;
	Texture friendPlayer;
	Texture mainPlayer;
	Texture mainPlayerStep1;
	Texture mainPlayerStep2;
	Texture mainPlayerLeft;
	Texture mainPlayerLeftStep1;
	Texture mainPlayerLeftStep2;
	Texture mainPlayerRight;
	Texture mainPlayerRightStep1;
	Texture mainPlayerRightStep2;
	Texture mainPlayerTop;
	Texture mainPlayerTopStep1;
	Texture mainPlayerTopStep2;
	Boolean step1 = true;
	Boolean step2 = false;
	private Socket socket;
	Texture[] arrayLeft = new Texture[5];
	Map<String,Player> friendlyPlayers;

	@Override
	public void create () {
		batch = new SpriteBatch();
		mainPlayer = new Texture("ninja1.png");
		mainPlayerStep1 = new Texture("ninja1DownStep1.png");
		mainPlayerStep2 = new Texture("ninja1DownStep2.png");
		mainPlayerLeft = new Texture("ninja1Left.png");
		mainPlayerLeftStep1 = new Texture("ninja1LeftStep1.png");
		mainPlayerLeftStep2 = new Texture("ninja1LeftStep2.png");
		mainPlayerRight = new Texture("ninja1Right.png");
		mainPlayerRightStep1 = new Texture("ninja1RightStep1.png");
		mainPlayerRightStep2 = new Texture("ninja1RightStep2.png");
		mainPlayerTop = new Texture("ninja1Top.png");
		mainPlayerTopStep1 = new Texture("ninja1TopStep1.png");
		mainPlayerTopStep2 = new Texture("ninja1TopStep2.png");
		friendPlayer = new Texture("ninja2.png");
		arrayLeft = new Texture[]{mainPlayerLeft, mainPlayerLeftStep1, mainPlayerLeft, mainPlayerLeftStep2, mainPlayerLeft};
		friendlyPlayers = new HashMap<String, Player>();
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
				data.put("texture",player.getTexture());
				socket.emit("playerMoved",data);

			}catch (JSONException e){
				Gdx.app.log("SOCKET.IO","Error enviando datos de recarga");
			}
		}
	}

	@Override
	public void render () {
		try {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			handleInput(Gdx.graphics.getDeltaTime());
			updateServer(Gdx.graphics.getDeltaTime());
			batch.begin();
			if(player != null){
				player.draw(batch);
			}
			for(Map.Entry<String,Player> entry : friendlyPlayers.entrySet()){
				entry.getValue().draw(batch);
			}
			batch.end();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void handleInput(float dt) throws InterruptedException {
		if(player != null){
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				player.setTexture(mainPlayerLeft);
				if(player.getX() > 0){
					for (int i = 0; i > arrayLeft.length; i++) {
						player.setTexture(arrayLeft[i]);
					}
					player.setPosition(player.getX()+(-200*dt),player.getY());

				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				player.setTexture(mainPlayerRight);
				if(player.getX() < 600){
					if(step1) {
						player.setTexture(mainPlayerRightStep1);
						step1 = false;
						step2 = true;
					}
					else if(step2) {
						player.setTexture(mainPlayerRightStep2);
						step1 = true;
						step2 = false;
					}
					player.setPosition(player.getX()+(+200*dt),player.getY());

				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
				player.setTexture(mainPlayerTop);
				if(player.getY() < 440){
					if(step1) {
						player.setTexture(mainPlayerTopStep1);
						step1 = false;
						step2 = true;
					}
					else if(step2) {
						player.setTexture(mainPlayerTopStep2);
						step1 = true;
						step2 = false;
					}
					player.setPosition(player.getX(), player.getY() + (+200 * dt));

				}
			}else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				player.setTexture(mainPlayer);
				if(player.getY() > 0){
					if(step1) {
						player.setTexture(mainPlayerStep1);
						step1 = false;
						step2 = true;
					}
					else if(step2) {
						player.setTexture(mainPlayerStep2);
						step1 = true;
						step2 = false;
					}
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
					if(friendlyPlayers.get(playerId)!=null){
						friendlyPlayers.get(playerId).setPosition(x.floatValue(),y.floatValue());
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