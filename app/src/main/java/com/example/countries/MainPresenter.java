package com.example.countries;

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

    public Observable<List<Country>> getCountries(final String url) {
        return Observable.create(emitter -> {
            List<Country> countryList = countryDao.getAll();
            if (countryList != null && !countryList.isEmpty())   {
                emitter.onNext(countryList);
            } else {
                try {
                    emitter.onNext(downloadFromInternet(url));
                } catch (IOException e) {
                    emitter.onError(new Exception("Отсутствует соединение с интернетом", e));
                } finally {
                    emitter.onComplete();
                }
            }
        });
    }

    public List<Country> downloadFromInternet(String url) throws IOException {
        List<Country> countryList = new ArrayList<>();
        Document doc;
        doc = Jsoup.connect(url).get();
        Elements countries = doc.select("body > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td:nth-child(2) > table > tbody > tr:nth-child(5) > td > table > tbody");
        boolean firstRow = true;
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
            Glide.with(view).downloadOnly().load(country.getFlag());
            countryList.add(country);
        }
        countryDao.insert(countryList);
        return countryList;
    }
}
