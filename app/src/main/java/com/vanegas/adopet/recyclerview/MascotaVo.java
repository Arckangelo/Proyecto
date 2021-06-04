package com.vanegas.adopet.recyclerview;

public class MascotaVo {

    private String id, nombre, raza, peso, fecna, animal, propietario;
    private boolean adoptable;

    public MascotaVo() {

    }

    public MascotaVo(String id, String nombre, String raza, String peso, String fecna, String animal, String propietario, boolean adoptable) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.peso = peso;
        this.fecna = fecna;
        this.animal = animal;
        this.propietario = propietario;
        this.adoptable = adoptable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public boolean isAdoptable() {
        return adoptable;
    }

    public void setAdoptable(boolean adoptable) {
        this.adoptable = adoptable;
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

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getFecna() {
        return fecna;
    }

    public void setFecna(String fecna) {
        this.fecna = fecna;
    }
}
