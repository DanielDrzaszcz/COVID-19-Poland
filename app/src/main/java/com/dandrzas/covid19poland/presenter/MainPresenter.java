package com.dandrzas.covid19poland.presenter;

import android.util.Log;
import com.dandrzas.covid19poland.model.domain.Covid19Data;
import com.dandrzas.covid19poland.model.remotedatasource.RemoteDataSourceIF;
import com.dandrzas.covid19poland.view.MainActivityIF;
import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements MainPresenterIF {
    private MainActivityIF view;
    private RemoteDataSourceIF remoteDataSource;

    public MainPresenter(MainActivityIF view, RemoteDataSourceIF remoteDataSource) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void refreshData(boolean isInternetConnection, Scheduler scheduler) {
        if (isInternetConnection) {
            remoteDataSource.downloadData()
                    .subscribeOn(scheduler)
                    .subscribe(new Observer<Covid19Data>() {
                        Disposable disposable;

                        @Override
                        public void onSubscribe(Disposable disposable) {
                            this.disposable = disposable;
                            Log.d("MainPresenter RxTest: ", "onSubscribe");
                            view.clearCountersData();
                            view.setProgressBarsVisibility(true);
                        }

                        @Override
                        public void onNext(Covid19Data covid19Data) {
                            Log.d("MainPresenter RxTest: ", "onNext ");
                            ArrayList<String> countersData = new ArrayList<String>();
                            countersData.add(0, String.valueOf(covid19Data.getCasesAll()));
                            countersData.add(1, String.valueOf(covid19Data.getCasesToday()));
                            countersData.add(2, String.valueOf(covid19Data.getCuredAll()));
                            countersData.add(3,String.valueOf(covid19Data.getDeathsAll()));
                            countersData.add(4,String.valueOf(covid19Data.getDeathsToday()));
                            view.setProgressBarsVisibility(false);
                            view.setCountersData(countersData);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("MainPresenter RxTest: ", "onError");
                            view.setProgressBarsVisibility(false);
                            view.setCountersError();
                        }

                        @Override
                        public void onComplete() {
                            Log.d("MainPresenter RxTest: ", "onComplete");
                            disposable.dispose();
                        }
                    });
        } else {
            view.showConnectionAlert();
        }
    }
}
