package com.dandrzas.covid19poland.ui.trendsfragment.presenter;

import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSourceIF;
import com.dandrzas.covid19poland.ui.trendsfragment.view.TrendsFragmentIF;

import io.reactivex.Scheduler;


public class TrendsPresenter implements TrendsPresenterIF {
    private TrendsFragmentIF view;
    private RemoteDataSourceIF remoteDataSource;

    public TrendsPresenter(TrendsFragmentIF view, RemoteDataSourceIF remoteDataSource) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void refreshData(boolean isInternetConnection, Scheduler scheduler) {

    }

    @Override
    public void initData(boolean isInternetConnection, Scheduler scheduler) {

    }
}
