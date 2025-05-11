// app/src/main/java/com/example/testresultapp/ui/CreateJourneyFragment.java
package com.example.testresultapp.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testresultapp.MainActivity;
import com.example.testresultapp.R;
import com.example.testresultapp.adapter.StationAdapter;
import com.example.testresultapp.database.entity.StationEntity;
import com.example.testresultapp.database.entity.TrainJourneyEntity;
import com.example.testresultapp.util.StationsList;
import com.example.testresultapp.viewmodel.ETCSViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class CreateJourneyFragment extends Fragment {
    private ETCSViewModel viewModel;
    private TextInputEditText trainNumberEdit;
    private TextInputEditText dateEdit;
    private RecyclerView stationsRecyclerView;
    private StationAdapter stationAdapter;
    private List<StationEntity> tempStations = new ArrayList<>();
    private int stationOrder = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_journey, container, false);

        trainNumberEdit = view.findViewById(R.id.edit_train_number);
        dateEdit = view.findViewById(R.id.edit_date);
        stationsRecyclerView = view.findViewById(R.id.recycler_view_stations);

        Button addStationButton = view.findViewById(R.id.button_add_station);
        Button saveJourneyButton = view.findViewById(R.id.button_save_journey);

        // Nastavení RecyclerView
        stationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stationAdapter = new StationAdapter();
        stationsRecyclerView.setAdapter(stationAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ETCSViewModel.class);

        // Nastavení datumového pole
        dateEdit.setOnClickListener(v -> showDatePicker());

        // Přidání stanice
        view.findViewById(R.id.button_add_station).setOnClickListener(v -> showAddStationDialog());

        // Uložení jízdy
        view.findViewById(R.id.button_save_journey).setOnClickListener(v -> saveJourney());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear;
                    dateEdit.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    // app/src/main/java/com/example/testresultapp/ui/CreateJourneyFragment.java
// Upravíme metodu showAddStationDialog()

    private void showAddStationDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_station, null);

        // Nahradíme EditText pro jméno stanice za Spinner
        Spinner stationSpinner = dialogView.findViewById(R.id.spinner_station_name);
        EditText arrivalTimeEdit = dialogView.findViewById(R.id.edit_arrival_time);

        // Nastavení adaptéru pro spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                StationsList.getAllStations());
        stationSpinner.setAdapter(adapter);

        // Nastavení date & time pickeru pro čas příjezdu
        arrivalTimeEdit.setOnClickListener(v -> showDateTimePicker(arrivalTimeEdit));

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.add_station)
                .setView(dialogView)
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    String stationName = stationSpinner.getSelectedItem().toString();
                    String arrivalTime = arrivalTimeEdit.getText().toString();

                    if (!stationName.isEmpty() && !arrivalTime.isEmpty()) {
                        StationEntity station = new StationEntity();
                        station.setName(stationName);
                        station.setPlannedArrivalTime(arrivalTime);
                        station.setStationOrder(stationOrder++);

                        tempStations.add(station);
                        stationAdapter.setStations(tempStations);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    // Přidáme do CreateJourneyFragment
    private void showDateTimePicker(final EditText editText) {
        // Aktuální datum a čas
        final Calendar calendar = Calendar.getInstance();

        // DatePickerDialog pro výběr datumu
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Po výběru datumu zobrazíme TimePickerDialog
                    new TimePickerDialog(
                            requireContext(),
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // Formátování vybraného datumu a času
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                                String formattedDate = sdf.format(calendar.getTime());
                                editText.setText(formattedDate);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    ).show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveJourney() {
        String trainNumber = trainNumberEdit.getText().toString();
        String date = dateEdit.getText().toString();

        if (trainNumber.isEmpty() || date.isEmpty() || tempStations.isEmpty()) {
            Toast.makeText(requireContext(), "Vyplňte všechna pole a přidejte alespoň jednu stanici",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Uložení jízdy
        TrainJourneyEntity journey = new TrainJourneyEntity(trainNumber, date);

        // Atomická proměnná pro sledování vložených stanic
        final AtomicInteger stationsInserted = new AtomicInteger(0);

        // Nastavit indikátor průběhu
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Ukládání");
        progressDialog.setMessage("Ukládání jízdy a stanic...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        viewModel.insertTrainJourney(journey);

        // Poslech na ID vložené jízdy a následné sekvenční uložení stanic
        viewModel.getInsertedId().observe(getViewLifecycleOwner(), journeyId -> {
            if (journeyId != null && journeyId > 0) {
                // Ukládáme jednu stanici za druhou
                saveNextStation(journeyId, 0, progressDialog);
            } else {
                progressDialog.dismiss();
                Toast.makeText(requireContext(), "Chyba při ukládání jízdy", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Metoda pro sekvenční ukládání stanic
    private void saveNextStation(int journeyId, int stationIndex, ProgressDialog progressDialog) {
        // Kontrola, zda jsme uložili všechny stanice
        if (stationIndex >= tempStations.size()) {
            // Všechny stanice byly uloženy, vrátíme se na seznam jízd
            progressDialog.dismiss();
            requireActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        // Aktualizovat zprávu dialogu
        progressDialog.setMessage("Ukládání stanice " + (stationIndex + 1) + " z " + tempStations.size());

        // Získáme aktuální stanici a nastavíme jí ID jízdy
        StationEntity station = tempStations.get(stationIndex);
        station.setJourneyId(journeyId);

        // Uložíme stanici a po jejím uložení pokračujeme další stanicí
        viewModel.insertStation(station);

        viewModel.getInsertedId().observe(getViewLifecycleOwner(), stationId -> {
            if (stationId != null && stationId > 0) {
                // Stanice byla úspěšně uložena, pokračujeme další stanicí
                viewModel.getInsertedId().removeObservers(getViewLifecycleOwner());
                saveNextStation(journeyId, stationIndex + 1, progressDialog);
            }
        });
    }
}