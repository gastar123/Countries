package com.example.countries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CountryActivity extends AppCompatActivity {

    private Country country;
    private ImageView ivFlag;
    private TextView tvName;
    private TextView tvCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        country = (Country) getIntent().getSerializableExtra("country");

        ivFlag = findViewById(R.id.ivFlag);
        tvName = findViewById(R.id.tvName);
        tvCurrency = findViewById(R.id.tvCurrency);

        Glide.with(this).load(country.getFlag()).onlyRetrieveFromCache(true).override(App.px, App.px).into(ivFlag);
        tvName.setText(country.getName());
        tvCurrency.setText(country.getCurrency());
    }
}
