package Scenes;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

/**
 * Created by cfgs on 15/05/17.
 */

public class Hud implements Disposable{

    public Stage stage;
    private Viewport viewport;
    private float timeCount ;
    private boolean timeUp;
    private Integer worldTimer;
    private static Integer score;

    Label countdownLabel;
    static Label scoreLabel;
    Label ninjaLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 0;
        score = 0;
        timeCount = 0;
        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        ninjaLabel = new Label("LIFE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(ninjaLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(countdownLabel).expandX().padTop(10);

        stage.addActor(table);
    }
    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }
    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer >=0) {
                worldTimer++;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
    public static Label getScoreLabel() {
        return scoreLabel;
    }

    public static Integer getScore() {
        return score;
    }

}