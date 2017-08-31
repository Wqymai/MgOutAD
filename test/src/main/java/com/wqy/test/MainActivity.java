package com.wqy.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mg.outad.ooa.MAdSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MAdSDK.getInstance().startMiiService(this);

    }
}
