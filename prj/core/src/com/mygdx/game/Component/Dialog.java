package com.mygdx.game.Component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Button.ClickListener;
import com.mygdx.game.Button.MyButton;
import com.mygdx.game.Util.MyUtil;

import java.util.ArrayList;

public class Dialog {
    Texture texture;
    float x;
    float y;
    float width = 420;
    float height = 230;
    float paddingLeft = 10;
    float paddingTop = 30;
    float paddingRight = 30;
    float paddingBottom = 10;
    MyButton myButton;
    public boolean isVisable = false;
    public Dialog(Texture texture) {
        this.texture = texture;
        myButton = new MyButton(new Texture(Gdx.files.internal("button.png")),"OK");
        x = MyUtil.WINDOW_WIDTH/2-width/2;
        y = MyUtil.WINDOW_HEIGHT/2+height/2;
//        myButton.setPosition(x+width-paddingRight-myButton.getW(),y+height+paddingBottom+myButton.getH());
        myButton.setPosition(x+width-myButton.getW()-paddingLeft,y+paddingBottom);
        System.out.println(myButton.getX()+","+myButton.getY());

    }
    public void draw(SpriteBatch batch)
    {
        if (isVisable)
        {
            batch.begin();

            batch.draw(texture,x,y,width,height);
            batch.end();
            myButton.draw(batch);

        }
    }
    public void click(float sx, float sy) throws Exception {
        if (isVisable) {
            Rectangle rectangle = new Rectangle(x, y, width, height);
            if (rectangle.contains(sx, sy)) {
                myButton.click(sx, sy, new ClickListener() {
                    @Override
                    public void onClick() throws Exception {
                        isVisable = false;
                        throw new Exception();
                    }
                });
                throw new Exception();
            }
            }

        }
}
