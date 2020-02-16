import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mygdx.game.Entity.NN;
import com.mygdx.game.Entity.Trace;
import com.mygdx.game.Util.FileHelper;
import com.mygdx.game.Util.Line;
import com.mygdx.game.Util.MyUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        NN nn = new NN(new int[]{5, 3, 2}, 0.01, 0.8);
        String mapData = FileHelper.read("trace");
        ArrayList<Trace> traces = JSON.parseObject(mapData,new TypeReference<ArrayList<Trace>>(){});
        double[][] data = new double[traces.size()][5];
        double[][] output = new double[traces.size()][2];

        for (int i = 0; i <traces.size() ; i++) {
            for (int j = 0; j < 5; j++) {
                double t[] = traces.get(i).getInput();
                data[i][j] = t[j];
                System.out.print(data[i][j]+",");
            }
            System.out.println();
        }
        for (int i = 0; i <traces.size() ; i++) {
            for (int j = 0; j < 2; j++) {
                double t[] = traces.get(i).getOutput();
                output[i][j] = t[j];
                System.out.print(output[i][j]+",");
            }
            System.out.println();
        }
        for(int n=0;n<5000;n++) {
            for (int i = 0; i < data.length; i++) {
                nn.train(data[i], output[i]);
            }
        }
        MyUtil.saveNN(nn);

//        for(int j=0;j<data.length;j++){
//            double[] result = nn.computeOut(data[j]);
//            System.out.println(Arrays.toString(data[j])+":"+ Arrays.toString(result));
//        }
//        String nnStr = FileHelper.read("nn");
//        NN brain = (NN) JSONObject.parseObject(nnStr, NN.class);
        double[] x = new double[]{0,1,0,0,0};
        double[] result = nn.computeOut(x);
        System.out.println(Arrays.toString(x)+":"+Arrays.toString(result));
    }
    public static double convert(double[] doubles)
    {
        return doubles[0];
    }
}
