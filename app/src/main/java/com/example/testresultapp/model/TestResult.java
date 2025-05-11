// app/src/main/java/com/example/testresultapp/model/TestResult.java
package com.example.testresultapp.model;

public class TestResult {
    private int id;
    private int stationId;
    private int testDefinitionId;
    private String result; // "Prošel" nebo "Neprošel"
    private String photoPath;
    private String rescheduledTime; // null pokud test nebyl přesunut

    // Konstruktory, gettery a settery
    public TestResult() { }

    public TestResult(int id, int stationId, int testDefinitionId, String result) {
        this.id = id;
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
}