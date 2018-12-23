package com.example.zimma.testketel.Models.NetModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaticticResult {

    @SerializedName("ResultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("WorkID")
    @Expose
    private Integer workID;
    @SerializedName("Result")
    @Expose
    private String mark;

    @SerializedName("DateTime")
    @Expose
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getWorkID() {
        return workID;
    }

    public void setWorkID(Integer workID) {
        this.workID = workID;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}