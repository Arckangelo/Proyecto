package com.vanegas.adopet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PerfilUsuarioActivity extends AppCompatActivity {

    TextView nombreApellido, usuario, email, direccion;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        nombreApellido = (TextView) findViewById(R.id.tv_nombreApellidos_perfilUsuario);
        usuario = (TextView) findViewById(R.id.tv_usuario_perfilUsuario);
        email = (TextView) findViewById(R.id.tv_email_perfilUsuario);
        direccion = (TextView) findViewById(R.id.tv_direccion_perfilUsuario);

        datosUsuario();

    }

    public void datosUsuario(){

        Intent intentP = getIntent();
        String nombreUsuario = intentP.getStringExtra("Nombre");

        db.collection("propietario")
                .whereEqualTo("usuario", nombreUsuario)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        if(document.getData().get("persona").equals(true)){
                            nombreApellido.setText(document.getData().get("nombre").toString() + " " + document.getData().get("apellido").toString());
                            usuario.setText(document.getData().get("usuario").toString());
                            email.setText(document.getData().get("email").toString());
                            direccion.setText(document.getData().get("direccion").toString());
                        }
                    }
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_btn_logout:
                intent = new Intent(PerfilUsuarioActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_btn_pantallaPrincipal:
                intent = new Intent(PerfilUsuarioActivity.this, PantallaPrincipalActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_btn_perfil:
                intent = new Intent(PerfilUsuarioActivity.this, PerfilUsuarioActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}