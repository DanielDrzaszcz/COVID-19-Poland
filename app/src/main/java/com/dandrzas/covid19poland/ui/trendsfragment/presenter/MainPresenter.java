package com.dandrzas.covid19poland.ui.trendsfragment.presenter;

import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSourceIF;
import com.dandrzas.covid19poland.ui.trendsfragment.view.TrendsFragmentIF;


public class MainPresenter implements MainPresenterIF {
    private TrendsFragmentIF view;
    private RemoteDataSourceIF remoteDataSource;

    public MainPresenter(TrendsFragmentIF view, RemoteDataSourceIF remoteDataSource) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
    }

}
