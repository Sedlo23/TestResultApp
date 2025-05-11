// app/src/main/java/com/example/testresultapp/database/StationDao.java
package com.example.testresultapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testresultapp.database.entity.StationEntity;
import com.example.testresultapp.database.entity.TestDefinitionEntity;

import java.util.List;

@Dao
public interface StationDao {

    @Insert
    long insert(StationEntity station);

    @Update
    void update(StationEntity station);

    @Query("SELECT * FROM stations WHERE journeyId = :journeyId ORDER BY stationOrder")
    LiveData<List<StationEntity>> getStationsForJourney(int journeyId);

    @Query("SELECT * FROM stations WHERE id = :id")
    LiveData<StationEntity> getStationById(int id);

    // Do StationDao.java přidáme
    @Query("SELECT * FROM stations WHERE id = :id")
    StationEntity getStationByIdSync(int id);

    // Do TestDefinitionDao.java přidáme
    @Query("SELECT * FROM test_definitions WHERE id = :id")
    TestDefinitionEntity getTestDefinitionByIdSync(int id);
}