package com.example.mihailov.lt;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.mihailov.lt.fragments.CamInput;
import com.example.mihailov.lt.fragments.KeyboardInput;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity  {
    private boolean takePic = false;
    private  final String TAG = this.getClass().getSimpleName();
    private FragmentTransaction fragmentManager;
    private CamInput camInput;
    private KeyboardInput keyboardInput;
    private final int LUCKY_NUMBER = 1;
    private final int NON_LUCKY_NUMBER = 0;
    private final int EMPTY_ARRAY = 2;

    @BindView(R.id.btnWithText)
    ImageButton checkButton;
    @BindView(R.id.camMode)
    ImageButton camButton;
    @BindView(R.id.textMode)
    ImageButton keyButton;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER&
                event.getAction()!=KeyEvent.ACTION_UP){
            setResultTheme(keyboardInput.getNumbers());
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        keyboardInput = new KeyboardInput();
        camInput = new CamInput();
      setCamFragment();

    }

    @OnClick(R.id.btnWithText)
 void click(){
        if(camInput.isVisible()) {
            if (!takePic) {
                startWork();
            } else {
                stopWork();
            }
        }
        else  setResultTheme(keyboardInput.getNumbers());

    }

    @OnClick(R.id.camMode)
    void setKeyFragment(){
        fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.layout,camInput);
        fragmentManager.commit();
    }

    @OnClick(R.id.textMode)
    void setCamFragment(){
        fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.layout,keyboardInput);
        fragmentManager.commit();
    }


    public void freezeButtonCam(){
        takePic = true;
        Log.d(TAG, "freezeButtonCam: ");
        checkButton.setBackgroundResource(R.drawable.active_action_button);
        keyButton.setEnabled(false);
    }


    public void setResultTheme(int result) {
        takePic = false;

        switch (result){
           case  LUCKY_NUMBER:
               setLuckyTheme();
            break;
           case NON_LUCKY_NUMBER:
               setNonLuckyTheme();
               break;
            case EMPTY_ARRAY:
                if(camInput.isVisible())
                    Toast.makeText(this,R.string.emptyPic,Toast.LENGTH_SHORT).show();
                else Toast.makeText(this,R.string.emptyArray,Toast.LENGTH_SHORT).show();
        }

        checkButton.setBackgroundResource(R.drawable.action_button);
        keyButton.setEnabled(true);
    }

    private void startWork(){
        Log.d(TAG, "startWork: ");
        camInput.camFocus();
    }

    private void stopWork(){
        camInput.stopOCR();
        takePic = false;
        checkButton.setBackgroundResource(R.drawable.action_button);
    }


    public void callKeyBoard(EditText text){
        Log.d(TAG, "callKeyBoard: "+text.getId());
        text.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT);


    }
    public void setCamTheme(){
        camButton.setVisibility(View.INVISIBLE);
        keyButton.setVisibility(View.VISIBLE);
        mainLayout.setBackgroundColor(getResources().getColor(R.color.photo));
    }
    public void setTextTheme(){
        camButton.setVisibility(View.VISIBLE);
        keyButton.setVisibility(View.INVISIBLE);
        mainLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setLuckyTheme(){
        mainLayout.setBackgroundColor(getResources().getColor(R.color.lucky));
        setLuckyDialog();
    }

    private void setNonLuckyTheme(){
        mainLayout.setBackgroundColor(getResources().getColor(R.color.non_lucky));
        setNonLuckyDialog();
    }

    private void setDefaultTheme(){

        if(keyboardInput.isVisible())setTextTheme();
        else setCamTheme();
    }



    private void setLuckyDialog(){

        AlertDialog.Builder dialogLucky;
        if(keyboardInput.isVisible()) {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog);
            dialogLucky = new AlertDialog.Builder(themedContext);
        }
        else dialogLucky = new AlertDialog.Builder(this);
        dialogLucky.setTitle("Поздравляем");
        dialogLucky.setMessage("Ура, у вас счастливый билет!");
        dialogLucky.setCancelable(true);
        dialogLucky.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDefaultTheme();
                dialog.cancel();
            }
        });

        dialogLucky.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setDefaultTheme();
                dialog.cancel();
            }
        });

        dialogLucky.show();
    }
    private void setNonLuckyDialog() {
        AlertDialog.Builder dialogNonLucky;
        if(keyboardInput.isVisible()) {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog);
            dialogNonLucky = new AlertDialog.Builder(themedContext);
        }
        else dialogNonLucky = new AlertDialog.Builder(this);

        dialogNonLucky.setTitle("Опс!");
        dialogNonLucky.setMessage("К сожалению, Вам не повезло.");
        dialogNonLucky.setCancelable(true);
        dialogNonLucky.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    setDefaultTheme();
                    dialog.cancel();
            }
        });

        dialogNonLucky.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setDefaultTheme();
                dialog.cancel();
            }
        });

        dialogNonLucky.show();
    }


}
