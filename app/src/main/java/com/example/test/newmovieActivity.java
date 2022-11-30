package com.example.test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.test.Adapters.AutorAdapter;
import com.example.test.Class.Autor;
import com.example.test.Class.Imagen;
import com.example.test.Class.ImgBase64;
import com.example.test.Class.Movie;
import com.example.test.DataBase.AppDataBase;
import com.example.test.Services.IAutor;
import com.example.test.Services.IImage;
import com.example.test.Services.IMovie;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class newmovieActivity extends AppCompatActivity {

    ImageButton buttonBase64;
    public String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmovie);

        EditText titulo = findViewById(R.id.titulo);
        EditText sinopsis = findViewById(R.id.sinopsis);
        buttonBase64 = (ImageButton) findViewById(R.id.textBase64);
        Button crear = findViewById(R.id.newMovie);
        Spinner spinner = findViewById(R.id.acAutores);

        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("ELIGUE UN AUTOR");

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
                    for (Autor autorA : autorList) {
                        tipos.add(autorA.getNombre());
                    }
                }else{
                    Log.i("CONEXION","BAD");
                }
            }
            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {
                Log.i("CONEXION","FALLO EN EL SERVIDOR");
            }
        });

        ArrayAdapter adapterArray = new ArrayAdapter(newmovieActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,tipos);

        spinner.setAdapter(adapterArray);

        /* //esto solo sirve para cuando seleccionas una opcion xde
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String elemento = (String) spinner.getAdapter().getItem(i);
                Toast.makeText(getApplicationContext(), "Tipo" + elemento, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MAIN_APP", "2 link " + link);
                String tituloM = titulo.getText().toString().trim();
                String sinopsisM = sinopsis.getText().toString().trim();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://637427ec08104a9c5f7b28eb.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                IMovie service = retrofit.create(IMovie.class);
                Movie movie = new Movie();
                movie.setTitulo(tituloM);
                movie.setSinopsis(sinopsisM);
                movie.setImagen(link);

                Call<Movie> call = service.create(movie);

                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.code() == 200){
                            Log.i("CONEXION","OK");
                        }else{
                            Log.i("CONEXION","BAD");
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        Log.i("CONEXION","FALLO EN EL SERVIDOR");
                        AppDataBase db = AppDataBase.getInstance(getApplicationContext());
                        db.iMovieDao().insert(movie);
                    }
                });
                Intent intent = new Intent(newmovieActivity.this,listmovie_activity.class);
                startActivity(intent);
            }
        });

        buttonBase64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermisoGalery()) {
                    chargeImage();
                }else{
                    SolicitarPermisoG();
                }
            }

        });

    }

    private boolean PermisoGalery() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void SolicitarPermisoG() {
        String[] permisos = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permisos, 200);
    }

    private void chargeImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione aplicacion"),5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {
                Uri path = data.getData();
                buttonBase64.setImageURI(path);
                final InputStream imageStream = getContentResolver().openInputStream(path);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl("https://api.imgur.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ImgBase64 imagen = new ImgBase64();
                imagen.image = encodeImage(selectedImage);

                IImage services = retrofit2.create(IImage.class);
                services.create(imagen).enqueue(new Callback<Imagen>() {
                    @Override
                    public void onResponse(Call<Imagen> call, Response<Imagen> response) {
                        Imagen data = response.body();
                        link = data.data.link;
                        Log.i("MAIN_APP", "1 link " + link);
                    }

                    @Override
                    public void onFailure(Call<Imagen> call, Throwable t) {
                        Log.i("CONEXION","FALLO EN EL SERVIDOR");
                    }
                });

            }else {
                Toast.makeText(this, "No cargo ninguna imagen", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Algo salio mal", Toast.LENGTH_LONG).show();
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}