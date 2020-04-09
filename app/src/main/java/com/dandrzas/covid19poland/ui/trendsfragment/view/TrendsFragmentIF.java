package com.dandrzas.covid19poland.ui.trendsfragment.view;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;

public interface TrendsFragmentIF {

    void setChartsData(BarData barChartData, LineData lineChartData, String[] labelsData);
    void setChartsVisibility(boolean visibilityEnable);
    void setErrorVisibility(boolean visibilityEnable);
    void setProgressBarsVisibility(boolean visibilityEnable);
    void showConnectionAlert();

}
