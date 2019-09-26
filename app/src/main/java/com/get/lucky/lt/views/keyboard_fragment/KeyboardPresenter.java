package com.get.lucky.lt.views.keyboard_fragment;

import android.util.Log;

import com.get.lucky.lt.models.Calculator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.get.lucky.lt.Constants.EMPTY_ARRAY;
import static com.get.lucky.lt.Constants.LUCKY_NUMBER;
import static com.get.lucky.lt.Constants.NON_LUCKY_NUMBER;

public class KeyboardPresenter {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Calculator calculator;
    private KeyboardInterface keyboardInterface;

    public KeyboardPresenter(Calculator calculator) {
        this.calculator = calculator;
    }

    void attach(KeyboardInterface keyboardInterface) {
        this.keyboardInterface = keyboardInterface;
    }

    void checkResult(char[] toCharArray) {

        Disposable a = calculator.getResult(toCharArray)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setResultTheme,
                        throwable -> keyboardInterface.checkError());
        compositeDisposable.add(a);
    }

    private void setResultTheme(Integer result) {
        switch (result) {
            case LUCKY_NUMBER:
                keyboardInterface.setLuckyTheme();
                break;
            case NON_LUCKY_NUMBER:
                keyboardInterface.setNonLuckyTheme();
                break;
            case EMPTY_ARRAY:
                keyboardInterface.emptyIntArray();
        }
    }

    void detach() {
        keyboardInterface = null;
        compositeDisposable.dispose();
    }
}
