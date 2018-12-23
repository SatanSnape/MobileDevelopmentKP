package com.example.zimma.testketel.Presenters;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.example.zimma.testketel.Activities.StartTestActivity;
import com.example.zimma.testketel.Models.ModelsInteract.UserAnswer;
import com.example.zimma.testketel.Models.NetModels.QuestionActionNet;
import com.example.zimma.testketel.Models.NetModels.QuestionsListNet;
import com.example.zimma.testketel.Models.NetModels.UniversalActionNet;
import com.example.zimma.testketel.R;
import com.example.zimma.testketel.ServerInteract.TestInteract;
import com.example.zimma.testketel.ServerInteract.UserContextOperation;
import com.example.zimma.testketel.Activities.TestActivity;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TestPresenter extends ICreatablePresenter<TestActivity> {

    private QuestionsListNet questionsListNet;
    private TestInteract testInteract = new TestInteract();
    private List<UserAnswer> userAnswers = new ArrayList<>();
    private CountDownTimer timer;
    private int currentQuestion = 0;

    public final Object lock = new Object();
    public boolean isTestGoing = false;

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public QuestionsListNet getQuestionsListNet() {
        return questionsListNet;
    }

    public void addUserAnswer(UserAnswer userAnswer) {
        userAnswers.add(userAnswer);
    }

    public int getCurrentQuestionInt() {
        return currentQuestion;
    }

    public int getCountQuestions() {
        return questionsListNet.getQuestionActionNets().size();
    }

    public UserAnswer isAnswered(int id) {
        for (UserAnswer userAnswer : userAnswers)
            if (userAnswer.getQuestionID() == id)
                return userAnswer;
        return null;
    }

    private void startTimer(final long time) {
        if (timer != null)
            return;
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView timerTV = activity.findViewById(R.id.infoRight);
                Date date = new Date(millisUntilFinished);
                DateFormat formatter = new SimpleDateFormat("mm:ss");
                timerTV.setText(formatter.format(date));
            }

            @Override
            public void onFinish() {
                BuilderPresenter.getTestPresenter().finishTest(true, true);
            }
        }.start();
    }

    public void setClear() {
        this.userAnswers.clear();
        currentQuestion = 0;
    }


    public void setFirstUnansweredQuestion() {
        int index = 0;
        for (QuestionActionNet actionNet : questionsListNet.getQuestionActionNets()) {
            if (isAnswered(actionNet.getUserID()) == null) {
                currentQuestion = index;
                break;
            }
            index++;
        }
    }

    public void setQuestionsListNet(QuestionsListNet questionsListNet) {
        this.questionsListNet = questionsListNet;
    }

    public QuestionActionNet getCurrentQuestion() {
        return questionsListNet.getQuestionActionNets().get(currentQuestion);
    }

    public QuestionActionNet nextQuestion() {
        currentQuestion++;
        return getCurrentQuestion();
    }

    public QuestionActionNet prevQuestion() {
        if (currentQuestion - 1 >= 0)
            currentQuestion--;
        return getCurrentQuestion();
    }

    public boolean IsLastQuestion() {
        return this.questionsListNet.getQuestionActionNets().size() == currentQuestion + 1;
    }

    public void answer(int id, int answer) {
        int question = this.questionsListNet.getQuestionActionNets().get(currentQuestion).getUserID();
        UserAnswer userAnswer =
                new UserAnswer(UserContextOperation.getUserID(), question, answer, UserContextOperation.getTestResultID());
        userAnswers.add(userAnswer);
        testInteract.answer(userAnswer, activity, id);
    }

    public void finishTest(boolean isPrev, boolean isTimeOut) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        testInteract.finishTest(activity, isPrev, isTimeOut);
    }

    @Override
    public void onCreate(TestActivity v) {
        this.activity = v;
        testInteract.questionsListNet(UserContextOperation.getTestResultID(), this, activity);
        startTimer(3600000);
    }

    public void isSTartTest(boolean isStart, StartTestActivity startTestActivity) {
        testInteract.isTestStart(isStart, startTestActivity);
    }

}
