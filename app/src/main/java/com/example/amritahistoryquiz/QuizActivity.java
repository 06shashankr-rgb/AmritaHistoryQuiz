package com.example.amritahistoryquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends Activity {
    private TextView questionText, progressText;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton;
    private DBHelper dbHelper;
    private List<Question> questions;
    private int[] selectedAnswers;
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
        int requestedCount = getIntent().getIntExtra("question_count", 10);
        int actualCount = Math.min(requestedCount, questions.size());
        if (actualCount > 0 && questions.size() > actualCount) {
            questions = new ArrayList<>(questions.subList(0, actualCount));
        }
        if (questions.isEmpty()) {
            Toast.makeText(this, "No questions found in the database.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        selectedAnswers = new int[questions.size()];
        for (int i = 0; i < selectedAnswers.length; i++) selectedAnswers[i] = -1;
        showQuestion();
        nextButton.setOnClickListener(v -> checkAnswer());
    }

    private void showQuestion() {
        Question question = questions.get(currentQuestion);
        progressText.setText("QUESTION " + (currentQuestion + 1) + " OF " + questions.size());
        questionText.setText(question.getQuestion());
        String[] options = question.getOptions();
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);
        optionsGroup.clearCheck();
        nextButton.setText(currentQuestion == questions.size() - 1 ? "Finish Quiz" : "Next Question");
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedAnswer;
        if (selectedId == R.id.option1) selectedAnswer = 0;
        else if (selectedId == R.id.option2) selectedAnswer = 1;
        else if (selectedId == R.id.option3) selectedAnswer = 2;
        else selectedAnswer = 3;

        selectedAnswers[currentQuestion] = selectedAnswer;
        if (selectedAnswer == questions.get(currentQuestion).getCorrectAnswer()) score++;

        currentQuestion++;
        if (currentQuestion < questions.size()) {
            showQuestion();
        } else {
            String studentId = getSharedPreferences("AmritaQuizSession", MODE_PRIVATE)
                    .getString("student_id", "");
            if (studentId.isEmpty()) {
                Toast.makeText(this, "Please log in again before saving your score.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            dbHelper.saveScore(studentId, score, questions.size());
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", questions.size());
            intent.putExtra("review_questions", getQuestionTexts());
            intent.putExtra("review_options", getFlattenedOptions());
            intent.putExtra("review_selected", selectedAnswers);
            intent.putExtra("review_correct", getCorrectAnswers());
            startActivity(intent);
            finish();
        }
    }

    private String[] getQuestionTexts() {
        String[] values = new String[questions.size()];
        for (int i = 0; i < questions.size(); i++) values[i] = questions.get(i).getQuestion();
        return values;
    }

    private String[] getFlattenedOptions() {
        String[] values = new String[questions.size() * 4];
        for (int i = 0; i < questions.size(); i++) {
            String[] options = questions.get(i).getOptions();
            for (int j = 0; j < 4; j++) values[i * 4 + j] = options[j];
        }
        return values;
    }

    private int[] getCorrectAnswers() {
        int[] values = new int[questions.size()];
        for (int i = 0; i < questions.size(); i++) values[i] = questions.get(i).getCorrectAnswer();
        return values;
    }
}
