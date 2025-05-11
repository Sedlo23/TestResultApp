// app/src/main/java/com/example/testresultapp/database/TestResultDao.java
package com.example.testresultapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testresultapp.database.entity.TestResultEntity;

import java.util.List;

@Dao
public interface TestResultDao {

    @Insert
    long insert(TestResultEntity testResult);

    @Update
    void update(TestResultEntity testResult);

    @Query("SELECT * FROM test_results WHERE stationId = :stationId")
    LiveData<List<TestResultEntity>> getTestResultsForStation(int stationId);

    @Query("SELECT * FROM test_results WHERE id = :id")
    LiveData<TestResultEntity> getTestResultById(int id);

    @Query("SELECT * FROM test_results " +
            "JOIN stations ON test_results.stationId = stations.id " +
            "WHERE stations.journeyId = :journeyId")

    LiveData<List<TestResultEntity>> getTestResultsForJourney(int journeyId);
}