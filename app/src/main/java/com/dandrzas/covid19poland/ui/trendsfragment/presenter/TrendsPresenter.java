package com.dandrzas.covid19poland.ui.trendsfragment.presenter;

import android.graphics.Color;

import com.dandrzas.covid19poland.R;
import com.dandrzas.covid19poland.data.domain.Covid19HistoricalData;
import com.dandrzas.covid19poland.data.domain.Covid19TodayData;
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
            remoteDataSource.downloadHistoricalData()
                    .subscribeOn(scheduler)
                    .subscribe(new Observer<Covid19HistoricalData>() {
                        Disposable disposable;

                        @Override
                        public void onSubscribe(Disposable disposable) {
                            this.disposable = disposable;
                            view.setChartsVisibility(false);
                            view.setErrorVisibility(false);
                            view.setProgressBarsVisibility(true);
                        }

                        @Override
                        public void onNext(Covid19HistoricalData ccvid19HistoricalData) {

                            LineData lineData = hashMapToLineData(ccvid19HistoricalData.getHistoryCasesAll());
                            BarData barData = hashMapToBarData(ccvid19HistoricalData.getHistoryCasesDaily());
                            String[] lineChartDateLabels = Arrays.copyOf(ccvid19HistoricalData.getHistoryCasesAll().keySet().toArray(), ccvid19HistoricalData.getHistoryCasesAll().keySet().toArray().length, String[].class);
                            String[] barChartDateLabels = Arrays.copyOf(ccvid19HistoricalData.getHistoryCasesDaily().keySet().toArray(), ccvid19HistoricalData.getHistoryCasesDaily().keySet().toArray().length, String[].class);

                            view.setProgressBarsVisibility(false);
                            view.setChartsVisibility(true);
                            view.setLineChartDataAnimated(lineData,  lineChartDateLabels);
                            view.setBarChartDataAnimated(barData,  barChartDateLabels);
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

        Covid19HistoricalData data = remoteDataSource.getHistoricalData();
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
        LineData data = new LineData();

        // dataset 1
        String[] mapKeys = Arrays.copyOf(hashMap.keySet().toArray(), hashMap.keySet().toArray().length, String[].class);
        ArrayList<Entry> entries = new ArrayList<>();
        for(int i=0; i<mapKeys.length; i++) {
            entries.add(new Entry(i,hashMap.get(mapKeys[i])));
        }
        LineDataSet dataset = new LineDataSet(entries, "zachorowania");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setColor(Color.WHITE);
        dataset.setCircleColor(Color.TRANSPARENT);
        dataset.setCircleHoleColor(Color.WHITE);
        dataset.setHighlightEnabled(false);
        data.addDataSet(dataset);

        // dataset 2
        ArrayList<Entry> entries2 = new ArrayList<>();
        for(int i=0; i<mapKeys.length; i++) {
            entries2.add(new Entry(i,100*i));
        }
        LineDataSet dataset2 = new LineDataSet(entries2, "zgony");
        dataset2.setValueTextColor(Color.WHITE);
        dataset2.setColor(Color.RED);
        dataset2.setCircleColor(Color.TRANSPARENT);
        dataset2.setCircleHoleColor(Color.RED);
        dataset2.setHighlightEnabled(false);
        data.addDataSet(dataset2);

        // dataset 2
        ArrayList<Entry> entries3 = new ArrayList<>();
        for(int i=0; i<mapKeys.length; i++) {
            entries3.add(new Entry(i,20+150*i));
        }
        LineDataSet dataset3 = new LineDataSet(entries3, "wyleczenia");
        dataset3.setValueTextColor(Color.WHITE);
        dataset3.setColor(Color.GREEN);
        dataset3.setCircleColor(Color.TRANSPARENT);
        dataset3.setCircleHoleColor(Color.GREEN);
        dataset3.setHighlightEnabled(false);
        data.addDataSet(dataset3);

        data.setDrawValues(false);
        return data;
    }

    private BarData hashMapToBarData(HashMap<String,Integer> hashMap){

        BarData data = new BarData();
        data.setBarWidth(0.25f);

        // dataset 1
        String[] mapKeys = Arrays.copyOf(hashMap.keySet().toArray(), hashMap.keySet().toArray().length, String[].class);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<mapKeys.length; i++) {
            entries.add(new BarEntry(i, hashMap.get(mapKeys[i])));
        }
        BarDataSet dataset = new BarDataSet(entries, "zachorowania");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setColor(Color.WHITE);
        dataset.setHighlightEnabled(false);
        data.addDataSet(dataset);

        // dataset 2
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        for(int i=0; i<mapKeys.length; i++) {
            entries2.add(new BarEntry(i, (float)(5*i)));
        }
        BarDataSet dataset2 = new BarDataSet(entries2, "zgony");
        dataset2.setValueTextColor(Color.WHITE);
        dataset2.setColor(Color.RED);
        dataset2.setHighlightEnabled(false);
        data.addDataSet(dataset2);

        // dataset 3
        ArrayList<BarEntry> entries3 = new ArrayList<>();
        for(int i=0; i<mapKeys.length; i++) {
            entries3.add(new BarEntry(i, (float)(2*i)));
        }
        BarDataSet dataset3 = new BarDataSet(entries3, "wyleczenia");
        dataset3.setValueTextColor(Color.WHITE);
        dataset3.setColor(Color.GREEN);
        dataset3.setHighlightEnabled(false);
        data.addDataSet(dataset3);

        data.setDrawValues(false);
        return data;
    }
}
