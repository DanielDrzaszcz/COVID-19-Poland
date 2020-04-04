package com.dandrzas.covid19poland.ui.countersfragment.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dandrzas.covid19poland.R;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.countersfragment.presenter.MainPresenter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;


public class CountersFragment extends Fragment implements CountersFragmentIF {

    private MainPresenter presenter;
    @BindViews({R.id.text_view_all_cases_counter, R.id.text_view_today_cases_counter, R.id.text_view_all_cured_counter, R.id.text_view_all_deaths_counter, R.id.text_view_today_deaths_counter})
    List<TextView> textViewsCounters;
    @BindViews({R.id.progress_bar_1, R.id.progress_bar_2, R.id.progress_bar_3, R.id.progress_bar_4, R.id.progress_bar_5})
    List<ProgressBar> progressBars;
    private float coutersTextSize;
    @BindView(R.id.counters_fragment) View countersFragmentView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_counters, container, false);
        ButterKnife.bind(this, view);

        coutersTextSize = textViewsCounters.get(0).getTextSize();
        countersFragmentView.setOnTouchListener(new TouchRefreshDataListener());


        presenter = new MainPresenter(this, RemoteDataSource.getInstance());
        presenter.refreshData(checkInternetConnection(), Schedulers.newThread());

        return view;
    }

    @OnClick(R.id.fab)
    public void fabClick() {
        presenter.refreshData(checkInternetConnection(), Schedulers.newThread());

    }

    @Override
    public void setCountersData(ArrayList<String> countersData) {
        textViewsCounters.get(2).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorGreen, null));
        for(String counterData : countersData){
            textViewsCounters.get(countersData.indexOf(counterData)).setTextSize(TypedValue.COMPLEX_UNIT_PX, coutersTextSize);
            textViewsCounters.get(countersData.indexOf(counterData)).setText(counterData);
        }
    }

    @Override
    public void clearCountersData() {
        for (TextView counterData : textViewsCounters) {
            textViewsCounters.get(textViewsCounters.indexOf(counterData)).setText("");
        }
    }

    @Override
    public void setCountersError() {
        textViewsCounters.get(2).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorRed, null));
        for (TextView counterData : textViewsCounters) {
            textViewsCounters.get(textViewsCounters.indexOf(counterData)).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (coutersTextSize * 0.4));
            textViewsCounters.get(textViewsCounters.indexOf(counterData)).setText(R.string.download_error);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.connection_dialog_message)
                .setTitle(R.string.connection_dialog_title);
        builder.create().show();
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
