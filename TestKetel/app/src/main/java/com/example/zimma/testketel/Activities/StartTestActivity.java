package com.example.zimma.testketel.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.zimma.testketel.Models.NetModels.StartActionNet;
import com.example.zimma.testketel.Presenters.BuilderPresenter;
import com.example.zimma.testketel.R;
import com.example.zimma.testketel.ServerInteract.TestInteract;
import com.example.zimma.testketel.ServerInteract.UserContextOperation;

public class StartTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
    }

    public void StartTestResult(final StartActionNet startActionNet) {
        if (startActionNet.getIsStartPrev()) {
            final StartTestActivity startTestActivity = this;
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            BuilderPresenter.getTestPresenter().isSTartTest(true, startTestActivity);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            BuilderPresenter.getTestPresenter().getUserAnswers().clear();
                            BuilderPresenter.getTestPresenter().isSTartTest(false, startTestActivity);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Хотите продолжить тест?").setPositiveButton("Да", dialogClickListener)
                    .setNegativeButton("Нет", dialogClickListener).show();
        } else {
            UserContextOperation.setTestResultID(startActionNet.getPrevID());
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        }
    }

    public void StartTest(View view) {
        TestInteract testInteract = new TestInteract();
        testInteract.startTest(UserContextOperation.getUserID(), this);
    }

    public void openMenu(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
}
