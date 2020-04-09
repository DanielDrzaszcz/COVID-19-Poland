package com.dandrzas.covid19poland.ui.trendsfragment.presenter;

import io.reactivex.Scheduler;

public interface TrendsPresenterIF {
    void refreshData(boolean isInternetConnection, Scheduler scheduler);
    void initData(boolean isInternetConnection, Scheduler scheduler);
}