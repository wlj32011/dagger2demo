package com.iiseeuu.dagger2demo.scope.a;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.iiseeuu.dagger2demo.MainActivity;
import com.iiseeuu.dagger2demo.R;
import com.iiseeuu.dagger2demo.scope.App;
import com.iiseeuu.dagger2demo.scope.User;
import com.iiseeuu.dagger2demo.scope.UserNamed;
import com.iiseeuu.dagger2demo.scope.b.BActivity;

import javax.inject.Inject;

/**
 * Created by wanglj on 16/6/22.
 */

public class AActivity extends AppCompatActivity{
    @UserNamed("a")
    @Inject
    User userA;

    @UserNamed("b")
    @Inject
    User userB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        //注入
        App.get(this).getUserComponent().plus(new AModule()).inject(this);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("username:"+userA.getName()+"--user:"+userA+""+"username:"+userB.getName()+"--user:"+userB);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AActivity.this, BActivity.class));
            }
        });

    }

}
