package com.dandrzas.covid19poland.view;

import java.util.ArrayList;

public interface MainActivityIF {
     void setCountersData(ArrayList<String> countersData);
     void clearCountersData();
     void setCountersError();
     void setProgressBarsVisibility(boolean visibilityEnable);
     void showConnectionAlert();
}
