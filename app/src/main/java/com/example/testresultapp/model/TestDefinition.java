// app/src/main/java/com/example/testresultapp/model/TestDefinition.java
package com.example.testresultapp.model;

public class TestDefinition {
    private int id;
    private String name;
    private String description;

    // Konstruktory, gettery a settery
    public TestDefinition() { }

    public TestDefinition(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Gettery a settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}