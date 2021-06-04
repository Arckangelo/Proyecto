package com.vanegas.adopet.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vanegas.adopet.MainActivity;
import com.vanegas.adopet.mascota.PerfilMascotaActivity;
import com.vanegas.adopet.recyclerview.PantallaPrincipalActivity;
import com.vanegas.adopet.R;

import java.util.HashMap;
import java.util.Map;

public class PerfilUsuarioActivity extends AppCompatActivity {

    TextView nombreApellido, usuario, error;
    EditText email, direccion;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        nombreApellido = (TextView) findViewById(R.id.tv_nombreApellidos_perfilUsuario);
        usuario = (TextView) findViewById(R.id.tv_usuario_perfilUsuario);
        email = (EditText) findViewById(R.id.tv_email_perfilUsuario);
        direccion = (EditText) findViewById(R.id.tv_direccion_perfilUsuario);
        error = (TextView) findViewById(R.id.tv_error_PerfilUsuario);

        datosUsuario();

    }

    @Override
    public void onBackPressed() {
    }

    public void datosUsuario() {

        Intent intentP = getIntent();
        String nombreUsuario = intentP.getStringExtra("Nombre");

        db.collection("propietario")
                .whereEqualTo("usuario", nombreUsuario)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        id = document.getId();

                        if (document.getData().get("persona").equals(true))
                            nombreApellido.setText(document.getData().get("nombre").toString() + " " + document.getData().get("apellido").toString());
                        else
                            nombreApellido.setText(document.getData().get("nombre").toString());

                        usuario.setText(document.getData().get("usuario").toString());
                        email.setText(document.getData().get("email").toString());
                        direccion.setText(document.getData().get("direccion").toString());

                    }
                }
            }
        });
    }

    public void editar(View view) {

        if(!errores(view)){
            Map<String, Object> propietario = new HashMap<>();
            propietario.put("email", email.getText().toString());
            propietario.put("direccion", direccion.getText().toString());

            db.collection("propietario").document(id)
                    .set(propietario, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Usuario editado correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PerfilUsuarioActivity.this, PantallaPrincipalActivity.class);
                            intent.putExtra("Nombre", usuario.getText().toString());
                            startActivity(intent);
                        }
                    });
        }
    }

    public boolean errores(View view){
        boolean errores = false;

        error.setVisibility(view.GONE);;

        if(email.getText().toString().isEmpty() || direccion.getText().toString().isEmpty()){
            error.setText("ø Debe introducir los datos necesarios");
            error.setVisibility(view.VISIBLE);
            errores = true;
        }else if(!email.getText().toString().contains("@gmail.com") && !email.getText().toString().contains("@hotmail.com") && !email.getText().toString().contains("@outlook.com") && !email.getText().toString().contains("@yahoo.com")) {
            error.setText("ø El email no tiene el formato correcto");
            error.setVisibility(view.VISIBLE);
            errores = true;
        }

        return errores;
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
                intent = new Intent(PerfilUsuarioActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.menu_btn_pantallaPrincipal:
                intent = new Intent(PerfilUsuarioActivity.this, PantallaPrincipalActivity.class);
                intent.putExtra("Nombre", nombreUsuario);
                startActivity(intent);
                return true;
            case R.id.menu_btn_perfil:
                Toast.makeText(getApplicationContext(), "Ya estás en tu perfil", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}