package com.vanegas.adopet;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.Objects;

public class Mascota {

    private String id, nombre, raza;
    private float peso;
    private Date fecna;

    public Mascota() {
    }

    public Mascota(String nombre, String raza, float peso, Date fecna) {
        this.nombre = nombre;
        this.raza = raza;
        this.peso = peso;
        this.fecna = fecna;
    }

    public Mascota(String id, String nombre, String raza, float peso, Date fecna) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.peso = peso;
        this.fecna = fecna;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public Date getFecna() {
        return fecna;
    }

    public void setFecna(Date fecna) {
        this.fecna = fecna;
    }

    @Override
    public String toString() {
        return "Mascota{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", raza='" + raza + '\'' +
                ", peso=" + peso +
                ", fecna=" + fecna +
                '}';
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mascota mascota = (Mascota) o;
        return Objects.equals(id, mascota.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
