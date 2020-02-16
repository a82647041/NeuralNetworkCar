package com.mygdx.game.Component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.nio.channels.SelectableChannel;


public class CompoentButton {
    float x;
    float y;
    float width = 169;
    float height = 137;
    float marginLeft = 30;
    float marginTop = 30;
    int id;
    boolean isSelect = false;
    Texture texture;
    Color selectColor;

    public CompoentButton(Texture texture) {
        this.texture = texture;
        selectColor = new Color(1,0,0,1);
    }
    Rectangle getRectangle()
    {
        return new Rectangle(x,y,width, height);
    }
    public void draw(SpriteBatch batch)
    {
        if (isSelect)
            batch.setColor(selectColor);
        else
            batch.setColor(new Color(1,1,1,1));
        batch.draw(texture,x,y,width,height);
        batch.setColor(1,1,1,1);
    }

}
