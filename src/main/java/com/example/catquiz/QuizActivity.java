package com.example.catquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.catquiz.data.DBHandler;
import com.example.catquiz.entity.Question;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    private List<Question> allQuestions = new ArrayList<>();
    private int idx = 0;
    private Question question;
    private RadioGroup optionGroup;
    private TextView questionTxt;
    private TextView resultTxt;
    private EditText answerText;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton option4;
    private SharedPreferences mPrefs;
    private CountDownTimer timer;
    private Button timerBtn;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mPrefs = getSharedPreferences(getLocalClassName(), MODE_PRIVATE);

        if (allQuestions.isEmpty()) {
            allQuestions = DBHandler.getAllQuestions(getApplicationContext());
            idx = mPrefs.getInt("QuestionId", MODE_PRIVATE);
            question = allQuestions.get(9);
        }

        questionTxt = findViewById(R.id.questionText);
        resultTxt = findViewById(R.id.resultText);
        answerText = findViewById(R.id.answerText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        optionGroup = findViewById(R.id.optionGroup);

        questionTxt.setTypeface(null, Typeface.BOLD);
        option1.setTypeface(null, Typeface.BOLD);
        option2.setTypeface(null, Typeface.BOLD);
        option3.setTypeface(null, Typeface.BOLD);
        option4.setTypeface(null, Typeface.BOLD);

        timerBtn = findViewById(R.id.timerBtn);
        nextBtn = findViewById(R.id.btnSubmit);
        setQuestionValues();
        startTimer();
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timerBtn.setText(f.format(min) + ":" + f.format(sec));

                if(sec <= 10) {
                    timerBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.warn));
                }
            }

            public void onFinish() {
                enableRadioButtons(false);
                highlightCorrectAnswer();
                enableNextBtn(true);
            }
        }.start();
    }

    private void setQuestionValues() {
        questionTxt.setText(question.getQuestionText());
        if(Objects.isNull(question.getOption1())) {
            showAnswerText();
        } else {
            hideAnswerText();
            option1.setText(question.getOption1());
            option2.setText(question.getOption2());
            option3.setText(question.getOption3());
            option4.setText(question.getOption4());
        }
        enableNextBtn(false);
    }

    public void checkAndShowResult(View view) {
        RadioButton btn = (RadioButton) view;
        String text = btn.getText().toString();

        if(text.equalsIgnoreCase(question.getCorrectAnswer())) {
            btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.success));
        } else {
            btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.fail));
            highlightCorrectAnswer();
        }

        enableRadioButtons(false);
        timer.cancel();
        enableNextBtn(true);
    }

    public void checkAndShowResultText(View view) {
        EditText answerTxt = (EditText) view;
        String text = answerTxt.getText().toString();
        answerTxt.setEnabled(false);

        String resultText = "";
        if(text.equalsIgnoreCase(question.getCorrectAnswer())) {
            resultText = "Correct Answer :)";
        } else {
            resultText = "Wrong Answer !! \n Correct Answer is " + question.getCorrectAnswer();
        }

        resultTxt.setVisibility(View.VISIBLE);
        resultTxt.setText(resultText);

        timer.cancel();
        enableNextBtn(true);
    }

    private void enableRadioButtons(boolean b) {
        optionGroup.getChildAt(0).setEnabled(b);
        optionGroup.getChildAt(1).setEnabled(b);
        optionGroup.getChildAt(2).setEnabled(b);
        optionGroup.getChildAt(3).setEnabled(b);
    }

    private void showAnswerText() {
        optionGroup.setVisibility(View.GONE);
        option1.setVisibility(View.GONE);
        option2.setVisibility(View.GONE);
        option3.setVisibility(View.GONE);
        option4.setVisibility(View.GONE);

        answerText.setVisibility(View.VISIBLE);
        answerText.setFocusable(true);
        answerText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE
                    || keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                checkAndShowResultText(textView);
            }
            return true;
        });
    }

    private void hideAnswerText() {
        answerText.setVisibility(View.GONE);
        resultTxt.setVisibility(View.GONE);
        optionGroup.setVisibility(View.VISIBLE);
        option1.setVisibility(View.VISIBLE);
        option2.setVisibility(View.VISIBLE);
        option3.setVisibility(View.VISIBLE);
        option4.setVisibility(View.VISIBLE);
    }

    private void highlightCorrectAnswer() {
        if(option1.getText().toString().equalsIgnoreCase(question.getCorrectAnswer())) {
            option1.performClick();
            option1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.success));
        } else if(option2.getText().toString().equalsIgnoreCase(question.getCorrectAnswer())) {
            option2.performClick();
            option2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.success));
        } else if(option3.getText().toString().equalsIgnoreCase(question.getCorrectAnswer())) {
            option3.performClick();
            option3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.success));
        } else if(option4.getText().toString().equalsIgnoreCase(question.getCorrectAnswer())) {
            option4.performClick();
            option4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.success));
        }
    }

    public void moveToNextQuestion(View view) {
        if(idx < allQuestions.size() - 1) {
            question = allQuestions.get(++idx);

            clearSelection();
            enableRadioButtons(true);

            setQuestionValues();
            startTimer();
        } else {
            idx = 0;
            finish();
        }
    }

    private void clearSelection() {
        answerText.setText("");
        resultTxt.setText("");
        answerText.setEnabled(true);
        optionGroup.clearCheck();
        option1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        option2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        option3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        option4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        timerBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("QuestionId", idx);
        ed.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPrefs = getSharedPreferences(getLocalClassName(), MODE_PRIVATE);
        idx = mPrefs.getInt("QuestionId", MODE_PRIVATE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("QuestionId", idx);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        idx = savedInstanceState.getInt("QuestionId");
    }

    private void enableNextBtn(boolean enable) {
        if(enable) {
            nextBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));
        } else {
            nextBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.grey));
        }
        nextBtn.setEnabled(enable);
    }
}