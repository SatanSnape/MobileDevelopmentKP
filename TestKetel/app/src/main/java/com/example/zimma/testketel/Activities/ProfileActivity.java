package com.example.zimma.testketel.Activities;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.UserManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.zimma.testketel.App;
import com.example.zimma.testketel.Models.ModelsInteract.IntegerOutput;
import com.example.zimma.testketel.Models.ModelsInteract.UserSave;
import com.example.zimma.testketel.Models.NetModels.UniversalActionNet;
import com.example.zimma.testketel.R;
import com.example.zimma.testketel.ServerInteract.AccountInteract;
import com.example.zimma.testketel.ServerInteract.UserContextOperation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        new AccountInteract().loadProfileInfo(this);
    }


    public void save(View view) {
        String nickname = ((EditText) findViewById(R.id.name)).getText().toString();
        int age = Integer.valueOf(((EditText) findViewById(R.id.age)).getText().toString());
        boolean sex = ((Spinner) findViewById(R.id.sex)).getSelectedItem().toString() == "M";
        Call<UniversalActionNet> call = App.getApi().SaveUser(new UserSave(UserContextOperation.getUserID(),
                nickname, age, sex));
        final ProfileActivity profileActivity = this;
        call.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                Intent intent = new Intent(profileActivity, UserActivity.class);
                profileActivity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
            }
        });
    }


    public void changePhoto(View view) {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 100 &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                ((ImageView) findViewById(R.id.image)).setImageBitmap(imageBitmap);
                App.SetImage(imageBitmap, this);
            }
        }
    }
}
