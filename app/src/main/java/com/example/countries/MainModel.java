package com.example.countries;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainModel {

    private MainActivity view;

    public MainModel(MainActivity view) {
        this.view = view;
    }

    public void observer() {
        new MainPresenter(view).getCountries("https://fxtop.com/ru/countries-currencies.php")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Country>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Country> countries) {
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
