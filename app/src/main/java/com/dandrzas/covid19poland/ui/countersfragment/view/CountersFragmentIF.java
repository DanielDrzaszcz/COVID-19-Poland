package com.dandrzas.covid19poland.ui.countersfragment.view;

import java.util.ArrayList;
import java.util.Date;

public interface CountersFragmentIF {
    void setCountersData(ArrayList<String> countersData, Date updateTime);
    void clearCountersData();
    void setCountersError();
    void setProgressBarsVisibility(boolean visibilityEnable);
    void showConnectionAlert();
}
