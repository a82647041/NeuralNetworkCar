package com.mygdx.game.Component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Util.MyUtil;

import java.util.ArrayList;

public class ToolBar {
    Texture texture;
    ArrayList<CompoentButton> compoentButtons;
    int selectId = 0;
    float width = 80;
    float height = 1080;
    float paddingLeft = 10;
    float paddingTop = 10;
    Color selectColor ;
    public boolean isVisable = false;
    public ToolBar(Texture texture) {
        selectColor = new Color(1,1,1,1);
        this.texture = texture;
        compoentButtons = new ArrayList<>();
//        addButton("block.png");
//        addButton("block.png");
//        addButton("block.png");
//        addButton("block.png");
    }
    void fadIn()
    {

    }
    public void draw(SpriteBatch batch)
    {
        if (isVisable)
        {
            batch.begin();
            batch.draw(texture,0,200,width,height);
            for (int i = 0; i < compoentButtons.size(); i++) {
                compoentButtons.get(i).draw(batch);
            }
            batch.end();
        }
    }
    public void click(float sx, float sy) throws Exception {
        if (isVisable) {
            Rectangle rectangle = new Rectangle(0, 0, width, height);
            if (rectangle.contains(sx, sy)) {
                for (int i = 0; i < compoentButtons.size(); i++) {
                    Rectangle buttonRect = compoentButtons.get(i).getRectangle();
                    if (buttonRect.contains(sx, sy))
                    {
                        resetButtonState();
                        compoentButtons.get(i).isSelect = true;

                        throw new Exception();
                    }
                }
            }else
            {
                isVisable = false;
            }
            throw new Exception();

        }
    }
    /**
    * @Description:添加按钮
    */
    void addButton(String path)
    {
        CompoentButton compoentButton = new CompoentButton(new Texture(Gdx.files.internal(path)));
        compoentButton.id = compoentButtons.size();
        compoentButton.x = paddingLeft + compoentButtons.size()*(compoentButton.marginLeft+compoentButton.width);
        compoentButton.y = paddingTop;
        compoentButtons.add(compoentButton);
    }
    /**
    * @Description:重置按钮的状态
    */
    void resetButtonState()
    {
        for (int i = 0; i < compoentButtons.size(); i++) {
            compoentButtons.get(i).isSelect = false;
        }
    }

}
