package com.vanegas.adopet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilMascotaActivity  extends AppCompatActivity {

    TextView nombre, raza, peso, fecna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        nombre = (TextView) findViewById(R.id.tv_nombre_perfilMascota);
        raza = (TextView) findViewById(R.id.tv_raza_perfilMascota);
        peso = (TextView) findViewById(R.id.tv_peso_perfilMascota);
        fecna = (TextView) findViewById(R.id.tv_fechaNacimiento_perfilMascota);

        Intent intent = getIntent();

        nombre.setText(intent.getStringExtra("Nombre"));
        raza.setText(intent.getStringExtra("Raza"));
        peso.setText(intent.getStringExtra("Peso"));
        fecna.setText(intent.getStringExtra("Fecna"));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_btn_logout:
                intent = new Intent(PerfilMascotaActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_btn_pantallaPrincipal:
                intent = new Intent(PerfilMascotaActivity.this, PantallaPrincipalActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_btn_perfil:
                intent = new Intent(PerfilMascotaActivity.this, PerfilUsuarioActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}