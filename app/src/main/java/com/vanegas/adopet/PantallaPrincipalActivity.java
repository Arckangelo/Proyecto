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
        intent = new Intent(this, PerfilMascotaActivity.class);

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, animal));

        spinner.setSelection(0);

        llenarMascotas();




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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerMascotas.setAdapter(adapter);
    }

    private void llenarMascotas() {

        db.collection("mascota")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<MascotaVo> lista = new ArrayList<>();
                if (!queryDocumentSnapshots.isEmpty())
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Log.d("nombre de la mascota: ", document.getData().get("nombre").toString());
                        Log.d("raza de la mascota: ", document.getData().get("raza").toString());
                        Log.d("peso de la mascota: ", document.getData().get("peso").toString());
                        Log.d("fecna de la mascota: ", document.getData().get("fecna").toString());
                        Log.d("propietario de la mascota: ", document.getData().get("idPropietario").toString());
                        Log.d("animal de la mascota: ", document.getData().get("animal").toString());
                        Log.d("estado de la mascota: ", (document.getData().get("adoptable").toString() == "true") ? "Si" : "No");

                        lista.add(new MascotaVo(document.getData().get("nombre").toString(), document.getData().get("raza").toString(), document.getData().get("peso").toString(), document.getData().get("fecna").toString(), document.getData().get("animal").toString(), document.getData().get("idPropietario").toString(), true));
                    }
                    recyclerMascotas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new AdaptadorMascotas(lista);
                recyclerMascotas.setAdapter(adapter);

            }
        });

        /*listaMascotas.add(new MascotaVo("Manchas", "Tricolor", "6Kg", "01/01/2000", "hola", "Sir Negas", true));
        listaMascotas.add(new MascotaVo("Nala", "Común", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Chimbo", "Atigrado", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Nano", "Atigrado", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Yago", "Cocker Spaniel", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Vincent", "Cocker Spaniel", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Blanca", "Siamés", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Manchas", "Tricolor", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Nala", "Común", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Chimbo", "Atigrado", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Nano", "Atigrado", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Yago", "Cocker Spaniel", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Vincent", "Cocker Spaniel", "6Kg", "01/01/2000"));
        listaMascotas.add(new MascotaVo("Blanca", "Siamés", "6Kg", "01/01/2000"));*/
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
        Intent intent;
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
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}