package Utils;

import com.arquitectura.apirest.Entidades.Historial;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistorialService {

    @POST("registrar")
    Call<Historial> registrarHistorial(@Body Historial historial);
    @GET("/usuario/{username}")
    Call<Historial> obtenerHistorialPorUsername(@Path("username") String username);
    @GET("/usuario/{username}/puntos-totales")
    Call<Historial> obtenerTotalPuntosPorUsuario(@Path("username")String username);
}
