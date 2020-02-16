package com.mygdx.game.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) {
        String data = "[{\"start\":{\"zero\":false,\"unit\":false,\"x\":453.0,\"y\":169.0},\"end\":{\"zero\":false,\"unit\":false,\"x\":638.0,\"y\":126.0}},{\"start\":{\"zero\":false,\"unit\":false,\"x\":723.0,\"y\":137.0},\"end\":{\"zero\":false,\"unit\":false,\"x\":915.0,\"y\":190.0}},{\"start\":{\"zero\":false,\"unit\":false,\"x\":1003.0,\"y\":248.0},\"end\":{\"zero\":false,\"unit\":false,\"x\":1048.0,\"y\":418.0}},{\"start\":{\"zero\":false,\"unit\":false,\"x\":1048.0,\"y\":550.0},\"end\":{\"zero\":false,\"unit\":false,\"x\":996.0,\"y\":618.0}},{\"start\":{\"zero\":false,\"unit\":false,\"x\":849.0,\"y\":623.0},\"end\":{\"zero\":false,\"unit\":false,\"x\":626.0,\"y\":613.0}},{\"start\":{\"zero\":false,\"unit\":false,\"x\":466.0,\"y\":609.0},\"end\":{\"zero\":false,\"unit\":false,\"x\":102.0,\"y\":525.0}},{\"start\":{\"zero\":false,\"unit\":false,\"x\":119.0,\"y\":390.0},\"end\":{\"zero\":false,\"unit\":false,\"x\":149.0,\"y\":232.0}}]";
        ArrayList<Line> jsonObject = JSON.parseObject(data,new TypeReference<ArrayList<Line>>(){});
        System.out.println(jsonObject);
    }
}
