package com.dandrzas.covid19poland.ui.trendsfragment.presenter;

import com.dandrzas.covid19poland.data.domain.Covid19HistoricalData;
import com.dandrzas.covid19poland.data.domain.Covid19TodayData;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.trendsfragment.view.TrendsFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TrendsPresenterTest {

    private HashMap<String, Integer> casesHistoryMap = new LinkedHashMap<>();
    private HashMap<String, Integer> deathsHistoryMap = new LinkedHashMap<>();
    private HashMap<String, Integer> curedHistoryMap = new LinkedHashMap<>();
    private TrendsPresenter  presenter;
    private Covid19HistoricalData data = new Covid19HistoricalData();
    private String[] dateDaysAll = {"3/25/20", "3/26/20", "3/27/20", "3/28/20", "3/29/20"};
    private String[] dateDaysDaily = {"3/26/20", "3/27/20", "3/28/20", "3/29/20"};

    @Mock
    TrendsFragment viewMock;

    @Mock
    RemoteDataSource remoteDataSourceMock;

    @Before
    public void setUp(){
        presenter = new TrendsPresenter(viewMock, remoteDataSourceMock);

        casesHistoryMap.put(dateDaysAll[0], 150);
        casesHistoryMap.put(dateDaysAll[1], 170);
        casesHistoryMap.put(dateDaysAll[2], 168);
        casesHistoryMap.put(dateDaysAll[3], 249);
        casesHistoryMap.put(dateDaysAll[4], 224);
        data.setHistoryCasesAll(casesHistoryMap);

        deathsHistoryMap.put(dateDaysAll[0], 22);
        deathsHistoryMap.put(dateDaysAll[1], 42);
        deathsHistoryMap.put(dateDaysAll[2], 67);
        deathsHistoryMap.put(dateDaysAll[3], 99);
        deathsHistoryMap.put(dateDaysAll[4], 155);
        data.setHistoryDeathsAll(deathsHistoryMap);

        curedHistoryMap.put(dateDaysAll[0], 1);
        curedHistoryMap.put(dateDaysAll[1], 14);
        curedHistoryMap.put(dateDaysAll[2], 25);
        curedHistoryMap.put(dateDaysAll[3], 67);
        curedHistoryMap.put(dateDaysAll[4], 98);
        data.setHistoryCuredAll(curedHistoryMap);
    }

    @Test
    public void refreshDataTestWhenInternetEnableAndResponseOK() {

        // Config
        when(remoteDataSourceMock.downloadHistoricalData()).thenReturn(Observable.just(data));

        // Trigger
        presenter.refreshData(true, Schedulers.trampoline());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, never()).setErrorVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(false);
        verify(viewMock, atLeastOnce()).setChartsVisibility(true);
        verify(viewMock, atLeastOnce()).setLineChartDataAnimated(any(), eq(dateDaysAll));
        verify(viewMock, atLeastOnce()).setBarChartDataAnimated(any(), eq(dateDaysDaily));

    }

    @Test
    public void refreshDataTestWhenInternetEnableAndResponseNG() {

        // Config
        when(remoteDataSourceMock.downloadHistoricalData()).thenReturn(Observable.error(new Throwable()));

        // Trigger
        presenter.refreshData(true, Schedulers.trampoline());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, atLeastOnce()).setErrorVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(false);
        verify(viewMock, atLeastOnce()).setChartsVisibility(false);
        verify(viewMock, never()).setLineChartDataAnimated(any(), any());
        verify(viewMock, never()).setBarChartDataAnimated(any(), any());

    }

    @Test
    public void refreshDataTestWhenInternetDisable() {

        // Trigger
        presenter.refreshData(false, Schedulers.trampoline());

        // Verify
        verify(viewMock, atLeastOnce()).showConnectionAlert();
        verify(viewMock, never()).setErrorVisibility(anyBoolean());
        verify(viewMock, never()).setProgressBarsVisibility(anyBoolean());
        verify(viewMock, never()).setChartsVisibility(anyBoolean());
        verify(viewMock, never()).setLineChartDataAnimated(any(), any());
        verify(viewMock, never()).setBarChartDataAnimated(any(), any());

    }

    @Test
    public void initDataTestWhenDataIsAvailable() {

        // Config
        when(remoteDataSourceMock.getHistoricalData()).thenReturn(data);

        // Trigger
        presenter.initData(true, Schedulers.trampoline());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, atLeastOnce()).setErrorVisibility(false);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(false);
        verify(viewMock, atLeastOnce()).setChartsVisibility(true);
        verify(viewMock, atLeastOnce()).setLineChartData(any(), eq(dateDaysAll) );
        verify(viewMock, atLeastOnce()).setBarChartData(any(), eq(dateDaysDaily));

    }

    @Test
    public void initDataTestWhenDataIsUnavailable() {

        // Config
        when(remoteDataSourceMock.downloadHistoricalData()).thenReturn(Observable.just(data));

        // Trigger
        presenter.initData(true, Schedulers.trampoline());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, never()).setErrorVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(true);
        verify(viewMock, atLeastOnce()).setProgressBarsVisibility(false);
        verify(viewMock, atLeastOnce()).setChartsVisibility(true);
        verify(viewMock, atLeastOnce()).setLineChartDataAnimated(any(), eq(dateDaysAll));
        verify(viewMock, atLeastOnce()).setBarChartDataAnimated(any(), eq(dateDaysDaily));

    }

}