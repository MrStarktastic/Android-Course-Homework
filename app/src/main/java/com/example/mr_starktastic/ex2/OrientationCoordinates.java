package com.example.mr_starktastic.ex2;

import java.io.Serializable;

public class OrientationCoordinates implements Serializable {
    private float[][] coordinates = new float[2][];

    public OrientationCoordinates() {

    }

    /**
     * @param orientation Integer indicating an orientation (portrait is 1, landscape is 2).
     */
    public float[] getCoordinates(int orientation) {
        return coordinates[orientation - 1];
    }

    public void setCoordinates(int orientation, float[] coordinates) {
        this.coordinates[orientation - 1] = coordinates;
    }
}
