// app/src/main/java/com/example/testresultapp/database/entity/StationEntity.java
package com.example.testresultapp.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "stations",
        foreignKeys = @ForeignKey(
                entity = TrainJourneyEntity.class,
                parentColumns = "id",
                childColumns = "journeyId",
                onDelete = ForeignKey.CASCADE
        ))
public class StationEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int journeyId;
    private String name;
    private String plannedArrivalTime;
    private int stationOrder; // Pořadí stanice v jízdě

    // Konstruktory, gettery a settery
    public StationEntity() { }

    public StationEntity(int journeyId, String name, String plannedArrivalTime, int stationOrder) {
        this.journeyId = journeyId;
        this.name = name;
        this.plannedArrivalTime = plannedArrivalTime;
        this.stationOrder = stationOrder;
    }

    // Gettery a settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getJourneyId() { return journeyId; }
    public void setJourneyId(int journeyId) { this.journeyId = journeyId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPlannedArrivalTime() { return plannedArrivalTime; }
    public void setPlannedArrivalTime(String plannedArrivalTime) { this.plannedArrivalTime = plannedArrivalTime; }
    public int getStationOrder() { return stationOrder; }
    public void setStationOrder(int stationOrder) { this.stationOrder = stationOrder; }
}