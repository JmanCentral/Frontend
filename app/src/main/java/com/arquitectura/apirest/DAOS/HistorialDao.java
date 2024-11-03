package com.arquitectura.apirest.DAOS;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.arquitectura.apirest.Entidades.Historial;
import com.arquitectura.apirest.Room.HistorialRoom;

@Dao
public interface HistorialDao {

    // Método para insertar un historial. Si hay un conflicto (mismo ID), se reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistorial(HistorialRoom historial);
}
