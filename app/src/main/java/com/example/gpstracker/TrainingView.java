package com.example.gpstracker;

public class TrainingView {
    public String dateOfTraining;
    public Object distanceOfTraining;
    public String timeOfTraining;

    public TrainingView(String dateOfTraining, Object distanceOfTraining, String  timeOfTraining){

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

    public Object getDistanceOfTraining() {
        return this.distanceOfTraining;
    }

    public void setDistanceOfTraining(Object distanceOfTraining) {
        this.distanceOfTraining = distanceOfTraining;
    }

    public String getTimeOfTraining() {
        return this.timeOfTraining;
    }

    public void setTimeOfTraining(String timeOfTraining) {
        this.timeOfTraining = timeOfTraining;
    }
}
