package com.example.zimma.testketel.ServerInteract;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zimma.testketel.Activities.StatisticActivity;
import com.example.zimma.testketel.App;
import com.example.zimma.testketel.Models.ModelsInteract.FinishTest;
import com.example.zimma.testketel.Models.ModelsInteract.IntegerOutput;
import com.example.zimma.testketel.Models.ModelsInteract.IsStartTest;
import com.example.zimma.testketel.Models.ModelsInteract.UserAnswer;
import com.example.zimma.testketel.Models.NetModels.AnswerActionNet;
import com.example.zimma.testketel.Models.NetModels.QuestionActionNet;
import com.example.zimma.testketel.Models.NetModels.QuestionsListNet;
import com.example.zimma.testketel.Models.NetModels.StartActionNet;
import com.example.zimma.testketel.Models.NetModels.StaticticResult;
import com.example.zimma.testketel.Models.NetModels.StatisticsListNet;
import com.example.zimma.testketel.Models.NetModels.UniversalActionNet;
import com.example.zimma.testketel.Presenters.BuilderPresenter;
import com.example.zimma.testketel.Presenters.TestPresenter;
import com.example.zimma.testketel.R;
import com.example.zimma.testketel.ResultCode;
import com.example.zimma.testketel.Activities.StartTestActivity;
import com.example.zimma.testketel.Activities.TestActivity;
import com.example.zimma.testketel.Activities.UserActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestInteract {

    private APIService apiService = App.getApi();

    public void startTest(int user, final StartTestActivity startTestActivity) {
        Call<StartActionNet> actionNetCall = apiService.StartTest(new IntegerOutput(user));
        actionNetCall.enqueue(new Callback<StartActionNet>() {
            @Override
            public void onResponse(Call<StartActionNet> call, Response<StartActionNet> response) {
                StartActionNet actionNet = response.body();
                if (ResultCode.values()[actionNet.getResultCode()] == ResultCode.Success) {
                    UserContextOperation.setTestResultID(actionNet.getPrevID());
                    startTestActivity.StartTestResult(actionNet);
                }
            }

            @Override
            public void onFailure(Call<StartActionNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void questionsListNet(int user, final TestPresenter questionsListNet, final TestActivity testActivity) {
        Call<QuestionsListNet> netCall = apiService.GetQuestions(new IntegerOutput(user));
        netCall.enqueue(new Callback<QuestionsListNet>() {
            @Override
            public void onResponse(Call<QuestionsListNet> call, Response<QuestionsListNet> response) {
                questionsListNet.setQuestionsListNet(response.body());
                for (QuestionActionNet actionNet : questionsListNet.getQuestionsListNet().getQuestionActionNets()) {
                    if (ResultCode.values()[actionNet.getResultCode().intValue()] == ResultCode.Success) {
                        for (AnswerActionNet answerActionNet : actionNet.getAnswerActionNets()) {
                            if (ResultCode.values()[answerActionNet.getResultCode().intValue()] == ResultCode.Success) {
                                questionsListNet.addUserAnswer(new UserAnswer(UserContextOperation.getUserID(),
                                        actionNet.getUserID(), answerActionNet.getUserID(), response.body().getUserID()));
                                break;
                            }
                        }
                    }
                }
                testActivity.onCreate();
            }

            @Override
            public void onFailure(Call<QuestionsListNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void finishTest(final TestActivity testActivity, final boolean isPrev, boolean timeout) {
        Call<UniversalActionNet> netCall = apiService.FinishTest(new FinishTest(UserContextOperation.getTestResultID(), timeout));
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                UniversalActionNet actionNet = response.body();
                switch (ResultCode.values()[actionNet.getResultCode().intValue()]) {
                    case Success:
                        BuilderPresenter.getTestPresenter().setClear();
                        if (!isPrev) {
                            Intent intent = new Intent(testActivity, UserActivity.class);
                            intent.putExtra("FinishTest", true);
                            testActivity.startActivity(intent);
                        }
                        break;
                    case Error:
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                testActivity.setFirstUnansweredQuestion();
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(testActivity);
                        builder.setMessage("You answer not all question").setPositiveButton("Ok", dialogClickListener).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void isTestStart(boolean isStart, final StartTestActivity startTestActivity) {
        IsStartTest isStartTest = new IsStartTest(isStart, UserContextOperation.getTestResultID(), UserContextOperation.getUserID());
        Call<UniversalActionNet> netCall = apiService.IsStartTest(isStartTest);
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                UserContextOperation.setTestResultID(response.body().getWorkID());
                Intent intent = new Intent(startTestActivity, TestActivity.class);
                startTestActivity.startActivity(intent);
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
            }
        });
    }

    public void statistics(final StatisticActivity statisticActivity) {
        Call<StatisticsListNet> netCall = apiService.Statistics(new IntegerOutput(UserContextOperation.getUserID()));
        netCall.enqueue(new Callback<StatisticsListNet>() {
            @Override
            public void onResponse(Call<StatisticsListNet> call, Response<StatisticsListNet> response) {
                StatisticsListNet listNet = response.body();
                LinearLayout layout = statisticActivity.findViewById(R.id.statistics);
                for (StaticticResult result : listNet.getStaticticResults()) {
                    LinearLayout linearLayout = new LinearLayout(statisticActivity);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    linearLayout.setLayoutParams(lp);
                    linearLayout.setPadding(0,10,0,10);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setGravity(Gravity.CENTER);
                    TextView date = new TextView(statisticActivity), content = new TextView(statisticActivity);
                    date.setText(result.getDate());
                    content.setText(result.getMark());
                    linearLayout.addView(date);
                    linearLayout.addView(content);
                    layout.addView(linearLayout);
                }
            }

            @Override
            public void onFailure(Call<StatisticsListNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void answer(final UserAnswer userAnswer, final TestActivity testActivity, final int id) {
        Call<UniversalActionNet> netCall = apiService.Answer(userAnswer);
        netCall.enqueue(new Callback<UniversalActionNet>() {
            @Override
            public void onResponse(Call<UniversalActionNet> call, Response<UniversalActionNet> response) {
                UniversalActionNet actionNet = response.body();
                TextView answer = (TextView) testActivity.findViewById(id);
                answer.setTextColor(Color.parseColor("#ffffff"));
                answer.setBackgroundColor(Color.parseColor("#000000"));
                LinearLayout answers = (LinearLayout) testActivity.findViewById(R.id.answers);
                for (int i = 0; i < answers.getChildCount(); i++)
                    answers.getChildAt(i).setOnClickListener(null);
                testActivity.findViewById(R.id.nextBtn).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<UniversalActionNet> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
