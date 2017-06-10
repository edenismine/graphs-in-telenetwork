package fciencias.edatos.network;

/**
 * This exception is raised when a duplicate area code is provided when creating or modifying a Station.
 *
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class DuplicateAreaCodeException extends Exception {
    public DuplicateAreaCodeException() {
    }

    public DuplicateAreaCodeException(String message) {
        super(message);
    }
}
