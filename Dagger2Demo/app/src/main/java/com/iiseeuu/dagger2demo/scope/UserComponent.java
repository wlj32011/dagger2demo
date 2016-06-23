package com.iiseeuu.dagger2demo.scope;

import com.iiseeuu.dagger2demo.scope.a.AComponent;
import com.iiseeuu.dagger2demo.scope.a.AModule;
import com.iiseeuu.dagger2demo.scope.b.BComponent;
import com.iiseeuu.dagger2demo.scope.b.BModule;
import com.iiseeuu.dagger2demo.scope.c.CComponent;
import com.iiseeuu.dagger2demo.scope.c.CModule;

import dagger.Subcomponent;

/**
 * Created by wanglj on 16/6/22.
 */
@UserScope
@Subcomponent(modules = UserModule.class)
public interface UserComponent {
    AComponent plus(AModule aModule);
    BComponent plus(BModule bModule);
    CComponent plus(CModule cModule);
}
