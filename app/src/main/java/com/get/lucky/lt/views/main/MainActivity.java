package com.get.lucky.lt.views.main;

import android.Manifest;
import android.content.DialogInterface;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.get.lucky.lt.R;
import com.get.lucky.lt.views.cam_fragment.CamFragment;
import com.get.lucky.lt.views.keyboard_fragment.KeyboardFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements MainViewInterface {

    private FragmentTransaction fragmentManager;
    private CamFragment camInput;
    private KeyboardFragment keyboardInput;
    private final RxPermissions rxPermissions = new RxPermissions(this);
    private MainPresenter mainPresenter = new MainPresenter();
    private ImageButton checkButton;
    private ImageButton camButton;
    private ImageButton keyButton;
    private RelativeLayout mainLayout;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER &
                event.getAction() != KeyEvent.ACTION_UP) {
            keyboardInput.checkResult();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter.attach(this);
        checkButton = findViewById(R.id.btnWithText);
        camButton = findViewById(R.id.camMode);
        keyButton = findViewById(R.id.textMode);
        mainLayout = findViewById(R.id.main_layout);
        keyboardInput = new KeyboardFragment();
        camInput = new CamFragment();
        callKeyFragment();

        keyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callKeyFragment();
            }
        });

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionCam();
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.checkNumbers();
            }
        });
    }

    @Override
    public CamFragment getCamFragment() {
        return camInput;
    }

    @Override
    public KeyboardFragment getKeyboardFragment() {
        return keyboardInput;
    }

    private void requestPermissionCam() {
        Disposable a = rxPermissions
                .request(Manifest.permission.CAMERA)
                .delay(750, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if (granted) {
                        setCamTheme();
                        callCamFragment();
                    } else {
                        Toast.makeText(this, "Для работы приложения необходим доступ к камере", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void freezeButtonCam() {
        checkButton.setBackgroundResource(R.drawable.active_action_button);
        keyButton.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void startCamWork() {
        camInput.camFocus();
    }


    public void setCamTheme() {
        camButton.setVisibility(View.INVISIBLE);
        keyButton.setVisibility(View.VISIBLE);
        mainLayout.setBackgroundColor(getResources().getColor(R.color.photo));
    }

    public void setTextTheme() {
        camButton.setVisibility(View.VISIBLE);
        keyButton.setVisibility(View.INVISIBLE);
        mainLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void setLuckyTheme() {
        mainLayout.setBackgroundColor(getResources().getColor(R.color.lucky));
        setLuckyDialog();
    }

    @Override
    public void emptyPic() {
        Toast.makeText(this, R.string.emptyPic, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emptyIntArrayTheme() {
        Toast.makeText(this, R.string.emptyArray, Toast.LENGTH_SHORT).show();
    }

    public void setNonLuckyTheme() {
        mainLayout.setBackgroundColor(getResources().getColor(R.color.non_lucky));
        setNonLuckyDialog();
    }

    public void setDefaultButton(){
        checkButton.setBackgroundResource(R.drawable.action_button);
        keyButton.setEnabled(true);
    }

    private void setDefaultTheme() {
        if (keyboardInput.isVisible()) setTextTheme();
        else setCamTheme();
    }

    private void setLuckyDialog() {
        AlertDialog.Builder dialogLucky;
        if (keyboardInput.isVisible()) {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog);
            dialogLucky = new AlertDialog.Builder(themedContext);
        } else dialogLucky = new AlertDialog.Builder(this);
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
        if (keyboardInput.isVisible()) {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog);
            dialogNonLucky = new AlertDialog.Builder(themedContext);
        } else dialogNonLucky = new AlertDialog.Builder(this);

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


    private void callCamFragment() {
        fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.layout, camInput);
        fragmentManager.commit();
    }

    private void callKeyFragment() {
        fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.replace(R.id.layout, keyboardInput);
        fragmentManager.commit();
    }

    @Override
    public void errorMessage() {
        Toast.makeText(this,R.string.check_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mainPresenter.detach();
        super.onDestroy();
    }
}
