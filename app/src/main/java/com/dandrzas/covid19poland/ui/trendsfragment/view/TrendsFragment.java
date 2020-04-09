package com.dandrzas.covid19poland.ui.trendsfragment.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dandrzas.covid19poland.R;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.trendsfragment.presenter.TrendsPresenter;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrendsFragment extends Fragment implements TrendsFragmentIF {

    private TrendsPresenter presenter;
    private Map<String, Integer> casesHistoryMap = new LinkedHashMap<>();
    private String[] mapKeys;

    @BindView(R.id.chart2)
    BarChart mpaBarChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trends, container, false);
        ButterKnife.bind(this, view);


        presenter = new TrendsPresenter(this, RemoteDataSource.getInstance());
        casesHistoryMap.put("3/25/20", 150);
        casesHistoryMap.put("3/26/20", 170);
        casesHistoryMap.put("3/27/20", 168);
        casesHistoryMap.put("3/28/20", 249);
        casesHistoryMap.put("3/29/20", 224);
        casesHistoryMap.put("3/30/20", 193);
        casesHistoryMap.put("3/31/20", 256);
        casesHistoryMap.put("4/1/20", 243);
        casesHistoryMap.put("4/2/20", 392);
        casesHistoryMap.put("4/3/20", 437);
        casesHistoryMap.put("4/4/20", 244);
        casesHistoryMap.put("4/5/20", 475);
        casesHistoryMap.put("4/6/20", 311);
        casesHistoryMap.put("4/7/20", 435);
        casesHistoryMap.put("4/8/20", 357);


        mapKeys = Arrays.copyOf(casesHistoryMap.keySet().toArray(), casesHistoryMap.keySet().toArray().length, String[].class);
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i=0; i<mapKeys.length; i++) {
            entries.add(new BarEntry(i,casesHistoryMap.get(mapKeys[i])));
        }

        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setValueTextColor(Color.WHITE);
        dataset.setColor(Color.RED);
        dataset.setHighlightEnabled(false);


        BarData data = new BarData(dataset);
        mpaBarChart.setData(data);
        mpaBarChart.setBorderColor(Color.WHITE);
        mpaBarChart.setNoDataTextColor(Color.WHITE);
        mpaBarChart.setPinchZoom(false);
//
        mpaBarChart.moveViewToX(mpaBarChart.getData().getXMax()-10);

        mpaBarChart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
        mpaBarChart.getAxisRight().setTextColor(Color.WHITE);
        mpaBarChart.getDescription().setEnabled(false);
        mpaBarChart.getXAxis().setDrawLabels(true);
        mpaBarChart.getXAxis().setDrawAxisLine(true);
        mpaBarChart.getXAxis().setLabelRotationAngle(270);
        mpaBarChart.getXAxis().setValueFormatter(new ChartXAxisLabelsFormatter());
        mpaBarChart.getXAxis().setTextColor(Color.WHITE);
        mpaBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mpaBarChart.getXAxis().setGranularity(1f);
        mpaBarChart.getLegend().setEnabled(false);
        mpaBarChart.invalidate();

        return view;
    }

    class ChartXAxisLabelsFormatter extends ValueFormatter{
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return mapKeys[(int)value];
        }
    }

}
