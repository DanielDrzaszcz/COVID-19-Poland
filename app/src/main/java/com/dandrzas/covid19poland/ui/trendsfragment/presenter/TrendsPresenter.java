package com.dandrzas.covid19poland.ui.trendsfragment.presenter;

import android.graphics.Color;

import com.dandrzas.covid19poland.data.domain.Covid19Data;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSourceIF;
import com.dandrzas.covid19poland.ui.trendsfragment.view.TrendsFragmentIF;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;


public class TrendsPresenter implements TrendsPresenterIF {
    private TrendsFragmentIF view;
    private RemoteDataSourceIF remoteDataSource;

    public TrendsPresenter(TrendsFragmentIF view, RemoteDataSourceIF remoteDataSource) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void refreshData(boolean isInternetConnection, Scheduler scheduler) {
        if (isInternetConnection) {
            remoteDataSource.downloadData()
                    .subscribeOn(scheduler)
                    .subscribe(new Observer<Covid19Data>() {
                        Disposable disposable;

                        @Override
                        public void onSubscribe(Disposable disposable) {
                            this.disposable = disposable;
                            view.setChartsVisibility(false);
                            view.setErrorVisibility(false);
                            view.setProgressBarsVisibility(true);
                        }

                        @Override
                        public void onNext(Covid19Data covid19Data) {
                            ArrayList<String> countersData = new ArrayList<String>();
                            countersData.add(0, String.valueOf(covid19Data.getCasesAll()));
                            countersData.add(1, String.valueOf(covid19Data.getCasesToday()));
                            countersData.add(2, String.valueOf(covid19Data.getCuredAll()));
                            countersData.add(3,String.valueOf(covid19Data.getDeathsAll()));
                            countersData.add(4,String.valueOf(covid19Data.getDeathsToday()));

                            LineData lineData = hashMapToLineData(covid19Data.getHistoryCasesAll());
                            BarData barData = hashMapToBarData(covid19Data.getHistoryCasesDaily());
                            String[] lineChartDateLabels = Arrays.copyOf(covid19Data.getHistoryCasesAll().keySet().toArray(), covid19Data.getHistoryCasesAll().keySet().toArray().length, String[].class);
                            String[] barChartDateLabels = Arrays.copyOf(covid19Data.getHistoryCasesDaily().keySet().toArray(),covid19Data.getHistoryCasesDaily().keySet().toArray().length, String[].class);

                            view.setProgressBarsVisibility(false);
                            view.setChartsVisibility(true);
                            view.setLineChartData(lineData,  lineChartDateLabels);
                            view.setBarChartData(barData,  barChartDateLabels);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.setProgressBarsVisibility(false);
                            view.setErrorVisibility(true);
                        }

                        @Override
                        public void onComplete() {
                            disposable.dispose();
                        }
                    });
        } else {
            view.showConnectionAlert();
        }
    }

    @Override
    public void initData(boolean isInternetConnection, Scheduler scheduler) {

        view.setProgressBarsVisibility(false);
        view.setErrorVisibility(false);
        view.setChartsVisibility(true);

        Covid19Data data = remoteDataSource.getData();
        if(data != null){
            if(data.getHistoryCasesAll() != null) {

                LineData lineData = hashMapToLineData(data.getHistoryCasesAll());
                BarData barData = hashMapToBarData(data.getHistoryCasesDaily());
                String[] lineChartDateLabels = Arrays.copyOf(data.getHistoryCasesAll().keySet().toArray(), data.getHistoryCasesAll().keySet().toArray().length, String[].class);
                String[] barChartDateLabels = Arrays.copyOf(data.getHistoryCasesDaily().keySet().toArray(), data.getHistoryCasesDaily().keySet().toArray().length, String[].class);

                view.setLineChartData(lineData, lineChartDateLabels);
                view.setBarChartData(barData, barChartDateLabels);
            }
            else{
                refreshData(isInternetConnection, scheduler);
            }
        }
        else{
            refreshData(isInternetConnection, scheduler);
        }
    }

    private LineData hashMapToLineData(HashMap<String,Integer> hashMap){

        ArrayList<Entry> entries = new ArrayList<>();
        String[] mapKeys = Arrays.copyOf(hashMap.keySet().toArray(), hashMap.keySet().toArray().length, String[].class);

        for(int i=0; i<mapKeys.length; i++) {
            entries.add(new Entry(i,hashMap.get(mapKeys[i])));
        }

        LineDataSet dataset = new LineDataSet(entries, "");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setColor(Color.RED);
        dataset.setHighlightEnabled(false);
        LineData data = new LineData(dataset);

        return data;
    }

    private BarData hashMapToBarData(HashMap<String,Integer> hashMap){

        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] mapKeys = Arrays.copyOf(hashMap.keySet().toArray(), hashMap.keySet().toArray().length, String[].class);

        for(int i=0; i<mapKeys.length; i++) {
            entries.add(new BarEntry(i,hashMap.get(mapKeys[i])));
        }

        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setColor(Color.RED);
        dataset.setHighlightEnabled(false);
        BarData data = new BarData(dataset);

        return data;
    }
}
