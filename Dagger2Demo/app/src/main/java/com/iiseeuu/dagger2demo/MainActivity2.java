package com.iiseeuu.dagger2demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by wanglj on 16/6/22.
 */

public class MainActivity2 extends AppCompatActivity {

    @Inject
    A a;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerAComponent.builder().aModule(new AModule()).build().inject(this);
        Log.e("mainactivity2","a = "+a);

    }
}
