package com.example.countries;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Presenter {

    private Activity view;
    private Model model;

    public Presenter(Model model) {
        this.model = model;
        model.setPresenter(this);
    }

    public void setView(Activity view) {
        this.view = view;
    }

    @SuppressLint("CheckResult")
    public void toMainActivity() {
        model.downloadCountries("https://fxtop.com/ru/countries-currencies.php")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> toNextScreen(), this::onError);
    }

    private void toNextScreen() {
        Intent intent = new Intent(view, MainActivity.class);
        view.startActivity(intent);
        view.finish();
    }

    @SuppressLint("CheckResult")
    public void observer(MainActivity.AdapterCreator adapterCreator) {
        model.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countries -> onNext(countries, adapterCreator), this::onError);
//                .subscribe(countries -> onNext(countries), e -> onError(e));  // То же самое
    }

    private void onNext(List<Country> countries, MainActivity.AdapterCreator adapterCreator) {
        adapterCreator.setCountryList(countries);
        Toast.makeText(view, "Загружено стран: " + countries.size(), Toast.LENGTH_SHORT).show();
    }

    public void onError(Throwable e) {
        Log.e("My error!!!!!!!!!!!!!", e.getMessage(), e);
        Toast.makeText(view, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void openCountry(Country country) {
        Intent intent = new Intent(view, CountryActivity.class);
        intent.putExtra("country", country);
        view.startActivity(intent);
    }

    public void deleteCountries() {
        model.deleteDataBase();
    }
}
