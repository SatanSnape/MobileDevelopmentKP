package com.example.zimma.testketel.ServerInteract;

import com.example.zimma.testketel.Models.ModelsInteract.FinishTest;
import com.example.zimma.testketel.Models.ModelsInteract.IntegerOutput;
import com.example.zimma.testketel.Models.ModelsInteract.IsStartTest;
import com.example.zimma.testketel.Models.ModelsInteract.RegisterModel;
import com.example.zimma.testketel.Models.ModelsInteract.UserAnswer;
import com.example.zimma.testketel.Models.ModelsInteract.UserLogin;
import com.example.zimma.testketel.Models.ModelsInteract.UserSave;
import com.example.zimma.testketel.Models.NetModels.ProfileUserInfo;
import com.example.zimma.testketel.Models.NetModels.QuestionsListNet;
import com.example.zimma.testketel.Models.NetModels.StartActionNet;
import com.example.zimma.testketel.Models.NetModels.StatisticsListNet;
import com.example.zimma.testketel.Models.NetModels.UniversalActionNet;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {


    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("login")
    Call<UniversalActionNet> Login(@Body UserLogin userLogin);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("register")
    Call<UniversalActionNet> Register(@Body RegisterModel registerModel);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("startTest")
    Call<StartActionNet> StartTest(@Body IntegerOutput user);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("getQuestions")
    Call<QuestionsListNet> GetQuestions(@Body IntegerOutput id);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("answer")
    Call<UniversalActionNet> Answer(@Body UserAnswer userAnswer);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("finishTest")
    Call<UniversalActionNet> FinishTest(@Body FinishTest integerOutput);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("isStartTest")
    Call<UniversalActionNet> IsStartTest(@Body IsStartTest isStartTest);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("profileUserInfo")
    Call<ProfileUserInfo> ProfileInfo(@Body IntegerOutput integerOutput);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("getStatistics")
    Call<StatisticsListNet> Statistics(@Body IntegerOutput integerOutput);


    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("SaveUser")
    Call<UniversalActionNet> SaveUser(@Body UserSave userLogin);

}
