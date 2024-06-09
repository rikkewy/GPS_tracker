package com.example.gpstracker;

public class ResultTraining {
    String distance;
    String steps;
    String bestSpeed;
    String timeOfTraining;
    String dateOfTraining;
    String tempOfTraining;

    public String getBestSpeed() {
        return bestSpeed;
    }

    public String getDateOfTraining() {
        return dateOfTraining;
    }

    public String getTempOfTraining() {
        return tempOfTraining;
    }

    public String getTimeOfTraining() {
        return timeOfTraining;
    }

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

    public ResultTraining(String dateOfTraining, String timeOfTraining, String distance, String bestSpeed, String tempOfTraining, String steps) {
        this.dateOfTraining = dateOfTraining;
        this.timeOfTraining = timeOfTraining;
        this.distance = distance;
        this.bestSpeed = bestSpeed;
        this.tempOfTraining = tempOfTraining;
        this.steps = steps;
    }
}
