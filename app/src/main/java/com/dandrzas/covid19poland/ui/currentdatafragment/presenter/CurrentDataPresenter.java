package com.dandrzas.covid19poland.ui.currentdatafragment.presenter;

import com.dandrzas.covid19poland.data.domain.Covid19TodayData;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSourceIF;
import com.dandrzas.covid19poland.ui.currentdatafragment.view.CurrentDataFragmentIF;
import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;


public class CurrentDataPresenter implements CurrentDataPresenterIF {
    private CurrentDataFragmentIF view;
    private RemoteDataSourceIF remoteDataSource;

    public CurrentDataPresenter(CurrentDataFragmentIF view, RemoteDataSourceIF remoteDataSource) {
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
                            ArrayList<Integer> countersData = new ArrayList<>();
                            countersData.add(0, covid19TodayData.getCasesAll());
                            countersData.add(1, covid19TodayData.getCasesToday());
                            countersData.add(2, covid19TodayData.getCasesActive());
                            countersData.add(3, covid19TodayData.getCuredAll());
                            countersData.add(4, covid19TodayData.getDeathsAll());
                            countersData.add(5, covid19TodayData.getDeathsToday());
                            view.setProgressBarsVisibility(false);
                            view.setCountersDataAnimated(countersData, covid19TodayData.getUpdatedTime());
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
            ArrayList<Integer> countersData = new ArrayList<>();
            countersData.add(0, data.getCasesAll());
            countersData.add(1, data.getCasesToday());
            countersData.add(2, data.getCasesActive());
            countersData.add(3, data.getCuredAll());
            countersData.add(4, data.getDeathsAll());
            countersData.add(5, data.getDeathsToday());
            view.setCountersData(countersData, data.getUpdatedTime());
        }
        else{
            refreshData(isInternetConnection, scheduler);
        }
    }
}
