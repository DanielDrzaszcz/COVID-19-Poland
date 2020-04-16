package com.dandrzas.covid19poland.data.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Covid19TodayData {

    private int casesAll;
    private int casesActive;
    private int casesToday;
    private int curedAll;
    private int deathsAll;
    private int deathsToday;
    private Date updatedTime;

    public Covid19TodayData() {
    }

    public int getCasesAll() {
        return casesAll;
    }

    public void setCasesAll(int casesAll) {
        this.casesAll = casesAll;
    }

    public int getCasesActive() {
        return casesActive;
    }

    public void setCasesActive(int casesActive) {
        this.casesActive = casesActive;
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

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
