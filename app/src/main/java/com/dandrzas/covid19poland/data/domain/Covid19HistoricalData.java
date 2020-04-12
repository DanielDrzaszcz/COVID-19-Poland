package com.dandrzas.covid19poland.data.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Covid19HistoricalData {
    private HashMap<String, Integer> historyCasesAll;
    private HashMap<String, Integer> historyCasesDaily;

    public HashMap<String, Integer> getHistoryCasesAll() {
        return historyCasesAll;
    }

    public void setHistoryCasesAll(HashMap<String, Integer> historyCasesAll) {
        historyCasesDaily = calcCasesDaily(historyCasesAll);
        this.historyCasesAll = historyCasesAll;
    }

    public HashMap<String, Integer> getHistoryCasesDaily() {
        return historyCasesDaily;
    }

    private HashMap<String, Integer> calcCasesDaily(HashMap<String, Integer> historyCasesAll){
        HashMap<String, Integer> dailyCases = new LinkedHashMap<>();
        String[] keysDailyCases = Arrays.copyOf(historyCasesAll.keySet().toArray(), historyCasesAll.keySet().toArray().length, String[].class);

        // dailyCases.put(keysDailyCases[0], 0);
        for(int i=0; i<keysDailyCases.length-1; i++){
            int dailyCase = historyCasesAll.get(keysDailyCases[i+1]) - historyCasesAll.get(keysDailyCases[i]) ;
            dailyCases.put(keysDailyCases[i+1], dailyCase);
        }
        return dailyCases;
    }
}
