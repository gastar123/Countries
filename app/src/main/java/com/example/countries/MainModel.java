package com.example.countries;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainModel {

    private MainPresenter mainPresenter;
    private MainActivity view;

    public MainModel(MainPresenter mainPresenter, MainActivity view) {
        this.mainPresenter = mainPresenter;
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void observer() {
        mainPresenter.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNext, this::onError);
//                .subscribe(countries -> onNext(countries), e -> onError(e));  // То же самое
    }

    private void onNext(List<Country> countries) {
        view.createCountryAdapter(countries);
        Toast.makeText(view, "Загружено стран: " + countries.size(), Toast.LENGTH_SHORT).show();
    }

    private void onError(Throwable e) {
        Log.e("My error!!!!!!!!!!!!!", e.getMessage(), e);
        Toast.makeText(view, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
