package com.iiseeuu.dagger2demo.scope;

import android.app.Application;
import android.content.Context;

/**
 * Created by wanglj on 16/6/22.
 */

public class App extends Application{
    private AppComponent appComponent;
    private UserComponent userComponent;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public UserComponent getUserComponent() {
        return userComponent;
    }

    public UserComponent createUserComponent(User userA,User userB) {
        userComponent = appComponent.plus(new UserModule(userA,userB));
        return userComponent;
    }

    public void releaseUserComponent() {
        userComponent = null;
    }
}
