package com.dandrzas.covid19poland.model;

import android.util.Log;

import com.dandrzas.covid19poland.remotedatasource.NetworkHandler;
import com.dandrzas.covid19poland.remotedatasource.NetworkHandlerIF;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataRepository implements DataRepositoryIF {
    private static final DataRepository ourInstance = new DataRepository();
    private Covid19Data covid19Data = new Covid19Data();
    private Observable<Integer> covid19DataEmitter = Observable.create(new Covid19DataEmitter());
    private Observer<Covid19Data> covid19DataObserver = new DataObserver();
    private NetworkHandlerIF networkHandlerInstance = NetworkHandler.getInstance();
    private ObservableEmitter<Integer> subscribedEmitter;

    public static DataRepository getInstance() {
        return ourInstance;
    }

    private DataRepository() {
    }

    @Override
    public Observable<Integer> refreshAndGetData() {
        networkHandlerInstance.downloadData().
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(covid19DataObserver);
        return covid19DataEmitter;
    }

    private class Covid19DataEmitter implements ObservableOnSubscribe<Integer> {

        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) {
            subscribedEmitter = emitter;
        }

    }

    private class DataObserver implements Observer<Covid19Data> {

        @Override
        public void onSubscribe(Disposable d) {
            Log.d("DataRepository RxTest: ", "onSubscribe");
        }

        @Override
        public void onNext(Covid19Data data) {
            Log.d("DataRepository RxTest: ", "onNext ");
            covid19Data = data;
            subscribedEmitter.onNext(covid19Data.getCasesAll());
            subscribedEmitter.onNext(covid19Data.getCasesToday());
            subscribedEmitter.onNext(covid19Data.getCuredAll());
            subscribedEmitter.onNext(covid19Data.getDeathsAll());
            subscribedEmitter.onNext(covid19Data.getDeathsToday());
        }

        @Override
        public void onError(Throwable e) {
            Log.d("DataRepository RxTest: ", "onError");
            subscribedEmitter.onError(new Throwable());
        }

        @Override
        public void onComplete() {
            Log.d("DataRepository RxTest: ", "onComplete");
            subscribedEmitter.onComplete();
        }
    }
}
