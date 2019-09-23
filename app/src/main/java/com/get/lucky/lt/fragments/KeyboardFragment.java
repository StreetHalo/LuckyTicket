package com.get.lucky.lt.fragments;

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

import com.get.lucky.lt.MainActivity;
import com.get.lucky.lt.R;
import com.get.lucky.lt.models.Calculator;

import butterknife.BindView;
import butterknife.ButterKnife;


public class KeyboardFragment extends Fragment  {

    @BindView(R.id.editText2)
    EditText numbers;

    private String TAG = this.getClass().getSimpleName();
    private MainActivity mainActivity;
    private Calculator calculator;
    private InputMethodManager imm;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.keyboard_layout,container,false);
        ButterKnife.bind(this,view);
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
