package com.iiseeuu.dagger2demo.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by wanglj on 16/6/22.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {

}
