package com.mygdx.game.Entity;

import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Util.FileHelper;
import com.mygdx.game.Util.Line;
import com.mygdx.game.Util.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.mygdx.game.Util.MyUtil.P2M;

public class Robot {
    public Color color;
    public boolean isSeleted = false;
    public float fitness = 0;
    float versionDistance = Config.sight_length;
    Vector2 position;
    Body body;
    BodyDef def;
    int id;
    public double[] output;
    public double dist[];
    public NN brain;
    public boolean isAlive = true;
    BodyInfo bodyInfo;

    public NN getBrain() {
        return brain;
    }
    public void setBrain(NN brain) {
        this.brain = brain;
    }

    public Robot(float x, float y, World world)
    {
        color = new Color(1, 1, 1, 1);
        bodyInfo = new BodyInfo("robot");
        bodyInfo.add("robot",this);
        output = new double[2];
        //“nn”是自己训练出来的基本数据
        if (Config.useGeneFile) {
            String nnStr = FileHelper.read(Config.branFile);
            brain = (NN) JSONObject.parseObject(nnStr, NN.class);
        }else
        {
            brain = new NN(new int[]{Config.versionNum, 3, 2}, 0.01, 0.8f);
        }
//        brain = new NN(new int[]{Config.versionNum, 3, 2}, 0.01, 0.8f);
        brain.mutate();
        position = new Vector2(x,y);
        def = new BodyDef();
        id = 0;
        PolygonShape polygonShape = new PolygonShape();

        BodyDef def = new BodyDef();
        dist = new double[Config.versionNum];
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(position.x/P2M, position.y/ P2M);
        polygonShape.setAsBox(8.0f/P2M,5.0f/P2M);
        body = world.createBody(def);
        Fixture fixture =  body.createFixture(polygonShape, 1f);
        fixture.setSensor(true);
        body.setUserData(bodyInfo);
        for (int i = 0; i < Config.versionNum; i++)
            dist[i] = versionDistance;
    }
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
//    public Line[] getVersionLine()
//    {
//        float angle = body.getAngle();
//        float length = versionDistance/P2M;
//        float x = (float)(0 + Math.cos(angle)*length);
//        float y = (float)(0 + Math.sin(angle)*length);
//        float x1 = (float)(0 + Math.cos(angle-Math.toRadians(45))*length);
//        float y1 = (float)(0 + Math.sin(angle-Math.toRadians(45))*length);
//        float x2 = (float)(0 + Math.cos(angle+Math.toRadians(45))*length);
//        float y2 = (float)(0 + Math.sin(angle+Math.toRadians(45))*length);
//        float x3 = (float)(0 + Math.cos(angle-Math.toRadians(90))*length);
//        float y3 = (float)(0 + Math.sin(angle-Math.toRadians(90))*length);
//        float x4 = (float)(0 + Math.cos(angle+Math.toRadians(90))*length);
//        float y4 = (float)(0 + Math.sin(angle+Math.toRadians(90))*length);
//        Line lines[] = new Line[Config.versionNum];
//        lines[0] = new Line(new Vector2(body.getPosition().x*P2M,body.getPosition().y*P2M), new Vector2(body.getPosition().x*P2M+x*P2M,  body.getPosition().y*P2M+y*P2M));
//        lines[1] = new Line(new Vector2(body.getPosition().x*P2M,body.getPosition().y*P2M), new Vector2(body.getPosition().x*P2M+x1*P2M, body.getPosition().y*P2M+y1*P2M));
//        lines[2] = new Line(new Vector2(body.getPosition().x*P2M,body.getPosition().y*P2M), new Vector2(body.getPosition().x*P2M+x2*P2M, body.getPosition().y*P2M+y2*P2M));
//        lines[3] = new Line(new Vector2(body.getPosition().x*P2M,body.getPosition().y*P2M), new Vector2(body.getPosition().x*P2M+x3*P2M, body.getPosition().y*P2M+y3*P2M));
//        lines[4] = new Line(new Vector2(body.getPosition().x*P2M,body.getPosition().y*P2M), new Vector2(body.getPosition().x*P2M+x4*P2M, body.getPosition().y*P2M+y4*P2M));
//        return lines;
//    }
    public Line[] getVersionLine()
    {
        float angle = body.getAngle();
        float baseAngle = (float) (angle - Math.toRadians(90));
        float length = versionDistance/P2M;
        float x[] = new float[Config.versionNum];
        float y[] = new float[Config.versionNum];
        Line lines[] = new Line[Config.versionNum];
        for (int i = 0; i < Config.versionNum; i++) {
            x[i]= (float)(0 + Math.cos(baseAngle+Math.toRadians((180/(Config.versionNum-1)*i)))*length);
            y[i]= (float)(0 + Math.sin(baseAngle+Math.toRadians((180/(Config.versionNum-1)*i)))*length);
//            x[i] = (float) (Math.cos(Math.toRadians(baseAngle + (180 / (Config.versionNum-1)) * i) * length));
//            y[i] = (float) (Math.sin(Math.toRadians(baseAngle + (180 / (Config.versionNum-1)) * i) * length));
            lines[i] = new Line(new Vector2(body.getPosition().x*P2M,body.getPosition().y*P2M), new Vector2(body.getPosition().x*P2M+x[i]*P2M,  body.getPosition().y*P2M+y[i]*P2M));
        }
        return lines;
    }
    public void handlerVersion1(ArrayList<Line> walls,final ShapeRenderer shapeRenderer)
    {
        if (isAlive) {
            Line[] lines = getVersionLine();
            for (int i = 0; i < Config.versionNum; i++) {
                final Line line = lines[i];
                dist[i] = versionDistance;
                float minDist = versionDistance;
                for (int j = 0; j < walls.size(); j++) {
                    Vector2 point = walls.get(j).getCrossPoint(line);
                    float d = versionDistance;
                    if (point!=null)
                    {
                        d = (float) Math.sqrt((line.getStart().x-point.x)*(line.getStart().x-point.x)+(line.getStart().y-point.y)*(line.getStart().y-point.y) );
                        if (d <minDist)
                            minDist = d;
                    }
                    dist[i] = minDist;
                    continue;
                }
            }
        }
    }
    public void handlerVersion(World world,final ShapeRenderer shapeRenderer)
    {
        if (isAlive) {
            final Line[] lines = getVersionLine();
            for (int i = 0; i < Config.versionNum; i++) {
                final Line line = lines[i];
                dist[i] = versionDistance;

                final int temp_i = i;
                world.rayCast(new RayCastCallback() {
                    @Override
                    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                        /*画出焦点*/
                        float _dist = MyUtil.getDistace(line.getStart(), line.getEnd());

                        dist[temp_i] = _dist * fraction;
                        return -1;
                    }
                }, lines[i].getStart().x / P2M, lines[i].getStart().y / P2M, lines[i].getEnd().x / P2M, lines[i].getEnd().y / P2M);
            }
        }
    }
    public double[] think()
    {
        for (int i = 0; i < Config.versionNum; i++) {
            dist[i] /= versionDistance;
        }
        double[] result = (double[]) brain.computeOut(dist);
        return result;
    }

    public void drawTrueVersion(ShapeRenderer shapeRenderer)
    {
        if (isAlive && Config.sight) {

            float angle = body.getAngle();
            float baseAngle = (float) (angle - Math.toRadians(90));
//            float angle = body.getAngle();
//            float x = (float) (0 + Math.cos(angle) * dist[0] / P2M);
//            float y = (float) (0 + Math.sin(angle) * dist[0] / P2M);
//            float x1 = (float) (0 + Math.cos(angle - Math.toRadians(45)) * dist[1] / P2M);
//            float y1 = (float) (0 + Math.sin(angle - Math.toRadians(45)) * dist[1] / P2M);
//            float x2 = (float) (0 + Math.cos(angle + Math.toRadians(45)) * dist[2] / P2M);
//            float y2 = (float) (0 + Math.sin(angle + Math.toRadians(45)) * dist[2] / P2M);
//            float x3 = (float) (0 + Math.cos(angle - Math.toRadians(90)) * dist[3] / P2M);
//            float y3 = (float) (0 + Math.sin(angle - Math.toRadians(90)) * dist[3] / P2M);
//            float x4 = (float) (0 + Math.cos(angle + Math.toRadians(90)) * dist[4] / P2M);
//            float y4 = (float) (0 + Math.sin(angle + Math.toRadians(90)) * dist[4] / P2M);
            Line lines[] = getVersionLine();
            float x[] = new float[Config.versionNum];
            float y[] = new float[Config.versionNum];
            for (int i = 0; i < Config.versionNum; i++) {
                x[i] = (float) (0 + Math.cos(baseAngle+Math.toRadians((180/(Config.versionNum-1)*i)))* dist[i] / P2M);
                y[i] = (float) (0 + Math.sin(baseAngle+Math.toRadians((180/(Config.versionNum-1)*i)))* dist[i] / P2M);
                lines[i] = new Line(new Vector2(body.getPosition().x * P2M, body.getPosition().y * P2M), new Vector2(body.getPosition().x * P2M + x[i] * P2M, body.getPosition().y * P2M + y[i] * P2M));
            }
//            lines[0] = new Line(new Vector2(body.getPosition().x * P2M, body.getPosition().y * P2M), new Vector2(body.getPosition().x * P2M + x * P2M, body.getPosition().y * P2M + y * P2M));
//            lines[1] = new Line(new Vector2(body.getPosition().x * P2M, body.getPosition().y * P2M), new Vector2(body.getPosition().x * P2M + x1 * P2M, body.getPosition().y * P2M + y1 * P2M));
//            lines[2] = new Line(new Vector2(body.getPosition().x * P2M, body.getPosition().y * P2M), new Vector2(body.getPosition().x * P2M + x2 * P2M, body.getPosition().y * P2M + y2 * P2M));
//            lines[3] = new Line(new Vector2(body.getPosition().x * P2M, body.getPosition().y * P2M), new Vector2(body.getPosition().x * P2M + x3 * P2M, body.getPosition().y * P2M + y3 * P2M));
//            lines[4] = new Line(new Vector2(body.getPosition().x * P2M, body.getPosition().y * P2M), new Vector2(body.getPosition().x * P2M + x4 * P2M, body.getPosition().y * P2M + y4 * P2M));

            for (int i = 0; i < Config.versionNum; i++) {
                shapeRenderer.line(lines[i].getStart(), lines[i].getEnd());
            }
        }
    }
    public void move(float speed )
    {
        if (Config.isMove )
        {
            if (isAlive && !MyUtil.PAUSE) {
                fitness++;
                float speed_x = (float) Math.cos(this.getBody().getAngle()) * speed;
                float speed_y = (float) Math.sin(this.getBody().getAngle()) * speed;
                this.getBody().setLinearVelocity(new Vector2(speed_x, speed_y));
                output = this.think();
                if (output[0] > 0.2)
                    if (output[1] < 0.2)
                        this.getBody().setTransform(this.getBody().getPosition(), (float) (this.getBody().getAngle() + Math.toRadians(-Config.angle_speed)));
                if (output[1] > 0.2)
                    if (output[0] < 0.2)
                        this.getBody().setTransform(this.getBody().getPosition(), (float) (this.getBody().getAngle() + Math.toRadians(Config.angle_speed)));
            }
        }
    }
}
