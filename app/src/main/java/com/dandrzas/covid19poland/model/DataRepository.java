package com.dandrzas.covid19poland.model;

import android.os.SystemClock;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DataRepository implements DataRepositoryIF {
    private static final DataRepository ourInstance = new DataRepository();
    private int[] covid19Data;
    private Observable<Integer> covid19DataEmitter = Observable.create(new Covid19DataEmitter());

    public static DataRepository getInstance() {
        return ourInstance;
    }

    private DataRepository() {

    }

    @Override
    public Observable<Integer> getData() {
        return covid19DataEmitter;
    }

    private class Covid19DataEmitter implements ObservableOnSubscribe<Integer> {

        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) {
            SystemClock.sleep(2000);
            if(covid19Data!=null) {
                for (int data : covid19Data) {
                    Log.d("rxJavatest ", "Next" + data);
                    emitter.onNext(data);
                }
                emitter.onComplete();
            }
            else{
                emitter.onError(new Throwable());
            }
        }
    }
}
