package com.example.catquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkExistingState();
    }

    public void startQuiz(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        checkExistingState();
    }

    private void checkExistingState() {
        SharedPreferences mPrefs = getSharedPreferences(QuizActivity.class.getSimpleName(), MODE_PRIVATE);
        int id = mPrefs.getInt("QuestionId", MODE_PRIVATE);
        if(id > 0) {
            Button btnView = findViewById(R.id.quantButton);
            startQuiz(btnView);
        }
    }
}