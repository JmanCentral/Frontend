package com.arquitectura.apirest.DAOS;

public class DificultadCategoria {
    private String dificultad;
    private String categoria;

    // Constructor, getters y setters
    public DificultadCategoria(String dificultad, String categoria) {
        this.dificultad = dificultad;
        this.categoria = categoria;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}

