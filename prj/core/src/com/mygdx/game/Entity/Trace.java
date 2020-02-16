package com.mygdx.game.Entity;

import java.util.Arrays;

public class Trace {
    double input[];
    double output[];
    public Trace() {
    }
    public void setSource(double[] input, double[] output)
    {
        this.input = input;
        this.output = output;
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double[] input) {
        this.input = input;
    }

    public double[] getOutput() {
        return output;
    }

    public void setOutput(double[] output) {
        this.output = output;
    }
    
}
