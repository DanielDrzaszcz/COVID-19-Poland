package com.dandrzas.covid19poland.data.domain;

import org.junit.Test;
import java.util.Date;


import static org.junit.Assert.*;

public class Covid19TodayDataTest {
    private Covid19TodayData data = new Covid19TodayData();
    private final int TEST_ALL_CASES = 1344;
    private final int TEST_TODAY_CASES = 122;
    private final int TEST_ACTIVE_CASES = 1200;
    private final int TEST_ALL_CURED = 31;
    private final int TEST_ALL_DEATHS = 43;
    private final int TEST_TODAY_DEATHS = 5;
    private final long DATE_TIMESTAMP = 1586985399859L;

    @Test
    public void setAndGetTest() {

        // Set the data
        data.setCasesAll(TEST_ALL_CASES);
        data.setCasesToday(TEST_TODAY_CASES);
        data.setCasesActive(TEST_ACTIVE_CASES);
        data.setCuredAll(TEST_ALL_CURED);
        data.setDeathsAll(TEST_ALL_DEATHS);
        data.setDeathsToday(TEST_TODAY_DEATHS);
        data.setUpdatedTime(new Date(DATE_TIMESTAMP));

        // Verify
        assertEquals(TEST_ALL_CASES, data.getCasesAll());
        assertEquals(TEST_TODAY_CASES, data.getCasesToday());
        assertEquals(TEST_ACTIVE_CASES, data.getCasesActive());
        assertEquals(TEST_ALL_CURED, data.getCuredAll());
        assertEquals(TEST_ALL_DEATHS, data.getDeathsAll());
        assertEquals(TEST_TODAY_DEATHS, data.getDeathsToday());
        assertEquals(DATE_TIMESTAMP, data.getUpdatedTime().getTime());

    }
}