package fciencias.edatos.util;

/**
 * This exception is thrown whenever inconsistencies are found within a Graph object or if one of its methods is called with parameters that would render the graph inconsistent.
 *
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class UnstableGraphException extends RuntimeException {
    public UnstableGraphException() {
    }

    public UnstableGraphException(String message) {
        super(message);
    }
}
