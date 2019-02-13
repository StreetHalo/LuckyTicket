package com.example.mihailov.lt.models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mihailov on 06.02.2019.
 */

public class Calculator {
   private ArrayList<Integer> numbers;
   private final String TAG = this.getClass().getSimpleName();
   private  int LUCKY_NUMBER = 1;
   private  int NON_LUCKY_NUMBER = 0;
   private  int EMPTY_ARRAY = 2;

    public Calculator(){
        numbers = new ArrayList<>();
    }

    public int getResult(char[] chars){
        if(chars.length<2) return EMPTY_ARRAY;
        for (char a:chars) {
            numbers.add(Character.getNumericValue(a));
        }

        if(numbers.size()%2==0) return evenNumber(numbers);
        else return oddNumber(numbers);
    }

    private int oddNumber(ArrayList<Integer> numbers) {

        numbers.remove(numbers.size()/2);
        int fHalf=0, lHalf=0;
        for (int i = 0; i < numbers.size()/2; i++) {
            fHalf = fHalf+ numbers.get(i);
            lHalf = lHalf+ numbers.get(numbers.size()-1-i);
        }
        numbers.clear();
        if(fHalf == lHalf) return LUCKY_NUMBER;
        else return NON_LUCKY_NUMBER;

    }

    private int evenNumber(ArrayList<Integer> numbers) {
       int fHalf=0, lHalf=0;
        for (int i = 0; i < numbers.size()/2; i++) {
            fHalf = fHalf+ numbers.get(i);
            lHalf = lHalf+ numbers.get(numbers.size()-1-i);
        }

        numbers.clear();
        if(fHalf == lHalf)
        return 1;
        else return 0;
    }
}
