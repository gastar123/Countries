package com.example.countries;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;

@Dao
public interface CountryDao {

    @Query("SELECT * FROM Country")
    List<Country> getAll();

    @Query("SELECT * FROM Country WHERE countryCode = :id")
    Country getById(long id);

    @Insert
    Completable insert(List<Country> countries);

    @Update
    Completable update(Country country);

    @Delete
    Completable delete(Country country);

}
