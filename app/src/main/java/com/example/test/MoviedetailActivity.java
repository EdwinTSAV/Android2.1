package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.Class.Movie;
import com.example.test.DataBase.AppDataBase;
import com.example.test.Services.IMovie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviedetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetail);

        TextView titulo = findViewById(R.id.titulo);
        TextView sinopsis = findViewById(R.id.sinopsis);
        TextView autor = findViewById(R.id.autor);
        ImageView imagen = findViewById(R.id.imagen);
        FloatingActionButton eliminar = findViewById(R.id.delete);
        FloatingActionButton editar = findViewById(R.id.edit);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://637427ec08104a9c5f7b28eb.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IMovie iMovie = retrofit.create(IMovie.class);
        Call<Movie> movie = iMovie.getMovieId(id);

        movie.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.code() == 200){
                    Log.i("CONEXION","OK");
                    Movie movieDetail = response.body();
                    titulo.setText(movieDetail.getTitulo());
                    sinopsis.setText(movieDetail.getSinopsis());
                    //autor.setText(movieDetail.getAutorid().getNombre());
                    Picasso.get().load(movieDetail.getImagen()).into(imagen);
                }else{
                    Log.i("CONEXION","BAD");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.i("CONEXION","FALLO EN EL SERVIDOR");
                AppDataBase db = AppDataBase.getInstance(getApplicationContext());
                Movie movie = db.iMovieDao().getMovie(id);
                titulo.setText(movie.getTitulo());
                sinopsis.setText(movie.getSinopsis());
                Picasso.get().load(movie.getImagen()).into(imagen);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoviedetailActivity.this,EditmovieActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://637427ec08104a9c5f7b28eb.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                IMovie iMovie = retrofit.create(IMovie.class);
                Call<Void> call = iMovie.delete(id);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200){
                            Log.i("CONEXION","OK");
                            AppDataBase db = AppDataBase.getInstance(getApplicationContext());
                            Movie movie = db.iMovieDao().getMovie(id);
                            db.iMovieDao().delete(movie);
                        }else{
                            Log.i("CONEXION","BAD");
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.i("CONEXION","FALLO EN EL SERVIDOR");
                        AppDataBase db = AppDataBase.getInstance(getApplicationContext());
                        Movie movie = db.iMovieDao().getMovie(id);
                        db.iMovieDao().delete(movie);
                    }
                });

                Intent intent = new Intent(MoviedetailActivity.this,listmovie_activity.class);
                startActivity(intent);
            }
        });
    }
}