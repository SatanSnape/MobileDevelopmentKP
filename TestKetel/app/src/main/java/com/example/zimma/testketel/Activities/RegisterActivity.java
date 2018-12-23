package com.example.zimma.testketel.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.zimma.testketel.Models.ModelsInteract.RegisterModel;
import com.example.zimma.testketel.Models.NetModels.UniversalActionNet;
import com.example.zimma.testketel.R;
import com.example.zimma.testketel.ServerInteract.APIService;
import com.example.zimma.testketel.ServerInteract.AccountInteract;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    AccountInteract accountInteract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        accountInteract = new AccountInteract((TextView) findViewById(R.id.textViewInfo));
    }

    public void register(View view) {
        String login = ((EditText) findViewById(R.id.textBoxLogin)).getText().toString();
        String password = ((EditText) findViewById(R.id.textBoxPassword)).getText().toString();
        String confirmedPassword = ((EditText) findViewById(R.id.textBoxConfirmedPassword)).getText().toString();
        RadioGroup radioGroup = findViewById(R.id.sex);
        int id = radioGroup.getCheckedRadioButtonId();
        boolean sex = ((RadioButton) radioGroup.findViewById(id)).getText().equals("M") ? true : false;
        int date = Integer.parseInt(((EditText) findViewById(R.id.textBoxAge)).getText().toString());
        accountInteract.register(login, password, confirmedPassword, this, sex, date);
    }
}
