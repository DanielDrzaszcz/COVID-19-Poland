package com.dandrzas.covid19poland.presenter;

import com.dandrzas.covid19poland.view.MainActivityIF;

public class MainPresenter implements MainPresenterIF {
    private MainActivityIF view;

    public MainPresenter(MainActivityIF view) {
        this.view = view;
    }

    @Override
    public void refreshData() {
    }

}
