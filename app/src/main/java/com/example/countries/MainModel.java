package com.example.countries;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainModel {

    private MainPresenter mainPresenter;
    private MainActivity view;

    public MainModel(MainPresenter mainPresenter, MainActivity view) {
        this.mainPresenter = mainPresenter;
        this.view = view;
    }

    public void observer() {
        mainPresenter.getCountries("https://fxtop.com/ru/countries-currencies.php")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Country>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Country> countries) {
                        view.createCountryAdapter(countries);
                        Toast.makeText(view, "Загружено стран: " + countries.size(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("My error!!!!!!!!!!!!!", e.getMessage(), e);
                        Toast.makeText(view, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
