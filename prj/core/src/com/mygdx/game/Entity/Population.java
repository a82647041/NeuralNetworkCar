package com.mygdx.game.Entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Util.FileHelper;
import com.mygdx.game.Util.Line;
import com.mygdx.game.Util.MyUtil;

import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.game.Util.MyUtil.P2M;
import static com.mygdx.game.Util.MyUtil.WINDOW_HEIGHT;
import static com.mygdx.game.Util.MyUtil.WINDOW_WIDTH;

public class Population {
    static public float begin_x = 15, begin_y = 148;
    private static float set_angle;
    public float bestFitness = 0;
    World world;
    ArrayList<Robot> matingPool;
    static private boolean hasSet =false;
    NN[] nns;
    public static Robot[] robots;
    static public NN bestGene;
    static public NN selectGene;
    public Population(World world) {
        bestGene = new NN(new int[]{5, 3, 2}, 0.01f, 0.8);
        this.world = world;
        robots = new Robot[Config.popSize];
        matingPool = new ArrayList<>();
//        String nnStr = FileHelper.read("\\gene\\gene1");
//        baseNN = (NN) JSONObject.parseObject(nnStr, NN.class);

        nns = new NN[Config.popSize];
        Random random = new Random();
        for (int i = 0; i < Config.popSize; i++) {
            robots[i] = new Robot(begin_x,begin_y,world);
            nns[i] = robots[i].getBrain();
        }

    }
    /**
    * @Description:进化
    */
    public void evaluate() {
        matingPool.clear();
        float maxFitness = 0;
        for (int i = 0; i < Config.popSize; i++) {
            if (robots[i].fitness > maxFitness)
                maxFitness = robots[i].fitness;
        }
        for (int i = 0; i < Config.popSize; i++) {
            robots[i].fitness /= maxFitness;
        }
        for (int i = 0; i < Config.popSize; i++) {
            for (int j = 0; j < robots[i].fitness*100; j++) {
                matingPool.add(robots[i]);
            }
        }
    }
    /*选择*/
    public void selection() {
        Random random = new Random();
        for (int i = 0; i < Config.popSize; i++) {
//            Robot parentA =  matingPool.get(random.nextInt(matingPool.size()/10));
//            Robot parentB =  matingPool.get(random.nextInt(matingPool.size()/10));
//            matingPool.get(matingPool.size());

            robots[i].isAlive = true;
            robots[i].fitness = 0;
            robots[i].getBody().setTransform(begin_x/P2M, begin_y/P2M, hasSet?set_angle:0);

//            NN child = parentA.mating(parentB);
            NN child = new NN(new int[]{5, 3, 2}, 0.01f, 0.8);
            child.setLayer_weight(MyUtil.copy3Array(bestGene.getLayer_weight()));
            child.setLayer_weight_delta(MyUtil.copy3Array(bestGene.getLayer_weight_delta()));
            child.mutate();
            robots[i].setBrain(child);
        }
    }
    /**
    * @Description:判断机器人是否存活
    */
    boolean isRobotNoAlive() {
        for (int i = 0; i < robots.length; i++) {
            if (robots[i].isAlive)
                return false;
        }
        return true;
    }
    /**
    * @Description:选出最好的基因
    */
    public NN getBestGene() {
        NN bg = new NN(new int[]{5, 3, 2}, 0.01f, 0.8);
        int maxIndex = 0;
        for (int i = 0; i < Config.popSize; i++) {
            if (robots[maxIndex].fitness < robots[i].fitness)
                maxIndex = i;
        }
        double[][][] weight = MyUtil.copy3Array(robots[maxIndex].getBrain().getLayer_weight());
        double[][][] weight_delta = MyUtil.copy3Array(robots[maxIndex].getBrain().getLayer_weight_delta());
        bg.setLayer_weight(weight);
        bg.setLayer_weight_delta(weight_delta);
        return bg;
    }
    /**
    * @Description:获得最高的Fitness的基因
    */
    public float getBestFitness() {
        float best = 0;
        for (int i = 0; i < Config.popSize; i++) {
            if (robots[i].fitness >best)
                best = robots[i].fitness;
        }
        return best;
    }
    /**
    * @Description:运算
    */
    public void run(ArrayList <Line> walls, ShapeRenderer shapeRenderer) {
        if (isRobotNoAlive())
        {
            float getBestFitness = getBestFitness();
            if (bestFitness < getBestFitness)
            {
                bestFitness = getBestFitness;
                bestGene = getBestGene();
            }
            evaluate();
            selection();
        }
        for (int i = 0; i < Config.popSize; i++) {
            robots[i].move(Config.speed);
            if(Config.hitMode == 1)
            {
                robots[i].handlerVersion1(walls,shapeRenderer);
            }else
            {
                robots[i].handlerVersion(world, shapeRenderer);
            }
            robots[i].drawTrueVersion(shapeRenderer);
        }
    }
    /**
    * @Description:复制基因
    */
    public  double [][][]copyGene(double src[][][]) {
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
    /**
    * @Description:获得存活的机器人的数量
    */
    public int getAlive() {
        int count = 0;
        for (int i = 0; i < Config.popSize; i++) {
            if (robots[i].isAlive)
                count++;
        }
        return count;
    }
    public static void saveBest(String filename)
    {
        FileHelper.saveDataToFile("\\gene\\"+filename,""+ JSON.toJSON(bestGene));
    }
    public static void saveSelect(String filename)
    {
        if (selectGene!=null)
        FileHelper.saveDataToFile("\\gene\\"+filename,""+ JSON.toJSON(selectGene));
    }
    public static void ss(String filename)
    {
        Robot robot = null;
        for (int i = 0; i < Config.popSize; i++) {
            if (robots[i].isSeleted )
            {
                robot = robots[i];
                break;
            }

        }
        if (robot!=null)
            FileHelper.saveDataToFile("\\gene\\"+filename,""+ JSON.toJSON(robot));

    }
    public static void reset()
    {
        for (int i = 0; i < Config.popSize; i++) {
            robots[i].isAlive = false;
        }
    }
    public static void setRobotTrasform(Vector2 pos,float angle)
    {
        for (int i = 0; i < Config.popSize; i++) {
            begin_x = pos.x ;
            begin_y = pos.y ;
            set_angle = angle;
            hasSet = true;
            robots[i].getBody().setTransform(pos.x/P2M,pos.y/P2M, angle);
        }
    }
    public static void update()
    {
        String nnStr = FileHelper.read(Config.branFile);
        NN brain = (NN) JSONObject.parseObject(nnStr, NN.class);

        for (int i = 0; i < Config.popSize; i++) {
            brain.mutate();
            robots[i].setBrain(brain);
        }
    }
}
