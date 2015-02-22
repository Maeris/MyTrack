package com.epitech.mytrack;

/**
 * Created by Sevilyoti on 22/02/2015.
 */
public class Track {

    private Double vitMoyenne;
    private Double vitMax;
    private Double distance;

    private String startPoint;
    private String endPoint;

    public Track(Double vitMoyenne, Double vitMax, Double distance, String startPoint, String endPoint)
    {
        this.vitMoyenne = vitMoyenne;
        this.vitMax = vitMax;
        this.distance = distance;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void setVitMoyenne(Double vitMoyenne) {
        this.vitMoyenne = vitMoyenne;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setVitMax(Double vitMax) {
        this.vitMax = vitMax;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getVitMax() {
        return vitMax;
    }

    public Double getVitMoyenne() {
        return vitMoyenne;
    }
}
