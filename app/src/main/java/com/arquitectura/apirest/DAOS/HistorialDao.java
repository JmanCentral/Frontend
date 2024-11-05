package com.arquitectura.apirest.DAOS;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.arquitectura.apirest.Entidades.Historial;
import com.arquitectura.apirest.Room.HistorialRoom;

import java.util.List;

@Dao
public interface HistorialDao {

    // Método para insertar un historial. Si hay un conflicto (mismo ID), se reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistorial(HistorialRoom historial);

    @Query("SELECT * FROM historial WHERE username = :username")
    List<HistorialRoom> obtenerHistorialPorUsuario(String username);

    @Query("SELECT SUM(h.puntaje) FROM historial h WHERE h.username = :username")
    Integer getTotalPuntosDelUsuario(String username);

    @Query("SELECT SUM(h.ayudas) FROM historial h WHERE h.username = :username")
    Integer getTotalAyudasDelUsuario(String username);

    @Query("SELECT SUM(h.tiempo) FROM historial h WHERE h.username = :username")
    Integer getTiempoTotalDelUsuario(String username);

}
