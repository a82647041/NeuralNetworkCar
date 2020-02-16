import com.alibaba.fastjson.JSONObject;
import com.mygdx.game.Entity.NN;
import com.mygdx.game.Util.FileHelper;
import com.mygdx.game.Util.MyUtil;

import org.luaj.vm2.Globals;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.script.LuajContext;

import java.util.Random;

//
public class Main1 {
    public static void main(String[] args) {
        String nnStr = FileHelper.read("nn");
        NN n1 = (NN) JSONObject.parseObject(nnStr, NN.class);
//        double a[][][] = MyUtil.copy3Array(n1.getLayer_weight());
        double b[][][] = copyGene(n1.getLayer_weight());
        System.out.println(isEquals(b, n1.getLayer_weight()));
//        System.out.println(a);
//        System.out.println(MyUtil.copy3Array(n1.getLayer_weight()));
//        double d[][][] =
//        MyUtil.copy3Array1(n1);

//        System.out.println(d);
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

}
