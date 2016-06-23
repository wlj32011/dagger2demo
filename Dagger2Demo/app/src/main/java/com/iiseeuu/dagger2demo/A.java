package com.iiseeuu.dagger2demo;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by wanglj on 16/6/22.
 */

public class A {
    public String field;


    @Inject
    public A(){

    }


    public void doSomething(){
        Log.e("A", "do something"+field);
    }
}
