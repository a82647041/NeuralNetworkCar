package com.mygdx.game.Component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class EditText {
    TextField.TextFieldStyle style ;
    float width =250;
    float height =20;
    public TextField field;
    boolean foucs = false;
    Rectangle rectangle;
    String text;
    float x,y;
    HashMap<String,Integer> commands;
    Stack<String>up;
    Stack<String>down;
    int history_pos = 0;
    int command_num = 0;
    Globals globals;
    LuaValue per;
    public EditText(float x, float y, Stage stage)
    {
        up = new Stack<>();
        down = new Stack<>();
        globals = JsePlatform.standardGlobals();
        per = globals.loadfile("C:\\MyPath\\img\\MyApplication\\core\\assets\\func.lua").call();
        commands = new HashMap<>();
        width = 250;
        text = "";
        this.x = x;
        this.y = y;
        style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        style.fontColor = new Color(1, 1, 1, 1);

        field = new TextField("", style);
        field.setPosition(x, y);
        field.setMessageText("Input Command");
        field.setWidth(width);
        field.setHeight(height);
        rectangle = new Rectangle(x, y, field.getWidth(), field.getHeight());
        field.setBlinkTime(0.5f);;
        Texture cursorTexture = createCursorTexture();
        Texture bgTexture = createBackgroundTexture();
        style.cursor = new TextureRegionDrawable(new TextureRegion(cursorTexture));
        style.background = new TextureRegionDrawable(new TextureRegion(bgTexture));

        stage.addActor(field);

    }
    private Texture createCursorTexture() {
        Pixmap pixmap = new Pixmap(1, 300 - 4, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 1);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
    public String onKeyDown(int keycode)
    {
        String out = "";
        if (field.hasKeyboardFocus())
        {
            if (keycode == Input.Keys.ENTER)
            {
                up.add(field.getText());
                out = field.getText();
                try
                {
                    LuaValue chunk = globals.load(field.getText());
                    chunk.call();
                }catch (Exception e )
                {

                }finally {
                    field.setText("");
                }

            }
            if (keycode == Input.Keys.UP)
            {
                if (!up.isEmpty())
                {
                    String str = up.pop();
                    field.setText(str);
                    down.push(str);
                }
            }
            if (keycode == Input.Keys.DOWN)
            {

                if (!down.isEmpty())
                {
                    String str = down.pop();
                    field.setText(str);
                    up.push(str);
                }
            }
        }
        return out;
    }
    private String getKey(int value){
        String key="";
        for (Map.Entry<String, Integer> entry : commands.entrySet()) {
            if(value == entry.getValue()){
                key=entry.getKey();
            }
        }
        return key;
    }
    private Texture createBackgroundTexture() {
//        Pixmap pixmap = new Pixmap(1, 300 - 4, Pixmap.Format.RGBA8888);
        Pixmap stroke = new Pixmap((int) width,(int)field.getHeight(),Pixmap.Format.RGBA8888);
        stroke.setColor((float)(56.0/255.0f),(float)(56.0/255.0f),(float)(56.0/255.0f),1);
        stroke.fillRectangle(0, 0, stroke.getWidth(), stroke.getHeight());

        Texture texture = new Texture(stroke);
        stroke.dispose();
        return texture;
    }
}
