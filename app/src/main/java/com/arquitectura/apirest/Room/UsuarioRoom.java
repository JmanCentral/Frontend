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
    private String logro;

    // Constructor


    public UsuarioRoom(Long id, String username, String password, String email, String logro) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.logro = logro;
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

    public String getLogro() {
        return logro;
    }

    public void setLogro(String logro) {
        this.logro = logro;
    }
}
