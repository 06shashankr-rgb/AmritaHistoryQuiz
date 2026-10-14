package com.example.amritahistoryquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultTitle = findViewById(R.id.resultTitle);
        TextView scoreText = findViewById(R.id.scoreText);
        TextView percentageText = findViewById(R.id.percentageText);
        TextView messageText = findViewById(R.id.messageText);
        Button retryButton = findViewById(R.id.retryButton);
        Button homeButton = findViewById(R.id.homeButton);
        Button viewAnswersButton = findViewById(R.id.viewAnswersButton);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        int percentage = total > 0 ? (score * 100) / total : 0;
        resultTitle.setText("QUIZ COMPLETED!");
        scoreText.setText("Your Score: " + score + " / " + total);
        percentageText.setText("Percentage: " + percentage + "%");
        if (percentage >= 80) messageText.setText("Excellent! You know Amrita's history well.");
        else if (percentage >= 50) messageText.setText("Good job! Keep learning about Amrita.");
        else messageText.setText("Keep practising and try again!");

        viewAnswersButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, ReviewActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", total);
            intent.putExtra("review_questions", getIntent().getStringArrayExtra("review_questions"));
            intent.putExtra("review_options", getIntent().getStringArrayExtra("review_options"));
            intent.putExtra("review_selected", getIntent().getIntArrayExtra("review_selected"));
            intent.putExtra("review_correct", getIntent().getIntArrayExtra("review_correct"));
            startActivity(intent);
        });
        retryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
