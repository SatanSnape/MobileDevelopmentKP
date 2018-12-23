package com.example.zimma.testketel.Presenters;

public class BuilderPresenter {

    private static TestPresenter testPresenter;

    public static TestPresenter getTestPresenter() {
        if (testPresenter == null)
            testPresenter = new TestPresenter();
        return testPresenter;
    }
}
