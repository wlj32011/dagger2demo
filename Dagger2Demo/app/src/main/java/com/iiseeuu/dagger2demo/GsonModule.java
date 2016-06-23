package com.iiseeuu.dagger2demo;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wanglj on 16/6/22.
 */
@Module
public class GsonModule {

    @Provides
    public Gson provideGson(){
        return new Gson();
    }
}
