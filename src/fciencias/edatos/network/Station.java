package fciencias.edatos.network;

import java.util.HashSet;

/**
 * Station class that represents the network's stations.
 *
 * @author Luis Daniel Aragon Bermudez.
 * @author Jorge Cortes Lopez.
 * @author Kai Ueda Kawasaki.
 */
public class Station {

    private static final HashSet<Integer> TAKEN_AREA_CODES = new HashSet<>();
    /**
     * The station's name.
     */
    private String name;
    /**
     * The station's area code.
     */
    private int areaCode;
    /**
     * The clients connected to the station.
     */
    private HashSet<Client> clients;

    /**
     * Creates an empty station with the provided name and area code (unique).
     *
     * @param name     The station's name.
     * @param areaCode The station's unique area code.
     * @throws DuplicateAreaCodeException if the provided area code is already taken.
     */
    public Station(String name, int areaCode) throws DuplicateAreaCodeException {
        this.name = name;
        if (TAKEN_AREA_CODES.add(areaCode)) {
            this.areaCode = areaCode;
        } else {
            throw new DuplicateAreaCodeException();
        }
        this.clients = new HashSet<>();
    }

    /**
     * Retrieves the station's name.
     *
     * @return The name of the station.
     */
    public String getStationName() {
        return name;
    }

    /**
     * Changes the station's name with the provided String.
     *
     * @param name The station's new name.
     */
    public void setStationName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the station's unique area code.
     *
     * @return The unique area code of the Station.
     */
    public int getAreaCode() {
        return areaCode;
    }

    /**
     * Changes the station's area code with the provided identifier.
     *
     * @param areaCode The station's new areaCode.
     * @throws DuplicateAreaCodeException if the provided area code is already taken.
     */
    public void setAreaCode(int areaCode) throws DuplicateAreaCodeException {
        if (TAKEN_AREA_CODES.add(areaCode)) {
            TAKEN_AREA_CODES.remove(areaCode);
            this.areaCode = areaCode;
        } else {
            throw new DuplicateAreaCodeException();
        }
    }

    /**
     * Retrieves the clients connected to the station.
     *
     * @return The clients connected to the station.
     */
    public HashSet<Client> getClients() {
        return clients;
    }

    /**
     * Adds a client to the station, and returns a boolean to indicate if the station was modified as a result.
     *
     * @param client The client that should be added to the station.
     * @return True if the provided client was indeed a new, false otherwise.
     */
    public boolean addClient(Client client) {
        return clients.add(client);
    }
}