package com.example.countries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMain;
    private CountryAdapter countryAdapter;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mainPresenter = new MainPresenter(this);
        new MainModel(mainPresenter,this).observer();
    }


    public void createCountryAdapter(List<Country> countryList) {
        rvMain = findViewById(R.id.rvMain);
        rvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        countryAdapter = new CountryAdapter(this, countryList);
        countryAdapter.setOnItemClickListener(country -> mainPresenter.openCountry(country));
        rvMain.setAdapter(countryAdapter);
    }
}
