package Utils;

public class APIS {

    public static final String URL_001 = "http://192.168.10.10:8862/Trivia/usuarios/";
    public static final String URL_002 = "http://192.168.10.10:8862/Trivia/preguntas/";

    public static UsuarioService getUsuarioService(){
        return Cliente.getCliente(URL_001).create(UsuarioService.class);
    }

    public static PreguntaService getPreguntaService(){
        return Cliente.getCliente(URL_002).create(PreguntaService.class);
    }

    /*
    public static HistorialService getRespuestaService(){
        return Cliente.getCliente(URL_001).create(HistorialService.class);
    }

     */
}
