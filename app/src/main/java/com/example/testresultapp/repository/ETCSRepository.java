// app/src/main/java/com/example/testresultapp/repository/ETCSRepository.java
package com.example.testresultapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.testresultapp.database.AppDatabase;
import com.example.testresultapp.database.StationDao;
import com.example.testresultapp.database.TestDefinitionDao;
import com.example.testresultapp.database.TestResultDao;
import com.example.testresultapp.database.TrainJourneyDao;
import com.example.testresultapp.database.entity.StationEntity;
import com.example.testresultapp.database.entity.TestDefinitionEntity;
import com.example.testresultapp.database.entity.TestResultEntity;
import com.example.testresultapp.database.entity.TrainJourneyEntity;
import com.example.testresultapp.util.PredefinedTests;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ETCSRepository {
    private final TrainJourneyDao trainJourneyDao;
    private final StationDao stationDao;
    private final TestDefinitionDao testDefinitionDao;
    private final TestResultDao testResultDao;
    private final ExecutorService executorService;

    public ETCSRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        trainJourneyDao = db.trainJourneyDao();
        stationDao = db.stationDao();
        testDefinitionDao = db.testDefinitionDao();
        testResultDao = db.testResultDao();
        executorService = Executors.newFixedThreadPool(4);
    }

    // Train Journey metody
    public LiveData<List<TrainJourneyEntity>> getAllTrainJourneys() {
        return trainJourneyDao.getAllTrainJourneys();
    }

    public LiveData<TrainJourneyEntity> getTrainJourneyById(int id) {
        return trainJourneyDao.getTrainJourneyById(id);
    }

    public void insertTrainJourney(TrainJourneyEntity trainJourney, InsertCallback callback) {
        executorService.execute(() -> {
            long id = trainJourneyDao.insert(trainJourney);
            callback.onInsertComplete((int) id);
        });
    }

    // Station metody
    public LiveData<List<StationEntity>> getStationsForJourney(int journeyId) {
        return stationDao.getStationsForJourney(journeyId);
    }

    public void insertStation(StationEntity station, InsertCallback callback) {
        executorService.execute(() -> {
            long id = stationDao.insert(station);
            callback.onInsertComplete((int) id);
        });
    }

    // TestDefinition metody
    public LiveData<List<TestDefinitionEntity>> getAllTestDefinitions() {
        return testDefinitionDao.getAllTestDefinitions();
    }

    public void insertTestDefinition(TestDefinitionEntity testDefinition, InsertCallback callback) {
        executorService.execute(() -> {
            long id = testDefinitionDao.insert(testDefinition);
            callback.onInsertComplete((int) id);
        });
    }

    // TestResult metody
    public LiveData<List<TestResultEntity>> getTestResultsForStation(int stationId) {
        return testResultDao.getTestResultsForStation(stationId);
    }

    public LiveData<List<TestResultEntity>> getTestResultsForJourney(int journeyId) {
        return testResultDao.getTestResultsForJourney(journeyId);
    }






    public void updateTestResult(TestResultEntity testResult) {
        executorService.execute(() -> testResultDao.update(testResult));
    }

    // Interface pro callback po insertu
    public interface InsertCallback {
        void onInsertComplete(int id);

        // Výchozí implementace pro zpětnou kompatibilitu
        default void onInsertFailed(String errorMessage) {
            // Prázdná implementace
        }
    }

    // Přidat do třídy ETCSRepository:

    // Metody pro získání jednotlivých entit podle ID
    public LiveData<StationEntity> getStationById(int id) {
        return stationDao.getStationById(id);
    }

    public LiveData<TestDefinitionEntity> getTestDefinitionById(int id) {
        return testDefinitionDao.getTestDefinitionById(id);
    }

    public LiveData<TestResultEntity> getTestResultById(int id) {
        return testResultDao.getTestResultById(id);
    }


    // Do ETCSRepository.java přidáme novou metodu
    public void initializePredefinedTests() {
        executorService.execute(() -> {
            // Zkontrolujeme, zda už existují testy v databázi
            List<TestDefinitionEntity> existingTests = testDefinitionDao.getAllTestDefinitionsSync();

            if (existingTests == null || existingTests.isEmpty()) {
                // Vložíme všechny předefinované testy
                for (PredefinedTests.TestDefinition test : PredefinedTests.ETCS_TESTS) {
                    TestDefinitionEntity entity = new TestDefinitionEntity();
                    entity.setName(test.getName());
                    entity.setDescription(test.getDescription());
                    testDefinitionDao.insert(entity);
                }
            }
        });
    }


    // V ETCSRepository.java upravíme metodu insertTestResult
    public void insertTestResult(TestResultEntity testResult, InsertCallback callback) {
        executorService.execute(() -> {
            try {
                // Nejprve ověříme, že stanice existuje
                StationEntity station = stationDao.getStationByIdSync(testResult.getStationId());
                if (station == null) {
                    if (callback != null) {
                        callback.onInsertFailed("Stanice s ID " + testResult.getStationId() + " neexistuje");
                    }
                    return;
                }

                // Poté ověříme, že definice testu existuje
                TestDefinitionEntity testDef = testDefinitionDao.getTestDefinitionByIdSync(testResult.getTestDefinitionId());
                if (testDef == null) {
                    if (callback != null) {
                        callback.onInsertFailed("Definice testu s ID " + testResult.getTestDefinitionId() + " neexistuje");
                    }
                    return;
                }

                // Teď můžeme bezpečně vložit výsledek testu
                long id = testResultDao.insert(testResult);
                if (callback != null) {
                    callback.onInsertComplete((int) id);
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onInsertFailed(e.getMessage());
                }
            }
        });
    }

    // V ETCSRepository.java

}