package com.example.test.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.Class.Autor;
import com.example.test.Class.Movie;

import java.util.List;

@Dao
public interface IMovieDao {
    @Query("SELECT * FROM Movie")
    List<Movie> getAll();

    @Query("SELECT * FROM Movie WHERE id = :id")
    Movie getMovie(int id);

    @Insert
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);
}
