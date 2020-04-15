package com.dandrzas.covid19poland.ui.historicaldatafragment.presenter;

import io.reactivex.Scheduler;

public interface HistoricalDataPresenterIF {
    void refreshData(boolean isInternetConnection, Scheduler scheduler);
    void initData(boolean isInternetConnection, Scheduler scheduler);
}