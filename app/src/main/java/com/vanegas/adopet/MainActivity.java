package com.vanegas.adopet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {

    EditText nombre, password;
    TextView error;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.tv_usuarioLogin);
        password = (EditText) findViewById(R.id.tv_passwordLogin);
        error = (TextView) findViewById(R.id.tv_errorDatos_Login);

    }

    public void registroLogin(View view) {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    public void loginLogin(View view) {

        boolean fallo = false;

        error.setVisibility(view.GONE);

        if (nombre.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            error.setText("ø Introduzca los datos");
            error.setVisibility(view.VISIBLE);
            fallo = true;
        }

        if (!fallo) {
            db.collection("propietario")
                    .whereEqualTo("usuario", nombre.getText().toString().trim())
                    .whereEqualTo("password", password.getText().toString().trim())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        Intent intent = new Intent(MainActivity.this, PantallaPrincipalActivity.class);
                        startActivity(intent);
                    }else{
                        error.setText("ø Datos incorrectos");
                        error.setVisibility(view.VISIBLE);
                    }
                }
            });
        }
    }
}