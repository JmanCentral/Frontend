package Utils;

import com.arquitectura.apirest.Entidades.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioService {

    @POST("registro")
    Call<Usuario> registrarUsuario(@Body Usuario usuario);

    @POST("verificarUsuario")
    Call<Usuario>  loginUsuario(@Body Usuario usuario);

    @GET("verificarUsuarioExistente/{username}")
    Call<Usuario> verificarUsuarioExistente(@Path("username") String username);

    @PUT("{username}/nivel")
    Call<Usuario> actualizarNivel(@Path("username") String username);

    

}
