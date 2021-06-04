package com.vanegas.adopet.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vanegas.adopet.MainActivity;
import com.vanegas.adopet.recyclerview.MascotaVo;
import com.vanegas.adopet.recyclerview.PantallaPrincipalActivity;
import com.vanegas.adopet.usuario.PerfilUsuarioActivity;
import com.vanegas.adopet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AnadirMascotaActivity extends AppCompatActivity {

    TextView errorDatos;
    EditText nombre, peso, fecna, raza;
    Spinner spinner;
    String[] animal = {"Seleccione un animal", "Perro", "Gato", "Pájaro", "Conejo"};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_mascota);

        nombre = (EditText) findViewById(R.id.tv_nombreMascota_anadirMascota);
        peso = (EditText) findViewById(R.id.number_pesoMascota_anadirMascota);
        fecna = (EditText) findViewById(R.id.date_fechaNacMascota_anadirMascota);
        raza = (EditText) findViewById(R.id.tv_razaMascota_AnadirMascota);
        errorDatos = (TextView) findViewById(R.id.tv_error_AnadirMascota);
        spinner = (Spinner) findViewById(R.id.spinner_animalMascota_anadirMascota);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, animal));
        spinner.setSelection(0);

    }

    @Override
    public void onBackPressed() {
    }

    public void anadirMascota(View view) {
        Intent intentP = getIntent();
        String nombreUsuario = intentP.getStringExtra("Nombre");
        if (!errores(view)) {
            Map<String, Object> mascota = new HashMap<>();
            mascota.put("nombre", nombre.getText().toString());
            mascota.put("animal", spinner.getSelectedItem().toString());
            mascota.put("peso", peso.getText().toString());
            mascota.put("fecna", fecna.getText().toString());
            mascota.put("raza", raza.getText().toString());
            mascota.put("idPropietario", nombreUsuario);
            mascota.put("adoptable", true);

            db.collection("mascota")
                    .whereEqualTo("nombre", mascota.get("nombre").toString())
                    .whereEqualTo("animal", mascota.get("animal").toString())
                    .whereEqualTo("raza", mascota.get("raza").toString())
                    .whereEqualTo("peso", mascota.get("peso").toString())
                    .whereEqualTo("fecna", mascota.get("fecna").toString())
                    .whereEqualTo("idPropietario", mascota.get("idPropietario").toString())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty()){

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            id = document.getId();
                        }

                        db.collection("mascota").document(id)
                                .set(mascota).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"Ya existe una mascota con esos datos", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AnadirMascotaActivity.this, PantallaPrincipalActivity.class);
                                intent.putExtra("Nombre", nombreUsuario);
                                startActivity(intent);
                            }
                        });
                    }else{
                        db.collection("mascota")
                                .add(mascota)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getApplicationContext(), "Mascota añadida correctamente", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AnadirMascotaActivity.this, PantallaPrincipalActivity.class);
                                        intent.putExtra("Nombre", nombreUsuario);
                                        startActivity(intent);
                                    }
                                });
                    }
                }
            });
        }
    }

    public boolean errores(View view) {
        boolean error = false;
        Date fecha = new Date(), fechaactual = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        errorDatos.setVisibility(view.GONE);

        if(!fecna.getText().toString().contains("/") || fecna.getText().toString().length() != 10){
            errorDatos.setText("ø La fecha de nacimiento no tiene el formato adecuado");
            errorDatos.setVisibility(view.VISIBLE);
            error = true;
        }else{
            try {
                fecha = format.parse(fecna.getText().toString());
                fechaactual = format.parse(fechaActual);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (nombre.getText().toString().isEmpty() || peso.getText().toString().isEmpty() || fecna.getText().toString().isEmpty() || raza.getText().toString().isEmpty() || spinner.getSelectedItemPosition() == 0) {
            errorDatos.setText("ø Debe introducir los datos necesarios");
            errorDatos.setVisibility(view.VISIBLE);
            error = true;
        } else if (Float.parseFloat(peso.getText().toString()) == 0) {
            errorDatos.setText("ø El peso no puede ser 0");
            errorDatos.setVisibility(view.VISIBLE);
            error = true;
        } else if (fecha.after(fechaactual)) {
            errorDatos.setText("ø La fecha de nacimiento no puede ser mayor a la actual");
            errorDatos.setVisibility(view.VISIBLE);
            error = true;
        }

        return error;
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
                intent = new Intent(AnadirMascotaActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.menu_btn_pantallaPrincipal:
                intent = new Intent(AnadirMascotaActivity.this, PantallaPrincipalActivity.class);
                intent.putExtra("Nombre", nombreUsuario);
                startActivity(intent);
                return true;
            case R.id.menu_btn_perfil:
                intent = new Intent(AnadirMascotaActivity.this, PerfilUsuarioActivity.class);
                intent.putExtra("Nombre", nombreUsuario);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}