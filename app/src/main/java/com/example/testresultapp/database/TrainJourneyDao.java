// app/src/main/java/com/example/testresultapp/database/TrainJourneyDao.java
package com.example.testresultapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testresultapp.database.entity.TrainJourneyEntity;

import java.util.List;

@Dao
public interface TrainJourneyDao {

    @Insert
    long insert(TrainJourneyEntity trainJourney);

    @Update
    void update(TrainJourneyEntity trainJourney);

    @Query("SELECT * FROM train_journeys ORDER BY date DESC")
    LiveData<List<TrainJourneyEntity>> getAllTrainJourneys();

    @Query("SELECT * FROM train_journeys WHERE id = :id")
    LiveData<TrainJourneyEntity> getTrainJourneyById(int id);
}