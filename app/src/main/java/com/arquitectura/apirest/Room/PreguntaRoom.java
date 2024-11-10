package com.arquitectura.apirest.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PREGUNTAS")
public class PreguntaRoom {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String pregunta;
    private String op1;
    private String op2;
    private String op3;
    private String op4;
    private String respuesta;
    private String dificultad;
    private String categoria;
    private boolean estado;

    public PreguntaRoom(Long id, String pregunta, String op1, String op2, String op3, String op4, String respuesta, String dificultad, String categoria, boolean estado) {
        this.id = id;
        this.pregunta = pregunta;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
        this.respuesta = respuesta;
        this.dificultad = dificultad;
        this.categoria = categoria;
        this.estado = estado;
    }

    public PreguntaRoom() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}

