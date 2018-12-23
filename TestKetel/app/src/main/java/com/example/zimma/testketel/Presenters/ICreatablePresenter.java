package com.example.zimma.testketel.Presenters;

import android.view.View;

public abstract class ICreatablePresenter<T> {
    public abstract void onCreate(T v);

    protected T activity;

    public void setView(T v) {
        this.activity = v;
    }

    public T getView() {
        return activity;
    }
}
