package com.example.amritahistoryquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;

public class MainActivity extends Activity {

    private Spinner questionCountSpinner;
    private EditText studentNameInput, studentIdInput, pinInput;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionCountSpinner = findViewById(R.id.questionCountSpinner);
        studentNameInput = findViewById(R.id.studentNameInput);
        studentIdInput = findViewById(R.id.studentIdInput);
        pinInput = findViewById(R.id.pinInput);
        Button startButton = findViewById(R.id.startButton);
        dbHelper = new DBHelper(this);

        String[] counts = {
                "10 Questions", "15 Questions", "20 Questions",
                "25 Questions", "30 Questions"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, counts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionCountSpinner.setAdapter(adapter);

        startButton.setOnClickListener(v -> {
            String studentName = studentNameInput.getText().toString().trim();
            String studentId = studentIdInput.getText().toString().trim();
            String pin = pinInput.getText().toString().trim();

            if (studentName.isEmpty() || studentId.isEmpty() || pin.length() < 4) {
                Toast.makeText(this, "Enter your name, student ID, and a PIN of at least 4 characters.", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                int accountResult = dbHelper.registerOrLoginStudent(studentId, studentName, pin);
                if (accountResult == 0) {
                    Toast.makeText(this, "Incorrect PIN for this student ID.", Toast.LENGTH_LONG).show();
                    return;
                }
                getSharedPreferences("AmritaQuizSession", MODE_PRIVATE).edit()
                        .putString("student_id", studentId)
                        .putString("student_name", dbHelper.getStudentName(studentId))
                        .apply();

                String message = accountResult == 2 ? "Student account created." : "Login successful.";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                int selectedCount = (questionCountSpinner.getSelectedItemPosition() + 1) * 5 + 5;
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("question_count", selectedCount);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Could not sign in. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
}