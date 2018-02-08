package com.winterpei.licenseplateutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.winterpei.LicensePlateView;

public class MainActivity extends AppCompatActivity implements LicensePlateView.InputListener {

    private LicensePlateView mPlateView;
    private RelativeLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlateView = findViewById(R.id.activity_lpv);
        mContainer = findViewById(R.id.main_rl_container);
        mPlateView.setInputListener(this);

        mPlateView.setKeyboardContainerLayout(mContainer);
        mPlateView.showLastView();
        mPlateView.hideLastView();
        mPlateView.onSetTextColor(R.color.colorAccent);
    }

    @Override
    public void inputComplete(String content) {

    }

    @Override
    public void deleteContent() {

    }

}
