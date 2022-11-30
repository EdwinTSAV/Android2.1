package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.test.Adapters.AutorAdapter;
import com.example.test.Class.Autor;
import com.example.test.DataBase.AppDataBase;
import com.example.test.Services.IAutor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class listautorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listautor);

        RecyclerView rv = findViewById(R.id.rvAutores);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listautorActivity.this,newautorActivity.class);
                startActivity(intent);
            }
        });

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://637427ec08104a9c5f7b28eb.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IAutor iAutor = retrofit.create(IAutor.class);
        Call<List<Autor>> autor = iAutor.getAll();

        autor.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(Call<List<Autor>> call, Response<List<Autor>> response) {
                if (response.code() == 200){
                    Log.i("CONEXION","OK");
                    List<Autor> autorList = response.body();
                    rv.setAdapter(new AutorAdapter(autorList));
                }else{
                    Log.i("CONEXION","BAD");
                }
            }
            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {
                Log.i("CONEXION","FALLO EN EL SERVIDOR");
                AppDataBase db = AppDataBase.getInstance(getApplicationContext());
                List<Autor> autor = db.iAutorDao().getAll();
                rv.setAdapter(new AutorAdapter(autor));
                Log.i("CONEXION", autor.toString());
            }
        });
    }
}