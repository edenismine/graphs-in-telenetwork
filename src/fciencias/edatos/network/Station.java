package fciencias.edatos.network;

import fciencias.edatos.util.UnstableGraphException;

import java.util.HashSet;

/**
 * Station class that represents the network's stations. These stations are also labeled vertices, which means
 * that they can be used more easily bu the network; their area code is their unique label. Since the network
 * is a labeled graph, the following should hold true:
 * If {@code v} and {@code u} are labels, then:
 * <ol>
 * <li>If {@code v} and {@code u} are in the graph, {@code v.isAdjacent(u)} <b>iff</b> {@code u.isAdjacent(v)}.</li>
 * </ol>
 *
 * @author Luis Daniel Aragon Bermudez.
 * @author Jorge Cortes Lopez.
 * @author Kai Ueda Kawasaki.
 */
public class Station {

    /**
     * Taken area codes.
     */
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
     * The station's neighbors.
     */
    private HashSet<Station> neighbors;

    /**
     * Creates an empty station with the provided name and area code (unique).
     *
     * @param name     The station's name.
     * @param areaCode The station's unique area code.
     * @param clients  The station's clients.
     * @throws DuplicateAreaCodeException if the provided area code is already taken.
     */
    Station(String name, int areaCode, HashSet<Client> clients) throws DuplicateAreaCodeException {
        this.name = name;
        if (TAKEN_AREA_CODES.add(areaCode)) {
            this.areaCode = areaCode;
        } else {
            throw new DuplicateAreaCodeException();
        }
        if (clients != null && !clients.isEmpty()) {
            this.clients = clients;
            this.neighbors = new HashSet<>();
        } else {
            throw new IllegalArgumentException("Stations must have at least one client");
        }
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
    void setStationName(String name) {
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
     * Retrieves the current clients connected to the station.
     *
     * @return The clients currently connected to the station.
     */
    public HashSet<Client> getClients() {
        return new HashSet<>(clients);
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

    /**
     * Retrieves the degree of this station, ie: cardinality of it's neighbors set.
     *
     * @return this station's degree.
     */
    public int degree() {
        return neighbors.size();
    }

    /**
     * Checks if this station is adjacent to the provided station. Since this station is part
     * of a network, if a one-way connection is found, this implementation opts for completing the edge
     * and returning true.
     *
     * @param station The provided station.
     * @return True if it's adjacent to this station, false otherwise.
     */
    public boolean isAdjacent(Station station) {
        if (station == null)
            return false;
        boolean flag = this.neighbors.contains(station);
        if (flag != station.neighbors.contains(this)) {
            // When a directed edge is found, complete the edge and return true:
            station.neighbors.add(this);
            this.neighbors.add(station);
            return true;
        } else {
            // If edge is either complete or missing, we can easily return the value of any of the adjacency checks.
            return flag;
        }
    }

    /**
     * Checks if this station is adjacent to the station that corresponds to the given areaCode.
     * This method is not safe, as it doesn't check for a complete edge, when possible the method
     * that uses Stations should be used, also note that for a complete graph,
     * this method has time complexity O(n), even more reason to avoid using it.
     *
     * @param areaCode The provided areaCode.
     * @return True if the corresponding station is adjacent to this station, false otherwise.
     */
    public boolean isAdjacent(int areaCode) {
        for (Station station : this.neighbors)
            if (station.areaCode == areaCode)
                return true;
        return false;
    }

    /**
     * Retrieves the station's current adjacency set (copy).
     *
     * @return A copy of this station's current adjacency set.
     */
    HashSet<Station> getNeighbors() {
        return new HashSet<>(neighbors);
    }

    /**
     * Links this station with the provided station.
     *
     * @return True if a new edge was created, False if the edge already existed.
     */
    public boolean addNeighbor(Station neighbor) {
        if (neighbor == null) throw new IllegalArgumentException("Null station.");
        if (this.equals(neighbor)) throw new UnstableGraphException("Loops cannot be added to a simple graph.");
        // if edge already exists return false, else add neighbor and force edge creation through isAdjacent,
        // which will yield true this time around because of it'll complete the edge.
        return !this.isAdjacent(neighbor) && this.neighbors.add(neighbor) && this.isAdjacent(neighbor);
    }

    /**
     * For any vertex, the only parameter that conveys identity is the area code itself
     * even if other attributes differ, as an element of a vertex set, it should be treated
     * as the same objects to enable inplace modifications using "other" (memory-wise) objects.
     *
     * @param obj Object tested for equality.
     * @return True obj is a Station and obj has the same area code as this station.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Station && ((Station) obj).areaCode == this.areaCode;
    }

    /**
     * The hash function for stations should be based on their area code, sine this is their only defining feature.
     *
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return areaCode;
    }
}