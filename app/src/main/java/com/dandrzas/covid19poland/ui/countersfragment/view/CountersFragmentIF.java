package com.dandrzas.covid19poland.ui.countersfragment.view;

import java.util.ArrayList;

public interface CountersFragmentIF {
    void setCountersData(ArrayList<String> countersData);
    void clearCountersData();
    void setCountersError();
    void setProgressBarsVisibility(boolean visibilityEnable);
    void showConnectionAlert();
}
