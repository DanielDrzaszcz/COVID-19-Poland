package com.dandrzas.covid19poland.ui.trendsfragment.presenter;

import com.dandrzas.covid19poland.data.domain.Covid19Data;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.countersfragment.presenter.CountersPresenter;
import com.dandrzas.covid19poland.ui.countersfragment.view.CountersFragment;
import com.dandrzas.covid19poland.ui.trendsfragment.view.TrendsFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrendsPresenterTest {

    private HashMap<String, Integer> casesHistoryMap = new LinkedHashMap<>();
    private TrendsPresenter  presenter;
    private Covid19Data data = new Covid19Data();
    private String[] dateDays = {"3/25/20", "3/26/20", "3/27/20", "3/28/20", "3/29/20"};

    @Mock
    TrendsFragment viewMock;

    @Mock
    RemoteDataSource remoteDataSourceMock;

    @Before
    public void setUp(){
        presenter = new TrendsPresenter(viewMock, remoteDataSourceMock);
        casesHistoryMap.put(dateDays[0], 150);
        casesHistoryMap.put(dateDays[1], 170);
        casesHistoryMap.put(dateDays[2], 168);
        casesHistoryMap.put(dateDays[3], 249);
        casesHistoryMap.put(dateDays[4], 224);
        data.setHistoryCasesAll(casesHistoryMap);
    }

    @Test
    public void refreshDataTestWhenInternetEnableAndResponseOK() {

        // Config
        when(remoteDataSourceMock.downloadData()).thenReturn(Observable.just(data));

        // Trigger
        presenter.refreshData(true, new TestScheduler());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, never()).setErrorVisibility(anyBoolean());
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(false);
        verify(viewMock, atLeastOnce()).setChartsVisibility(true);
        verify(viewMock, atMostOnce()).setChartsData(any(), any(), dateDays);

    }

    @Test
    public void refreshDataTestWhenInternetEnableAndResponseNG() {

        // Config
        when(remoteDataSourceMock.downloadData()).thenReturn(Observable.error(new Throwable()));

        // Trigger
        presenter.refreshData(true, new TestScheduler());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, atLeastOnce()).setErrorVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(false);
        verify(viewMock, atLeastOnce()).setChartsVisibility(false);
        verify(viewMock, never()).setChartsData(any(), any(), any());

    }

    @Test
    public void refreshDataTestWhenInternetDisable() {

        // Trigger
        presenter.refreshData(false, new TestScheduler());

        // Verify
        verify(viewMock, atLeastOnce()).showConnectionAlert();
        verify(viewMock, never()).setErrorVisibility(anyBoolean());
        verify(viewMock, never()).setProgressBarsVisibility(anyBoolean());
        verify(viewMock, never()).setChartsVisibility(anyBoolean());
        verify(viewMock, never()).setChartsData(any(), any(), any());

    }

    @Test
    public void initDataTestWhenDataIsAvailable() {

        // Trigger
        presenter.initData(true, new TestScheduler());

        // Verify
        verify(viewMock, atLeastOnce()).showConnectionAlert();
        verify(viewMock, never()).setErrorVisibility(anyBoolean());
        verify(viewMock, never()).setProgressBarsVisibility(anyBoolean());
        verify(viewMock, never()).setChartsVisibility(anyBoolean());
        verify(viewMock, never()).setChartsData(any(), any(), dateDays);

    }

    @Test
    public void initDataTestWhenDataIsUnavailable() {

        // Config
        when(remoteDataSourceMock.downloadData()).thenReturn(Observable.just(data));

        // Trigger
        presenter.initData(true, new TestScheduler());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, never()).setErrorVisibility(anyBoolean());
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(false);
        verify(viewMock, atLeastOnce()).setChartsVisibility(true);
        verify(viewMock, atMostOnce()).setChartsData(any(), any(), dateDays);

    }

}