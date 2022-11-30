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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.Class.Imagen;
import com.example.test.Class.ImgBase64;
import com.example.test.Services.IImage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageButton buttonBase64;
    String imgDecodableString, link;
    TextView texBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButton = findViewById(R.id.imageButton);
        ImageButton autorButton = findViewById(R.id.imageAutor);
        buttonBase64 = (ImageButton) findViewById(R.id.base64);
        texBase64 = findViewById(R.id.textBase64);
        Button subir = findViewById(R.id.subirImage);

        Picasso.get().load("https://d500.epimg.net/cincodias/imagenes/2020/12/31/lifestyle/1609408585_467254_1609408795_noticia_normal.jpg").into(imageButton);
        Picasso.get().load("https://www.pngall.com/wp-content/uploads/4/Animated-Camera-PNG-Image.png").into(buttonBase64);
        Picasso.get().load("https://cloudfront-us-east-1.images.arcpublishing.com/infobae/QRGTS65ZVBEEFPLVGTJ6KOCCPY.jpg").into(autorButton);

        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,listmovie_activity.class);
                startActivity(intent);
            }
        });

        autorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,listautorActivity.class);
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

        subir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.imgur.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ImgBase64 imagen = new ImgBase64();
                imagen.image = imgDecodableString;

                IImage services = retrofit.create(IImage.class);
                services.create(imagen).enqueue(new Callback<Imagen>() {
                    @Override
                    public void onResponse(Call<Imagen> call, Response<Imagen> response) {
                        Imagen data = response.body();
                        link = data.data.link;
                        Log.i("MAIN_APP", new Gson().toJson(data));
                    }

                    @Override
                    public void onFailure(Call<Imagen> call, Throwable t) {
                        Log.i("MAIN_APP", "Fallo a obtener datos");
                    }
                });


                /*httpClient = new OkHttpClient.Builder().build();

                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image",imgDecodableString)
                        .build();

                Request request = new Request.Builder()
                        .url("https://api.imgur.com/3/image")
                        .method("POST", body)
                        .header("Authorization","Client-ID 8bcc638875f89d9")
                        .build();

                try {
                    Response response = httpClient.newCall(request).execute();
                    Log.wtf("RESPONSE",""+response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("CONEXION","FALLO EN EL SERVIDOR");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200){
                            Log.i("CONEXION","OK");
                            response = httpClient.newCall(request).execute();
                            Log.wtf("RESPONSE","" + response);

                        }else{
                            Log.i("CONEXION","BAD");
                        }
                    }
                });*/
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
                imgDecodableString = encodeImage(selectedImage);
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
        imgDecodableString = Base64.encodeToString(b, Base64.DEFAULT);
        texBase64.setText(imgDecodableString);
        return imgDecodableString;
    }

    public class ImagenBase64 {
        private String image;
    }

}
