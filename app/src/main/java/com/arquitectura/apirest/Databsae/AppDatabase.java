package com.arquitectura.apirest.Databsae;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.arquitectura.apirest.DAOS.HistorialDao;
import com.arquitectura.apirest.DAOS.PreguntaDao;
import com.arquitectura.apirest.DAOS.UsuarioDao;
import com.arquitectura.apirest.Room.HistorialRoom;
import com.arquitectura.apirest.Room.PreguntaRoom;
import com.arquitectura.apirest.Room.UsuarioRoom;

@Database(entities = {UsuarioRoom.class , PreguntaRoom.class , HistorialRoom.class},  version = 5)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract PreguntaDao preguntaDao();
    public abstract HistorialDao historialDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "TriviaAPP")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

