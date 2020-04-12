package com.dandrzas.covid19poland.ui.countersfragment.presenter;

import com.dandrzas.covid19poland.data.domain.Covid19TodayData;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSourceIF;
import com.dandrzas.covid19poland.ui.countersfragment.view.CountersFragmentIF;
import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;


public class CountersPresenter implements CountersPresenterIF {
    private CountersFragmentIF view;
    private RemoteDataSourceIF remoteDataSource;

    public CountersPresenter(CountersFragmentIF view, RemoteDataSourceIF remoteDataSource) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void refreshData(boolean isInternetConnection, Scheduler scheduler) {
        if (isInternetConnection) {
            remoteDataSource.downloadTodayData()
                    .subscribeOn(scheduler)
                    .subscribe(new Observer<Covid19TodayData>() {
                        Disposable disposable;

                        @Override
                        public void onSubscribe(Disposable disposable) {
                            this.disposable = disposable;
                            view.clearCountersData();
                            view.setProgressBarsVisibility(true);
                        }

                        @Override
                        public void onNext(Covid19TodayData covid19TodayData) {
                            ArrayList<String> countersData = new ArrayList<String>();
                            countersData.add(0, String.valueOf(covid19TodayData.getCasesAll()));
                            countersData.add(1, String.valueOf(covid19TodayData.getCasesToday()));
                            countersData.add(2, String.valueOf(covid19TodayData.getCuredAll()));
                            countersData.add(3,String.valueOf(covid19TodayData.getDeathsAll()));
                            countersData.add(4,String.valueOf(covid19TodayData.getDeathsToday()));
                            view.setProgressBarsVisibility(false);
                            view.setCountersData(countersData);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.setProgressBarsVisibility(false);
                            view.setCountersError();
                        }

                        @Override
                        public void onComplete() {
                            disposable.dispose();
                        }
                    });
        } else {
            view.showConnectionAlert();
        }
    }

    @Override
    public void initData(boolean isInternetConnection, Scheduler scheduler) {
        Covid19TodayData data = remoteDataSource.getTodayData();
        if(data != null){
            ArrayList<String> countersData = new ArrayList<String>();
            countersData.add(0, String.valueOf(data.getCasesAll()));
            countersData.add(1, String.valueOf(data.getCasesToday()));
            countersData.add(2, String.valueOf(data.getCuredAll()));
            countersData.add(3,String.valueOf(data.getDeathsAll()));
            countersData.add(4,String.valueOf(data.getDeathsToday()));
            view.setCountersData(countersData);
        }
        else{
            refreshData(isInternetConnection, scheduler);
        }
    }
}
