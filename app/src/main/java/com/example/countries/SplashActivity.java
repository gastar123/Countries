package com.example.countries;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @SuppressLint("CheckResult")
    private void init() {
        new DownloadFromInternet(this).downloadCountries("https://fxtop.com/ru/countries-currencies.php")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> toNextScreen(), this::onError);
    }

    private void toNextScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void onError(Throwable e) {
        Log.e("My error!!!!!!!!!!!!!", e.getMessage(), e);
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
