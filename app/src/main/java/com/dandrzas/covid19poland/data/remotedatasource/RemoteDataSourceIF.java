package com.dandrzas.covid19poland.data.remotedatasource;

import com.dandrzas.covid19poland.data.domain.Covid19Data;

import io.reactivex.Observable;

public interface RemoteDataSourceIF {
    Observable<Covid19Data> downloadData();
}
