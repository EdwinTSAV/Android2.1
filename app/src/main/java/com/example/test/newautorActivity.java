package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.Class.Autor;
import com.example.test.DataBase.AppDataBase;
import com.example.test.Services.IAutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class newautorActivity extends AppCompatActivity {

    private LocationManager mLocationManager;

    public EditText latitudA, longitudA;
    public double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newautor);

        EditText nombreA = findViewById(R.id.nombre);
        latitudA = findViewById(R.id.latitud);
        longitudA = findViewById(R.id.longitud);
        Button crear = findViewById(R.id.newMovie);
        Button ubicacion = findViewById(R.id.btnGetUbication);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreA.getText().toString().trim();
                String latitud = latitudA.getText().toString().trim();
                String longitud = longitudA.getText().toString().trim();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://637427ec08104a9c5f7b28eb.mockapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                IAutor service = retrofit.create(IAutor.class);
                Autor autor = new Autor();
                autor.setNombre(nombre);
                autor.setLatitud(latitud);
                autor.setLongitud(longitud);

                Call<Autor> call = service.create(autor);

                call.enqueue(new Callback<Autor>() {
                    @Override
                    public void onResponse(Call<Autor> call, Response<Autor> response) {
                        if (response.code() == 200){
                            Log.i("CONEXION","OK");
                        }else{
                            Log.i("CONEXION","BAD");
                        }
                    }

                    @Override
                    public void onFailure(Call<Autor> call, Throwable t) {
                        Log.i("CONEXION","FALLO EN EL SERVIDOR");
                        AppDataBase db = AppDataBase.getInstance(getApplicationContext());
                        db.iAutorDao().insert(autor);
                    }
                });
                Intent intent = new Intent(newautorActivity.this,listautorActivity.class);
                startActivity(intent);
            }
        });
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(newautorActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(newautorActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(newautorActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                } else {
                    locationStart();
                }
            }
        });
    }
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {

        newautorActivity newPokemonActivity;
        public newautorActivity getMainActivity() {
            return newPokemonActivity;
        }

        public void setMainActivity(newautorActivity mainActivity) {
            this.newPokemonActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            Log.i("CONEXION","Latitud: " + latitud + longitud);
            latitudA.setText("" + loc.getLatitude());
            longitudA.setText("" + loc.getLongitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

}