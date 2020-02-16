package com.mygdx.game.Util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.geom.Line2D;

public class Line {
    Vector2 start;
    Vector2 end;
    public  float k;
    public  float b;
    public float len;
    public Line(Vector2 start,Vector2 end)
    {
        this.start = start;
        this.end = end;
        k = (end.y - start.y) / (end.x - start.x);
        b = end.y - k * end.x;
    }
    public Vector2 getStart() {
        return start;
    }

    public void setStart(Vector2 start) {
        this.start = start;
        k = (end.y - start.y) / (end.x - start.x);
        b = end.y - k * end.x;
    }

    public Vector2 getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Line{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public void setEnd(Vector2 end) {
        this.end = end;
        k = (end.y - start.y) / (end.x - start.x);
        b = end.y - k * end.x;
    }
    public Vector2 getCrossPoint(Line line)
    {
        boolean rs =  Line2D.linesIntersect(start.x, start.y, end.x, end.y, line.start.x, line.start.y, line.end.x, line.end.y);
        if (!rs) return null;
        float x = (line.b - b) / (k - line.k);
        float y = k * x + b;
        if (Float.isInfinite(k))
        {
            x = start.x;
            if (!Float.isInfinite(line.k))
                y = line.k * x + line.b;
        }
        if (Float.isInfinite(line.k))
        {
            x = line.start.x;
            if (!Float.isInfinite(k))
                y = k * x + b;
        }

        return new Vector2(x, y);
    }
}
