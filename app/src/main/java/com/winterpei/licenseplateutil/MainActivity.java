package com.winterpei.licenseplateutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import com.pxy.licenseplateutil.R;
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
    }

    @Override
    public void inputComplete(String content) {
        Log.d("MainActivity", content.toString());
    }

    @Override
    public void deleteContent() {

    }

}
