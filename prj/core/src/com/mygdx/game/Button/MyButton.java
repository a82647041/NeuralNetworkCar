package com.mygdx.game.Button;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Util.MyUtil;

import org.w3c.dom.css.Rect;

import java.awt.Point;

public class MyButton {
    String text;
    float x;
    float y;

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    float w = 64;
    float h = 32;
    float marginLeft = 10;
    float marginTop = 20;
    int id = 0;
    Texture texture;
    ClickListener listener;
    BitmapFont bitmapFont;
    boolean isClick = false;
    public MyButton(Texture texture,String text)
    {
        bitmapFont = new BitmapFont();
        id = MyUtil.ButtonNum;
        MyUtil.ButtonNum++;
        this.texture = texture;
        this.text = text;
        x  = MyUtil.WINDOW_WIDTH-w-marginLeft;
        y = MyUtil.WINDOW_HEIGHT-(id)*h-MyUtil.ButtonNum*marginTop;
    }
    /**
    * @Description:绘制函数
    */
    public void draw(SpriteBatch batch)
    {

        /*绘制按钮图片*/
        batch.begin();
        batch.draw(texture,x,y,w,h);
        batch.end();
        /*绘制文字*/
        batch.begin();
        float fontWidthSize = bitmapFont.getXHeight();
        int textLen = text.length();
        bitmapFont.draw(batch, "" + text, x+textLen*fontWidthSize/2, y+(h+fontWidthSize)/2);
        batch.end();
    }
    public void click(float sx,float sy,ClickListener listener) throws Exception {
        Rectangle rectangle = new Rectangle(x,y,w,h);
        if (rectangle.contains(sx,sy))
        {
            listener.onClick();
            isClick = !isClick;
            throw new Exception();
        }
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public ClickListener getListener() {
        return listener;
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }
    public void setPosition(float x,float y)
    {
        this.x = x;
        this.y = y;
    }
}
