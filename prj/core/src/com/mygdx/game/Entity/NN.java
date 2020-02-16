package com.mygdx.game.Entity;
import com.mygdx.game.Util.MyUtil;

import java.util.Random;
public class NN{
    static public float mr = 0.05f;
    static public float rr = 8;

    public double[][] layer;//神经网络各层节点
    public double[][] layerErr;//神经网络各节点误差

    public double[][][] getLayer_weight() {
        return layer_weight;
    }

    public void setLayer_weight(double[][][] layer_weight) {
        this.layer_weight = layer_weight;
    }

    public double[][][] getLayer_weight_delta() {
        return layer_weight_delta;
    }

    public void setLayer_weight_delta(double[][][] layer_weight_delta) {
        this.layer_weight_delta = layer_weight_delta;
    }

    public double[][][] layer_weight;//各层节点权重
    public double[][][] layer_weight_delta;//各层节点权重动量
    public double mobp;//动量系数
    public double rate;//学习系数
    public NN()
    {

    }
    public NN(int[] layernum, double rate, double mobp){
        this.mobp = mobp;
        this.rate = rate;
        layer = new double[layernum.length][];
        layerErr = new double[layernum.length][];
        layer_weight = new double[layernum.length][][];
        layer_weight_delta = new double[layernum.length][][];
        Random random = new Random();
        for(int l=0;l<layernum.length;l++){
            layer[l]=new double[layernum[l]];
            layerErr[l]=new double[layernum[l]];
            if(l+1<layernum.length){
                layer_weight[l]=new double[layernum[l]+1][layernum[l+1]];
                layer_weight_delta[l]=new double[layernum[l]+1][layernum[l+1]];
                for(int j=0;j<layernum[l]+1;j++)
                    for(int i=0;i<layernum[l+1];i++)
                        layer_weight[l][j][i]=random.nextDouble()*2 - 1;//随机初始化权重
            }
        }
    }

    public double[][] getLayer() {
        return layer;
    }

    public void setLayer(double[][] layer) {
        this.layer = layer;
    }

    public double[][] getLayerErr() {
        return layerErr;
    }

    public void setLayerErr(double[][] layerErr) {
        this.layerErr = layerErr;
    }

    public double getMobp() {
        return mobp;
    }

    public void setMobp(double mobp) {
        this.mobp = mobp;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    //逐层向前计算输出
    public double[] computeOut(double[] in){
        for(int l=1;l<layer.length;l++){
            for(int j=0;j<layer[l].length;j++){
                double z=layer_weight[l-1][layer[l-1].length][j];
                for(int i=0;i<layer[l-1].length;i++){
                    layer[l - 1][i] = l == 1 ? in[i] : layer[l - 1][i];
                    z += layer_weight[l - 1][i][j] * layer[l - 1][i];
                }
                layer[l][j] = 1 / (1 + Math.exp(-z));
            }
        }
        return layer[layer.length - 1];
    }

    //逐层反向计算误差并修改权重
    public void updateWeight(double[] tar){
        int l=layer.length-1;
        for(int j=0;j<layerErr[l].length;j++)
            layerErr[l][j]=layer[l][j]*(1-layer[l][j])*(tar[j]-layer[l][j]);

        while(l-->0){
            for(int j=0;j<layerErr[l].length;j++){
                double z = 0.0;
                for(int i=0;i<layerErr[l+1].length;i++){
                    z=z+l>0?layerErr[l+1][i]*layer_weight[l][j][i]:0;
                    layer_weight_delta[l][j][i]= mobp*layer_weight_delta[l][j][i]+rate*layerErr[l+1][i]*layer[l][j];//隐含层动量调整
                    layer_weight[l][j][i]+=layer_weight_delta[l][j][i];//隐含层权重调整
                    if(j==layerErr[l].length-1){
                        layer_weight_delta[l][j+1][i]= mobp*layer_weight_delta[l][j+1][i]+rate*layerErr[l+1][i];//截距动量调整
                        layer_weight[l][j+1][i]+=layer_weight_delta[l][j+1][i];//截距权重调整
                    }
                }
                layerErr[l][j]=z*layer[l][j]*(1-layer[l][j]);//记录误差
            }
        }
    }

    public void train(double[] in, double[] tar){
        double[] out = computeOut(in);
        updateWeight(tar);
    }
    public void mutate()
    {
        Random random = new Random();
        Random random_range = new Random();
        for (int i = 0; i < this.layer_weight.length-1; i++) {
            for (int j = 0; j < this.layer_weight[i].length; j++) {
                for (int k = 0; k < layer_weight_delta[i][j].length; k++) {
                    if (random.nextFloat() <mr) {
                        double range = random_range.nextDouble() *rr -rr/2;
                        layer_weight[i][j][k] += range;
                    }

                    if (random.nextFloat() <mr) {
                        double range = random_range.nextDouble() *rr -rr/2;
                        layer_weight_delta[i][j][k] += range;
                    }
                }
            }
        }
    }
}
