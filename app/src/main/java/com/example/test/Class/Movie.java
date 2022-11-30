package com.example.test.Class;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movie", foreignKeys = @ForeignKey(entity = Autor.class, parentColumns = "id", childColumns = "autorid"))
public class Movie {
    @ColumnInfo(name = "titulo")
    private String titulo;
    @ColumnInfo(name = "sinopsis")
    private String sinopsis;
    @ColumnInfo(name = "imagen")
    private String imagen;

    @ColumnInfo(name = "autorid")
    private int autorid;
    //private Autor autorid;

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getAutorid() {
        return autorid;
    }

    public void setAutorid(int autorid) {
        this.autorid = autorid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
