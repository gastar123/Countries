package com.example.countries;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        Model model = new Model(this);
        presenter = new Presenter(model);
        presenter.setView(this);

        presenter.toMainActivity();
    }
}
