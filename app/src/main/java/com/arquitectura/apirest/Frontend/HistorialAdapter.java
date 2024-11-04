package com.arquitectura.apirest.Frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arquitectura.apirest.Entidades.Historial;
import com.arquitectura.apirest.R;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private List<Historial> historiales;
    private Context context;

    public HistorialAdapter(Context context, List<Historial> historiales) {
        this.context = context;
        this.historiales = historiales;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_historial, parent, false); // Reemplaza con el nombre de tu layout XML
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        Historial historial = historiales.get(position);

        // Configura los elementos de la vista con los datos del historial

        holder.username.setText(historial.getUsername());
        holder.puntosTotales.setText(String.valueOf(historial.getPuntaje()));
        holder.fecha.setText(historial.getFecha());
        holder.ayudas.setText(String.valueOf(historial.getAyudas()));
        holder.tiempo.setText(String.valueOf(historial.getTiempo()));
        holder.categoria.setText(historial.getCategoria());
        holder.dificultad.setText(historial.getDificultad());
    }

    @Override
    public int getItemCount() {
        return historiales.size();
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView username, puntosTotales, fecha, ayudas, tiempo, categoria, dificultad , id_historial , id_pregunta , id_usuario;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            puntosTotales = itemView.findViewById(R.id.overallPoints);
            fecha = itemView.findViewById(R.id.Fecha);
            ayudas = itemView.findViewById(R.id.Ayudas);
            tiempo = itemView.findViewById(R.id.Tiempo);
            categoria = itemView.findViewById(R.id.Categoria);
            dificultad = itemView.findViewById(R.id.Dificultad);
        }
    }
}

