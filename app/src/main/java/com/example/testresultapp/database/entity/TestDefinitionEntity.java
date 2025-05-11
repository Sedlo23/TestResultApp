// app/src/main/java/com/example/testresultapp/database/entity/TestDefinitionEntity.java
package com.example.testresultapp.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "test_definitions")
public class TestDefinitionEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;

    // Konstruktory, gettery a settery
    public TestDefinitionEntity() { }

    public TestDefinitionEntity(String name, String description) {
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