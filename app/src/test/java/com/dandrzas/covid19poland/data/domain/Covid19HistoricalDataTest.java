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
    private HashMap<String, Integer> casesHistoryAll = new LinkedHashMap<>();

    @Test
    public void setAndGetTest(){

        // Set the data
        for(int i=0; i<casesAll.length; i++){
            casesHistoryAll.put(datesAll[i], casesAll[i]);
        }
        data.setHistoryCasesAll(casesHistoryAll);

        // Verify
        assertArrayEquals(datesAll, data.getHistoryCasesAll().keySet().toArray());
        assertArrayEquals(datesDaily, data.getHistoryCasesDaily().keySet().toArray());

        Integer [] dataCasesAll = Arrays.copyOf(data.getHistoryCasesAll().values().toArray(), data.getHistoryCasesAll().values().toArray().length, Integer[].class);
        Integer [] dataCasesDaily = Arrays.copyOf(data.getHistoryCasesDaily().values().toArray(), data.getHistoryCasesDaily().values().toArray().length, Integer[].class);
        assertArrayEquals(casesDaily, dataCasesDaily);
        assertArrayEquals(casesAll, dataCasesAll);
    }
}
