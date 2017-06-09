package fciencias.edatos.network;

/**
 * Client class. Represents clients connected to the network through the stations.
 *
 * @author Luis Daniel Aragon Bermudez.
 * @author Jorge Cortes Lopez.
 * @author Kai Ueda Kawasaki.
 */
public class Client {
    /**
     * The client's name.
     */
    private String name;
    /**
     * The client's phone number.
     */
    private int phone;

    /**
     * Creates a new client with all its attributes.
     *
     * @param name  The client's name.
     * @param phone The client's phone number.
     */
    public Client(String name, int phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * Retrieves the name of the client.
     *
     * @return The client's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the client's name with the provided string.
     *
     * @param name The client's new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the phone number of the client.
     *
     * @return The client's phone number.
     */
    public int getPhone() {
        return phone;
    }

    /**
     * Changes the client's phone number with the provided string.
     *
     * @param phone The client's new phone number.
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }
}