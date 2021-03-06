package com.dandrzas.covid19poland.ui.currentdatafragment.presenter;

import com.dandrzas.covid19poland.data.domain.Covid19TodayData;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.currentdatafragment.view.CurrentDataFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrentDataPresenterTest {

    private final Integer TEST_ALL_CASES = 9999;
    private final Integer TEST_TODAY_CASES = 344;
    private final Integer TEST_ACTIVE_CASES = 8994;
    private final Integer TEST_ALL_CURED = 54;
    private final Integer TEST_ALL_DEATHS = 65;
    private final Integer TEST_TODAY_DEATHS = 5;
    private CurrentDataPresenter presenter;
    private Covid19TodayData data = new Covid19TodayData();
    private ArrayList<Integer> dataList = new ArrayList<>();

    @Mock
    CurrentDataFragment viewMock;

    @Mock
    RemoteDataSource remoteDataSourceMock;

    @Before
    public void setUp() {
        presenter = new CurrentDataPresenter(viewMock, remoteDataSourceMock);
        data.setCasesAll(TEST_ALL_CASES);
        data.setCasesToday(TEST_TODAY_CASES);
        data.setCasesActive(TEST_ACTIVE_CASES);
        data.setCuredAll(TEST_ALL_CURED);
        data.setDeathsAll(TEST_ALL_DEATHS);
        data.setDeathsToday(TEST_TODAY_DEATHS);
        dataList.add(0, data.getCasesAll());
        dataList.add(1, data.getCasesToday());
        dataList.add(2, data.getCasesActive());
        dataList.add(3, data.getCuredAll());
        dataList.add(4, data.getDeathsAll());
        dataList.add(5, data.getDeathsToday());
    }

    @Test
    public void refreshDataTestWhenInternetEnableAndResponseOK() {

        // Config
        when(remoteDataSourceMock.downloadTodayData()).thenReturn(Observable.just(data));

        // Trigger
        presenter.refreshData(true, Schedulers.trampoline());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, atLeastOnce()).clearCountersData();
        verify(viewMock, atLeast(2)).setProgressBarsVisibility(anyBoolean());
        verify(viewMock, never()).setCountersData(any(), any());
        verify(viewMock, atLeastOnce()).setCountersDataAnimated(Mockito.eq(dataList), any());
        verify(viewMock, never()).setCountersError();

    }


    @Test
    public void refreshDataTestWhenInternetEnableAndResponseNG() {

        // Config
        when(remoteDataSourceMock.downloadTodayData()).thenReturn(Observable.error(new Throwable()));

        // Trigger
        presenter.refreshData(true, Schedulers.trampoline());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, atLeastOnce()).clearCountersData();
        verify(viewMock, atLeast(2)).setProgressBarsVisibility(anyBoolean());
        verify(viewMock, never()).setCountersData(any(), any());
        verify(viewMock, never()).setCountersDataAnimated(any(), any());
        verify(viewMock, atLeastOnce()).setCountersError();

    }

    @Test
    public void refreshDataTestWhenInternetDisable() {

        // Trigger
        presenter.refreshData(false, Schedulers.trampoline());

        // Verify
        verify(viewMock, atLeastOnce()).showConnectionAlert();
        verify(viewMock, never()).clearCountersData();
        verify(viewMock, never()).setProgressBarsVisibility(anyBoolean());
        verify(viewMock, never()).setCountersData(any(), any());
        verify(viewMock, never()).setCountersDataAnimated(any(), any());
        verify(viewMock, never()).setCountersError();

    }

    @Test
    public void initDataTestWhenDataIsAvailable() {

        // Config
        when(remoteDataSourceMock.getTodayData()).thenReturn(data);

        // Trigger
        presenter.initData(true, Schedulers.trampoline());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, never()).clearCountersData();
        verify(viewMock, never()).setProgressBarsVisibility(anyBoolean());
        verify(viewMock, atLeastOnce()).setCountersData(Mockito.eq(dataList), any());
        verify(viewMock, never()).setCountersDataAnimated(any(), any());
        verify(viewMock, never()).setCountersError();

    }

    @Test
    public void initDataTestWhenDataIsUnavailable() {

        // Config
        when(remoteDataSourceMock.getTodayData()).thenReturn(null);
        when(remoteDataSourceMock.downloadTodayData()).thenReturn(Observable.just(data));


        // Trigger
        presenter.initData(true, Schedulers.trampoline());

        // Verify
        verify(viewMock, never()).showConnectionAlert();
        verify(viewMock, atLeastOnce()).clearCountersData();
        verify(viewMock, atLeast(2)).setProgressBarsVisibility(anyBoolean());
        verify(viewMock, never()).setCountersData(any(), any());
        verify(viewMock, atLeastOnce()).setCountersDataAnimated(Mockito.eq(dataList), any());
        verify(viewMock, never()).setCountersError();

    }
}