package com.dandrzas.covid19poland.data.remotedatasource;

import com.dandrzas.covid19poland.data.domain.Covid19HistoricalData;
import com.dandrzas.covid19poland.data.domain.Covid19TodayData;

import io.reactivex.Observable;

public interface RemoteDataSourceIF {
    Observable<Covid19TodayData> downloadTodayData();
    Covid19TodayData getTodayData();
    Observable<Covid19HistoricalData> downloadHistoricalData();
    Covid19HistoricalData getHistoricalData();
}
