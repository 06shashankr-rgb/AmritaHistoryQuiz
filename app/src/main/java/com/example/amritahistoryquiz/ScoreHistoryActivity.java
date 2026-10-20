package com.example.amritahistoryquiz;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ScoreHistoryActivity extends Activity {
    private LinearLayout list;
    private DBHelper helper;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(38, 25, 86));
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(24));
        scroll.addView(root);
        TextView title = new TextView(this);
        title.setText("MY QUIZ SCORE HISTORY");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);
        root.addView(title, params(-1, -2));
        TextView subtitle = new TextView(this);
        subtitle.setText("Your previous quiz attempts saved on this device");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setTextSize(15);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle, params(-1, -2));
        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = params(-1, -2);
        lp.topMargin = dp(18);
        root.addView(list, lp);
        helper = new DBHelper(this);
        loadHistory();
        TextView back = new TextView(this);
        back.setText("Back to Result");
        back.setTextColor(Color.rgb(40, 25, 80));
        back.setTextSize(17);
        back.setGravity(Gravity.CENTER);
        back.setPadding(dp(12), dp(14), dp(12), dp(14));
        back.setBackgroundColor(Color.rgb(255, 215, 106));
        LinearLayout.LayoutParams bp = params(-1, -2);
        bp.topMargin = dp(20);
        root.addView(back, bp);
        back.setOnClickListener(v -> finish());
        setContentView(scroll);
    }

    private void loadHistory() {
        SQLiteDatabase db = helper.getReadableDatabase();
        int attempt = 1;
        try (Cursor c = db.rawQuery("SELECT score, total, played_at FROM scores ORDER BY id ASC", null)) {
            if (!c.moveToFirst()) {
                addText("No quiz attempts yet. Complete a quiz to see your score here.", Color.WHITE, 17);
                return;
            }
            do {
                int score = c.getInt(0), total = c.getInt(1);
                String date = c.isNull(2) ? "Date unavailable" : c.getString(2);
                int percent = total > 0 ? score * 100 / total : 0;
                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(dp(16), dp(14), dp(16), dp(14));
                card.setBackgroundColor(Color.WHITE);
                LinearLayout.LayoutParams cp = params(-1, -2);
                cp.bottomMargin = dp(12);
                list.addView(card, cp);
                TextView a = new TextView(this);
                a.setText("Attempt " + attempt++);
                a.setTextColor(Color.rgb(56, 32, 120));
                a.setTextSize(18);
                a.setTypeface(null, 1);
                card.addView(a);
                TextView s = new TextView(this);
                s.setText("Score: " + score + " / " + total + " (" + percent + "%)");
                s.setTextColor(Color.DKGRAY);
                s.setTextSize(17);
                card.addView(s);
                TextView d = new TextView(this);
                d.setText("Date: " + date);
                d.setTextColor(Color.GRAY);
                d.setTextSize(13);
                card.addView(d);
            } while (c.moveToNext());
        }
    }

    private void addText(String text, int color, int size) {
        TextView view = new TextView(this);
        view.setText(text); view.setTextColor(color); view.setTextSize(size);
        view.setGravity(Gravity.CENTER); view.setPadding(dp(16), dp(24), dp(16), dp(24));
        list.addView(view, params(-1, -2));
    }
    private LinearLayout.LayoutParams params(int w, int h) { return new LinearLayout.LayoutParams(w, h); }
    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density + 0.5f); }
    @Override protected void onDestroy() { if (helper != null) helper.close(); super.onDestroy(); }
}