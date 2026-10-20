package com.example.amritahistoryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AmritaHistoryQuiz.db";
    private static final int DATABASE_VERSION = 4;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question TEXT NOT NULL, option1 TEXT NOT NULL, " +
                "option2 TEXT NOT NULL, option3 TEXT NOT NULL, " +
                "option4 TEXT NOT NULL, answer INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE students (" +
                "student_id TEXT PRIMARY KEY, student_name TEXT NOT NULL, pin TEXT NOT NULL)");
        db.execSQL("CREATE TABLE scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, student_id TEXT NOT NULL, " +
                "score INTEGER NOT NULL, total INTEGER NOT NULL, " +
                "played_at TEXT DEFAULT CURRENT_TIMESTAMP)");

        insertQuestion(db, "In which year did Amrita's educational journey begin with its School of Engineering?", "1990", "1994", "2003", "2005", 1);
        insertQuestion(db, "Where was Amrita's first School of Engineering established?", "Bengaluru", "Kochi", "Ettimadai, Coimbatore", "Chennai", 2);
        insertQuestion(db, "In which year was Amrita declared a deemed-to-be University?", "1994", "2000", "2003", "2010", 2);
        insertQuestion(db, "Which organisation accredits higher education institutions in India and awarded Amrita an A++ grade?", "AICTE", "NAAC", "NTA", "UGC NET", 1);
        insertQuestion(db, "Which national framework ranks universities and institutions in India?", "NIRF", "GATE", "CAT", "NEET", 0);
        insertQuestion(db, "Amrita Vishwa Vidyapeetham is best described as a:", "Single-subject college", "Multidisciplinary university", "School board", "Sports academy", 1);
        insertQuestion(db, "Which of these is an academic field associated with Amrita?", "Engineering", "Medicine and health sciences", "Arts and humanities", "All of these", 3);
        insertQuestion(db, "Which Amrita campus is located at Vallikavu in Kerala?", "Amritapuri", "Amaravati", "Coimbatore", "Mysuru", 0);
        insertQuestion(db, "Which city is associated with Amrita's School of Engineering beginnings?", "Pune", "Coimbatore", "Jaipur", "Lucknow", 1);
        insertQuestion(db, "Which statement describes a multidisciplinary university?", "It teaches only one subject", "It offers learning across several academic fields", "It only conducts school exams", "It only offers sports coaching", 1);
        insertQuestion(db, "Which campus is located in Andhra Pradesh's planned capital region?", "Amritapuri", "Amaravati", "Faridabad", "Mysuru", 1);
        insertQuestion(db, "What is one important purpose of university research centres?", "Support research and advanced learning", "Replace every classroom", "Issue driving licences", "Manage public transport", 0);
        insertQuestion(db, "Which programme is commonly associated with advanced university research?", "Doctoral programme", "Driving test", "Passport course", "School assembly", 0);
        insertQuestion(db, "Which of these is a core activity of a university?", "Teaching and research", "Only selling products", "Only running hotels", "Only organising concerts", 0);
        insertQuestion(db, "What does university accreditation mainly help evaluate?", "Institutional quality and standards", "Student phone models", "Bus colours", "Canteen menu prices", 0);
        insertQuestion(db, "Which of these is a health-sciences discipline offered in university settings such as Amrita?", "Medicine", "Shipbuilding only", "Film distribution only", "Bank auditing only", 0);
        insertQuestion(db, "Which place is associated with Amrita's School of Engineering in Tamil Nadu?", "Ettimadai", "Vallikavu", "Amaravati", "Faridabad", 0);
        insertQuestion(db, "What is a key benefit of a multidisciplinary university?", "Learn across different fields", "No access to other subjects", "Only one course for all students", "No research opportunities", 0);
        insertQuestion(db, "Which of these is an example of an academic school within a university?", "School of Engineering", "Railway station", "Municipal ward", "Post office", 0);
        insertQuestion(db, "Which state is home to Amritapuri campus?", "Kerala", "Punjab", "Rajasthan", "Assam", 0);
        insertQuestion(db, "Which city is associated with Amrita's medical education campus?", "Kochi", "Bhopal", "Patna", "Agra", 0);
        insertQuestion(db, "What does NIRF stand for?", "National Institutional Ranking Framework", "National Institute of Rural Finance", "New Indian Research Fund", "National Internal Results Form", 0);
        insertQuestion(db, "What is the purpose of academic programmes at a university?", "Provide structured learning in selected fields", "Only arrange sports events", "Only manage accommodation", "Replace all examinations with games", 0);
        insertQuestion(db, "Why can a university have campuses in different regions?", "To provide education and academic activities across locations", "To stop students from studying", "To offer only one subject nationwide", "To replace libraries", 0);
        insertQuestion(db, "Which combination best represents a modern university's activities?", "Teaching, research and community engagement", "Only shopping and sales", "Only transport and construction", "Only entertainment", 0);

        // A small set of questions about Amma, alongside the university-focused questions.
        insertQuestion(db, "Amma is widely known by which name?", "Sri Mata Amritanandamayi Devi", "Sarojini Naidu", "Annie Besant", "Savitribai Phule", 0);
        insertQuestion(db, "Which organisation is closely associated with Amma's humanitarian and charitable activities?", "Mata Amritanandamayi Math", "Indian Space Research Organisation", "Election Commission of India", "National Testing Agency", 0);
        insertQuestion(db, "Which value is especially associated with Amma's public message and humanitarian work?", "Compassion and service", "Competition at any cost", "Commercial profit only", "Political campaigning", 0);
        insertQuestion(db, "Amrita SeRVe is associated with work focused on supporting which communities?", "Rural communities", "Only international airports", "Only film studios", "Only stock exchanges", 0);
        insertQuestion(db, "Which activity best reflects the service-oriented work associated with Amma and her organisations?", "Humanitarian relief and community support", "Manufacturing mobile phones", "Running a professional sports league", "Managing railway timetables", 0);
    }

    private void insertQuestion(SQLiteDatabase db, String question, String option1,
                                String option2, String option3, String option4, int answer) {
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
        // Preserve existing questions and scores when upgrading older installs.
        if (oldVersion < 4) {
            db.execSQL("CREATE TABLE IF NOT EXISTS students (" +
                    "student_id TEXT PRIMARY KEY, student_name TEXT NOT NULL, pin TEXT NOT NULL)");
            db.execSQL("ALTER TABLE scores ADD COLUMN student_id TEXT NOT NULL DEFAULT 'legacy'");
        }
    }

    // Returns 1 for successful login, 2 for a newly created local account,
    // and 0 when an existing account's PIN is incorrect.
    public int registerOrLoginStudent(String studentId, String studentName, String pin) {
        SQLiteDatabase db = getWritableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT pin FROM students WHERE student_id = ?",
                new String[]{studentId})) {
            if (cursor.moveToFirst()) {
                return hashPin(pin).equals(cursor.getString(0)) ? 1 : 0;
            }
        }
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("student_name", studentName);
        values.put("pin", hashPin(pin));
        db.insertOrThrow("students", null, values);
        return 2;
    }

    public void saveScore(String studentId, int score, int total) {
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("score", score);
        values.put("total", total);
        getWritableDatabase().insertOrThrow("scores", null, values);
    }

    private String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) result.append(String.format("%02x", b & 0xff));
            return result.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to secure student PIN", e);
        }
    }

    public String getStudentName(String studentId) {
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT student_name FROM students WHERE student_id = ?",
                new String[]{studentId})) {
            return cursor.moveToFirst() ? cursor.getString(0) : studentId;
        }
    }

    public List<Question> getQuestions() {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT question, option1, option2, option3, option4, answer FROM questions", null)) {
            while (cursor.moveToNext()) {
                String[] options = {cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4)};
                questionList.add(new Question(cursor.getString(0), options, cursor.getInt(5)));
            }
        }
        Collections.shuffle(questionList);
        return questionList;
    }

}