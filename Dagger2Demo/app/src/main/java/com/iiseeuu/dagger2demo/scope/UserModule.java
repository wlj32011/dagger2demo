package com.iiseeuu.dagger2demo.scope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wanglj on 16/6/22.
 */
@Module
public class UserModule {
    private User userA;
    private User userB;

    public UserModule(User userA,User userB) {
        this.userB = userB;
        this.userA = userA;
    }

    @UserNamed("a")
    @Provides
    @UserScope
    User provideUserA(){
        return userA;
    }
    @UserNamed("b")
    @Provides
    @UserScope
    User provideUserB(){
        return userB;
    }

}
