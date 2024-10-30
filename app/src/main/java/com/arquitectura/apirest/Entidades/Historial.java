package com.arquitectura.apirest.Entidades;

public class Historial {

    private Long id;
    private int puntaje;
    private String fecha;
    private String tiempo;
    private int ayudas;
    private Long id_usuario;
    private Long id_pregunta;
    private String username;
    private String categoria;
    private String dificultad;

    public Historial(Long id, int puntaje, String fecha, String tiempo, int ayudas, Long id_usuario, Long id_pregunta, String username, String categoria, String dificultad) {
        this.id = id;
        this.puntaje = puntaje;
        this.fecha = fecha;
        this.tiempo = tiempo;
        this.ayudas = ayudas;
        this.id_usuario = id_usuario;
        this.id_pregunta = id_pregunta;
        this.username = username;
        this.categoria = categoria;
        this.dificultad = dificultad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public int getAyudas() {
        return ayudas;
    }

    public void setAyudas(int ayudas) {
        this.ayudas = ayudas;
    }

    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Long getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(Long id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
}
