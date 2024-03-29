package com.example.countries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class Model {

    private Context context;
    private Presenter presenter;
    private CountryDao countryDao;

    public Model(Context context) {
        this.context = context;
        AppDatabase db = App.getInstance().getDatabase();
        countryDao = db.countryDao();
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public Observable<String> downloadCountries(final String url) {
        return Observable.create(emitter -> {
            List<Country> countryList = countryDao.getAll();
            if (countryList != null && !countryList.isEmpty()) {
                emitter.onNext("");
            } else {
                try {
                    downloadFromInternet(url);
                    emitter.onNext("");
                } catch (IOException e) {
                    emitter.onError(new Exception("Отсутствует соединение с интернетом", e));
                } finally {
                    emitter.onComplete();
                }
            }
        });
    }

    public List<Country> downloadFromInternet(String url) throws IOException, ExecutionException, InterruptedException {
        List<Country> countryList = new ArrayList<>();
        Document doc;
        doc = Jsoup.connect(url).get();
        Elements countries = doc.select("body > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td:nth-child(2) > table > tbody > tr:nth-child(5) > td > table > tbody");
        boolean firstRow = true;
        List<Future<Drawable>> futureList = new ArrayList<>();
        RequestManager requestManager = Glide.with(context);
        for (Element countryRow : countries.select("tr")) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            Country country = new Country();
            country.setCountryCode(countryRow.select("td:nth-child(6)").text());
            country.setName(countryRow.select("td:nth-child(1) > a").text());
            country.setCurrency(countryRow.select("td:nth-child(3) > a").text());
            country.setFlag("https://fxtop.com" + countryRow.select("td:nth-child(5) > img").attr("src"));

            // Добавляем изображжения для загрузки (кэширование без отображения)
            futureList.add(requestManager.load(country.getFlag()).submit());
            countryList.add(country);
        }
        // Пока все изображения не загрузятся дальше не продолжаем
        for (Future<Drawable> f : futureList) {
            f.get();
        }
        countryDao.insert(countryList);
        return countryList;
    }

    public Observable<List<Country>> getCountries() {
        return Observable.create(emitter -> {
            List<Country> countryList = countryDao.getAll();
            emitter.onNext(countryList);
        });
    }

    @SuppressLint("CheckResult")
    public void deleteDataBase() {
        countryDao.delete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::deleteGlideCache, e -> presenter.onError(e));
    }

    private void deleteGlideCache() {
        new Thread(() -> {
            Glide glide = Glide.get(context);
            glide.clearDiskCache();
        }).start();
    }
}
