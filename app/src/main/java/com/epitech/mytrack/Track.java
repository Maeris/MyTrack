package com.epitech.mytrack;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Sevilyoti on 22/02/2015.
 */
public class Track {

    private Double vitMoyenne;
    private Double vitMax;
    private Double distance;
    private Double time;
    private LatLng startPoint;
    private LatLng endPoint;

    private String date;

    public Track() {
    }

    public Track(Double vitMoyenne, Double vitMax, Double distance, Double time, LatLng startPoint, LatLng endPoint, String date)
    {
        this.vitMoyenne = vitMoyenne;
        this.vitMax = vitMax;
        this.distance = distance;
        this.time = time;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
    }

    public HashMap<String, String> hashMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("vitMoy", vitMoyenne.toString());
        map.put("vitMax", vitMax.toString());
        map.put("distance", distance.toString());
        map.put("time", time.toString());
        map.put("startPoint", startPoint.toString());
        map.put("endPoint", endPoint.toString());
        map.put("date", date);
        return map;
    }

    public void setVitMoyenne(Double vitMoyenne) {
        this.vitMoyenne = vitMoyenne;
    }

    public void setEndPoint(LatLng endPoint) {
        this.endPoint = endPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setVitMax(Double vitMax) {
        this.vitMax = vitMax;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public LatLng getStartPoint() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
