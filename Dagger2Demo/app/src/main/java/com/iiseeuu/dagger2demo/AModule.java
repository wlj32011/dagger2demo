package com.iiseeuu.dagger2demo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wanglj on 16/6/22.
 */
@Module
public class AModule {

    @Provides
    public A provideA(){
        return new A();
    }

}
