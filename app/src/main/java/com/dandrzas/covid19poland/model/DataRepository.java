package com.dandrzas.covid19poland.model;

public class DataRepository implements DataRepositoryIF{
    private static final DataRepository ourInstance = new DataRepository();
    private int[] covid19Data = new int[5];

    public static DataRepository getInstance() {
        return ourInstance;
    }

    private DataRepository() {
    }

    @Override
    public int[] getData() {
        return new int[5];
    }
}
