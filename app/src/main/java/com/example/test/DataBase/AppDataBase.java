package com.example.test.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.test.Class.Autor;
import com.example.test.Class.Movie;

@Database(entities = {Movie.class, Autor.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract IMovieDao iMovieDao();
    public abstract IAutorDao iAutorDao();

    public static AppDataBase getInstance(Context context){
        return Room.databaseBuilder(context,AppDataBase.class,"Examen")
                .allowMainThreadQueries()
                .build();
    }
}
