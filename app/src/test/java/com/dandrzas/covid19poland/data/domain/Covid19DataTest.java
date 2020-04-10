package com.dandrzas.covid19poland.data.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

public class Covid19DataTest {
    private Covid19Data data = new Covid19Data();
    private final int TEST_ALL_CASES = 1;
    private final int TEST_TODAY_CASES = 2;
    private final int TEST_ALL_CURED = 3;
    private final int TEST_ALL_DEATHS = 4;
    private final int TEST_TODAY_DEATHS = 5;
    private final String[] dates = {"4/01/20", "4/02/20", "4/03/20", "4/04/20", "4/05/20", "4/06/20", "4/07/20", "4/08/20", "4/09/20"};
    private final Integer[] casesAll= {2554, 2946, 3383, 3627, 4102, 4413, 4848, 5205, 5575};
    private final Integer[] casesDaily = {0, 392, 437, 244, 475, 311, 435, 357, 370};
    private HashMap<String, Integer> casesHistoryAll = new LinkedHashMap<>();

    @Test
    public void setAndGetTest() {

        // Set the data
        data.setCasesAll(TEST_ALL_CASES);
        data.setCasesToday(TEST_TODAY_CASES);
        data.setCuredAll(TEST_ALL_CURED);
        data.setDeathsAll(TEST_ALL_DEATHS);
        data.setDeathsToday(TEST_TODAY_DEATHS);
        for(int i=0; i<casesAll.length; i++){
            casesHistoryAll.put(dates[i], casesAll[i]);
        }
        data.setHistoryCasesAll(casesHistoryAll);

        // Verify
        assertEquals(TEST_ALL_CASES, data.getCasesAll());
        assertEquals(TEST_TODAY_CASES, data.getCasesToday());
        assertEquals(TEST_ALL_CURED, data.getCuredAll());
        assertEquals(TEST_ALL_DEATHS, data.getDeathsAll());
        assertEquals(TEST_TODAY_DEATHS, data.getDeathsToday());
        Integer [] dataCasesDaily = Arrays.copyOf(data.getHistoryCasesDaily().values().toArray(), data.getHistoryCasesDaily().values().toArray().length, Integer[].class);

        assertArrayEquals(casesDaily, dataCasesDaily);
    }
}