package com.mygdx.game.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Entity.BodyInfo;
import com.mygdx.game.Entity.Config;
import com.mygdx.game.Entity.NN;
import com.mygdx.game.Entity.Population;
import com.mygdx.game.Entity.Robot;
import com.mygdx.game.Entity.Trace;

import java.util.ArrayList;
import java.util.Random;

public class MyUtil {
    public static final int P2M = 10;
    public static boolean PAUSE = false;
    public static final int WINDOW_WIDTH = 1280;
    public static int mode = 1;
    public static final int WINDOW_HEIGHT = 720;
    public static int ButtonNum = 0;
    public static void render(Body body, ShapeRenderer renderer) {
        for(Fixture f : body.getFixtureList()) {
            switch(f.getType()) {
                case Polygon:
                {
                    BodyInfo bodyInfo = (BodyInfo) body.getUserData();

                    if ("robot".equals(bodyInfo.name))
                    {
                        Robot robot = (Robot) bodyInfo.get("robot");
                        if (!robot.isAlive)
                            continue;
                    }
                    /*获取形状*/
                    PolygonShape s = (PolygonShape)f.getShape();
                    Vector2[] vertices = new Vector2[s.getVertexCount()];
                    // 得到线段的每一个顶点
                    for(int i = 0; i < s.getVertexCount(); ++i) {
                        vertices[i] = new Vector2();
                        s.getVertex(i, vertices[i]);
                        body.getTransform().mul(vertices[i]);
                    }
                    /*画出机器人的辅助线条*/
//                    if ("Robot".equals(f.getBody().getUserData()))
//                        Drawer.drawRobotEarnInfo(renderer, f, vertices);
                    /*画出多边形的线段*/
                    Robot robot = (Robot) bodyInfo.get("robot");
                    if (robot !=null && robot.isSeleted)
                    {
                        robot.color.set(1, 0, 0, 1);
                    }
                    
                    renderer.setColor(robot.color);
                    for(int i = 0; i < s.getVertexCount(); ++i) {
                        Vector2 a = vertices[i];
                        Vector2 b = vertices[(i + 1) % s.getVertexCount()];
                        renderer.line(a.x*P2M, a.y*P2M, b.x*P2M, b.y*P2M);
                    }
                    renderer.setColor(1, 1, 1, 1);
                }
                break;
                case Edge:
                    EdgeShape s = (EdgeShape) f.getShape();
                    Vector2 start = new Vector2();
                    Vector2 end = new Vector2();
                    s.getVertex1(start);
                    s.getVertex2(end);
                    renderer.line(start.x*P2M, start.y*P2M, end.x*P2M, end.y*P2M);
                    break;
                // And then cover the other types, Circle and Egde here.
            }
        }
    }
    public static void createBody(World world,float x,float y)
    {
        BodyDef bd = new BodyDef(); //声明物体定义
        bd.position.set(x, y);
        bd.type= BodyDef.BodyType.DynamicBody;
        Body b = world.createBody(bd); //通过world创建一个物体
        b.setUserData("Dynamc");
        PolygonShape polygonShape = new PolygonShape(); //创建一个形状（圆）

        polygonShape.setAsBox(1,1);
        Fixture fixture =  b.createFixture(polygonShape, 1f); //将形状和密度赋给物体

    }
    public static void createLine(World world,Line line)
    {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        Body box = world.createBody(def);
        box.setUserData(new BodyInfo("wall"));
        EdgeShape poly = new EdgeShape();
        poly.set(new Vector2(line.getStart().x/P2M, line.getStart().y/P2M), new Vector2(line.getEnd().x/P2M, line.getEnd().y/P2M));
        box.createFixture(poly, 20f);
    }
    public static void createBlock(World world, Line line, float width)
    {

        BodyDef bd = new BodyDef(); //声明物体定义
        bd.position.set(0, 0);
        bd.type= BodyDef.BodyType.StaticBody;
        Body b = world.createBody(bd); //通过world创建一个物体
        b.setUserData(new BodyInfo("wall"));

        float dx = (line.start.x- line.end.x);
        float dy = line.start.y- line.end.y;
        double angle = Math.toDegrees(Math.atan(dy/dx));

        float x1 = (float)(line.start.x + Math.cos(Math.toRadians(angle+90))*width)/P2M;
        float y1 = (float)(line.start.y + Math.sin(Math.toRadians(angle+90))*width)/P2M;
        float x2 = (float)(line.start.x + Math.cos(Math.toRadians(angle-90))*width)/P2M;
        float y2 = (float)(line.start.y + Math.sin(Math.toRadians(angle-90))*width)/P2M;

        float x3 = (float)(line.end.x + Math.cos(Math.toRadians(angle+90))*width)/P2M;
        float y3 = (float)(line.end.y + Math.sin(Math.toRadians(angle+90))*width)/P2M;
        float x4 = (float)(line.end.x + Math.cos(Math.toRadians(angle-90))*width)/P2M;
        float y4 = (float)(line.end.y + Math.sin(Math.toRadians(angle-90))*width)/P2M;

        PolygonShape polygonShape = new PolygonShape(); //创建一个形状（圆）
        Vector2[] vector2s = new Vector2[]{
                new Vector2(x1,y1),new Vector2(x2,y2),
                new Vector2(x3,y3),new Vector2(x4,y4),
        };
        polygonShape.set(vector2s);
        b.createFixture(polygonShape, 1f); //将形状和密度赋给物体
    }
    public static void saveMap(ArrayList<Line> lines)
    {
        FileHelper.saveDataToFile("map",""+JSON.toJSON(lines));
    }
    public static void saveTrace(ArrayList<Trace> traces)
    {
        FileHelper.saveDataToFile("trace",""+JSON.toJSON(traces));
    }
    public static void saveNN(NN nn)
    {
        FileHelper.saveDataToFile("nn",""+JSON.toJSON(nn));
    }
    public static ArrayList<Line> gerateMap(World world,String data)
    {
        ArrayList<Line> lines = JSON.parseObject(data,new TypeReference<ArrayList<Line>>(){});
        for (Line line:lines) {
//            createBlock(world,line,3);
            createLine(world, line);
        }
        return lines;
    }
    public static float getDistace(Vector2 start,Vector2 end)
    {
        float x = (end.x - start.x) * (end.x - start.x);
        float y = (end.y - start.y) * (end.y - start.y);
        return (float) Math.sqrt(x + y);
    }
    public static void saveConfig(Config config)
    {
        FileHelper.saveDataToFile("config",""+JSON.toJSON(config));
    }
    static public double[][] copy2Array(double[][] src)
    {
        double[][] temp = new double[src.length][];
        for (int i = 0; i < src.length; i++) {
            temp[i] = new double[src[i].length];
        }
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                temp[i][j] = src[i][j];
            }
        }
        return temp;
    }
    static public double[][][] copy3Array(double[][][] src)
    {
        double[][][] temp = new double[Config.versionNum+1][Config.versionNum+1][6];

        for (int i = 0; i < src.length; i++) {
            if (src[i]!=null)
            for (int j = 0; j < src[i].length; j++) {
                if (src[i][j]!=null)
                for (int k = 0; k < src[i][j].length; k++) {
                    temp[i][j][k] = src[i][j][k];
                }
            }
        }
        return temp;
    }
    static public void copy3Array1(double[][][] src)
    {
        double[][][] temp = new double[src.length][][];
        for (int i = 0; i < src.length; i++) {
            temp[i] = new double[src.length][];
        }
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                temp[i][j] = new double[src[i][j].length];
            }
        }

        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                for (int k = 0; k < src[i][j].length; k++) {
                    temp[i][j][k] = src[i][j][k];
                }
            }
        }
    }
    static public double[][][] add3Array(double [][][] b,double range)
    {
        Random random = new Random();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                for (int k = 0; k < b[i][j].length; k++) {
                    double t = random.nextDouble() * range - range / 2;
                    b[i][j][k] += t;
                }
            }
        }
        return b;
    }
    public static double [][][]copyGene(double src[][][])
    {
        double save[][][] = new double[src.length][][];
        for (int i = 0; i < src.length-1; i++) {
            save[i] = new double[src[i].length][];
        }
        for (int i = 0; i < src.length-1; i++) {
            for (int j = 0; j < src[i].length; j++) {
                save[i][j] = new double[src[i][j].length];
            }
        }
        for (int i = 0; i < src.length-1; i++) {
            for (int j = 0; j < src[i].length; j++) {
                for (int k = 0; k < src[i][j].length; k++) {
                    save[i][j][k] = src[i][j][k];
                }
            }
        }
        return save;
    }
    public static boolean isEquals(double[][][] a,double [][][] b)
    {
        for (int i = 0; i < a.length-1; i++) {
            for (int j = 0; j < a[i].length; j++) {
                for (int k = 0; k < a[i][j].length; k++) {
                    if (a[i][j][k] != b[i][j][k]) return false;
                }
            }
        }
        return true;
    }
    public static void save(String filename,int id)
    {
        
        FileHelper.saveDataToFile(filename,""+JSON.toJSON(Population.robots[id].getBrain()));
    }
    public static boolean intersection(double l1x1, double l1y1, double l1x2, double l1y2,
                                       double l2x1, double l2y1, double l2x2, double l2y2) {
        // 快速排斥实验 首先判断两条线段在 x 以及 y 坐标的投影是否有重合。 有一个为真，则代表两线段必不可交。
        if (Math.max(l1x1, l1x2) < Math.min(l2x1, l2x2)
                || Math.max(l1y1, l1y2) < Math.min(l2y1, l2y2)
                || Math.max(l2x1, l2x2) < Math.min(l1x1, l1x2)
                || Math.max(l2y1, l2y2) < Math.min(l1y1, l1y2)) {
            return false;
        }
        // 跨立实验  如果相交则矢量叉积异号或为零，大于零则不相交
        if ((((l1x1 - l2x1) * (l2y2 - l2y1) - (l1y1 - l2y1) * (l2x2 - l2x1))
                * ((l1x2 - l2x1) * (l2y2 - l2y1) - (l1y2 - l2y1) * (l2x2 - l2x1))) > 0
                || (((l2x1 - l1x1) * (l1y2 - l1y1) - (l2y1 - l1y1) * (l1x2 - l1x1))
                * ((l2x2 - l1x1) * (l1y2 - l1y1) - (l2y2 - l1y1) * (l1x2 - l1x1))) > 0) {
            return false;
        }
        return true;
    }

}
