package com.test.examplemod.model;

public class ModelFace {

    float[] x = new float[3];
    float[] y = new float[3];
    float[] z = new float[3];
    float[] u = new float[3];
    float[] v = new float[3];
    static float normX;
    static float normY;
    static float normZ;

    public ModelFace(float[] x, float[] y, float[] z, float[] u, float[] v, float normX, float normY, float normZ) {
        this.x = x;
        this.v = v;
        this.u = u;
        this.z = z;
        this.y = y;
        this.normX = normX;
        this.normY = normY;
        this.normZ = normZ;
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

    public float getNormX() {
        return normX;
    }

    public float getNormY() {
        return normY;
    }

    public float getNormZ() {
        return normZ;
    }
}
