package com.example.amritahistoryquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class QuizActivity extends Activity {

    private TextView questionText;
    private TextView progressText;
    private RadioGroup optionsGroup;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton option4;
    private Button nextButton;

    private DBHelper dbHelper;
    private List<Question> questions;
    private int currentQuestion = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        progressText = findViewById(R.id.progressText);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);

        dbHelper = new DBHelper(this);
        questions = dbHelper.getQuestions();

        if (questions.isEmpty()) {
            Toast.makeText(this, "No questions found in the database.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        showQuestion();
        nextButton.setOnClickListener(v -> checkAnswer());
    }

    private void showQuestion() {
        Question question = questions.get(currentQuestion);
        progressText.setText("QUESTION " + (currentQuestion + 1) +
                " OF " + questions.size());
        questionText.setText(question.getQuestion());

        String[] options = question.getOptions();
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);
        optionsGroup.clearCheck();

        nextButton.setText(currentQuestion == questions.size() - 1
                ? "Finish Quiz" : "Next Question");
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedAnswer;
        if (selectedId == R.id.option1) {
            selectedAnswer = 0;
        } else if (selectedId == R.id.option2) {
            selectedAnswer = 1;
        } else if (selectedId == R.id.option3) {
            selectedAnswer = 2;
        } else {
            selectedAnswer = 3;
        }

        if (selectedAnswer == questions.get(currentQuestion).getCorrectAnswer()) {
            score++;
        }

        currentQuestion++;
        if (currentQuestion < questions.size()) {
            showQuestion();
        } else {
            dbHelper.saveScore(score, questions.size());
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", questions.size());
            startActivity(intent);
            finish();
        }
    }
}
