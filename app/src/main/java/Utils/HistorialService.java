package Utils;

import com.arquitectura.apirest.Entidades.Historial;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HistorialService {

    @POST("registrar")
    Call<Historial> registrarHistorial(@Body Historial historial);
}
