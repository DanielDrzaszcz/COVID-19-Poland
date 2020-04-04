package com.dandrzas.covid19poland.data.domain;

public class Covid19Data {

    private int casesAll;
    private int casesToday;
    private int curedAll;
    private int deathsAll;
    private int deathsToday;

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

}
