package com.arquitectura.apirest.DAOS;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.arquitectura.apirest.Entidades.Pregunta;
import com.arquitectura.apirest.Room.PreguntaRoom;

import java.util.List;

@Dao
public interface PreguntaDao {

    @Insert
    void insertarPregunta(PreguntaRoom pregunta);

    @Update
    void actualizarPregunta(PreguntaRoom pregunta);


    @Query("SELECT * FROM preguntas WHERE pregunta = :preguntaText AND categoria = :categoria")
    PreguntaRoom obtenerPreguntaPorTextoYCategoria(String preguntaText, String categoria);

    @Query("SELECT * FROM Preguntas WHERE categoria = :categoria AND dificultad = :dificultad")
    List<PreguntaRoom> obtenerPreguntasPorCategoriaYDificultad(String categoria, String dificultad);

    @Query("SELECT * FROM Preguntas WHERE categoria = :categoria AND dificultad = :dificultad AND estado = 1")
    List<PreguntaRoom> obtenerPreguntasPorCategoriaYDificultadConEstadoTrue(String categoria, String dificultad);

    @Query("SELECT * FROM preguntas")
    List<PreguntaRoom> obtenerTodasLasPreguntas();
}
