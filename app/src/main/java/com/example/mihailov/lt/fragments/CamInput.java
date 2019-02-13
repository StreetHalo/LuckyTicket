package com.example.mihailov.lt.fragments;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.edmodo.cropper.cropwindow.CropOverlayView;
import com.example.mihailov.lt.MainActivity;
import com.example.mihailov.lt.R;
import com.example.mihailov.lt.presenter.CamManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CamInput extends Fragment implements CamInterface {

    @BindView(R.id.cam)
    SurfaceView surfaceView;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layout_cam)
    RelativeLayout frameLayout;
    @BindView(R.id.cropOverlayView)
    CropOverlayView cropOverlayView;
    private Rect cropRect;
    private String TAG = this.getClass().getSimpleName();
    private CamManager camManager;
    private MainActivity mainActivity;

    @Override
    public void onResume() {
        super.onResume();
        if(camManager!=null)camManager.openCam();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ocr_layout, container, false);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView: ");
        camManager = new CamManager(getContext(),this);
        camManager.createCam(surfaceView);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.cpb_red),PorterDuff.Mode.SRC_IN);
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
                camManager.setLayoutSize(frameLayout.getMeasuredWidth(),frameLayout.getMeasuredHeight());
            }
        });

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity)
            mainActivity = (MainActivity) getActivity();
        mainActivity.setCamTheme();
    }

    public void closeCam(){
        camManager.closeCam();
    }
    public void camFocus(){
        camManager.camFocus();
    }
    public void stopOCR(){
        progressBar.setVisibility(View.INVISIBLE);
        camManager.stopOCR();
    }

    @Override
    public void freezeButtonCam() {
        progressBar.setVisibility(View.VISIBLE);
        mainActivity.freezeButtonCam();

    }
    @Override
    public void setResult(int result) {
        progressBar.setVisibility(View.INVISIBLE);
        mainActivity.setResultTheme(result);
    }

}
