package com.dandrzas.covid19poland.presenter;

import com.dandrzas.covid19poland.model.domain.Covid19Data;
import com.dandrzas.covid19poland.model.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.view.MainActivity;

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
public class MainPresenterTest {

    private final Integer TEST_ALL_CASES = 9999;
    private final Integer TEST_TODAY_CASES = 54;
    private final Integer TEST_ALL_CURED = 54;
    private final Integer TEST_ALL_DEATHS = 65;
    private final Integer TEST_TODAY_DEATHS = 5;
    private MainPresenter presenter;
    private Covid19Data data = new Covid19Data();
    private ArrayList<String> dataList = new ArrayList<>();

    @Mock
    MainActivity viewMock;

    @Mock
    RemoteDataSource remoteDataSourceMock;

    @Before
    public void setUp() {
        presenter = new MainPresenter(viewMock, remoteDataSourceMock);
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
        when(remoteDataSourceMock.downloadData()).thenReturn(Observable.just(data));

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
        when(remoteDataSourceMock.downloadData()).thenReturn(Observable.error(new Throwable()));

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
}