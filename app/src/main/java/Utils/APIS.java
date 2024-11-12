package Utils;

public class APIS {

    public static final String URL_001 = "https://backproduccion-1.onrender.com/Trivia/usuarios/";
    public static final String URL_002 = "https://backproduccion-1.onrender.com/Trivia/preguntas/";
    public static final String URL_003 = "https://backproduccion-1.onrender.com/Trivia/historial/";

    public static UsuarioService getUsuarioService(){
        return Cliente.getCliente(URL_001).create(UsuarioService.class);
    }

    public static PreguntaService getPreguntaService(){
        return Cliente.getCliente(URL_002).create(PreguntaService.class);
    }

    public static HistorialService getHistorialService(){
        return Cliente.getCliente(URL_003).create(HistorialService.class);
    }

}
