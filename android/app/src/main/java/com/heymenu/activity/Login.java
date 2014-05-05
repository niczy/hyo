package com.heymenu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.task.LoginTask;

import cn.buding.common.asynctask.HandlerMessageTask;

/**
 * Created by nicholaszhao on 4/13/14.
 */
public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view) {
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        LoginTask task = new LoginTask(this, username.getText().toString(), password.getText().toString());
        task.setCallback(new HandlerMessageTask.Callback() {
            @Override
            public void onSuccess(HandlerMessageTask task, Object t) {
                Intent intent = new Intent(Login.this, ListTables.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(HandlerMessageTask task, Object t) {
                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
        task.execute();
    }

}
