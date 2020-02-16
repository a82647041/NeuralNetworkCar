package com.mygdx.game.Entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Util.Line;
import com.mygdx.game.Util.MyUtil;

import java.util.ArrayList;

public class RayCastInfo {
    Line direction;

    public Line getDirection() {
        return direction;
    }

    public void setDirection(Line direction) {
        this.direction = direction;
    }

    public ArrayList<Vector2> point;
    public RayCastInfo()
    {
        point = new ArrayList<>();
    }
    public Vector2 getMin()
    {
        System.out.println(point.size());
        if (point.size() == 1)
            return point.get(0);
        int minId = 0;
        float minDist = MyUtil.getDistace(direction.getStart(), point.get(0));
        for (int i = 0; i < point.size(); i++) {
            float dist = MyUtil.getDistace(direction.getStart(), point.get(i));
            if (dist<minDist )
            {
                minDist = dist;
                minId = i;
            }
        }
        return point.get(minId);
    }
}
