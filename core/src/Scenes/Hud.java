package Scenes;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

/**
 * Created by cfgs on 15/05/17.
 */

public class Hud {

    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private Integer score;

    Label countdownLabel;
    Label scoreLabel;
    Label ninjaLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        score = 0;
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

}
