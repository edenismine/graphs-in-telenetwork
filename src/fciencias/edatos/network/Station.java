package fciencias.edatos.network;

import fciencias.edatos.util.LabeledVertex;

import java.util.Collection;
import java.util.HashSet;

/**
 * Station class that represents the network's stations.
 *
 * @author Luis Daniel Aragon Bermudez.
 * @author Jorge Cortes Lopez.
 * @author Kai Ueda Kawasaki.
 */
public class Station implements LabeledVertex<Station> {

    /**
     * Taken area codes.
     */
    private static final HashSet<Integer> TAKEN_AREA_CODES = new HashSet<>();
    /**
     * Default error message, shown when a dummy station tries to perform an unsupported operation.
     */
    private static final String DUMMY_ERROR = " a dummy station, but dummy stations do not support this operation.";
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
     * The station's neighbors.
     */
    private HashSet<Station> neighbors;

    /**
     * This station's type, either real station or dummy station.
     */
    private TYPE type;

    /**
     * Creates an empty station with the provided name and area code (unique).
     *
     * @param name     The station's name.
     * @param areaCode The station's unique area code.
     * @param clients  The station's clients.
     * @throws DuplicateAreaCodeException if the provided area code is already taken.
     */
    public Station(String name, int areaCode, HashSet<Client> clients) throws DuplicateAreaCodeException {
        this.name = name;
        if (TAKEN_AREA_CODES.add(areaCode)) {
            this.areaCode = areaCode;
        } else {
            throw new DuplicateAreaCodeException();
        }
        if (clients != null && !clients.isEmpty()) {
            this.clients = clients;
            this.neighbors = new HashSet<>();
            this.type = TYPE.REAL;
        } else {
            throw new IllegalArgumentException("Stations must have at least one client");
        }
    }

    private Station(int areaCode) {
        this.areaCode = areaCode;
        this.type = TYPE.DUMMY;
    }

    public static Station getDummy(int areaCode) {
        return new Station(areaCode);
    }

    /**
     * Retrieves the station's name.
     *
     * @return The name of the station.
     */
    public String getStationName() {
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to set the areaCode of" + DUMMY_ERROR);
        return name;
    }

    /**
     * Changes the station's name with the provided String.
     *
     * @param name The station's new name.
     */
    public void setStationName(String name) {
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to set the areaCode of" + DUMMY_ERROR);
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
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to set the areaCode of" + DUMMY_ERROR);
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
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to get the clients of" + DUMMY_ERROR);
        return clients;
    }

    /**
     * Adds a client to the station, and returns a boolean to indicate if the station was modified as a result.
     *
     * @param client The client that should be added to the station.
     * @return True if the provided client was indeed a new, false otherwise.
     */
    public boolean addClient(Client client) {
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to add a client to" + DUMMY_ERROR);
        return clients.add(client);
    }

    /**
     * Retrieves the degree of this station.
     *
     * @return this station's degree.
     */
    @Override
    public int degree() {
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to get the degree of" + DUMMY_ERROR);
        return neighbors.size();
    }

    /**
     * Checks if this station is adjacent to the provided station.
     *
     * @param labeledVertex The provided station.
     * @return True if it's adjacent to this station, false otherwise.
     */
    @Override
    public boolean isAdjacent(LabeledVertex<Station> labeledVertex) {
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to test adjacency of" + DUMMY_ERROR);
        Station station;
        if (labeledVertex instanceof Station) {
            station = (Station) labeledVertex;
        } else {
            return false;
        }
        return this.neighbors.contains(station);
    }

    /**
     * Checks if this station is adjacent to the station that corresponds to the given areaCode.
     *
     * @param areaCode The provided areaCode.
     * @return True if it's adjacent to this station, false otherwise.
     */
    @Override
    public boolean isAdjacent(int areaCode) {
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to test adjacency of" + DUMMY_ERROR);
        for (Station station : neighbors)
            if (station.areaCode == areaCode)
                return true;
        return false;
    }

    @Override
    public int getLabel() {
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to get the label of" + DUMMY_ERROR);
        return getAreaCode();
    }

    @Override
    public Station getVertex() {
        return this;
    }

    @Override
    public Collection<Station> getNeighbors() {
        if (this.type == TYPE.DUMMY)
            throw new UnsupportedOperationException("Tried to get the neighbors of" + DUMMY_ERROR);
        return neighbors;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Station && ((Station) obj).areaCode == this.areaCode;
    }

    @Override
    public int hashCode() {
        return areaCode;
    }

    /**
     * There are real and dummy stations.
     */
    private static enum TYPE {
        DUMMY, REAL
    }
}