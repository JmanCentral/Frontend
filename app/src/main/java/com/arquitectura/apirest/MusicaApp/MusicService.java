package com.arquitectura.apirest.MusicaApp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.arquitectura.apirest.R;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        int musicResource = intent.getIntExtra("MUSIC_RESOURCE", R.raw.trivia); // Recurso de música por defecto
        mediaPlayer = MediaPlayer.create(this, musicResource);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        return START_STICKY;
    }

    // Método para detener la música
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

