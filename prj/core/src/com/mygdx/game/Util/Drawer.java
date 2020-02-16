package com.mygdx.game.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import static com.mygdx.game.Util.MyUtil.P2M;

public class Drawer
{
    public static void drawAssistLine(ShapeRenderer shapeRenderer,Vector2 start,Vector2 end)
    {
        float dx = (start.x- Gdx.input.getX());
        float dy = start.y- (MyUtil.WINDOW_HEIGHT-Gdx.input.getY());
        double angle = Math.toDegrees(Math.atan(dy/dx));

        float x = (float)(start.x + Math.cos(Math.toRadians(angle+90))*10);
        float y = (float)(start.y + Math.sin(Math.toRadians(angle+90))*10);
        float x1 = (float)(start.x + Math.cos(Math.toRadians(angle-90))*10);
        float y1 = (float)(start.y + Math.sin(Math.toRadians(angle-90))*10);

        float x2 = (float)(Gdx.input.getX() + Math.cos(Math.toRadians(angle+90))*10);
        float y2 = (float)((MyUtil.WINDOW_HEIGHT-Gdx.input.getY()) + Math.sin(Math.toRadians(angle+90))*10);
        float x3 = (float)(Gdx.input.getX() + Math.cos(Math.toRadians(angle-90))*10);
        float y3 = (float)((MyUtil.WINDOW_HEIGHT-Gdx.input.getY()) + Math.sin(Math.toRadians(angle-90))*10);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(start,new Vector2(Gdx.input.getX(),MyUtil.WINDOW_HEIGHT-Gdx.input.getY()));

        shapeRenderer.line(start,new Vector2(x,y));
        shapeRenderer.line(start,new Vector2(x1,y1));
        shapeRenderer.line(new Vector2(Gdx.input.getX(),MyUtil.WINDOW_HEIGHT-Gdx.input.getY()),new Vector2(x2,y2));
        shapeRenderer.line(new Vector2(Gdx.input.getX(),MyUtil.WINDOW_HEIGHT-Gdx.input.getY()),new Vector2(x3,y3));

        shapeRenderer.end();
    }
    public static void drawRobotEarnInfo(ShapeRenderer renderer, Fixture f,Vector2[] vertices)
    {
        float cx = 0;
        float cy = 0;
        for (int i = 0; i < vertices.length; i++) {
            cx += vertices[i].x;
            cy += vertices[i].y;
        }
        cx /= vertices.length;
        cy /= vertices.length;
        renderer.point(cx*P2M,cy*P2M,0);
//                    float dx = (start.x- Gdx.input.getX());
//                    float dy = start.y- (MyUtil.WINDOW_HEIGHT-Gdx.input.getY());
//                    double angle = Math.toDegrees(Math.atan(dy/dx));
        float angle = f.getBody().getAngle();
        float x = (float)(cx + Math.cos(angle)*3);
        float y = (float)(cy + Math.sin(angle)*3);
        float x1 = (float)(cx + Math.cos(angle-Math.toRadians(45))*3);
        float y1 = (float)(cy + Math.sin(angle-Math.toRadians(45))*3);
        float x2 = (float)(cx + Math.cos(angle+Math.toRadians(45))*3);
        float y2 = (float)(cy + Math.sin(angle+Math.toRadians(45))*3);
        float x3 = (float)(cx + Math.cos(angle-Math.toRadians(90))*3);
        float y3 = (float)(cy + Math.sin(angle-Math.toRadians(90))*3);
        float x4 = (float)(cx + Math.cos(angle+Math.toRadians(90))*3);
        float y4 = (float)(cy + Math.sin(angle+Math.toRadians(90))*3);
        renderer.line(cx*P2M, cy*P2M, x*P2M, y*P2M);
        renderer.line(cx*P2M, cy*P2M, x1*P2M, y1*P2M);
        renderer.line(cx*P2M, cy*P2M, x2*P2M, y2*P2M);
        renderer.line(cx*P2M, cy*P2M, x3*P2M, y3*P2M);
        renderer.line(cx*P2M, cy*P2M, x4*P2M, y4*P2M);
    }
}
