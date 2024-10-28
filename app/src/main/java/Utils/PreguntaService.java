package Utils;

import com.arquitectura.apirest.Entidades.Pregunta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PreguntaService {

    @GET("categoria/{categoria}/dificultad/{dificultad}")
    Call<List<Pregunta>> obtenerPreguntasPorCategoriaYDificultad(
            @Path("categoria") String categoria,
            @Path("dificultad") String dificultad
    );
}
