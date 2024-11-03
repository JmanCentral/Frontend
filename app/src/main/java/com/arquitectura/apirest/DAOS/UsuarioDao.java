package com.arquitectura.apirest.DAOS;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arquitectura.apirest.Room.UsuarioRoom;

@Dao
public interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsuario(UsuarioRoom usuarioRoom);

    @Query("SELECT * FROM USUARIOS WHERE username = :username AND password = :password LIMIT 1")
    UsuarioRoom verificarUsuario(String username, String password);
}
