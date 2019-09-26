package com.get.lucky.lt.views.cam_fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.edmodo.cropper.cropwindow.CropOverlayView;
import com.get.lucky.lt.App;
import com.get.lucky.lt.views.main.MainActivity;
import com.get.lucky.lt.R;

import java.util.Objects;

import javax.inject.Inject;


public class CamFragment extends Fragment implements CamInterface {

    private SurfaceView surfaceView;
    private ProgressBar progressBar;
    private RelativeLayout frameLayout;
    private CropOverlayView cropOverlayView;
    private Rect cropRect;
    private SharedPreferences sharedPreferences;
    @Inject
    CamPresenter camPresenter;
    private MainActivity mainActivity;

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setDefaultButton();
        camPresenter.initCam(surfaceView);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.daggerCamComponent.inject(this);
        camPresenter.attach(this);
        sharedPreferences = getActivity().getSharedPreferences("SHOW_DIALOG", 0);
        boolean ifShowInfo = sharedPreferences.getBoolean("SHOW", true);
        if (ifShowInfo) showDialog();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ocr_layout, container, false);


        surfaceView = view.findViewById(R.id.cam);
        progressBar = view.findViewById(R.id.progressBar);
        frameLayout = view.findViewById(R.id.layout_cam);
        cropOverlayView = view.findViewById(R.id.cropOverlayView);


        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.cpb_red), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.INVISIBLE);
        cropRect = new Rect();
        cropRect.left = 0;
        cropRect.top = 0;
        cropRect.right = 200;
        cropRect.bottom = 200;
        cropOverlayView.setBitmapRect(cropRect);
        ViewTreeObserver vto = frameLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                cropRect.left = 0;
                cropRect.top = 0;
                cropRect.right = frameLayout.getMeasuredWidth();
                cropRect.bottom = frameLayout.getMeasuredHeight();
                cropOverlayView.setBitmapRect(cropRect);
                camPresenter.setLayoutSize(frameLayout.getMeasuredWidth(), frameLayout.getMeasuredHeight());
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity)
            mainActivity = (MainActivity) getActivity();
    }

    private void closeCam() {
        camPresenter.closeCam();
    }

    public void camFocus() {

        camPresenter.camFocus();
    }

    private void stopOCR() {
        progressBar.setVisibility(View.INVISIBLE);
        camPresenter.stopOCR();
    }

    @Override
    public void freezeButtonCam() {
        progressBar.setVisibility(View.VISIBLE);
        mainActivity.freezeButtonCam();

    }

    @Override
    public void setProgressBarInvisible() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setDefaultButton() {
        mainActivity.setDefaultButton();
    }

    @Override
    public void setLuckyTheme() {
        mainActivity.setLuckyTheme();
    }

    @Override
    public void setNonLuckyTheme() {
        mainActivity.setNonLuckyTheme();
    }

    @Override
    public void emptyIntArray() {
        mainActivity.emptyIntArrayTheme();
    }


    @Override
    public void onDestroy() {
        camPresenter.detach();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopOCR();
        closeCam();
    }

    @Override
    public void errorMessage() {
        mainActivity.errorMessage();
    }

    private void showDialog() {

        new AlertDialog.Builder(Objects.requireNonNull(getContext()))

                .setTitle("Инструкция")
                .setMessage("\n- Выделите область с номером\n\n" +
                        "- Нажмите на белую кнопку\n\n" +
                        "- Удачи!\n")
                .setNegativeButton("Не показывать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("SHOW", false);
                        editor.apply();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

}
