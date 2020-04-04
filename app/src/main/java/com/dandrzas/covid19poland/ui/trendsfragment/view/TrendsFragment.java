package com.dandrzas.covid19poland.ui.trendsfragment.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dandrzas.covid19poland.R;
import com.dandrzas.covid19poland.data.remotedatasource.RemoteDataSource;
import com.dandrzas.covid19poland.ui.trendsfragment.presenter.MainPresenter;

import butterknife.ButterKnife;


public class TrendsFragment extends Fragment implements TrendsFragmentIF {

    private MainPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trends, container, false);
        ButterKnife.bind(this, view);


        presenter = new MainPresenter(this, RemoteDataSource.getInstance());

        return view;
    }

}
