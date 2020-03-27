package com.dandrzas.covid19poland.presenter;

import android.util.Log;
import com.dandrzas.covid19poland.model.DataRepository;
import com.dandrzas.covid19poland.model.DataRepositoryIF;
import com.dandrzas.covid19poland.view.MainActivityIF;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainPresenterIF {
    private MainActivityIF view;
    private DataRepositoryIF dataRepository = DataRepository.getInstance();
    private Observer<Integer> covid19DataObserver = new DataObserver();

    public MainPresenter(MainActivityIF view) {
        this.view = view;
    }

    @Override
    public void refreshData() {
        dataRepository.refreshAndGetData()
                .subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(covid19DataObserver);
    }

    private class DataObserver implements Observer<Integer> {
        int i;
        String[] countersData = new String[5];

        @Override
        public void onSubscribe(Disposable d) {
            Log.d("MainPresenter RxTest: ", "onSubscribe");
            view.clearCountersData();
            view.setProgressBarsVisibility(true);
            i = 0;
        }

        @Override
        public void onNext(Integer integer) {
            Log.d("MainPresenter RxTest: ", "onNext " + integer);
            if(i <countersData.length){
                countersData[i] = integer.toString();
            }
            i++;
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
            view.setProgressBarsVisibility(false);
            view.setCountersData(countersData);
        }
    }
}
