package com.dandrzas.covid19poland.presenter;

import android.util.Log;

import com.dandrzas.covid19poland.model.DataRepository;
import com.dandrzas.covid19poland.view.MainActivityIF;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainPresenterIF {
    private MainActivityIF view;
    private DataRepository dataRepository = DataRepository.getInstance();
    private String[] countersData = new String[5];
    private Observer<Integer> covid19DataObserver = new DataObserver();

    public MainPresenter(MainActivityIF view) {
        this.view = view;
    }

    @Override
    public void refreshData() {
        dataRepository.getData()
                .subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(covid19DataObserver);
    }

    private class DataObserver implements Observer<Integer> {
        int counter;

        @Override
        public void onSubscribe(Disposable d) {
            Log.d("rxJavatest ", "onSubscribe");
            view.clearCountersData();
            view.setProgressBarsVisibility(true);
            counter = 0;
        }

        @Override
        public void onNext(Integer integer) {
            Log.d("rxJavatest ", "onNext " + integer);
            if(counter<countersData.length){
                countersData[counter] = integer.toString();
            }
            counter++;
        }

        @Override
        public void onError(Throwable e) {
            Log.d("rxJavatest ", "onError");
            view.setProgressBarsVisibility(false);
            view.setCountersError();
        }

        @Override
        public void onComplete() {
            Log.d("rxJavatest ", "onComplete");
            view.setProgressBarsVisibility(false);
            view.setCountersData(countersData);
        }
    }
}
