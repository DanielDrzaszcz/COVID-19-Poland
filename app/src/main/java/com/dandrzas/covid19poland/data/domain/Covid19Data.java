package com.dandrzas.covid19poland.data.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Covid19Data {

    private int casesAll;
    private int casesToday;
    private int curedAll;
    private int deathsAll;
    private int deathsToday;
    private HashMap<String, Integer> historyCasesAll;
    private HashMap<String, Integer> historyCasesDaily;

    public Covid19Data() {
    }

    public int getCasesAll() {
        return casesAll;
    }

    public void setCasesAll(int casesAll) {
        this.casesAll = casesAll;
    }

    public int getCasesToday() {
        return casesToday;
    }

    public void setCasesToday(int casesToday) {
        this.casesToday = casesToday;
    }

    public int getCuredAll() {
        return curedAll;
    }

    public void setCuredAll(int curedAll) {
        this.curedAll = curedAll;
    }

    public int getDeathsAll() {
        return deathsAll;
    }

    public void setDeathsAll(int deathsAll) {
        this.deathsAll = deathsAll;
    }

    public int getDeathsToday() {
        return deathsToday;
    }

    public void setDeathsToday(int deathsToday) {
        this.deathsToday = deathsToday;
    }

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

        dailyCases.put(keysDailyCases[0], 0);
        for(int i=1; i<keysDailyCases.length; i++){
            int dailyCase = historyCasesAll.get(keysDailyCases[i]) - historyCasesAll.get(keysDailyCases[i-1]);
                    dailyCases.put(keysDailyCases[i], dailyCase);
    }
        return dailyCases;
    }
}
