package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.test.Adapters.MovieAdapter;
import com.example.test.Class.Movie;
import com.example.test.DataBase.AppDataBase;
import com.example.test.Services.IMovie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class listmovie_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmovie);

        AppDataBase db = AppDataBase.getInstance(this);

        RecyclerView rv = findViewById(R.id.rvMovies);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listmovie_activity.this,newmovieActivity.class);
                startActivity(intent);
            }
        });

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://637427ec08104a9c5f7b28eb.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        IMovie iMovie = retrofit.create(IMovie.class);
        Call<List<Movie>> movies = iMovie.getAll();

        movies.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.code() == 200){
                    Log.i("CONEXION","OK");
                    List<Movie> movieList = response.body();
                    rv.setAdapter(new MovieAdapter(movieList));
                }else{
                    Log.i("CONEXION","BAD");
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.i("CONEXION","FALLO EN EL SERVIDOR");
                AppDataBase db = AppDataBase.getInstance(getApplicationContext());
                List<Movie> movie = db.iMovieDao().getAll();
                rv.setAdapter(new MovieAdapter(movie));
                Log.i("CONEXION", movie.toString());
            }
        });
    }
}