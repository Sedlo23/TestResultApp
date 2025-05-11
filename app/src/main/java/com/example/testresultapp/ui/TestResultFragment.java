// app/src/main/java/com/example/testresultapp/ui/TestResultFragment.java
package com.example.testresultapp.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testresultapp.R;
import com.example.testresultapp.database.entity.TestDefinitionEntity;
import com.example.testresultapp.database.entity.TestResultEntity;
import com.example.testresultapp.util.PhotoManager;
import com.example.testresultapp.viewmodel.ETCSViewModel;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class TestResultFragment extends Fragment {
    private static final String ARG_TEST_RESULT_ID = "test_result_id";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private int testResultId;
    private ETCSViewModel viewModel;
    private PhotoManager photoManager;

    private TextView testNameText;
    private TextView testDescriptionText;
    private RadioGroup resultRadioGroup;
    private RadioButton passRadio;
    private RadioButton failRadio;
    private RadioButton rescheduleRadio;
    private LinearLayout rescheduleLayout;
    private EditText rescheduleTimeEdit;
    private Button takePhotoButton;
    private ImageView testPhotoImage;
    private Button saveResultButton;

    private TestResultEntity currentTestResult;
    private TestDefinitionEntity currentTestDefinition;

    // app/src/main/java/com/example/testresultapp/database/entity/TestResultEntity.java
// Přidání nového pole pro čas testu
    private String testTime; // Kdy byl test skutečně proveden

    // Přidání getteru a setteru
    public String getTestTime() { return testTime; }
    public void setTestTime(String testTime) { this.testTime = testTime; }


    public static TestResultFragment newInstance(int testResultId) {
        TestResultFragment fragment = new TestResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TEST_RESULT_ID, testResultId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testResultId = getArguments().getInt(ARG_TEST_RESULT_ID);
        }

        photoManager = new PhotoManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_result, container, false);

        testNameText = view.findViewById(R.id.text_test_name);
        testDescriptionText = view.findViewById(R.id.text_test_description);
        resultRadioGroup = view.findViewById(R.id.radio_group_result);
        passRadio = view.findViewById(R.id.radio_pass);
        failRadio = view.findViewById(R.id.radio_fail);
        rescheduleRadio = view.findViewById(R.id.radio_reschedule);
        rescheduleLayout = view.findViewById(R.id.layout_reschedule);
        rescheduleTimeEdit = view.findViewById(R.id.edit_reschedule_time);
        takePhotoButton = view.findViewById(R.id.button_take_photo);
        testPhotoImage = view.findViewById(R.id.image_test_photo);
        saveResultButton = view.findViewById(R.id.button_save_result);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ETCSViewModel.class);

        // Nastavení listener pro radio tlačítka
        resultRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_reschedule) {
                rescheduleLayout.setVisibility(View.VISIBLE);
            } else {
                rescheduleLayout.setVisibility(View.GONE);
            }
        });

        // Nastavení time pickeru pro čas přesunutí
        rescheduleTimeEdit.setOnClickListener(v -> {
            // Implementace time pickeru
            // Pro jednoduchost tady jen ukázkové nastavení - v reálné aplikaci přidejte time picker
            rescheduleTimeEdit.setText("14:00");
        });

        // Nastavení tlačítka pro fotografii
        takePhotoButton.setOnClickListener(v -> dispatchTakePictureIntent());

        // Načtení výsledku testu
        viewModel.getTestResultById(testResultId).observe(getViewLifecycleOwner(), testResult -> {
            if (testResult != null) {
                currentTestResult = testResult;

                // Nastavení aktuálního výsledku
                if ("Prošel".equals(testResult.getResult())) {
                    passRadio.setChecked(true);
                } else if ("Neprošel".equals(testResult.getResult())) {
                    failRadio.setChecked(true);
                } else if (testResult.getRescheduledTime() != null) {
                    rescheduleRadio.setChecked(true);
                    rescheduleTimeEdit.setText(testResult.getRescheduledTime());
                    rescheduleLayout.setVisibility(View.VISIBLE);
                }

                // Zobrazení času testu, pokud existuje
                EditText testTimeEdit = requireView().findViewById(R.id.edit_test_time);
                if (testResult.getTestTime() != null && !testResult.getTestTime().isEmpty()) {
                    testTimeEdit.setText(testResult.getTestTime());
                }

                // Zobrazení fotografie, pokud existuje
                if (testResult.getPhotoPath() != null && !testResult.getPhotoPath().isEmpty()) {
                    File photoFile = new File(testResult.getPhotoPath());
                    if (photoFile.exists()) {
                        testPhotoImage.setImageURI(Uri.fromFile(photoFile));
                        testPhotoImage.setVisibility(View.VISIBLE);
                    }
                }

                // Načtení definice testu pro zobrazení názvu a popisu
                viewModel.getTestDefinitionById(testResult.getTestDefinitionId())
                        .observe(getViewLifecycleOwner(), testDefinition -> {
                            if (testDefinition != null) {
                                currentTestDefinition = testDefinition;
                                testNameText.setText(testDefinition.getName());
                                testDescriptionText.setText(testDefinition.getDescription());
                            }
                        });
            }
        });

        // Nastavení tlačítka pro uložení
        saveResultButton.setOnClickListener(v -> saveTestResult());

        // Přidání tlačítek pro aktuální čas a možnost úpravy
        Button addCurrentTimeButton = view.findViewById(R.id.button_add_current_time);
        Button editTimeButton = view.findViewById(R.id.button_edit_time);
        EditText testTimeEditText = view.findViewById(R.id.edit_test_time);

        // Tlačítko pro přidání aktuálního času
        addCurrentTimeButton.setOnClickListener(v -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            testTimeEditText.setText(currentTime);

            // Aktualizujeme také model dat
            if (currentTestResult != null) {
                currentTestResult.setTestTime(currentTime);
            }
        });

        // Tlačítko pro editaci času
        editTimeButton.setOnClickListener(v -> {
            showDateTimePicker(testTimeEditText);
        });


    }

    // Metoda pro zobrazení date-time pickeru
    private void showDateTimePicker(final EditText editText) {
        // Aktuální datum a čas
        final Calendar calendar = Calendar.getInstance();

        // Pokud je v editText již čas, načteme ho
        if (!TextUtils.isEmpty(editText.getText())) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
                Date date = sdf.parse(editText.getText().toString());
                if (date != null) {
                    calendar.setTime(date);
                }
            } catch (ParseException | java.text.ParseException e) {
                // V případě chyby použijeme aktuální čas
            }
        }

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
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
                                String formattedDate = sdf.format(calendar.getTime());
                                editText.setText(formattedDate);

                                // Aktualizujeme také model dat
                                if (currentTestResult != null) {
                                    currentTestResult.setTestTime(formattedDate);
                                }
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

    private void dispatchTakePictureIntent() {
        // Nejprve zkontrolujeme oprávnění
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Vyžádáme oprávnění, pokud není uděleno
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Pokračujeme s pořízením fotografie
            launchCamera();
        }
    }

    private void launchCamera() {
        try {
            Intent takePictureIntent = photoManager.createTakePhotoIntent();
            if (takePictureIntent != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Chyba při vytváření fotografie", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                Toast.makeText(requireContext(), "Oprávnění ke kameře je potřeba pro pořízení fotografie",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Fotografie byla pořízena a uložena
            String photoPath = photoManager.getCurrentPhotoPath();
            if (photoPath != null) {
                // Zobrazení pořízené fotografie
                testPhotoImage.setImageURI(Uri.fromFile(new File(photoPath)));
                testPhotoImage.setVisibility(View.VISIBLE);

                // Uložení cesty k fotografii do výsledku testu
                if (currentTestResult != null) {
                    currentTestResult.setPhotoPath(photoPath);
                    viewModel.updateTestResult(currentTestResult);
                }
            }
        }
    }

    // V TestResultFragment.java
    private void saveTestResult() {
        if (currentTestResult == null) {
            return;
        }

        // Určení výsledku podle vybraného radio tlačítka
        int selectedRadioId = resultRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioId == R.id.radio_pass) {
            currentTestResult.setResult("Prošel");
            currentTestResult.setRescheduledTime(null);
        } else if (selectedRadioId == R.id.radio_fail) {
            currentTestResult.setResult("Neprošel");
            currentTestResult.setRescheduledTime(null);
        } else if (selectedRadioId == R.id.radio_reschedule) {
            currentTestResult.setResult("Přesunuto");
            currentTestResult.setRescheduledTime(rescheduleTimeEdit.getText().toString());
        }

        // Uložení času testu
        EditText testTimeEdit = requireView().findViewById(R.id.edit_test_time);
        if (!TextUtils.isEmpty(testTimeEdit.getText())) {
            currentTestResult.setTestTime(testTimeEdit.getText().toString());
        }

        // Uložení výsledku
        viewModel.updateTestResult(currentTestResult);

        // Návrat na předchozí obrazovku
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}