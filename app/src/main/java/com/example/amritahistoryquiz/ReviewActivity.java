package com.example.amritahistoryquiz;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReviewActivity extends Activity {
    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView makeText(String text, int size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);
        if (bold) view.setTypeface(null, Typeface.BOLD);
        view.setPadding(dp(2), dp(5), dp(2), dp(5));
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        LinearLayout container = findViewById(R.id.reviewContainer);
        TextView heading = findViewById(R.id.reviewHeading);
        TextView scoreSummary = findViewById(R.id.reviewScoreSummary);
        Button doneButton = findViewById(R.id.reviewDoneButton);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        heading.setText("ANSWER REVIEW");
        scoreSummary.setText("Your score: " + score + " / " + total +
                "  •  " + (total > 0 ? (score * 100 / total) : 0) + "%");

        String[] questions = getIntent().getStringArrayExtra("review_questions");
        String[] options = getIntent().getStringArrayExtra("review_options");
        int[] selected = getIntent().getIntArrayExtra("review_selected");
        int[] correct = getIntent().getIntArrayExtra("review_correct");

        if (questions != null && options != null && selected != null && correct != null) {
            for (int i = 0; i < questions.length; i++) {
                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(dp(16), dp(12), dp(16), dp(12));
                card.setBackgroundColor(Color.WHITE);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cardParams.setMargins(0, 0, 0, dp(14));
                card.setElevation(dp(3));

                TextView q = makeText((i + 1) + ". " + questions[i], 17, Color.rgb(33, 18, 74), true);
                card.addView(q);
                String[] labels = {"A", "B", "C", "D"};
                for (int j = 0; j < 4; j++) {
                    int color = (j == correct[i]) ? Color.rgb(20, 125, 75) : Color.rgb(65, 61, 79);
                    if (j == selected[i] && selected[i] != correct[i]) color = Color.rgb(190, 45, 45);
                    String prefix = labels[j] + ". ";
                    String marker = j == correct[i] ? "  ✓ Correct answer" :
                            (j == selected[i] ? "  ✗ Your answer" : "");
                    card.addView(makeText(prefix + options[i * 4 + j] + marker, 14, color,
                            j == correct[i] || j == selected[i]));
                }
                String status = selected[i] == correct[i] ? "CORRECT" : "INCORRECT";
                int statusColor = selected[i] == correct[i] ? Color.rgb(20, 125, 75) : Color.rgb(190, 45, 45);
                TextView statusView = makeText(status, 13, statusColor, true);
                statusView.setGravity(Gravity.END);
                card.addView(statusView);
                container.addView(card, cardParams);
            }
        }
        doneButton.setOnClickListener(v -> finish());
    }
}
