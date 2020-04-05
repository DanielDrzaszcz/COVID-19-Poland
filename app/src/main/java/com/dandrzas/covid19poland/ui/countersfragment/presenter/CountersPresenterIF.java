package com.dandrzas.covid19poland.ui.countersfragment.presenter;

import io.reactivex.Scheduler;

public interface CountersPresenterIF {
    void refreshData(boolean isInternetConnection, Scheduler scheduler);
    void initData(boolean isInternetConnection, Scheduler scheduler);
}