package com.arquitectura.apirest.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "USUARIOS")
public class UsuarioRoom {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String nivel;
    private String logro1;
    private String logro2;

    public UsuarioRoom(Long id, String username, String password, String email, String nivel, String logro1, String logro2) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nivel = nivel;
        this.logro1 = logro1;
        this.logro2 = logro2;
    }

    public UsuarioRoom() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getLogro1() {
        return logro1;
    }

    public void setLogro1(String logro1) {
        this.logro1 = logro1;
    }

    public String getLogro2() {
        return logro2;
    }

    public void setLogro2(String logro2) {
        this.logro2 = logro2;
    }
}
