package dominio;

public class WorldHGException extends Exception {

    public static final String LEVEL_NOT_FOUND = "El nivel no se pudo encontrar o cargar.";
    public static final String INVALID_POSITION = "Posición inválida en el tablero.";
    public static final String IO_ERROR = "Error de entrada/salida al procesar el archivo.";

    public WorldHGException(String message) {
        super(message);
    }

    public WorldHGException(String message, Throwable cause) {
        super(message, cause);
    }
}
