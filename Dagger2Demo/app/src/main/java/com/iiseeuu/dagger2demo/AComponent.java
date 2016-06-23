package com.iiseeuu.dagger2demo;

import dagger.Component;

/**
 * Created by wanglj on 16/6/22.
 */
@Component(modules = {AModule.class,GsonModule.class})
public interface AComponent {
    void inject(MainActivity mainActivity);
    void inject(MainActivity2 mainActivity2);

}
