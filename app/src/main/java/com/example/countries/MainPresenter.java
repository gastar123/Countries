package com.example.countries;

import android.content.Intent;

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

import static android.content.Context.MODE_PRIVATE;

public class MainPresenter {

    private MainActivity view;
    CountryDao countryDao;

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
}
