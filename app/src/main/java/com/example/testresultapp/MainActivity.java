// app/src/main/java/com/example/testresultapp/MainActivity.java
package com.example.testresultapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testresultapp.ui.CreateJourneyFragment;
import com.example.testresultapp.ui.JourneyListFragment;
import com.example.testresultapp.viewmodel.ETCSViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Do onCreate metody v MainActivity.java přidejme
        ETCSViewModel viewModel = new ViewModelProvider(this).get(ETCSViewModel.class);
        viewModel.initializePredefinedTests();

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(view -> {
            loadFragment(new CreateJourneyFragment(), true);
        });

        if (savedInstanceState == null) {
            // Načtení JourneyListFragment jako výchozí obrazovku
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new JourneyListFragment())
                    .commit();
        }
    }
    // V MainActivity.java přidat metodu
    public void setFabAction(View.OnClickListener action) {
        FloatingActionButton fab = findViewById(R.id.fab_add);
        if (fab != null) {
            fab.setOnClickListener(action);
            fab.show(); // Zajistíme, že je tlačítko viditelné
        }
    }
    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}