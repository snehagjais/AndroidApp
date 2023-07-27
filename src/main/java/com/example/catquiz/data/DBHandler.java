package com.example.catquiz.data;

import android.content.Context;

import com.example.catquiz.entity.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class DBHandler {
    public static String getJsonFromAssets(Context context) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open("jip_ques.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public static List<Question> getAllQuestions(Context context) {
        String jsonString = getJsonFromAssets(context);

        Gson gson = new Gson();
        Type listQuestionType = new TypeToken<List<Question>>() { }.getType();
        List<Question> allQuest = gson.fromJson(jsonString, listQuestionType);
        return allQuest;
    }
}
