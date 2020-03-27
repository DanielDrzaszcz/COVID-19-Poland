package com.dandrzas.covid19poland.remotedatasource;

import io.reactivex.Observable;

public interface NetworkHandlerIF {
    Observable<Integer> downloadData();
}
