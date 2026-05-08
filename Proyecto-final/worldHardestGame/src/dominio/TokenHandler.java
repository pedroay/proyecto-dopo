package dominio;

/**
 * Functional interface defining a strategy to handle the parsing
 * and instantiation of a board cell from a string token.
 */
@FunctionalInterface
public interface TokenHandler {
    /**
     * Executes the strategy to populate the given row/col in the map.
     *
     * @param map     The board array
     * @param row     The row index
     * @param col     The col index
     * @param token   The raw token being parsed
     * @param context The WorldHG instance (allows handlers to register enemies, etc.)
     */
    void handle(Board[][] map, int row, int col, String token, WorldHG context);
}
