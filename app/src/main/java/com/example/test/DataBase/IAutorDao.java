package com.example.test.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.Class.Autor;

import java.util.List;

@Dao
public interface IAutorDao {
    @Query("SELECT * FROM Autor")
    List<Autor> getAll();

    @Query("SELECT * FROM Autor WHERE id = :id")
    Autor getAutor(int id);

    @Insert
    void insert(Autor autor);

    @Update
    void update(Autor autor);

    @Delete
    void delete(Autor autor);
}
