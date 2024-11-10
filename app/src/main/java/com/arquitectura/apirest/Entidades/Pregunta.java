package com.arquitectura.apirest.Entidades;

public class Pregunta {

    private Long id_pregunta;
    private String pregunta;
    private String op1;
    private String op2;
    private String op3;
    private String op4;
    private String respuesta;
    private String categoria;
    private String dificultad;
    private boolean estado;

    public Pregunta(Long id_pregunta, String pregunta, String op1, String op2, String op3, String op4, String respuesta, String categoria, String dificultad, boolean estado) {
        this.id_pregunta = id_pregunta;
        this.pregunta = pregunta;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.respuesta = respuesta;
        this.categoria = categoria;
        this.dificultad = dificultad;
        this.estado = estado;
    }

    public Pregunta() {

    }

    public Long getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(Long id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getOp1() {
        return op1;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public String getOp2() {
        return op2;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp3() {
        return op3;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public String getOp4() {
        return op4;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
