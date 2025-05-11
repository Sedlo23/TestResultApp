// app/src/main/java/com/example/testresultapp/database/AppDatabase.java
package com.example.testresultapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.testresultapp.database.entity.StationEntity;
import com.example.testresultapp.database.entity.TestDefinitionEntity;
import com.example.testresultapp.database.entity.TestResultEntity;
import com.example.testresultapp.database.entity.TrainJourneyEntity;

@Database(entities = {
        TrainJourneyEntity.class,
        StationEntity.class,
        TestDefinitionEntity.class,
        TestResultEntity.class
}, version = 2, exportSchema = false)  // Zvýšení verze z 1 na 2
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract TrainJourneyDao trainJourneyDao();
    public abstract StationDao stationDao();
    public abstract TestDefinitionDao testDefinitionDao();
    public abstract TestResultDao testResultDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "etcs_test_database"
                            )
                            .fallbackToDestructiveMigration()  // Toto způsobí smazání a znovuvytvoření databáze při změně verze
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}