package com.dandrzas.covid19poland.data.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Covid19HistoricalData {
    private HashMap<String, Integer> historyCasesAll;
    private HashMap<String, Integer> historyCasesDaily;
    private HashMap<String, Integer> historyDeathsAll;
    private HashMap<String, Integer> historyDeathsDaily;
    private HashMap<String, Integer> historyCuredAll;
    private HashMap<String, Integer> historyCuredDaily;
    private HashMap<String, Integer> historyActiveAll;

    public HashMap<String, Integer> getHistoryCasesAll() {
        return historyCasesAll;
    }

    public void setHistoryCasesAll(HashMap<String, Integer> historyCasesAll) {
        historyCasesDaily = calcDailyData(historyCasesAll);
        this.historyCasesAll = historyCasesAll;

        if ((historyDeathsAll!=null)&&(historyCuredAll!=null)){
            historyActiveAll = calcActiveCases(historyCasesAll, historyDeathsAll, historyCuredAll);
        }
    }

    public HashMap<String, Integer> getHistoryCasesDaily() {
        return historyCasesDaily;
    }

    public HashMap<String, Integer> getHistoryDeathsAll() {
        return historyDeathsAll;
    }

    public void setHistoryDeathsAll(HashMap<String, Integer> historyDeathsAll) {
        historyDeathsDaily = calcDailyData(historyDeathsAll);
        this.historyDeathsAll = historyDeathsAll;
        if ((historyCasesAll!=null)&&(historyCuredAll!=null)){
            historyActiveAll = calcActiveCases(historyCasesAll, historyDeathsAll, historyCuredAll);
        }
    }

    public HashMap<String, Integer> getHistoryDeathsDaily() {
        return historyDeathsDaily;
    }

    public HashMap<String, Integer> getHistoryCuredAll() {
        return historyCuredAll;
    }

    public void setHistoryCuredAll(HashMap<String, Integer> historyCuredAll) {
        historyCuredDaily = calcDailyData(historyCuredAll);
        this.historyCuredAll = historyCuredAll;
        if ((historyCasesAll!=null)&&(historyDeathsAll!=null)){
            historyActiveAll = calcActiveCases(historyCasesAll, historyDeathsAll, historyCuredAll);
        }
    }

    public HashMap<String, Integer> getHistoryCuredDaily() {
        return historyCuredDaily;
    }

    public HashMap<String, Integer> getHistoryActiveAll() {
        return historyActiveAll;
    }

    private HashMap<String, Integer> calcDailyData(HashMap<String, Integer> historyCasesAll){
        HashMap<String, Integer> dailyCases = new LinkedHashMap<>();
        String[] keysDailyCases = Arrays.copyOf(historyCasesAll.keySet().toArray(), historyCasesAll.keySet().toArray().length, String[].class);

        // dailyCases.put(keysDailyCases[0], 0);
        for(int i=0; i<keysDailyCases.length-1; i++){
            int dailyCase = historyCasesAll.get(keysDailyCases[i+1]) - historyCasesAll.get(keysDailyCases[i]) ;
            dailyCases.put(keysDailyCases[i+1], dailyCase);
        }
        return dailyCases;
    }

    private HashMap<String, Integer> calcActiveCases(HashMap<String,Integer> historyCasesAll, HashMap<String,Integer> historyDeathsAll, HashMap<String,Integer> historyCuredAll){
        HashMap<String, Integer> historyActiveAll = new LinkedHashMap<>();
        String[] keys = Arrays.copyOf(historyCasesAll.keySet().toArray(), historyCasesAll.keySet().toArray().length, String[].class);

        for(int i=0; i<keys.length; i++){
            int dailyActiveCase = historyCasesAll.get(keys[i]) - historyDeathsAll.get(keys[i]) - historyCuredAll.get(keys[i]);
            historyActiveAll.put(keys[i], dailyActiveCase);
        }
        return historyActiveAll;
    }

}
