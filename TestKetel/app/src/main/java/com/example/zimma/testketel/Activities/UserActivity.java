package com.example.zimma.testketel.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.zimma.testketel.App;
import com.example.zimma.testketel.R;

public class UserActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            boolean isFinish = (boolean) bd.get("FinishTest");
            if (isFinish)
                return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            boolean isFinish = (boolean) bd.get("FinishTest");
            if (!isFinish) {
                findViewById(R.id.startTest).setVisibility(View.INVISIBLE);
                findViewById(R.id.changeUser).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void startTest(View view) {
        Intent intent = new Intent(this, StartTestActivity.class);
        startActivity(intent);
    }

    public void showProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void showStatistic(View view) {
        Intent intent = new Intent(this, StatisticActivity.class);
        startActivity(intent);
    }

    public void changeUser(View view) {
        App.RemoveLoginInfo(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
