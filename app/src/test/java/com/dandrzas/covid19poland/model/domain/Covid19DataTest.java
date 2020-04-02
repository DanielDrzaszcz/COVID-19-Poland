package com.dandrzas.covid19poland.model.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Covid19DataTest {
    private Covid19Data data = new Covid19Data();
    private final int TEST_ALL_CASES = 1;
    private final int TEST_TODAY_CASES = 2;
    private final int TEST_ALL_CURED = 3;
    private final int TEST_ALL_DEATHS = 4;
    private final int TEST_TODAY_DEATHS = 5;


    @Test
    public void setAndGetTest() {

        // Set the data
        data.setCasesAll(TEST_ALL_CASES);
        data.setCasesToday(TEST_TODAY_CASES);
        data.setCuredAll(TEST_ALL_CURED);
        data.setDeathsAll(TEST_ALL_DEATHS);
        data.setDeathsToday(TEST_TODAY_DEATHS);

        // Verify
        assertEquals(TEST_ALL_CASES, data.getCasesAll());
        assertEquals(TEST_TODAY_CASES, data.getCasesToday());
        assertEquals(TEST_ALL_CURED, data.getCuredAll());
        assertEquals(TEST_ALL_DEATHS, data.getDeathsAll());
        assertEquals(TEST_TODAY_DEATHS, data.getDeathsToday());
    }
}