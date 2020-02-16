package com.mygdx.game.Component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import javax.xml.soap.Text;

public class Console {
    EditText command_box;
    TextField.TextFieldStyle style ;
    Label label;
    public Console(Stage stage)
    {
        Texture bgTexture = createBackgroundTexture();
        style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        style.fontColor = new Color(1, 1, 1, 1);
        style.background = new TextureRegionDrawable(new TextureRegion(bgTexture));
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.background = new TextureRegionDrawable(new TextureRegion(bgTexture));
        label = new Label("",labelStyle);
        label.setPosition(8, 40);
        label.setWidth(250);
        label.setHeight(300);
        label.setAlignment(Align.topLeft);
        command_box = new EditText(8, 20, stage);
    }
    public void draw(Batch batch)
    {
        label.draw(batch,1);
    }
    public void onKeyDown(int keycode)
    {
        String s =  command_box.onKeyDown(keycode);

        if (!"".equals(s))
            label.setText(label.getText()+"\n"+s);
        if ("clear".equals(s))
            label.setText("");

    }
    private Texture createBackgroundTexture() {
        Pixmap stroke = new Pixmap(250,200,Pixmap.Format.RGBA8888);
        stroke.setColor((float)(56.0/255.0f),(float)(56.0/255.0f),(float)(56.0/255.0f),0.8f);
        stroke.fillRectangle(0, 0, stroke.getWidth(), stroke.getHeight());
        Texture texture = new Texture(stroke);
        stroke.dispose();
        return texture;
    }
}
