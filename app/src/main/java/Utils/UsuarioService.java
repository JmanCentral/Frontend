package Utils;

import com.arquitectura.apirest.Entidades.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioService {

    @POST("registro")
    Call<Usuario> registrarUsuario(@Body Usuario usuario);

    @POST("verificarUsuario")
    Call<Usuario>  loginUsuario(@Body Usuario usuario);

}
