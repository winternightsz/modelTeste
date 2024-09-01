package com.test.examplemod.model;

public class ModelFace {

    float[] x = new float[3];
    float[] y = new float[3];
    float[] z = new float[3];
    float[] u = new float[3];
    float[] v = new float[3];

    public ModelFace(float[] x, float[] y, float[] z, float[] u, float[] v) {
        this.x = x;
        this.v = v;
        this.u = u;
        this.z = z;
        this.y = y;
    }

    public float[] getX() {
        return x;
    }

    public float[] getY() {
        return y;
    }

    public float[] getZ() {
        return z;
    }

    public float[] getU() {
        return this.u;
    }

    public float[] getV() {
        return v;
    }
}
