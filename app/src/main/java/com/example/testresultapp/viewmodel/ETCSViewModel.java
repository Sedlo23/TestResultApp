// app/src/main/java/com/example/testresultapp/viewmodel/ETCSViewModel.java
package com.example.testresultapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testresultapp.database.entity.StationEntity;
import com.example.testresultapp.database.entity.TestDefinitionEntity;
import com.example.testresultapp.database.entity.TestResultEntity;
import com.example.testresultapp.database.entity.TrainJourneyEntity;
import com.example.testresultapp.repository.ETCSRepository;

import java.util.List;

public class ETCSViewModel extends AndroidViewModel {
    private final ETCSRepository repository;
    private final MutableLiveData<Integer> insertedId = new MutableLiveData<>();

    public ETCSViewModel(@NonNull Application application) {
        super(application);
        repository = new ETCSRepository(application);
    }

    // Train Journey metody
    public LiveData<List<TrainJourneyEntity>> getAllTrainJourneys() {
        return repository.getAllTrainJourneys();
    }

    public LiveData<TrainJourneyEntity> getTrainJourneyById(int id) {
        return repository.getTrainJourneyById(id);
    }

    public LiveData<Integer> getInsertedId() {
        return insertedId;
    }

    public void insertTrainJourney(TrainJourneyEntity trainJourney) {
        repository.insertTrainJourney(trainJourney, id -> insertedId.postValue(id));
    }



    // Station metody
    public LiveData<List<StationEntity>> getStationsForJourney(int journeyId) {
        return repository.getStationsForJourney(journeyId);
    }

    public void insertStation(StationEntity station) {
        repository.insertStation(station, id -> insertedId.postValue(id));
    }

    // TestDefinition metody
    public LiveData<List<TestDefinitionEntity>> getAllTestDefinitions() {
        return repository.getAllTestDefinitions();
    }

    public void insertTestDefinition(TestDefinitionEntity testDefinition) {
        repository.insertTestDefinition(testDefinition, id -> insertedId.postValue(id));
    }

    // TestResult metody
    public LiveData<List<TestResultEntity>> getTestResultsForStation(int stationId) {
        return repository.getTestResultsForStation(stationId);
    }

    public LiveData<List<TestResultEntity>> getTestResultsForJourney(int journeyId) {
        return repository.getTestResultsForJourney(journeyId);
    }

    public void insertTestResult(TestResultEntity testResult) {
        repository.insertTestResult(testResult, id -> insertedId.postValue(id));
    }

    public void updateTestResult(TestResultEntity testResult) {
        repository.updateTestResult(testResult);
    }

    // Přidat do třídy ETCSViewModel:

    // Metody pro získání jednotlivých entit podle ID
    public LiveData<StationEntity> getStationById(int id) {
        return repository.getStationById(id);
    }

    public LiveData<TestDefinitionEntity> getTestDefinitionById(int id) {
        return repository.getTestDefinitionById(id);
    }

    public LiveData<TestResultEntity> getTestResultById(int id) {
        return repository.getTestResultById(id);
    }

    public void initializePredefinedTests() {
        repository.initializePredefinedTests();
    }

    // V ETCSViewModel.java přidáme metodu
    public void insertTestResultWithCallback(TestResultEntity testResult, ETCSRepository.InsertCallback callback) {
        repository.insertTestResult(testResult, callback);
    }
}