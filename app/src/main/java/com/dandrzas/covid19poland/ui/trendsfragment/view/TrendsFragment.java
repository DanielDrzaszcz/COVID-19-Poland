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
import com.dandrzas.covid19poland.ui.trendsfragment.presenter.TrendsPresenter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

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
        XAxis xAxis = chartLine.getXAxis();
        YAxis yAxisLeft = chartLine.getAxisLeft();
        YAxis yAxisRight = chartLine.getAxisRight();
        Legend legend = chartLine.getLegend();
        Description description = chartLine.getDescription();

        // date labels config
        xAxis.setValueFormatter(new ValueFormatter(){
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labelsData[(int)value];
            }
        } );
        xAxis.setLabelRotationAngle(270);

        // legend config
        legend.setEnabled(true);
        legend.setTextColor(Color.WHITE);

        // chart view config
        chartLine.setData(lineChartData);
        chartLine.setBorderColor(Color.WHITE);
        chartLine.setNoDataTextColor(Color.WHITE);
        chartLine.setPinchZoom(false);
        CustomMarkerView mv = new CustomMarkerView(getContext(), new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labelsData[(int)value];
            }
        });
        mv.setChartView(chartLine); // For bounds control
        chartLine.setMarker(mv); // Set the marker to the chart

        // x axis config
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // y axes config
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setTextColor(Color.WHITE);
        description.setEnabled(false);

    }

    private void chartBarConfig(BarData barChartData, String[] labelsDate){
        float groupSpace = 0.1f;
        float barSpace = 0f;
        XAxis xAxis = chartBar.getXAxis();
        YAxis yAxisLeft = chartBar.getAxisLeft();
        YAxis yAxisRight = chartBar.getAxisRight();
        Legend legend = chartBar.getLegend();
        Description description = chartBar.getDescription();

        // date labels config
        xAxis.setValueFormatter(new ValueFormatter(){
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if((value>=0)&&(value<labelsDate.length)){
                    return labelsDate[(int)value];
                }else{
                    return labelsDate[0];
                }
            }
        } );
        xAxis.setLabelRotationAngle(270);

        // legend config
        legend.setEnabled(true);
        legend.setTextColor(Color.WHITE);

        // chart view config
        chartBar.setData(barChartData);
        chartBar.setBorderColor(Color.WHITE);
        chartBar.setNoDataTextColor(Color.WHITE);
        chartBar.setPinchZoom(false);
        chartBar.groupBars(0f, groupSpace, barSpace);
        CustomMarkerView mv2 = new CustomMarkerView(getContext(), new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labelsDate[(int)value];
            }
        });
        mv2.setChartView(chartBar); // For bounds control
        chartBar.setMarker(mv2); // Set the marker to the chart

        // x axis config
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum((labelsDate.length*chartBar.getBarData().getGroupWidth(groupSpace, barSpace)));
        xAxis.setCenterAxisLabels(true);

        // y axes config
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setDrawTopYLabelEntry(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisRight.setTextColor(Color.WHITE);
        yAxisRight.setAxisMinimum(0f);


        // description config
        description.setEnabled(false);
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



    public class CustomMarkerView extends MarkerView {

        private final TextView tvContent;
        private final ValueFormatter xAxisValueFormatter;

        public CustomMarkerView(Context context, ValueFormatter xAxisValueFormatter) {
            super(context, R.layout.custom_marker_view);

            this.xAxisValueFormatter = xAxisValueFormatter;
            tvContent = findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX()) + "\n" + (int)e.getY());
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }

}
