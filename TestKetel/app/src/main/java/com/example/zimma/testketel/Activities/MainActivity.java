package com.example.zimma.testketel.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zimma.testketel.App;
import com.example.zimma.testketel.R;
import com.example.zimma.testketel.ServerInteract.AccountInteract;

public class MainActivity extends AppCompatActivity {

    AccountInteract accountInteract;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accountInteract = new AccountInteract((TextView)findViewById(R.id.textViewInfo));
        App.TryToLoginStart(this);
    }

    public void login(View view) {
        String login = ((EditText) findViewById(R.id.textBoxLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.textBoxPassword)).getText().toString();
        accountInteract.login(login, password, this);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
