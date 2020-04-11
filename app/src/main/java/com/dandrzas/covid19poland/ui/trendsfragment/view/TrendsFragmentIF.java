package com.dandrzas.covid19poland.ui.trendsfragment.view;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;

public interface TrendsFragmentIF {

    void setLineChartData(LineData lineChartData, String[] labelsData);
    void setBarChartData(BarData barChartData, String[] labelsData);
    void setChartsVisibility(boolean visibilityEnable);
    void setErrorVisibility(boolean visibilityEnable);
    void setProgressBarsVisibility(boolean visibilityEnable);
    void showConnectionAlert();

}
