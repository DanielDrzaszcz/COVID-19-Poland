package com.dandrzas.covid19poland.ui.countersfragment.presenter;

import io.reactivex.Scheduler;

public interface MainPresenterIF {
    void refreshData(boolean isInternetConnection, Scheduler scheduler);
}