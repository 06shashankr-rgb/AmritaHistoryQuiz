package com.example.amritahistoryquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Spinner questionCountSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionCountSpinner = findViewById(R.id.questionCountSpinner);
        Button startButton = findViewById(R.id.startButton);

        String[] counts = {"10 Questions", "15 Questions", "20 Questions", "25 Questions"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, counts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionCountSpinner.setAdapter(adapter);

        startButton.setOnClickListener(v -> {
            int selectedCount = (questionCountSpinner.getSelectedItemPosition() + 1) * 5 + 5;
            // Available choices are 10, 15, 20 and 25 questions.
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("question_count", selectedCount);
            startActivity(intent);
        });
    }
}
