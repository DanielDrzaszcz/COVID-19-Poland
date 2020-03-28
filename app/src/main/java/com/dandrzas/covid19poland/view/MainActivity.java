package com.dandrzas.covid19poland.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.dandrzas.covid19poland.R;
import com.dandrzas.covid19poland.remotedatasource.NetworkHandler;
import com.dandrzas.covid19poland.presenter.MainPresenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainActivityIF {
    private MainPresenter presenter;
    @BindViews({R.id.text_view_all_cases_counter, R.id.text_view_today_cases_counter, R.id.text_view_all_cured_counter, R.id.text_view_all_deaths_counter, R.id.text_view_today_deaths_counter})
    List<TextView> textViewsCounters;
    @BindViews({R.id.progress_bar_1, R.id.progress_bar_2, R.id.progress_bar_3, R.id.progress_bar_4, R.id.progress_bar_5})
    List<ProgressBar> progressBars;
    private float coutersTextSize;
    @BindView(R.id.main_activity_content) View activityView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        for (ProgressBar progressBar : progressBars) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        coutersTextSize = textViewsCounters.get(0).getTextSize();
        activityView.setOnTouchListener(new TouchRefreshDataListener());

        NetworkHandler.createInstance(this);
        presenter = new MainPresenter(this);
        presenter.refreshData(checkInternetConnection());

    }

    @OnClick(R.id.fab)
    public void fabClick() {
        presenter.refreshData(checkInternetConnection());
    }

    @Override
    public void setCountersData(String[] countersData) {
        textViewsCounters.get(2).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGreen, null));
        for (int i = 0; i < textViewsCounters.size(); i++) {
            if (i < countersData.length) {
                textViewsCounters.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX, coutersTextSize);
                textViewsCounters.get(i).setText(countersData[i]);
            }
        }
    }

    @Override
    public void clearCountersData() {
        for (int i = 0; i < textViewsCounters.size(); i++) {
            textViewsCounters.get(i).setText("");
        }
    }

    @Override
    public void setCountersError() {
        textViewsCounters.get(2).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorRed, null));
        for (int i = 0; i < textViewsCounters.size(); i++) {
            textViewsCounters.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (coutersTextSize * 0.4));
            textViewsCounters.get(i).setText(R.string.download_error);
        }
    }

    @Override
    public void setProgressBarsVisibility(boolean visibilityEnable) {
        for (ProgressBar progressBar : progressBars) {
            if (visibilityEnable) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showConnectionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.connection_dialog_message)
                .setTitle(R.string.connection_dialog_title);
        builder.create().show();
    }

    private boolean checkInternetConnection(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                presenter.refreshData(checkInternetConnection());
            }

            return true;
        }
    }

}
