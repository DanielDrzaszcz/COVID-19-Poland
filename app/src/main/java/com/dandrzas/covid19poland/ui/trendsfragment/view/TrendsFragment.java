package com.dandrzas.covid19poland.ui.trendsfragment.view;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dandrzas.covid19poland.R;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.countersfragment.view.CountersFragment;
import com.dandrzas.covid19poland.ui.trendsfragment.presenter.TrendsPresenter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;


public class TrendsFragment extends Fragment implements TrendsFragmentIF {

    private TrendsPresenter presenter;

    @BindView(R.id.chart_line)
    LineChart chartLine;

    @BindView(R.id.chart_bar) BarChart chartBar;

    @BindViews({R.id.textView_trend_line_error, R.id.textView_trend_bar_error})
    List<TextView> textViewsError;

    @BindViews({R.id.progress_bar_trend_line, R.id.progress_bar_trend_bar})
    List<ProgressBar> progressBars;

    @BindView(R.id.trends_fragment)
    View trendsFragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trends, container, false);
        ButterKnife.bind(this, view);
        trendsFragmentView.setOnTouchListener(new TouchRefreshDataListener());
        presenter = new TrendsPresenter(this, RemoteDataSource.getInstance());
        presenter.initData(checkInternetConnection(), Schedulers.newThread());

        return view;
    }


    @OnClick(R.id.fab)
    public void fabClick() {
        presenter.refreshData(checkInternetConnection(), Schedulers.newThread());

    }

    @Override
    public void setLineChartData(LineData lineChartData, String[] labelsData) {
        lineBarConfig(lineChartData, labelsData);
    }

    @Override
    public void setBarChartData(BarData barChartData, String[] labelsData) {
        chartBarConfig(barChartData, labelsData);
    }

    @Override
    public void setLineChartDataAnimated(LineData lineChartData, String[] labelsData) {
        lineBarConfig(lineChartData, labelsData);
        chartLine.animateY(1000);
    }

    @Override
    public void setBarChartDataAnimated(BarData barChartData, String[] labelsData) {
        chartBarConfig(barChartData, labelsData);
        chartBar.animateY(1000);
    }

    @Override
    public void setChartsVisibility(boolean visibilityEnable) {
       if(visibilityEnable){
           chartLine.setVisibility(View.VISIBLE);
           chartBar.setVisibility(View.VISIBLE);

       }
       else{
           chartLine.setVisibility(View.INVISIBLE);
           chartBar.setVisibility(View.INVISIBLE);
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

        chartLine.getXAxis().setValueFormatter(new ValueFormatter(){
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labelsData[(int)value];
            }
        } );
        chartLine.getXAxis().setLabelRotationAngle(270);
        chartLine.getLegend().setEnabled(true);
        chartLine.getLegend().setTextColor(Color.WHITE);
        chartLine.setData(lineChartData);
        chartLine.setBorderColor(Color.WHITE);
        chartLine.setNoDataTextColor(Color.WHITE);
        chartLine.setPinchZoom(false);
        chartLine.getAxis(YAxis.AxisDependency.LEFT).setAxisMinimum(0f);
        chartLine.getAxisLeft().setTextColor(Color.WHITE);
        chartLine.getAxisRight().setTextColor(Color.WHITE);
        chartLine.getDescription().setEnabled(false);
        chartLine.getXAxis().setDrawLabels(true);
        chartLine.getXAxis().setDrawAxisLine(true);
        chartLine.getXAxis().setTextColor(Color.WHITE);
        chartLine.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartLine.getXAxis().setGranularity(1f);
    }

    private void chartBarConfig(BarData barChartData, String[] labelsDate){
        float groupSpace = 0.25f;
        float barSpace = 0f;

        chartBar.getXAxis().setValueFormatter(new ValueFormatter(){
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if((value>=0)&&(value<labelsDate.length)){
                    return labelsDate[(int)value];
                }else{
                    return labelsDate[0];
                }
            }
        } );
        chartBar.getXAxis().setLabelRotationAngle(270);
        chartBar.getLegend().setEnabled(true);
        chartBar.getLegend().setTextColor(Color.WHITE);
        chartBar.setData(barChartData);
        chartBar.setBorderColor(Color.WHITE);
        chartBar.setNoDataTextColor(Color.WHITE);
        chartBar.setPinchZoom(false);
        chartBar.moveViewToX(chartBar.getData().getXMax()-10);
        chartBar.getAxisLeft().setTextColor(Color.WHITE);
        chartBar.getAxisLeft().setDrawTopYLabelEntry(false);
        chartBar.getAxisLeft().setAxisMinimum(0f);
        chartBar.getAxisRight().setTextColor(Color.WHITE);
        chartBar.getAxisRight().setAxisMinimum(0f);
        chartBar.getDescription().setEnabled(false);
        chartBar.getXAxis().setDrawLabels(true);
        chartBar.getXAxis().setDrawAxisLine(true);
        chartBar.getXAxis().setTextColor(Color.WHITE);
        chartBar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartBar.getXAxis().setGranularity(1f);
        chartBar.getXAxis().setAxisMinimum(0f);
        chartBar.getXAxis().setAxisMaximum((labelsDate.length*chartBar.getBarData().getGroupWidth(groupSpace, barSpace)));
        chartBar.getXAxis().setCenterAxisLabels(true);
        chartBar.groupBars(0f, groupSpace, barSpace);

    }

    private boolean checkInternetConnection(){
        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    class TouchRefreshDataListener implements View.OnTouchListener{
        float startY;
        float startX;
        boolean moveDone;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startX = event.getX();
                startY = event.getY();
                moveDone = false;
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                moveDone = true;
            }
            if((event.getAction()== MotionEvent.ACTION_UP)&&(moveDone)&&((Math.abs(event.getY()-startY))>100)&&((Math.abs(event.getX()-startX))<200)){
                presenter.refreshData(checkInternetConnection(), Schedulers.newThread());
            }
            return true;
        }
    }

}
