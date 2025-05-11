// app/src/main/java/com/example/testresultapp/database/TestDefinitionDao.java
package com.example.testresultapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testresultapp.database.entity.TestDefinitionEntity;

import java.util.List;

@Dao
public interface TestDefinitionDao {
    // Přidat do TestDefinitionDao.java
    @Insert
    long insertSync(TestDefinitionEntity testDefinition);

    @Query("SELECT COUNT(*) FROM test_definitions")
    int getTestDefinitionsCount();
    @Insert
    long insert(TestDefinitionEntity testDefinition);

    @Update
    void update(TestDefinitionEntity testDefinition);

    @Query("SELECT * FROM test_definitions ORDER BY name")
    LiveData<List<TestDefinitionEntity>> getAllTestDefinitions();

    @Query("SELECT * FROM test_definitions WHERE id = :id")
    LiveData<TestDefinitionEntity> getTestDefinitionById(int id);

    @Query("SELECT * FROM test_definitions ORDER BY name")
    List<TestDefinitionEntity> getAllTestDefinitionsSync();
    @Query("SELECT * FROM test_definitions WHERE id = :id")
    TestDefinitionEntity getTestDefinitionByIdSync(int id);
}