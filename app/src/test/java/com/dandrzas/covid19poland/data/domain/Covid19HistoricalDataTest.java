package com.dandrzas.covid19poland.data.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertArrayEquals;

public class Covid19HistoricalDataTest {
    private Covid19HistoricalData data = new Covid19HistoricalData();
    private final String[] datesAll = {"4/01/20", "4/02/20", "4/03/20", "4/04/20", "4/05/20", "4/06/20", "4/07/20", "4/08/20", "4/09/20"};
    private final String[] datesDaily = {"4/02/20", "4/03/20", "4/04/20", "4/05/20", "4/06/20", "4/07/20", "4/08/20", "4/09/20"};
    private final Integer[] casesAll= {2554, 2946, 3383, 3627, 4102, 4413, 4848, 5205, 5575};
    private final Integer[] casesDaily = {392, 437, 244, 475, 311, 435, 357, 370};
    private final Integer[] deathsAll= {79, 94, 107, 129, 159, 174, 181, 208, 232};
    private final Integer[] deathsDaily = {15, 13, 22, 30, 15, 7, 27, 24};
    private final Integer[] curedAll= {116, 134, 162, 191, 222, 284, 318, 375, 439};
    private final Integer[] curedDaily = {18, 28, 29, 31, 62, 34, 57, 64};
    private final Integer[] activeAll = {2359, 2718, 3114, 3307, 3721, 3955, 4349, 4622, 4904};
    private HashMap<String, Integer> casesHistoryAll = new LinkedHashMap<>();
    private HashMap<String, Integer> deathsHistoryAll = new LinkedHashMap<>();
    private HashMap<String, Integer> curedHistoryAll = new LinkedHashMap<>();

    @Test
    public void setAndGetTest(){

        // Set the data
        for(int i=0; i<casesAll.length; i++){
            casesHistoryAll.put(datesAll[i], casesAll[i]);
        }
        data.setHistoryCasesAll(casesHistoryAll);

        for(int i=0; i<deathsAll.length; i++){
            deathsHistoryAll.put(datesAll[i], deathsAll[i]);
        }
        data.setHistoryDeathsAll(deathsHistoryAll);

        for(int i=0; i<curedAll.length; i++){
            curedHistoryAll.put(datesAll[i], curedAll[i]);
        }
        data.setHistoryCuredAll(curedHistoryAll);

        // Verify
        assertArrayEquals(datesAll, data.getHistoryCasesAll().keySet().toArray());
        assertArrayEquals(datesDaily, data.getHistoryCasesDaily().keySet().toArray());
        assertArrayEquals(datesAll, data.getHistoryDeathsAll().keySet().toArray());
        assertArrayEquals(datesDaily, data.getHistoryDeathsDaily().keySet().toArray());
        assertArrayEquals(datesAll, data.getHistoryCuredAll().keySet().toArray());
        assertArrayEquals(datesDaily, data.getHistoryCuredDaily().keySet().toArray());

        Integer [] dataCasesAll = Arrays.copyOf(data.getHistoryCasesAll().values().toArray(), data.getHistoryCasesAll().values().toArray().length, Integer[].class);
        Integer [] dataCasesDaily = Arrays.copyOf(data.getHistoryCasesDaily().values().toArray(), data.getHistoryCasesDaily().values().toArray().length, Integer[].class);
        assertArrayEquals(casesDaily, dataCasesDaily);
        assertArrayEquals(casesAll, dataCasesAll);

        Integer [] dataDeathsAll = Arrays.copyOf(data.getHistoryDeathsAll().values().toArray(), data.getHistoryDeathsAll().values().toArray().length, Integer[].class);
        Integer [] dataDeathsDaily = Arrays.copyOf(data.getHistoryDeathsDaily().values().toArray(), data.getHistoryDeathsDaily().values().toArray().length, Integer[].class);
        assertArrayEquals(deathsDaily, dataDeathsDaily);
        assertArrayEquals(deathsAll, dataDeathsAll);

        Integer [] dataCuredAll = Arrays.copyOf(data.getHistoryCuredAll().values().toArray(), data.getHistoryCuredAll().values().toArray().length, Integer[].class);
        Integer [] dataCuredDaily = Arrays.copyOf(data.getHistoryCuredDaily().values().toArray(), data.getHistoryCuredDaily().values().toArray().length, Integer[].class);
        assertArrayEquals(curedDaily, dataCuredDaily);
        assertArrayEquals(curedAll, dataCuredAll);

        Integer [] dataActiveAll = Arrays.copyOf(data.getHistoryActiveAll().values().toArray(), data.getHistoryActiveAll().values().toArray().length, Integer[].class);
        assertArrayEquals(activeAll, dataActiveAll);
    }
}
