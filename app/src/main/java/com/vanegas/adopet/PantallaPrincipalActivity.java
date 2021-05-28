package com.vanegas.adopet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.ArrayList;

public class PantallaPrincipalActivity extends AppCompatActivity {

    ArrayList<MascotaVo> listaMascotas = new ArrayList<>();
    RecyclerView recyclerMascotas;
    Intent intent;
    Spinner spinner;
    String[] animal = {"Seleccione un animal", "Perro", "Gato", "Pájaro", "Conejo"};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AdaptadorMascotas adapter = new AdaptadorMascotas(listaMascotas);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        listaMascotas = new ArrayList<>();
        recyclerMascotas = (RecyclerView) findViewById(R.id.recyclerView);
        spinner = (Spinner) findViewById(R.id.spinner_pantallaPrincipal);
        recyclerMascotas.setLayoutManager(new LinearLayoutManager(this));
        recyclerMascotas.setAdapter(adapter);
        intent = new Intent(this, PerfilMascotaActivity.class);

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, animal));

        spinner.setSelection(0);

        llenarMascotas();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spinner.getSelectedItem().toString().equals("Seleccione un animal")) {
                    db.collection("mascota").whereEqualTo("animal", spinner.getSelectedItem().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            ArrayList<MascotaVo> lista = new ArrayList<>();

                            if (!queryDocumentSnapshots.isEmpty())
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                    lista.add(new MascotaVo(document.getData().get("nombre").toString(), document.getData().get("raza").toString(), document.getData().get("peso").toString(), document.getData().get("fecna").toString(), document.getData().get("animal").toString(), document.getData().get("idPropietario").toString(), true));
                                }

                            adapter = new AdaptadorMascotas(lista);
                            recyclerMascotas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerMascotas.setAdapter(adapter);
                            recyclerClick();
                        }
                    });
                }else
                    llenarMascotas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void llenarMascotas() {

        db.collection("mascota").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<MascotaVo> lista = new ArrayList<>();

                if (!queryDocumentSnapshots.isEmpty())
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        lista.add(new MascotaVo(document.getData().get("nombre").toString(), document.getData().get("raza").toString(), document.getData().get("peso").toString(), document.getData().get("fecna").toString(), document.getData().get("animal").toString(), document.getData().get("idPropietario").toString(), true));
                    }

                adapter = new AdaptadorMascotas(lista);
                recyclerMascotas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerMascotas.setAdapter(adapter);
                recyclerClick();
            }
        });
    }

    public void recyclerClick(){
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Nombre", listaMascotas.get(recyclerMascotas.getChildAdapterPosition(v)).getNombre().toString());
                intent.putExtra("Raza", listaMascotas.get(recyclerMascotas.getChildAdapterPosition(v)).getRaza().toString());
                intent.putExtra("Peso", listaMascotas.get(recyclerMascotas.getChildAdapterPosition(v)).getPeso()).toString();
                intent.putExtra("Fecna", listaMascotas.get(recyclerMascotas.getChildAdapterPosition(v)).getFecna().toString());
                startActivity(intent);
            }
        });
    }

    public void anadirMascota(View view) {
        Intent intent = new Intent(PantallaPrincipalActivity.this, AnadirMascotaActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent, intentP;
        intentP = getIntent();
        String nombreUsuario = intentP.getStringExtra("Nombre");

        Log.d("Nombre de usuario: ", nombreUsuario);

        switch (item.getItemId()) {
            case R.id.menu_btn_logout:
                intent = new Intent(PantallaPrincipalActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_btn_pantallaPrincipal:
                intent = new Intent(PantallaPrincipalActivity.this, PantallaPrincipalActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_btn_perfil:
                intent = new Intent(PantallaPrincipalActivity.this, PerfilUsuarioActivity.class);
                intent.putExtra("Nombre", nombreUsuario);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}