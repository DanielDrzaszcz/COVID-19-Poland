package com.dandrzas.covid19poland.data.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class Covid19TodayDataTest {
    private Covid19TodayData data = new Covid19TodayData();
    private final int TEST_ALL_CASES = 1;
    private final int TEST_TODAY_CASES = 2;
    private final int TEST_ALL_CURED = 3;
    private final int TEST_ALL_DEATHS = 4;
    private final int TEST_TODAY_DEATHS = 5;
    private final long DATE_TIMESTAMP = 1586985399859L;

    @Test
    public void setAndGetTest() {

        // Set the data
        data.setCasesAll(TEST_ALL_CASES);
        data.setCasesToday(TEST_TODAY_CASES);
        data.setCuredAll(TEST_ALL_CURED);
        data.setDeathsAll(TEST_ALL_DEATHS);
        data.setDeathsToday(TEST_TODAY_DEATHS);
        data.setUpdatedTime(new Date(DATE_TIMESTAMP));

        // Verify
        assertEquals(TEST_ALL_CASES, data.getCasesAll());
        assertEquals(TEST_TODAY_CASES, data.getCasesToday());
        assertEquals(TEST_ALL_CURED, data.getCuredAll());
        assertEquals(TEST_ALL_DEATHS, data.getDeathsAll());
        assertEquals(TEST_TODAY_DEATHS, data.getDeathsToday());
        assertEquals(DATE_TIMESTAMP, data.getUpdatedTime().getTime());

    }
}