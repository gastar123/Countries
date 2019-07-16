package com.example.countries;

import android.app.Application;
import android.util.TypedValue;

import androidx.room.Room;

public class App extends Application {

    public static App instance;
    public static int px;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();

        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
