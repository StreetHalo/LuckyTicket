package com.example.mihailov.lt.models;

import android.util.Log;

import java.util.ArrayList;

public class StringToNumbers {
    private char[] chars;
    private String TAG = this.getClass().getSimpleName();
    private int fHalf;
    private int lHalf;
    ArrayList<Integer> numbers;
    public StringToNumbers() {
        numbers = new ArrayList<>();
    }

    public void setWords(String words){


        chars = words.toCharArray();
        for (char a:chars) {
            numbers.add(Character.getNumericValue(a));
        }

        for (int i = 0; i < numbers.size()/2; i++) {
            fHalf = fHalf+numbers.get(i);
            lHalf = lHalf +numbers.get(numbers.size()-1-i);
        }

        Log.d(TAG, "setWords: fHalf "+fHalf+" lHalf "+lHalf);
        lHalf = 0;
        fHalf = 0;
        numbers.clear();
    }
}
