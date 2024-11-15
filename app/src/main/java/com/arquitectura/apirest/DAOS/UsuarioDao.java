package com.arquitectura.apirest.DAOS;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.arquitectura.apirest.Room.UsuarioRoom;

@Dao
public interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsuario(UsuarioRoom usuarioRoom);

    @Query("SELECT * FROM USUARIOS WHERE username = :username AND password = :password LIMIT 1")
    UsuarioRoom verificarUsuario(String username, String password);

    @Query("SELECT * FROM USUARIOS WHERE username = :username")
    UsuarioRoom obtenerUsuarioPorUsername(String username);

    // Método para actualizar un usuario en la base de datos
    @Update
    void actualizarUsuario(UsuarioRoom usuario);
}
