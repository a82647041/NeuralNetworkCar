package com.mygdx.game;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Util.Line;
import com.mygdx.game.Util.MyUtil;

/** * 游戏主程序的启动入口类 */
public class Test extends ApplicationAdapter  implements InputProcessor {

    // 舞台
    private Stage stage;
    Line line1;
    Line line2;
    ShapeRenderer shapeRenderer;
    @Override
    public void create() {
        // 设置日志输出级别
        stage = new Stage();
        Gdx.input.setInputProcessor(this);
        line1 = new Line(new Vector2(0,0),new Vector2(400,300));
        line2 = new Line(new Vector2(100,0),new Vector2(100,400));
        shapeRenderer = new ShapeRenderer();
    }


    @Override
    public void render() {

        // 黑色清屏
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(1, 1, 1, 1));
        shapeRenderer.line(line1.getStart(), line1.getEnd());
        shapeRenderer.line(line2.getStart(), line2.getEnd());
        System.out.println(line1.k);
        System.out.println(line1.b);
        Vector2 point = line1.getCrossPoint(line2);
        if (point!=null)
            shapeRenderer.circle(point.x, point.y,4);
        shapeRenderer.end();
        // 更新舞台逻辑
        stage.act();
        // 绘制舞台
        stage.draw();
    }

    @Override
    public void dispose() {
        // 应用退出时释放资源

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE)
        {
            Gdx.app.exit();

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        line1.setStart(new Vector2(screenX, MyUtil.WINDOW_HEIGHT - screenY));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}