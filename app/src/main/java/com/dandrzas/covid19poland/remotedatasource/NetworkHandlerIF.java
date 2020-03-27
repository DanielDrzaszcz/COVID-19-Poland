package com.dandrzas.covid19poland.remotedatasource;

import com.dandrzas.covid19poland.model.Covid19Data;

import io.reactivex.Observable;

public interface NetworkHandlerIF {
    Observable<Covid19Data> downloadData();
}
