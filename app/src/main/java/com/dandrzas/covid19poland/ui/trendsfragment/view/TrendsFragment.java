package com.dandrzas.covid19poland.ui.trendsfragment.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dandrzas.covid19poland.R;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.trendsfragment.presenter.TrendsPresenter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class TrendsFragment extends Fragment implements TrendsFragmentIF {

    private TrendsPresenter presenter;

    @BindView(R.id.chart_line)
    LineChart chartLine;

    @BindView(R.id.chart_bar)
    BarChart chartBar;

    @BindViews({R.id.textView_trend_line_error, R.id.textView_trend_bar_error})
    List<TextView> textViewsError;

    @BindViews({R.id.progress_bar_trend_line, R.id.progress_bar_trend_bar})
    List<ProgressBar> progressBars;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trends, container, false);
        ButterKnife.bind(this, view);
        presenter = new TrendsPresenter(this, RemoteDataSource.getInstance());

        return view;
    }

    @Override
    public void setChartsData(BarData barChartData, LineData lineChartData, String[] labelsData) {
        chartBarConfig(barChartData, labelsData);
        lineBarConfig(lineChartData, labelsData);
    }

    @Override
    public void setChartsVisibility(boolean visibilityEnable) {
       if(visibilityEnable){
           chartLine.setVisibility(View.VISIBLE);
       }
       else{
           chartLine.setVisibility(View.INVISIBLE);
       }
    }

    @Override
    public void setErrorVisibility(boolean visibilityEnable) {
        if(visibilityEnable){
            textViewsError.get(0).setVisibility(View.VISIBLE);
            textViewsError.get(1).setVisibility(View.VISIBLE);
        }
        else{
            textViewsError.get(0).setVisibility(View.INVISIBLE);
            textViewsError.get(1).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setProgressBarsVisibility(boolean visibilityEnable) {
        if(visibilityEnable){
            progressBars.get(0).setVisibility(View.VISIBLE);
            progressBars.get(1).setVisibility(View.VISIBLE);
        }
        else{
            progressBars.get(0).setVisibility(View.INVISIBLE);
            progressBars.get(1).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showConnectionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.connection_dialog_message)
                .setTitle(R.string.connection_dialog_title);
        builder.create().show();
    }


    private void lineBarConfig(LineData lineChartData, String[] labelsData){

        chartLine.setData(lineChartData);
        chartLine.setBorderColor(Color.WHITE);
        chartLine.setNoDataTextColor(Color.WHITE);
        chartLine.setPinchZoom(false);
        chartLine.moveViewToX(chartLine.getData().getXMax()-10);

        chartLine.getAxisLeft().setTextColor(Color.WHITE);
        chartLine.getAxisRight().setTextColor(Color.WHITE);
        chartLine.getDescription().setEnabled(false);
        chartLine.getXAxis().setDrawLabels(true);
        chartLine.getXAxis().setDrawAxisLine(true);
        chartLine.getXAxis().setLabelRotationAngle(270);
        chartLine.getXAxis().setValueFormatter(new ValueFormatter(){
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labelsData[(int)value];
            }
        } );
        chartLine.getXAxis().setTextColor(Color.WHITE);
        chartLine.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartLine.getXAxis().setGranularity(1f);
        chartLine.getLegend().setEnabled(false);
        chartLine.invalidate();
    }

    private void chartBarConfig(BarData barChartData, String[] labelsDate){

        chartBar.setData(barChartData);
        chartBar.setBorderColor(Color.WHITE);
        chartBar.setNoDataTextColor(Color.WHITE);
        chartBar.setPinchZoom(false);
        chartBar.moveViewToX(chartBar.getData().getXMax()-10);
        chartBar.getAxisLeft().setTextColor(Color.WHITE);
        chartBar.getAxisRight().setTextColor(Color.WHITE);
        chartBar.getDescription().setEnabled(false);
        chartBar.getXAxis().setDrawLabels(true);
        chartBar.getXAxis().setDrawAxisLine(true);
        chartBar.getXAxis().setLabelRotationAngle(270);
        chartLine.getXAxis().setValueFormatter(new ValueFormatter(){
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labelsDate[(int)value];
            }
        } );        chartBar.getXAxis().setTextColor(Color.WHITE);
        chartBar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartBar.getXAxis().setGranularity(1f);
        chartBar.getLegend().setEnabled(false);
        chartBar.invalidate();
    }

}
