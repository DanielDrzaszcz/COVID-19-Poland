package com.dandrzas.covid19poland.presenter;

import io.reactivex.Scheduler;

public interface MainPresenterIF {
    void refreshData(boolean isInternetConnection, Scheduler scheduler);
}