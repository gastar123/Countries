package com.example.countries;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.FutureTarget;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class MainPresenter {

    private MainActivity view;
    private CountryDao countryDao;

    public MainPresenter(MainActivity view) {
        this.view = view;
        AppDatabase db = App.getInstance().getDatabase();
        countryDao = db.countryDao();
    }

    public Observable<List<Country>> getCountries() {
        return Observable.create(emitter -> {
            List<Country> countryList = countryDao.getAll();
            emitter.onNext(countryList);
        });
    }

    public void openCountry(Country country) {
        Intent intent = new Intent(view, CountryActivity.class);
        intent.putExtra("country", country);
        view.startActivity(intent);
    }

    @SuppressLint("CheckResult")
    public void deleteDataBase() {
        countryDao.delete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Glide glide = Glide.get(view);
                                glide.clearDiskCache();
                            }
                        }).start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        Log.e("My error!!!!!!!!!!!!!", e.getMessage(), e);
                        Toast.makeText(view, "Произошла ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
