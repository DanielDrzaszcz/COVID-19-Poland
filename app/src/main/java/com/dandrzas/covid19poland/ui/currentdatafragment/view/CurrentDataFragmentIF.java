package com.dandrzas.covid19poland.ui.currentdatafragment.view;

import java.util.ArrayList;
import java.util.Date;

public interface CurrentDataFragmentIF {
    void setCountersData(ArrayList<Integer> countersData, Date updateTime);
    void setCountersDataAnimated(ArrayList<Integer> countersData, Date updateTime);
    void clearCountersData();
    void setCountersError();
    void setProgressBarsVisibility(boolean visibilityEnable);
    void showConnectionAlert();
}
