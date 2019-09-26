package com.get.lucky.lt.views.keyboard_fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.get.lucky.lt.App;
import com.get.lucky.lt.views.main.MainActivity;
import com.get.lucky.lt.R;

import javax.inject.Inject;


public class KeyboardFragment extends Fragment implements KeyboardInterface  {

    EditText numbers;
    private MainActivity mainActivity;
    private InputMethodManager imm;
    @Inject
    KeyboardPresenter keyboardPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.daggerCamComponent.inject(this);
        keyboardPresenter.attach(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.keyboard_layout,container,false);
        numbers = view.findViewById(R.id.editText2);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity)
            mainActivity = (MainActivity) getActivity();
        mainActivity.setTextTheme();
        numbers.requestFocus();
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    @Override
    public void onDestroy() {
        keyboardPresenter.detach();
        super.onDestroy();
    }

    public void checkResult() {

        keyboardPresenter.checkResult(numbers.getText().toString().toCharArray());
    }

    @Override
    public void emptyIntArray() {
        mainActivity.emptyIntArrayTheme();
    }

    @Override
    public void setNonLuckyTheme() {
        mainActivity.setNonLuckyTheme();
    }

    @Override
    public void setLuckyTheme() {
        mainActivity.setLuckyTheme();
    }

    @Override
    public void checkError() {
        mainActivity.errorMessage();
    }
}
