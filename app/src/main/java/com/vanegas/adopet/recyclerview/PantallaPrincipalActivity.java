package com.vanegas.adopet.recyclerview;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.vanegas.adopet.MainActivity;
import com.vanegas.adopet.R;
import com.vanegas.adopet.mascota.AnadirMascotaActivity;
import com.vanegas.adopet.mascota.PerfilMascotaActivity;
import com.vanegas.adopet.usuario.PerfilUsuarioActivity;

import java.util.ArrayList;

public class PantallaPrincipalActivity extends AppCompatActivity {

    ArrayList<MascotaVo> listaMascotas = new ArrayList<>();
    RecyclerView recyclerMascotas;
    Spinner spinner;
    String[] animal = {"Seleccione un animal", "Perro", "Gato", "Pájaro", "Conejo"};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static AdaptadorMascotas adapter = new AdaptadorMascotas( new ArrayList<>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        listaMascotas = new ArrayList<>();
        recyclerMascotas = (RecyclerView) findViewById(R.id.recyclerView);
        spinner = (Spinner) findViewById(R.id.spinner_pantallaPrincipal);
        recyclerMascotas.setLayoutManager(new LinearLayoutManager(this));
        recyclerMascotas.setAdapter(adapter);

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
                                    int foto;
                                    if(document.getData().get("animal").toString().toLowerCase().equals("pájaro"))
                                        foto = getResources().getIdentifier("pajaro", "drawable", getPackageName());
                                    else
                                        foto = getResources().getIdentifier(document.getData().get("animal").toString().toLowerCase(), "drawable", getPackageName());

                                    lista.add(new MascotaVo(document.getId(), document.getData().get("nombre").toString(), document.getData().get("raza").toString(), document.getData().get("peso").toString(), document.getData().get("fecna").toString(), document.getData().get("animal").toString(), document.getData().get("idPropietario").toString(), (document.getData().get("adoptable").equals("true")), foto));
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

    @Override
    public void onBackPressed() { }

    private void llenarMascotas() {

        db.collection("mascota").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<MascotaVo> lista = new ArrayList<>();

                if (!queryDocumentSnapshots.isEmpty())
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        int foto;
                        if(document.getData().get("animal").toString().toLowerCase().equals("pájaro"))
                            foto = getResources().getIdentifier("pajaro", "drawable", getPackageName());
                        else
                            foto = getResources().getIdentifier(document.getData().get("animal").toString().toLowerCase(), "drawable", getPackageName());

                        lista.add(new MascotaVo(document.getId(), document.getData().get("nombre").toString(), document.getData().get("raza").toString(), document.getData().get("peso").toString(), document.getData().get("fecna").toString(), document.getData().get("animal").toString(), document.getData().get("idPropietario").toString(), (document.getData().get("adoptable").equals("true")), foto));
                    }

                recyclerMascotas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new AdaptadorMascotas(lista);
                recyclerMascotas.setAdapter(adapter);
                recyclerClick();
            }
        });
    }

    public void recyclerClick(){
        Intent datosMascota = new Intent(getBaseContext(), PerfilMascotaActivity.class), intentP;
        intentP = getIntent();
        String nombreUsuario = intentP.getStringExtra("Nombre");
        adapter.setOnClickListener(new AdaptadorMascotas.ClickListener(){

            @Override
            public void onItemClick(View v, int position) {
                MascotaVo mascotaVo = adapter.getItem(position);
                datosMascota.putExtra("id", mascotaVo.getId());
                datosMascota.putExtra("Nombre", nombreUsuario);
               startActivity(datosMascota);
            }
        });
    }

    public void anadirMascota(View view) {
        Intent intentP;
        intentP = getIntent();
        String nombreUsuario = intentP.getStringExtra("Nombre");
        Intent intent = new Intent(PantallaPrincipalActivity.this, AnadirMascotaActivity.class);
        intent.putExtra("Nombre", nombreUsuario);
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

        switch (item.getItemId()) {
            case R.id.menu_btn_logout:
                intent = new Intent(PantallaPrincipalActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.menu_btn_pantallaPrincipal:
                Toast.makeText(getApplicationContext(), "Ya estás la pantalla principal", Toast.LENGTH_LONG).show();
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