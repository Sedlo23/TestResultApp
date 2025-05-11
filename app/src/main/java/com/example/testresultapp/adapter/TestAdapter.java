// app/src/main/java/com/example/testresultapp/adapter/TestAdapter.java
package com.example.testresultapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testresultapp.R;
import com.example.testresultapp.database.entity.TestDefinitionEntity;
import com.example.testresultapp.database.entity.TestResultEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private List<TestResultEntity> testResults = new ArrayList<>();
    private Map<Integer, TestDefinitionEntity> testDefinitionsMap = new HashMap<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        TestResultEntity currentTest = testResults.get(position);
        TestDefinitionEntity testDefinition = testDefinitionsMap.get(currentTest.getTestDefinitionId());

        if (testDefinition != null) {
            holder.textTestName.setText(testDefinition.getName());
            holder.textTestResult.setText(currentTest.getResult());

            // Nastavení barvy výsledku
            if ("Prošel".equals(currentTest.getResult())) {
                holder.textTestResult.setTextColor(holder.itemView.getContext().getColor(R.color.test_passed));
            } else if ("Neprošel".equals(currentTest.getResult())) {
                holder.textTestResult.setTextColor(holder.itemView.getContext().getColor(R.color.test_failed));
            } else {
                holder.textTestResult.setTextColor(holder.itemView.getContext().getColor(R.color.test_pending));
            }

            // Zobrazení informace o přesunutí
            if (currentTest.getRescheduledTime() != null && !currentTest.getRescheduledTime().isEmpty()) {
                holder.textRescheduled.setVisibility(View.VISIBLE);
                holder.textRescheduled.setText(holder.itemView.getContext().getString(
                        R.string.rescheduled_to, currentTest.getRescheduledTime()));
            } else {
                holder.textRescheduled.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return testResults.size();
    }

    public void setTestResults(List<TestResultEntity> testResults, List<TestDefinitionEntity> testDefinitions) {
        this.testResults = testResults;
        this.testDefinitionsMap.clear();

        // Vytvoření mapy pro rychlý přístup k definicím testů
        for (TestDefinitionEntity definition : testDefinitions) {
            this.testDefinitionsMap.put(definition.getId(), definition);
        }

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        private TextView textTestName;
        private TextView textTestResult;
        private TextView textRescheduled;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            textTestName = itemView.findViewById(R.id.text_test_name);
            textTestResult = itemView.findViewById(R.id.text_test_result);
            textRescheduled = itemView.findViewById(R.id.text_rescheduled);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(testResults.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TestResultEntity testResult);
    }
}