package com.example.gpstracker;

public class TrainingView {
    private String dateOfTraining;
    private int distanceOfTraining;
    private String timeOfTraining;

    public TrainingView(String dateOfTraining, int distanceOfTraining, String timeOfTraining){

        this.dateOfTraining=dateOfTraining;
        this.distanceOfTraining=distanceOfTraining;
        this.timeOfTraining=timeOfTraining;
    }

    public String getDateOfTraining() {
        return this.dateOfTraining;
    }

    public void setDateOfTraining(String dateOfTraining) {
        this.dateOfTraining = dateOfTraining;
    }

    public int getDistanceOfTraining() {
        return this.distanceOfTraining;
    }

    public void setDistanceOfTraining(int distanceOfTraining) {
        this.distanceOfTraining = distanceOfTraining;
    }

    public String getTimeOfTraining() {
        return this.timeOfTraining;
    }

    public void setTimeOfTraining(String timeOfTraining) {
        this.timeOfTraining = timeOfTraining;
    }
}
