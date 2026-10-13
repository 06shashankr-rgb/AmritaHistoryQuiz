package com.example.amritahistoryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AmritaHistoryQuiz.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question TEXT NOT NULL, " +
                "option1 TEXT NOT NULL, " +
                "option2 TEXT NOT NULL, " +
                "option3 TEXT NOT NULL, " +
                "option4 TEXT NOT NULL, " +
                "answer INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score INTEGER NOT NULL, " +
                "total INTEGER NOT NULL, " +
                "played_at TEXT DEFAULT CURRENT_TIMESTAMP)");

        insertQuestion(db, "In which year did Amrita begin its journey?",
                "1990", "1994", "2003", "2005", 1);
        insertQuestion(db, "Who founded Amrita Vishwa Vidyapeetham?",
                "Dr. A.P.J. Abdul Kalam", "Sri Mata Amritanandamayi Devi (Amma)",
                "Swami Vivekananda", "Dr. C.N.R. Rao", 1);
        insertQuestion(db, "Where did Amrita's School of Engineering begin?",
                "Bengaluru", "Kochi", "Ettimadai, Coimbatore", "Chennai", 2);
        insertQuestion(db, "In which year did Amrita receive deemed-to-be University status?",
                "1994", "2000", "2003", "2010", 2);
        insertQuestion(db, "What is Sri Mata Amritanandamayi Devi popularly known as?",
                "Amma", "Akka", "Mataji", "Devi", 0);
        insertQuestion(db, "Who is the Chancellor of Amrita Vishwa Vidyapeetham?",
                "Dr. P. Venkat Rangan", "Sri Mata Amritanandamayi Devi (Amma)",
                "Dr. A.P.J. Abdul Kalam", "Dr. C.N.R. Rao", 1);
        insertQuestion(db, "Who is the Vice Chancellor of Amrita Vishwa Vidyapeetham?",
                "Dr. P. Venkat Rangan", "Amma",
                "Dr. A.P.J. Abdul Kalam", "Swami Vivekananda", 0);
        insertQuestion(db, "Amrita Vishwa Vidyapeetham is best described as a:",
                "Single-subject college", "Multidisciplinary university",
                "School board", "Sports academy", 1);
        insertQuestion(db, "Which NAAC grade has Amrita received?",
                "A", "B++", "A++", "B", 2);
        insertQuestion(db, "Which academic fields are offered at Amrita?",
                "Engineering", "Medical sciences", "Arts and humanities",
                "All of the above", 3);
        insertQuestion(db, "In which country is Amrita Vishwa Vidyapeetham located?",
                "Sri Lanka", "India", "Nepal", "Singapore", 1);
        insertQuestion(db, "What does NAAC primarily assess?",
                "Institutional quality and accreditation", "Attendance only",
                "Sports results only", "Hostel facilities only", 0);
        insertQuestion(db, "Which city is associated with Amrita's beginnings?",
                "Coimbatore", "Mumbai", "Delhi", "Hyderabad", 0);
        insertQuestion(db, "Which of the following is associated with Amrita?",
                "Education and research", "Only sports training",
                "Only banking services", "Only tourism", 0);
        insertQuestion(db, "What is the main purpose of this quiz app?",
                "To calculate attendance", "To test knowledge about Amrita's history",
                "To register hostel rooms", "To teach typing only", 1);
    }

    private void insertQuestion(SQLiteDatabase db, String question,
                                String option1, String option2,
                                String option3, String option4, int answer) {
        ContentValues values = new ContentValues();
        values.put("question", question);
        values.put("option1", option1);
        values.put("option2", option2);
        values.put("option3", option3);
        values.put("option4", option4);
        values.put("answer", answer);
        db.insertOrThrow("questions", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }

    public List<Question> getQuestions() {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT question, option1, option2, option3, option4, answer FROM questions",
                null)) {
            while (cursor.moveToNext()) {
                String[] options = {
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                };
                questionList.add(new Question(cursor.getString(0), options, cursor.getInt(5)));
            }
        }

        Collections.shuffle(questionList);
        return questionList;
    }

    public void saveScore(int score, int total) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", score);
        values.put("total", total);
        db.insertOrThrow("scores", null, values);
    }
}
