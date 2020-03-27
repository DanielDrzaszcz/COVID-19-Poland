package com.dandrzas.covid19poland.model;

import io.reactivex.Observable;

public interface DataRepositoryIF {
    Observable<Integer> refreshAndGetData();
}
