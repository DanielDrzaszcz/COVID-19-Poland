package com.dandrzas.covid19poland.view;

public interface MainActivityIF {
     void setCountersData(String[] countersData);
     void clearCountersData();
     void setCountersError();
     void setProgressBarsVisibility(boolean visibilityEnable);
     void showConnectionAlert();
}
