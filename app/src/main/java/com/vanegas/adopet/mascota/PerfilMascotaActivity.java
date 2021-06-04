package com.vanegas.adopet.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.vanegas.adopet.MainActivity;
import com.vanegas.adopet.recyclerview.PantallaPrincipalActivity;
import com.vanegas.adopet.usuario.PerfilUsuarioActivity;
import com.vanegas.adopet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PerfilMascotaActivity extends AppCompatActivity {

    EditText nombre, raza, peso, fecna;
    TextView propietario, adoptable, errorDatos;
    String id, usuario;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button borrar, adoptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);
        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        usuario = intent.getStringExtra("Nombre");

        adoptar = (Button) findViewById(R.id.btn_adoptar_editar);
        borrar = (Button) findViewById(R.id.btn_borrar);

        nombre = (EditText) findViewById(R.id.tv_nombre_perfilMascota);
        raza = (EditText) findViewById(R.id.tv_raza_perfilMascota);
        peso = (EditText) findViewById(R.id.tv_peso_perfilMascota);
        fecna = (EditText) findViewById(R.id.tv_fechaNacimiento_perfilMascota);

        errorDatos = (TextView) findViewById(R.id.tv_error_perfilMascota);
        propietario = (TextView) findViewById(R.id.tv_propietario_perfilMascota);
        adoptable = (TextView) findViewById(R.id.tv_adoptable_perfilMascota);

        DocumentReference docRef = db.collection("mascota").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nombre.setText(documentSnapshot.getData().get("nombre").toString());
                raza.setText(documentSnapshot.getData().get("raza").toString());
                peso.setText(documentSnapshot.getData().get("peso").toString());
                fecna.setText(documentSnapshot.getData().get("fecna").toString());
                propietario.setText(documentSnapshot.getData().get("idPropietario").toString());

                if (propietario.getText().toString().equals(usuario)) {
                    adoptar.setText("Editar");
                    borrar.setClickable(true);
                } else {
                    adoptar.setText("Adoptar");
                    borrar.setClickable(false);
                    nombre.setFocusable(false);
                    raza.setFocusable(false);
                    peso.setFocusable(false);
                    fecna.setFocusable(false);
                }

                if (documentSnapshot.getData().get("adoptable").toString().equals("true")) {
                    adoptable.setText("Adoptable");
                    adoptar.setClickable(true);
                } else {
                    adoptable.setText("No adoptable");
                    adoptar.setClickable(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() { }

    public void adoptarEditar(View view) {
        if (adoptar.getText().toString().equals("Editar")) {
            if (!errores(view)) {
                Map<String, Object> mascota = new HashMap<>();
                mascota.put("nombre", nombre.getText().toString());
                mascota.put("raza", raza.getText().toString());
                mascota.put("peso", peso.getText().toString());
                mascota.put("fecna", fecna.getText().toString());

                db.collection("mascota").document(id)
                        .set(mascota, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Mascota editada correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PerfilMascotaActivity.this, PantallaPrincipalActivity.class);
                                intent.putExtra("Nombre", usuario);
                                startActivity(intent);
                            }
                        });
            }
        } else if (adoptar.getText().toString().equals("Adoptar")) {
            Map<String, Object> mascota = new HashMap<>();
            mascota.put("idPropietario", usuario);
            mascota.put("adoptable", false);

            db.collection("mascota").document(id)
                    .set(mascota, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Mascota adoptaba correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PerfilMascotaActivity.this, PantallaPrincipalActivity.class);
                            intent.putExtra("Nombre", usuario);
                            startActivity(intent);
                        }
                    });
        }
    }

    public void borrar(View view) {
        db.collection("mascota").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Mascota eliminada correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PerfilMascotaActivity.this, PantallaPrincipalActivity.class);
                intent.putExtra("Nombre", usuario);
                startActivity(intent);
            }
        });
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

        if (nombre.getText().toString().isEmpty() || peso.getText().toString().isEmpty() || fecna.getText().toString().isEmpty() || raza.getText().toString().isEmpty()) {
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
                intent = new Intent(PerfilMascotaActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.menu_btn_pantallaPrincipal:
                intent = new Intent(PerfilMascotaActivity.this, PantallaPrincipalActivity.class);
                intent.putExtra("Nombre", nombreUsuario);
                startActivity(intent);
                return true;
            case R.id.menu_btn_perfil:
                intent = new Intent(PerfilMascotaActivity.this, PerfilUsuarioActivity.class);
                intent.putExtra("Nombre", nombreUsuario);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}