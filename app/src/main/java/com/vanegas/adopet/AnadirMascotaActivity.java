package com.vanegas.adopet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnadirMascotaActivity extends AppCompatActivity {

    TextView errorDatos;
    EditText nombre, peso, fecna;
    Spinner spinner;
    String[] animal = { "Seleccione un animal", "Perro", "Gato", "Pájaro", "Conejo"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_mascota);

        nombre = (EditText) findViewById(R.id.tv_nombreMascota_anadirMascota);
        peso = (EditText) findViewById(R.id.number_pesoMascota_anadirMascota);
        fecna = (EditText) findViewById(R.id.date_fechaNacMascota_anadirMascota);
        errorDatos = (TextView) findViewById(R.id.tv_error_AnadirMascota);
        spinner = (Spinner) findViewById(R.id.spinner_animalMascota_anadirMascota);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, animal));
        spinner.setSelection(0);

    }

    public void anadirMascota(View view) {
        if(!errores(view)){
            Intent intent = new Intent(AnadirMascotaActivity.this, PantallaPrincipalActivity.class);
            startActivity(intent);
        }
    }

    public boolean errores(View view)  {
        boolean error = false;

        Date fecha = new Date(), fechaactual = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        try {
            fecha = format.parse(fecna.getText().toString());
            fechaactual = format.parse(fechaActual);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        errorDatos.setVisibility(view.GONE);

        if(nombre.getText().toString().isEmpty() || peso.getText().toString().isEmpty() || fecna.getText().toString().isEmpty() || spinner.getSelectedItemPosition() == 0){
            errorDatos.setText("ø Debe introducir los datos necesarios");
            errorDatos.setVisibility(view.VISIBLE);
            error = true;
        }else if(Float.parseFloat(peso.getText().toString()) == 0){
            errorDatos.setText("ø El peso no puede ser 0");
            errorDatos.setVisibility(view.VISIBLE);
            error = true;
        }else if(fecha.after(fechaactual)){
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_btn_logout:
                intent = new Intent(AnadirMascotaActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_btn_pantallaPrincipal:
                intent = new Intent(AnadirMascotaActivity.this, PantallaPrincipalActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_btn_perfil:
                intent = new Intent(AnadirMascotaActivity.this, PerfilUsuarioActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}