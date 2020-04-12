package com.dandrzas.covid19poland.ui.countersfragment.presenter;

import com.dandrzas.covid19poland.data.domain.Covid19TodayData;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.countersfragment.view.CountersFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
public class CountersPresenterTest {

    private final Integer TEST_ALL_CASES = 9999;
    private final Integer TEST_TODAY_CASES = 54;
    private final Integer TEST_ALL_CURED = 54;
    private final Integer TEST_ALL_DEATHS = 65;
    private final Integer TEST_TODAY_DEATHS = 5;
    private CountersPresenter presenter;
    private Covid19TodayData data = new Covid19TodayData();
    private ArrayList<String> dataList = new ArrayList<>();

    @Mock
    CountersFragment viewMock;

    @Mock
    RemoteDataSource remoteDataSourceMock;

    @Before
    public void setUp() {
        presenter = new CountersPresenter(viewMock, remoteDataSourceMock);
        data.setCasesAll(TEST_ALL_CASES);
        data.setCasesToday(TEST_TODAY_CASES);
        data.setCuredAll(TEST_ALL_CURED);
        data.setDeathsAll(TEST_ALL_DEATHS);
        data.setDeathsToday(TEST_TODAY_DEATHS);
        dataList.add(0, String.valueOf(data.getCasesAll()));
        dataList.add(1, String.valueOf(data.getCasesToday()));
        dataList.add(2, String.valueOf(data.getCuredAll()));
        dataList.add(3, String.valueOf(data.getDeathsAll()));
        dataList.add(4, String.valueOf(data.getDeathsToday()));
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
        verify(viewMock, atLeastOnce()).setCountersData(dataList);
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
        verify(viewMock, never()).setCountersData(any());
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
        verify(viewMock, never()).setCountersData(any());
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
        verify(viewMock, atLeastOnce()).setCountersData(dataList);
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
        verify(viewMock, atLeastOnce()).setCountersData(dataList);
        verify(viewMock, never()).setCountersError();

    }
}