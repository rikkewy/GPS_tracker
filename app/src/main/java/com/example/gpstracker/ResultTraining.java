package com.example.gpstracker;

public class ResultTraining {
    String distance;
    String steps;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getSteps() {
        return steps;
    }

    public ResultTraining(String distance, String steps) {
        this.distance = distance;
        this.steps = steps;
    }
}
