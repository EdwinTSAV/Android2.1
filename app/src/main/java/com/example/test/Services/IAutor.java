package com.example.test.Services;

import com.example.test.Class.Autor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAutor {
    @GET("/Autor")
    Call<List<Autor>> getAll();

    @GET("/Autor/{id}")
    Call<Autor> getMovieId(@Path("id") int id);

    @POST("/Autor")
    Call<Autor> create(@Body Autor autor);

    @PUT("/Autor/{id}")
    Call<Autor> update(@Path("id") int id, @Body Autor autor);

    @DELETE("/Autor/{id}")
    Call<Void> delete(@Path("id") int id);
}
