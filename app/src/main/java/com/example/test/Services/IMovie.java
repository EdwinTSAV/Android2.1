package com.example.test.Services;

import com.example.test.Class.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IMovie {
    @GET("/Movie")
    Call<List<Movie>> getAll();

    @GET("/Movie/{id}")
    Call<Movie> getMovieId(@Path("id") int id);

    @POST("/Movie")
    Call<Movie> create(@Body Movie movie);

    @PUT("/Movie/{id}")
    Call<Movie> update(@Path("id") int id, @Body Movie movie);

    @DELETE("/Movie/{id}")
    Call<Void> delete(@Path("id") int id);
}
