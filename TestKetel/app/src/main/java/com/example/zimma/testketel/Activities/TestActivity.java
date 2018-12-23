package com.example.zimma.testketel.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zimma.testketel.Models.ModelsInteract.UserAnswer;
import com.example.zimma.testketel.Models.NetModels.AnswerActionNet;
import com.example.zimma.testketel.Models.NetModels.QuestionActionNet;
import com.example.zimma.testketel.Presenters.BuilderPresenter;
import com.example.zimma.testketel.Presenters.TestPresenter;
import com.example.zimma.testketel.R;

public class TestActivity extends AppCompatActivity {

    TestPresenter presenter;

    @Override
    public void onBackPressed() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        presenter.setClear();
                        presenter.finishTest(true, false);
                        TestActivity.super.onBackPressed();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Хотите продолжить тест?").setPositiveButton("Да", dialogClickListener)
                .setNegativeButton("Нет", dialogClickListener).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        presenter = BuilderPresenter.getTestPresenter();
        presenter.onCreate(this);
    }

    public void setFirstUnansweredQuestion() {
        presenter.setFirstUnansweredQuestion();
        onCreate();
    }

    public void onCreate() {
        Button next = this.findViewById(R.id.nextBtn);
        Button prev = this.findViewById(R.id.prevBtn);

        next.setVisibility(View.INVISIBLE);
        prev.setVisibility(View.INVISIBLE);

        QuestionActionNet questionActionNet = presenter.getCurrentQuestion();
        ((TextView) this.findViewById(R.id.infoCenter))
                .setText(new StringBuilder()
                        .append(presenter.getCurrentQuestionInt() + 1)
                        .append("/")
                        .append(presenter.getCountQuestions()).toString());

        TextView textView = findViewById(R.id.question);
        UserAnswer isAnswered = presenter.isAnswered(questionActionNet.getUserID());
        textView.setText(questionActionNet.getQuestionContent());
        LinearLayout answers = (LinearLayout) findViewById(R.id.answers);
        if (answers.getChildCount() > 0)
            answers.removeAllViews();
        int index = R.id.question + 1;
        for (final AnswerActionNet answerActionNet : questionActionNet.getAnswerActionNets()) {
            final int indexR = index;
            index++;
            TextView answer = new TextView(this);
            answer.setPadding(0, 10, 0, 10);
            answer.setId(indexR);
            answer.setText(answerActionNet.getAnswerContent());
            answer.setPadding(0, 15, 0, 15);
            if (isAnswered == null)
                answer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView answer = (TextView) v;
                        presenter.answer(answer.getId(), answerActionNet.getUserID());
                    }
                });
            else {
                if (isAnswered.getAnswerID() == answerActionNet.getUserID()) {
                    answer.setTextColor(Color.parseColor("#ffffff"));
                    answer.setBackgroundColor(Color.parseColor("#000000"));
                }
            }
            answers.addView(answer);
        }

        if (isAnswered == null)
            next.setVisibility(View.INVISIBLE);
        else
            next.setVisibility(View.VISIBLE);

        if (presenter.IsLastQuestion()) {
            next.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishTest();
                }
            });
            next.setBackgroundResource(R.drawable.finish);
        } else {
            next.setBackgroundResource(R.drawable.next);
            next.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextQuestion(v);
                }
            });
        }

        if (presenter.getCurrentQuestionInt() == 0) {
            prev.setVisibility(View.INVISIBLE);
        } else {
            prev.setVisibility(View.VISIBLE);
        }

    }

    public void finishTest() {
        presenter.finishTest(false, false);
    }

    public void nextQuestion(View view) {
        presenter.nextQuestion();
        onCreate();
    }

    public void prevQuestion(View view) {
        presenter.prevQuestion();
        onCreate();
    }

    public void openMenu(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("FinishTest", false);
        startActivity(intent);
    }

    public void returnBack(View view) {
        this.onBackPressed();
    }
}
