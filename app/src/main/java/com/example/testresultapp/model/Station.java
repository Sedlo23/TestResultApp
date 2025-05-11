// app/src/main/java/com/example/testresultapp/model/Station.java
package com.example.testresultapp.model;

public class Station {
    private int id;
    private String name;
    private String plannedArrivalTime;

    // Konstruktory, gettery a settery
    public Station() { }

    public Station(int id, String name, String plannedArrivalTime) {
        this.id = id;
        this.name = name;
        this.plannedArrivalTime = plannedArrivalTime;
    }

    // Gettery a settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPlannedArrivalTime() { return plannedArrivalTime; }
    public void setPlannedArrivalTime(String plannedArrivalTime) { this.plannedArrivalTime = plannedArrivalTime; }
}