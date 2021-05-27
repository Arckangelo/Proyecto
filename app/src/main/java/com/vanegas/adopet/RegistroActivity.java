package com.vanegas.adopet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Console;

public class RegistroActivity extends AppCompatActivity {

    EditText nombre, apellidos, email, direccion, usuario, password, password2;
    TextView errorEmail, errorUsuario, errorPassword;


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

        if(!errores(view)){
            Intent intent = new Intent(RegistroActivity.this, PantallaPrincipalActivity.class);
            startActivity(intent);
        }
    }

    public boolean errores(View view){
        boolean error = false;

        errorPassword.setVisibility(view.GONE);
        errorEmail.setVisibility(view.GONE);
        errorUsuario.setVisibility(view.GONE);

        if(nombre.getText().toString().isEmpty() || email.getText().toString().isEmpty() || direccion.getText().toString().isEmpty() || usuario.getText().toString().isEmpty() || password.getText().toString().isEmpty() || password2.getText().toString().isEmpty()){
            errorPassword.setText("ø Debe introducir los datos necesarios");
            errorPassword.setVisibility(view.VISIBLE);
            error = true;
        }else if(nombre.getText().toString().length() < 3){
            errorPassword.setText("ø El nombre es demasiado corto");
            errorPassword.setVisibility(view.VISIBLE);
            error = true;
        }else if(!email.getText().toString().contains("@")){
            errorEmail.setText("ø El email no tiene el formato correcto");
            errorEmail.setVisibility(view.VISIBLE);
            error = true;
            //Falta la condición para comprobar si hay un email igual a ese
        }else if(usuario.getText().toString().equals("")){ // Comprobar que no hya otro usuario igual
            errorUsuario.setVisibility(view.VISIBLE);
            error = true;
        }else if(!password.getText().toString().equals(password2.getText().toString())){
            errorPassword.setText("ø Las contraseñas no coinciden");
            errorPassword.setVisibility(view.VISIBLE);
            error = true;
        }

        return error;
    }
}