// app/src/main/java/com/example/testresultapp/ui/StationDetailFragment.java
package com.example.testresultapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testresultapp.MainActivity;
import com.example.testresultapp.R;
import com.example.testresultapp.adapter.TestAdapter;
import com.example.testresultapp.database.entity.StationEntity;
import com.example.testresultapp.database.entity.TestDefinitionEntity;
import com.example.testresultapp.database.entity.TestResultEntity;
import com.example.testresultapp.repository.ETCSRepository;
import com.example.testresultapp.util.PredefinedTests;
import com.example.testresultapp.viewmodel.ETCSViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class StationDetailFragment extends Fragment {
    private static final String ARG_STATION_ID = "station_id";

    private int stationId;
    private ETCSViewModel viewModel;
    private TextView stationNameText;
    private TextView plannedTimeText;
    private RecyclerView testsRecyclerView;
    private TestAdapter testAdapter;
    private List<TestDefinitionEntity> testDefinitions = new ArrayList<>();

    public static StationDetailFragment newInstance(int stationId) {
        StationDetailFragment fragment = new StationDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATION_ID, stationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stationId = getArguments().getInt(ARG_STATION_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station_detail, container, false);

        stationNameText = view.findViewById(R.id.text_station_name);
        plannedTimeText = view.findViewById(R.id.text_planned_time);
        testsRecyclerView = view.findViewById(R.id.recycler_view_tests);
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // FAB pro přidání nového testu
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddTestDialog());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (!isAdded()) return;  // Přidáme tuto kontrolu

        viewModel = new ViewModelProvider(requireActivity()).get(ETCSViewModel.class);

        testAdapter = new TestAdapter();
        testsRecyclerView.setAdapter(testAdapter);

        // Posluchač na kliknutí na test
        testAdapter.setOnItemClickListener(testResult -> {
            TestResultFragment fragment = TestResultFragment.newInstance(testResult.getId());
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        // Načtení detailů stanice
        viewModel.getStationById(stationId).observe(getViewLifecycleOwner(), station -> {
            if (station != null) {
                stationNameText.setText(station.getName());
                plannedTimeText.setText(getString(R.string.planned_time_format, station.getPlannedArrivalTime()));
            }
        });

        // Načtení všech dostupných definic testů
        viewModel.getAllTestDefinitions().observe(getViewLifecycleOwner(), definitions -> {
            if (definitions != null) {
                testDefinitions = definitions;
            }
        });

        // Načtení výsledků testů pro tuto stanici
        viewModel.getTestResultsForStation(stationId).observe(getViewLifecycleOwner(), testResults -> {
            if (testResults != null) {
                testAdapter.setTestResults(testResults, testDefinitions);
            }
        });

        // Nastavení vlastního FAB pro přidání nového testu
        FloatingActionButton fabAddTest = view.findViewById(R.id.fab_add_test);
        fabAddTest.setOnClickListener(v -> showAddTestDialog());
// Nastavení FAB pomocí MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFabAction(v -> showAddTestDialog());
        }

    }

    private void showAddTestDialog() {
        // Kontrola, zda je fragment stále připojen k aktivitě
        if (!isAdded()) {
            return;
        }

        // Použijeme přímo předem definované testy místo načítání z databáze
        List<PredefinedTests.TestDefinition> predefinedTests = PredefinedTests.ETCS_TESTS;

        // Dialog pro výběr testu ze seznamu definic
        String[] testNames = new String[predefinedTests.size()];
        for (int i = 0; i < predefinedTests.size(); i++) {
            testNames[i] = predefinedTests.get(i).getName();
        }

        // Uložíme referenci na aktivitu, abychom ji mohli použít později
        final FragmentActivity activity = requireActivity();

        // Nejprve načteme existující definice testů z databáze
        viewModel.getAllTestDefinitions().observe(activity, existingTests -> {
            // Opět kontrola, zda je fragment stále připojen
            if (!isAdded()) {
                return;
            }

            if (existingTests != null) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.select_test)
                        .setItems(testNames, (dialog, which) -> {
                            // Znovu kontrola, zda je fragment stále připojen
                            if (!isAdded()) {
                                return;
                            }

                            PredefinedTests.TestDefinition selectedPredefinedTest = predefinedTests.get(which);

                            // Hledáme, zda už tento test existuje v databázi
                            TestDefinitionEntity existingTest = findExistingTest(existingTests, selectedPredefinedTest.getName());

                            if (existingTest != null) {
                                // Test už existuje, použijeme jeho ID
                                createTestResult(existingTest.getId());
                            } else {
                                // Test ještě neexistuje, vytvoříme ho
                                TestDefinitionEntity newTestDef = new TestDefinitionEntity();
                                newTestDef.setName(selectedPredefinedTest.getName());
                                newTestDef.setDescription(selectedPredefinedTest.getDescription());

                                viewModel.insertTestDefinition(newTestDef);

                                // Po vložení definice testu vytvoříme výsledek testu
                                // Použití aktivity místo viewLifecycleOwner
                                viewModel.getInsertedId().observe(activity, newTestId -> {
                                    if (!isAdded()) {
                                        return;
                                    }

                                    if (newTestId != null && newTestId > 0) {
                                        createTestResult(newTestId);

                                        // Odstranění observeru, aby se nespustil vícekrát
                                        viewModel.getInsertedId().removeObservers(activity);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
    }

    private TestDefinitionEntity findExistingTest(List<TestDefinitionEntity> tests, String testName) {
        for (TestDefinitionEntity test : tests) {
            if (test.getName().equals(testName)) {
                return test;
            }
        }
        return null;
    }

    // Pomocná metoda pro vytvoření výsledku testu
    // V StationDetailFragment.java upravíme metodu createTestResult

    // Také upravíme metodu createTestResult
    private void createTestResult(int testDefinitionId) {
        // Kontrola, zda je fragment stále připojen
        if (!isAdded()) {
            return;
        }

        // Uložíme referenci na aktivitu
        final FragmentActivity activity = requireActivity();

        // Nejprve ověříme, zda stanice a definice testu existují
        viewModel.getStationById(stationId).observe(activity, station -> {
            // Opět kontrola, zda je fragment stále připojen
            if (!isAdded()) {
                return;
            }

            if (station == null) {
                Toast.makeText(requireContext(), "Chyba: Stanice nebyla nalezena", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.getTestDefinitionById(testDefinitionId).observe(activity, testDefinition -> {
                // Opět kontrola, zda je fragment stále připojen
                if (!isAdded()) {
                    return;
                }

                if (testDefinition == null) {
                    Toast.makeText(requireContext(), "Chyba: Definice testu nebyla nalezena", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Oba záznamy existují, můžeme vytvořit výsledek testu
                TestResultEntity testResult = new TestResultEntity();
                testResult.setStationId(stationId);
                testResult.setTestDefinitionId(testDefinitionId);
                testResult.setResult(getString(R.string.not_tested_yet));

                // Použijeme callback pro sledování výsledku operace
                viewModel.insertTestResultWithCallback(testResult, new ETCSRepository.InsertCallback() {
                    @Override
                    public void onInsertComplete(int id) {
                        // Spuštění kódu na hlavním vlákně
                        activity.runOnUiThread(() -> {
                            // Opět kontrola, zda je fragment stále připojen
                            if (!isAdded()) {
                                return;
                            }

                            TestResultFragment fragment = TestResultFragment.newInstance(id);
                            ((MainActivity) activity).loadFragment(fragment, true);
                        });
                    }

                    @Override
                    public void onInsertFailed(String errorMessage) {
                        activity.runOnUiThread(() -> {
                            // Opět kontrola, zda je fragment stále připojen
                            if (!isAdded()) {
                                return;
                            }

                            Toast.makeText(requireContext(), "Chyba: " + errorMessage, Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });
        });
    }

    private void showCreateTestDefinitionDialog() {

        if (!isAdded()) return;  // Přidáme tuto kontrolu

        // Dialog pro vytvoření nové definice testu, pokud ještě žádné neexistují
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_test_definition, null);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.create_new_test_definition)
                .setView(dialogView)
                .setPositiveButton(R.string.create, (dialog, which) -> {
                    String testName = ((TextView) dialogView.findViewById(R.id.edit_test_name)).getText().toString();
                    String testDescription = ((TextView) dialogView.findViewById(R.id.edit_test_description)).getText().toString();

                    if (!testName.isEmpty()) {
                        TestDefinitionEntity testDefinition = new TestDefinitionEntity();
                        testDefinition.setName(testName);
                        testDefinition.setDescription(testDescription);

                        viewModel.insertTestDefinition(testDefinition);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}