package com.vanegas.adopet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    EditText nombre, apellidos, email, direccion, usuario, password, password2;
    TextView errorEmail, errorUsuario, errorPassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean errorUser = false, erroremail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = (EditText) findViewById(R.id.tv_nombreRegistro);
        apellidos = (EditText) findViewById(R.id.tv_apellidosRegistro);
        email = (EditText) findViewById(R.id.tv_emailRegistro);
        direccion = (EditText) findViewById(R.id.tv_direccionRegistro);
        usuario = (EditText) findViewById(R.id.tv_usuarioRegistro);
        password = (EditText) findViewById(R.id.tv_passwordRegistro);
        password2 = (EditText) findViewById(R.id.tv_password2Registro);
        errorEmail = (TextView) findViewById(R.id.tv_errorEmail_Registro);
        errorUsuario = (TextView) findViewById(R.id.tv_errorUsuario_Registro);
        errorPassword = (TextView) findViewById(R.id.tv_errorContraseña_Registro);

    }

    public void loginRegistro(View view){
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void registroRegistro(View view){

        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre.getText().toString());

        if(apellidos.getText(). toString().isEmpty())
            user.put("apellido", "");
        else
            user.put("apellido", apellidos.getText().toString());

        user.put("direccion", direccion.getText().toString());
        user.put("email", email.getText().toString());
        user.put("password", password.getText().toString());
        user.put("usuario", usuario.getText().toString());
        if(apellidos.getText(). toString().isEmpty())
            user.put("persona", false);
        else
            user.put("persona", true);

        if(!errores(view)){
            db.collection("propietario")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),"Usuario añadida correctamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistroActivity.this, PantallaPrincipalActivity.class);
                            intent.putExtra("Nombre",usuario.getText().toString().trim());
                            startActivity(intent);
                        }
                    });
        }
    }

    public boolean errores(View view){
        boolean error = false;

        errorPassword.setVisibility(view.GONE);
        errorEmail.setVisibility(view.GONE);
        errorUsuario.setVisibility(view.GONE);

        consultaBD();

        Toast.makeText(getApplicationContext(),"Error usuario: " +errorUser, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"Error email: " + erroremail, Toast.LENGTH_SHORT).show();

        if(nombre.getText().toString().isEmpty() || email.getText().toString().isEmpty() || direccion.getText().toString().isEmpty() || usuario.getText().toString().isEmpty() || password.getText().toString().isEmpty() || password2.getText().toString().isEmpty()){
            errorPassword.setText("ø Debe introducir los datos necesarios");
            errorPassword.setVisibility(view.VISIBLE);
            error = true;
        }else if(nombre.getText().toString().length() < 3){
            errorPassword.setText("ø El nombre es demasiado corto");
            errorPassword.setVisibility(view.VISIBLE);
            error = true;
        }else if(!email.getText().toString().contains("@")) {
            errorEmail.setText("ø El email no tiene el formato correcto");
            errorEmail.setVisibility(view.VISIBLE);
            error = true;
        }else if(erroremail){
            errorEmail.setText("ø Email ya usado");
            errorEmail.setVisibility(view.VISIBLE);
            error = true;
        }else if(errorUser){ // Comprobar que no hya otro usuario igual
            errorUsuario.setText("ø Usuario ya usado");
            errorUsuario.setVisibility(view.VISIBLE);
            error = true;
        }else if(!password.getText().toString().equals(password2.getText().toString())){
            errorPassword.setText("ø Las contraseñas no coinciden");
            errorPassword.setVisibility(view.VISIBLE);
            error = true;
        }

        return error;
    }

    private void consultaBD() {
        db.collection("propietario")
                .whereEqualTo("usuario", usuario.getText().toString().trim())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    setErrorUser(true);
                    Toast.makeText(getApplicationContext(),"Usuario ya usado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        db.collection("propietario")
                .whereEqualTo("email", email.getText().toString().trim())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    setErrorEmail(true);
                    Toast.makeText(getApplicationContext(),"Email ya usado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setErrorUser(boolean errorUser) {
        this.errorUser = errorUser;
    }

    public void setErrorEmail(boolean erroremail) {
        this.erroremail = erroremail;
    }
}