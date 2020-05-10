package com.dandrzas.covid19poland.ui.historicaldatafragment.presenter;

import android.graphics.Color;

import com.dandrzas.covid19poland.data.domain.Covid19HistoricalData;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSourceIF;
import com.dandrzas.covid19poland.ui.historicaldatafragment.view.HistoricalDataIF;
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


public class HistoricalDataPresenter implements HistoricalDataPresenterIF {
    private HistoricalDataIF view;
    private RemoteDataSourceIF remoteDataSource;

    public HistoricalDataPresenter(HistoricalDataIF view, RemoteDataSourceIF remoteDataSource) {
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

                            LineData lineData = hashMapToLineData(ccvid19HistoricalData.getHistoryCasesAll(), ccvid19HistoricalData.getHistoryDeathsAll(), ccvid19HistoricalData.getHistoryCuredAll(), ccvid19HistoricalData.getHistoryActiveAll());
                            BarData barData = hashMapToBarData(ccvid19HistoricalData.getHistoryCasesDaily(), ccvid19HistoricalData.getHistoryDeathsDaily(), ccvid19HistoricalData.getHistoryCuredDaily());
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

                LineData lineData = hashMapToLineData(data.getHistoryCasesAll(), data.getHistoryDeathsAll(), data.getHistoryCuredAll(), data.getHistoryActiveAll());
                BarData barData = hashMapToBarData(data.getHistoryCasesDaily(), data.getHistoryDeathsDaily(), data.getHistoryCuredDaily());
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

    private LineData hashMapToLineData(HashMap<String,Integer> hashMapCases, HashMap<String,Integer> hashMapDeaths, HashMap<String,Integer> hashMapCured, HashMap<String,Integer> hashMapActiveCases){
        LineData data = new LineData();

        // dataset active cases
        String[] mapKeys0 = Arrays.copyOf(hashMapActiveCases.keySet().toArray(), hashMapActiveCases.keySet().toArray().length, String[].class);
        ArrayList<Entry> entries0 = new ArrayList<>();
        for(int i=0; i<mapKeys0.length; i++) {
            entries0.add(new Entry(i,hashMapActiveCases.get(mapKeys0[i])));
        }
        LineDataSet dataset0 = new LineDataSet(entries0, "aktywne");
        dataset0.setValueTextColor(Color.WHITE);
        dataset0.setColor(Color.YELLOW);
        dataset0.setCircleColor(Color.TRANSPARENT);
        dataset0.setCircleHoleColor(Color.YELLOW);
        dataset0.setHighlightEnabled(true);
        data.addDataSet(dataset0);

        // dataset all cases
        String[] mapKeys1 = Arrays.copyOf(hashMapCases.keySet().toArray(), hashMapCases.keySet().toArray().length, String[].class);
        ArrayList<Entry> entries1 = new ArrayList<>();
        for(int i = 0; i< mapKeys1.length; i++) {
            entries1.add(new Entry(i,hashMapCases.get(mapKeys1[i])));
        }
        LineDataSet dataset1 = new LineDataSet(entries1, "zakażenia");
        dataset1.setValueTextColor(Color.WHITE);
        dataset1.setColor(Color.WHITE);
        dataset1.setCircleColor(Color.TRANSPARENT);
        dataset1.setCircleHoleColor(Color.WHITE);
        dataset1.setHighlightEnabled(true);
        data.addDataSet(dataset1);

        // dataset deaths
        String[] mapKeys2 = Arrays.copyOf(hashMapDeaths.keySet().toArray(), hashMapDeaths.keySet().toArray().length, String[].class);
        ArrayList<Entry> entries2 = new ArrayList<>();
        for(int i=0; i<mapKeys2.length; i++) {
            entries2.add(new Entry(i,hashMapDeaths.get(mapKeys2[i])));
        }
        LineDataSet dataset2 = new LineDataSet(entries2, "śmiertelne");
        dataset2.setValueTextColor(Color.WHITE);
        dataset2.setColor(Color.RED);
        dataset2.setCircleColor(Color.TRANSPARENT);
        dataset2.setCircleHoleColor(Color.RED);
        dataset2.setHighlightEnabled(true);
        data.addDataSet(dataset2);

        // dataset cured cases
        String[] mapKeys3 = Arrays.copyOf(hashMapCured.keySet().toArray(), hashMapCured.keySet().toArray().length, String[].class);
        ArrayList<Entry> entries3 = new ArrayList<>();
        for(int i=0; i<mapKeys3.length; i++) {
            entries3.add(new Entry(i,hashMapCured.get(mapKeys3[i])));
        }
        LineDataSet dataset3 = new LineDataSet(entries3, "wyleczone");
        dataset3.setValueTextColor(Color.WHITE);
        dataset3.setColor(Color.GREEN);
        dataset3.setCircleColor(Color.TRANSPARENT);
        dataset3.setCircleHoleColor(Color.GREEN);
        dataset3.setHighlightEnabled(true);
        data.addDataSet(dataset3);

        data.setDrawValues(false);
        return data;
    }

    private BarData hashMapToBarData(HashMap<String,Integer> hashMapCases,HashMap<String,Integer> hashMapDeaths, HashMap<String,Integer> hashMapCured){

        BarData data = new BarData();
        data.setBarWidth(0.3f);

        // dataset daily cases
        String[] mapKeys = Arrays.copyOf(hashMapCases.keySet().toArray(), hashMapCases.keySet().toArray().length, String[].class);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<mapKeys.length; i++) {
            entries.add(new BarEntry(i, hashMapCases.get(mapKeys[i])));
        }
        BarDataSet dataset = new BarDataSet(entries, "zakażenia");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setColor(Color.WHITE);
        dataset.setHighlightEnabled(true);
        data.addDataSet(dataset);

        // dataset daily deaths
        String[] mapKeys2 = Arrays.copyOf(hashMapDeaths.keySet().toArray(), hashMapDeaths.keySet().toArray().length, String[].class);
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        for(int i=0; i<mapKeys2.length; i++) {
            entries2.add(new BarEntry(i, hashMapDeaths.get(mapKeys2[i])));
        }
        BarDataSet dataset2 = new BarDataSet(entries2, "śmiertelne");
        dataset2.setValueTextColor(Color.WHITE);
        dataset2.setColor(Color.RED);
        dataset2.setHighlightEnabled(true);
        data.addDataSet(dataset2);

        // dataset daily cured cases
        String[] mapKeys3 = Arrays.copyOf(hashMapCured.keySet().toArray(), hashMapCured.keySet().toArray().length, String[].class);
        ArrayList<BarEntry> entries3 = new ArrayList<>();
        for(int i=0; i<mapKeys3.length; i++) {
            entries3.add(new BarEntry(i, hashMapCured.get(mapKeys3[i])));
        }
        BarDataSet dataset3 = new BarDataSet(entries3, "wyleczone");
        dataset3.setValueTextColor(Color.WHITE);
        dataset3.setColor(Color.GREEN);
        dataset3.setHighlightEnabled(true);
        data.addDataSet(dataset3);

        data.setDrawValues(false);
        return data;
    }
}
