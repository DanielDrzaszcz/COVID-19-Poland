package com.dandrzas.covid19poland.model.remotedatasource;

import com.dandrzas.covid19poland.model.domain.Covid19Data;

import io.reactivex.Observable;

public interface RemoteDataSourceIF {
    Observable<Covid19Data> downloadData();
}
