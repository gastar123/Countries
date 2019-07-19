package com.example.countries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMain;
    private CountryAdapter countryAdapter;
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        Model model = new Model(this);
        presenter = new Presenter(model);
        presenter.setView(this);
        presenter.observer(this::createCountryAdapter);
    }

    private void createCountryAdapter(List<Country> countryList) {
        rvMain = findViewById(R.id.rvMain);
        rvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        countryAdapter = new CountryAdapter(this, countryList);
        countryAdapter.setOnItemClickListener(country -> presenter.openCountry(country));
        rvMain.setAdapter(countryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 2, 0, "delete data");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.deleteCountries();
        countryAdapter.changeData();
        return super.onOptionsItemSelected(item);
    }

    public interface AdapterCreator {
        void setCountryList(List<Country> countryList);
    }
}
