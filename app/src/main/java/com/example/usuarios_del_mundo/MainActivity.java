package com.example.usuarios_del_mundo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.usuarios_del_mundo.Adapter.UserAdapter;
import com.example.usuarios_del_mundo.Models.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    private static final String URL = "https://randomuser.me/api/?results=20"; // URL de la API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar los datos del JSON desde la URL
        loadUsersFromApi();
    }

    private void loadUsersFromApi() {
        // Crear una instancia de RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Crear una solicitud para obtener el JSON desde la URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta JSON
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                        JsonArray results = jsonObject.getAsJsonArray("results");

                        for (JsonElement element : results) {
                            User user = gson.fromJson(element, User.class);
                            userList.add(user);
                        }

                        // Inicializar y establecer el adaptador
                        userAdapter = new UserAdapter(userList, MainActivity.this);
                        recyclerView.setAdapter(userAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Mostrar un mensaje de error
                        Toast.makeText(MainActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Añadir la solicitud a la cola
        requestQueue.add(jsonObjectRequest);
    }
}
