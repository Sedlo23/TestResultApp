// app/src/main/java/com/example/testresultapp/ui/StationListFragment.java
package com.example.testresultapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testresultapp.MainActivity;
import com.example.testresultapp.R;
import com.example.testresultapp.adapter.StationAdapter;
import com.example.testresultapp.database.entity.TrainJourneyEntity;
import com.example.testresultapp.viewmodel.ETCSViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StationListFragment extends Fragment {
    private static final String ARG_JOURNEY_ID = "journey_id";

    private int journeyId;
    private ETCSViewModel viewModel;
    private TextView trainInfoText;
    private RecyclerView recyclerView;
    private FloatingActionButton exportFab;

    public static StationListFragment newInstance(int journeyId) {
        StationListFragment fragment = new StationListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_JOURNEY_ID, journeyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            journeyId = getArguments().getInt(ARG_JOURNEY_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Používáme stejný layout jako pro seznam jízd, ale můžeme vytvořit vlastní pro seznam stanic
        View view = inflater.inflate(R.layout.fragment_station_list, container, false);

        trainInfoText = view.findViewById(R.id.text_train_info);
        recyclerView = view.findViewById(R.id.recycler_view_stations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        exportFab = view.findViewById(R.id.fab_export_pdf);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ETCSViewModel.class);

        StationAdapter adapter = new StationAdapter();
        recyclerView.setAdapter(adapter);

        // Posluchač pro kliknutí na stanici
        adapter.setOnItemClickListener(station -> {
            StationDetailFragment fragment = StationDetailFragment.newInstance(station.getId());
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        // Pozorování jízdy pro zobrazení informací
        viewModel.getTrainJourneyById(journeyId).observe(getViewLifecycleOwner(), journey -> {
            if (journey != null) {
                trainInfoText.setText(getString(R.string.train_info_format,
                        journey.getTrainNumber(), journey.getDate()));
            }
        });

        // Pozorování seznamu stanic pro danou jízdu
        viewModel.getStationsForJourney(journeyId).observe(getViewLifecycleOwner(), stations -> {
            if (stations != null) {
                adapter.setStations(stations);
            }
        });

        // Nastavení FAB pro export PDF
        exportFab.setOnClickListener(v -> exportPDF());
    }

    private void exportPDF() {
        // Implementace exportu do PDF
        // Tuto funkci je potřeba doplnit - bude používat dříve vytvořený PDFExporter
    }
}