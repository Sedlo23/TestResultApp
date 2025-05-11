// app/src/main/java/com/example/testresultapp/database/entity/TestResultEntity.java
package com.example.testresultapp.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// app/src/main/java/com/example/testresultapp/database/entity/TestResultEntity.java
@Entity(tableName = "test_results",
        foreignKeys = {
                @ForeignKey(
                        entity = StationEntity.class,
                        parentColumns = "id",
                        childColumns = "stationId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = TestDefinitionEntity.class,
                        parentColumns = "id",
                        childColumns = "testDefinitionId",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index("stationId"),
                @Index("testDefinitionId")
        })
public class TestResultEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int stationId;
    private int testDefinitionId;
    private String result; // "Prošel" nebo "Neprošel"
    private String photoPath;
    private String rescheduledTime; // null pokud test nebyl přesunut
    private String testTime;        // Nové pole - Kdy byl test skutečně proveden

    // Konstruktory
    public TestResultEntity() { }

    @Ignore
    public TestResultEntity(int stationId, int testDefinitionId, String result) {
        this.stationId = stationId;
        this.testDefinitionId = testDefinitionId;
        this.result = result;
    }

    // Gettery a settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getStationId() { return stationId; }
    public void setStationId(int stationId) { this.stationId = stationId; }
    public int getTestDefinitionId() { return testDefinitionId; }
    public void setTestDefinitionId(int testDefinitionId) { this.testDefinitionId = testDefinitionId; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public String getRescheduledTime() { return rescheduledTime; }
    public void setRescheduledTime(String rescheduledTime) { this.rescheduledTime = rescheduledTime; }
    public String getTestTime() { return testTime; }
    public void setTestTime(String testTime) { this.testTime = testTime; }
}