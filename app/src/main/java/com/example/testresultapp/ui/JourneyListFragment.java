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
import com.example.testresultapp.adapter.JourneyAdapter;
import com.example.testresultapp.database.entity.TrainJourneyEntity;
import com.example.testresultapp.viewmodel.ETCSViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class JourneyListFragment extends Fragment {
    private ETCSViewModel viewModel;
    private TextView noJourneysText;
    private RecyclerView recyclerView;
    private JourneyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journey_list, container, false);

        try {
            noJourneysText = view.findViewById(R.id.text_no_journeys);
            recyclerView = view.findViewById(R.id.recycler_view_journeys);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Inicializace adaptéru s prázdným seznamem
            adapter = new JourneyAdapter();
            adapter.setJourneys(new ArrayList<>()); // Předejde NullPointerException
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            viewModel = new ViewModelProvider(requireActivity()).get(ETCSViewModel.class);

            // Posluchač pro kliknutí na jízdu
            adapter.setOnItemClickListener(journey -> {
                StationListFragment fragment = StationListFragment.newInstance(journey.getId());
                ((MainActivity) requireActivity()).loadFragment(fragment, true);
            });

            // Pozorování seznamu jízd
            viewModel.getAllTrainJourneys().observe(getViewLifecycleOwner(), journeys -> {
                if (journeys != null && !journeys.isEmpty()) {
                    adapter.setJourneys(journeys);
                    recyclerView.setVisibility(View.VISIBLE);
                    noJourneysText.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noJourneysText.setVisibility(View.VISIBLE);
                }
            });

            // Nastavení FAB v MainActivity
            MainActivity activity = (MainActivity) requireActivity();
            FloatingActionButton fab = activity.findViewById(R.id.fab_add);
            if (fab != null) {
                fab.setOnClickListener(v -> {
                    activity.loadFragment(new CreateJourneyFragment(), true);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}