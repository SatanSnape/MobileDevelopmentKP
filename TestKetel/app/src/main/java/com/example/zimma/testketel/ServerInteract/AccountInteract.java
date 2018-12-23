package com.example.zimma.testketel.ServerInteract;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.zimma.testketel.Activities.ProfileActivity;
import com.example.zimma.testketel.App;
import com.example.zimma.testketel.Models.ModelsInteract.IntegerOutput;
import com.example.zimma.testketel.Models.ModelsInteract.RegisterModel;
import com.example.zimma.testketel.Models.ModelsInteract.UserLogin;
import com.example.zimma.testketel.Models.NetModels.ProfileUserInfo;
import com.example.zimma.testketel.Models.NetModels.UniversalActionNet;
import com.example.zimma.testketel.R;
import com.example.zimma.testketel.ResultCode;
import com.example.zimma.testketel.Activities.StartTestActivity;
import com.example.zimma.testketel.Activities.UserActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInteract {

    private APIService apiService = App.getApi();
    private TextView textView;

    public AccountInteract() {
    }

    public AccountInteract(TextView textView) {
        this.textView = textView;
    }

    public void login(final String login, final String password, final AppCompatActivity intent) {
        try {
            UserLogin userLogin = new UserLogin(login, password);
            Call<UniversalActionNet> actionNetCall = apiService.Login(userLogin);
            actionNetCall.enqueue(new Callback<UniversalActionNet>() {
                @Override
                public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                    UniversalActionNet actionNet = response.body();
                    switch (ResultCode.values()[actionNet.getResultCode().intValue()]) {
                        case Success:
                            App.SetLoginPasswordShPref(login, password, intent);
                            UserContextOperation.setUserID(actionNet.getWorkID());
                            if (!actionNet.getIsTestWork())
                                intent.startActivity(new Intent(intent, StartTestActivity.class));
                            else
                                intent.startActivity(new Intent(intent, UserActivity.class));
                            break;
                        case Error:
                            textView.setText("Indefinite error:\n" + actionNet.getMessage());
                            break;
                        case ErrorLoginPassword:
                            textView.setText("Incorrect login/password");
                            break;
                    }
                }

                @Override
                public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                    textView.setText(t.getMessage());
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }

    public void register(String login, String password, String confirmedPassword, final AppCompatActivity intent, boolean sex, int date) {
        if (!password.equals(confirmedPassword)) {
            textView.setText("Password and Confirmed Password are not equals");
            return;
        }

        final RegisterModel registerModel = new RegisterModel(login, password, confirmedPassword, sex, date);

        Call<UniversalActionNet> netCall = apiService.Register(registerModel);
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                UniversalActionNet actionNet = response.body();
                switch (ResultCode.values()[actionNet.getResultCode().intValue()]) {
                    case Success:
                        new AccountInteract(textView).login(registerModel.getLogin(), registerModel.getPassword(), intent);
                        break;
                    case Error:
                        textView.setText("Indefinite error:\n" + actionNet.getMessage());
                        break;
                    case ErrorLoginPassword:
                        textView.setText("User with this Login exists");
                        break;
                }
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    public void loadStatistic() {

    }

    public void loadProfileInfo(final ProfileActivity profileActivity) {
        Call<ProfileUserInfo> call = apiService.ProfileInfo(new IntegerOutput(UserContextOperation.getUserID()));
        call.enqueue(new Callback<ProfileUserInfo>() {
            @Override
            public void onResponse(Call<ProfileUserInfo> call, Response<ProfileUserInfo> response) {
                ProfileUserInfo body = response.body();
                ((TextView) profileActivity.findViewById(R.id.name)).setText(body.getNickname());
                ((TextView) profileActivity.findViewById(R.id.age)).setText(body.getBirthday().toString());
                Spinner spin = ((Spinner) profileActivity.findViewById(R.id.sex));
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(profileActivity,
                        R.array.planets_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);
                int spinnerPosition = adapter.getPosition(body.getSex() ? "M" : "W");
                spin.setSelection(spinnerPosition);
                ((ImageView)profileActivity.findViewById(R.id.image)).setImageBitmap(App.GetActualBitmap(profileActivity));
            }

            @Override
            public void onFailure(Call<ProfileUserInfo> call, Throwable t) {

            }
        });
    }
}
