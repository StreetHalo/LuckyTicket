package com.get.lucky.lt.dagger;

import com.get.lucky.lt.models.Calculator;
import com.get.lucky.lt.views.keyboard_fragment.KeyboardPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class CalculatorModule {
    @Provides
    Calculator provideCalculator(){
        return new Calculator();
    }
    @Provides
    KeyboardPresenter provideKeyboardPresenter(Calculator calculator){
        return new KeyboardPresenter(calculator);
    }
}
