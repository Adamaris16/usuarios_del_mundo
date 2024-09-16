package com.example.usuarios_del_mundo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String city, country, firstName, lastName, imageUrl;
    private double latitude, longitude;
    private ImageView flagImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Obtener datos del intent
        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        city = intent.getStringExtra("city");
        country = intent.getStringExtra("country");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        imageUrl = intent.getStringExtra("imageUrl");

        // Asignar datos a las vistas
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        ImageView profileImageView = findViewById(R.id.profileImageView);
        flagImageView = findViewById(R.id.flagImageView);

        nameTextView.setText(firstName + " " + lastName);
        locationTextView.setText(city + ", " + country);
        Picasso.get().load(imageUrl).into(profileImageView);

        // Cargar la bandera del país usando la API
        loadCountryFlag(country);

        // Configurar el mapa
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void loadCountryFlag(String countryName) {
        String url = "https://restcountries.com/v3.1/name/" + countryName;

        // Crear una cola de solicitudes con Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Crear una solicitud JSON de tipo array
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Obtener el primer objeto del array (ya que el país viene en el primer objeto)
                            JSONObject countryData = response.getJSONObject(0);
                            JSONObject flags = countryData.getJSONObject("flags");

                            // Obtener la URL de la bandera en formato PNG
                            String flagUrl = flags.getString("png");

                            // Cargar la bandera en el ImageView usando Picasso
                            Picasso.get().load(flagUrl).into(flagImageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserDetailActivity.this, "Error al obtener la bandera", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la solicitud a la cola
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Colocar un marcador en la ubicación del usuario
        LatLng userLocation = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new MarkerOptions().position(userLocation).title(city));

        // Centrar el mapa en la ubicación del usuario
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));

        // Añadir un listener para mostrar un cuadro de diálogo con el nombre de la ciudad
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Mostrar un cuadro de diálogo o Toast con el nombre de la ciudad
                Toast.makeText(UserDetailActivity.this, "Ciudad: " + city, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}