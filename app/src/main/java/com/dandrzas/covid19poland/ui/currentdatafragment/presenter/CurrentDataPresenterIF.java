package com.dandrzas.covid19poland.ui.currentdatafragment.presenter;

import io.reactivex.Scheduler;

public interface CurrentDataPresenterIF {
    void refreshData(boolean isInternetConnection, Scheduler scheduler);
    void initData(boolean isInternetConnection, Scheduler scheduler);
}