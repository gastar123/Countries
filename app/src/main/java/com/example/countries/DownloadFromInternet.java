package com.example.countries;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import androidx.annotation.Nullable;
import io.reactivex.Observable;

public class DownloadFromInternet {

    private SplashActivity splashActivity;
    CountryDao countryDao;

    public DownloadFromInternet(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
        AppDatabase db = App.getInstance().getDatabase();
        countryDao = db.countryDao();
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

    public List<Country> downloadFromInternet(String url) throws IOException, InterruptedException {
        List<Country> countryList = new ArrayList<>();
        Document doc;
        doc = Jsoup.connect(url).get();
        Elements countries = doc.select("body > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td:nth-child(2) > table > tbody > tr:nth-child(5) > td > table > tbody");
        boolean firstRow = true;
        Elements elements = countries.select("tr");
        CountDownLatch countDownLatch = new CountDownLatch(elements.size() - 1);
        for (Element countryRow : elements) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            Country country = new Country();
            country.setCountryCode(countryRow.select("td:nth-child(6)").text());
            country.setName(countryRow.select("td:nth-child(1) > a").text());
            country.setCurrency(countryRow.select("td:nth-child(3) > a").text());
            country.setFlag("https://fxtop.com" + countryRow.select("td:nth-child(5) > img").attr("src"));
            Glide.with(splashActivity).load(country.getFlag()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    countDownLatch.countDown();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    countDownLatch.countDown();
                    return false;
                }
            }).preload();
            countryList.add(country);
        }
        countryDao.insert(countryList);
        countDownLatch.await();
        return countryList;
    }
}
