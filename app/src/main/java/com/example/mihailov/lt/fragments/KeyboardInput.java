package com.example.mihailov.lt.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.mihailov.lt.MainActivity;
import com.example.mihailov.lt.R;
import com.example.mihailov.lt.models.Calculator;

import butterknife.BindView;
import butterknife.ButterKnife;


public class KeyboardInput extends Fragment  {

    @BindView(R.id.editText2)
    EditText numbers;

    private String TAG = this.getClass().getSimpleName();
    private MainActivity mainActivity;
    private Calculator calculator;
    private InputMethodManager imm;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.keyboard_layout,container,false);
        ButterKnife.bind(this,view);
        Log.d(TAG, "onCreateView: ");
        calculator = new Calculator();

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

    public int getNumbers(){
      return calculator.getResult(numbers.getText().toString().toCharArray());
    }


}
