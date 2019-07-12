package com.example.countries;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Country.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CountryDao countryDao();
}
