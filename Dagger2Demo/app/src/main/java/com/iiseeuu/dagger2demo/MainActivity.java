package com.iiseeuu.dagger2demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.iiseeuu.dagger2demo.scope.App;
import com.iiseeuu.dagger2demo.scope.User;
import com.iiseeuu.dagger2demo.scope.a.AActivity;



public class MainActivity extends AppCompatActivity {

//    @Inject
//    A a;
//
//    @Inject
//    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        DaggerAComponent.builder().aModule(new AModule()).build().inject(this);

//        a.field = "test";
//        String aStr = gson.toJson(a);
//        Log.e("mainactivity","astr = "+aStr);
//        a.doSomething();
//        Log.e("mainactivity","a = "+a);
        final EditText etUserName = (EditText) findViewById(R.id.username);
        final EditText etPassword = (EditText) findViewById(R.id.password);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User userA = new User();
                userA.setName(etUserName.getText().toString()+"AAA");
                userA.setAvatar(etPassword.getText().toString());

                User userB = new User();
                userB.setName(etUserName.getText().toString()+"BBB");
                userB.setAvatar(etPassword.getText().toString());

                App.get(MainActivity.this).createUserComponent(userA,userB);
                startActivity(new Intent(MainActivity.this, AActivity.class));
            }
        });

    }
}
