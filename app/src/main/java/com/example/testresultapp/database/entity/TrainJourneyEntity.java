// app/src/main/java/com/example/testresultapp/database/entity/TrainJourneyEntity.java
package com.example.testresultapp.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "train_journeys")
public class TrainJourneyEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String trainNumber;
    private String date;

    // Konstruktory, gettery a settery
    public TrainJourneyEntity() { }

    public TrainJourneyEntity(String trainNumber, String date) {
        this.trainNumber = trainNumber;
        this.date = date;
    }

    // Gettery a settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}