// app/src/main/java/com/example/testresultapp/model/TrainJourney.java
package com.example.testresultapp.model;

import java.util.ArrayList;
import java.util.List;

public class TrainJourney {
    private int id;
    private String trainNumber;
    private String date;
    private List<Station> stations;

    // Konstruktory, gettery a settery
    public TrainJourney() {
        stations = new ArrayList<>();
    }

    public TrainJourney(int id, String trainNumber, String date) {
        this.id = id;
        this.trainNumber = trainNumber;
        this.date = date;
        this.stations = new ArrayList<>();
    }

    // Gettery a settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<Station> getStations() { return stations; }
    public void setStations(List<Station> stations) { this.stations = stations; }
    public void addStation(Station station) { this.stations.add(station); }
}