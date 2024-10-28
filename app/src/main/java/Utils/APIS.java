package Utils;

public class APIS {

    public static final String URL_001 = "http://192.168.10.10:8862/Trivia/usuarios/";

    public static UsuarioService getUsuarioService(){
        return Cliente.getCliente(URL_001).create(UsuarioService.class);
    }
}
