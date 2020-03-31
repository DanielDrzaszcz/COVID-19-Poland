package com.dandrzas.covid19poland.view;

import java.util.List;

public interface MainActivityIF {
     void setCountersData(List<String> countersData);
     void clearCountersData();
     void setCountersError();
     void setProgressBarsVisibility(boolean visibilityEnable);
     void showConnectionAlert();
}
