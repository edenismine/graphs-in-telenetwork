package fciencias.edatos.network;

import fciencias.edatos.util.LabeledGraph;
import fciencias.edatos.util.UnstableGraphException;

import java.util.*;

/**
 * This class represents a labeled undirected simple graph that models a telecommunications network.
 *
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class Network implements LabeledGraph<Station> {
    /**
     * The network's stations.
     */
    private Hashtable<Integer, Station> stations = new Hashtable<>();

    @Override
    public boolean addVertex(int label, Station vertex) throws UnstableGraphException, IllegalArgumentException {
        if (vertex == null || label < 0)
            throw new IllegalArgumentException("Vertex labels must be non-negative. Vertices must be non-null.");
        if (label != vertex.getAreaCode()) throw new UnstableGraphException("The provided areaCode does not match.");
        Station previous = stations.put(label, vertex);
        return previous == null || previous.equals(vertex);
    }

    @Override
    public boolean addEdge(int fromV, int toU) throws UnstableGraphException, IllegalArgumentException {
        if (fromV < 0 || toU < 0) throw new IllegalArgumentException("Vertex labels must be non-negative.");
        if (fromV == toU) throw new UnstableGraphException("Loops cannot be added to a simple graph.");
        // Get corresponding vertices.
        Station getV = stations.get(fromV);
        Station getU = stations.get(toU);
        return addEdge(getV, getU);
    }

    @Override
    public boolean addEdge(Station v, Station u) throws UnstableGraphException, IllegalArgumentException {
        if (v == null || u == null) throw new IllegalArgumentException("Null station.");
        if (v.equals(u)) throw new UnstableGraphException("Loops cannot be added to a simple graph.");
        return v.addNeighbor(u);
    }

    @Override
    public Station getVertex(int vertexKey) {
        if (vertexKey < 0) throw new IllegalArgumentException("Vertex labels must be non-negative");
        return stations.get(vertexKey);
    }

    @Override
    public int edgesSize() throws UnstableGraphException {
        return degreeSum() / 2;
    }

    /**
     * This method doesn't explore the graph exhaustively, and checks if its degree sequence is graphical.
     * If the network is assumed to be stable, this method is quicker than rebuilding the network and then
     * counting the edges using the handshaking lemma.
     *
     * @return How many edges are in the graph.
     * @throws UnstableGraphException if an invalid network is detected while performing this call.
     */
    public int quickEdgesSize() throws UnstableGraphException {
        int n = stations.size();
        ArrayList<Integer> degreeSequence = new ArrayList<>(n);
        int acc = 0;
        for (Station station : stations.values()) {
            int d = station.degree();
            if (d < n) {
                acc += d;
                degreeSequence.add(d);
            } else {
                throw new UnstableGraphException("The degree of a vertex exceeds the available vertices in the graph, therefore this can't be a simple graph.");
            }
        }
        if (acc % 2 != 0)
            throw new UnstableGraphException("A graph with odd number of odd degree vertices was found, this can't be a valid graph.");
        Collections.sort(degreeSequence, Collections.<Integer>reverseOrder());
        if (!linearErdosGallai(degreeSequence))
            throw new UnstableGraphException("The obtained degree sequence is not graphical.");
        return acc / 2;
    }

    /**
     * This method exhaustively searches and counts all stations in the network.
     *
     * @return The amount of stations currently in the Network.
     * @throws UnstableGraphException In this implementation,
     */
    @Override
    public int verticesSize() throws UnstableGraphException {
        if (isEmpty())
            return 0;
        // Create a set to store visited nodes.
        HashSet<Station> visited = new HashSet<>();
        // Queue to remove recursion.
        Queue<Station> queue = new LinkedList<>();
        // Traverse from all accounted-for nodes.
        HashSet<Station> vertexSet = new HashSet<>(stations.values());
        for (Station root : vertexSet) {
            // If node has not been visited, perform BFS on it.
            if (!visited.contains(root)) {
                queue.offer(root);
                while (!queue.isEmpty()) {
                    Station current = queue.poll();
                    // visit all neighbors, and queue them for BFS.
                    for (Station neighbor : current.getNeighbors()) {
                        if (visited.add(neighbor)) {
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
        // remove all previously accounted-for stations.
        visited.removeAll(vertexSet);
        // add the newly discovered stations.
        for (Station newStation : visited) {
            stations.put(newStation.getAreaCode(), newStation);
        }

        return stations.size();
    }

    /**
     * This method performs BFS from the first station and attempts to reach the second station.
     *
     * @param a The first station.
     * @param b The second station.
     * @return The trajectory if it exists, null otherwise.
     */
    public List<Station> getTrajectory(Station a, Station b) {
        return getTrajectory(a.getAreaCode(), b.getAreaCode());
    }

    /**
     * This method performs BFS from the station corresponding to the first label and attempts to reach the station that
     * corresponds to the second label.
     *
     * @param areaCodeA The first label.
     * @param areaCodeB The second label.
     * @return The trajectory if it exists, null otherwise.
     */
    public List<Station> getTrajectory(int areaCodeA, int areaCodeB) {
        // If the network is empty, there are no trajectories.
        if (isEmpty())
            return new LinkedList<>();

        // Update and validate network.
        edgesSize();

        // If one of the nodes is not in the network, return empty trajectory.
        if (!stations.keySet().contains(areaCodeA) || !stations.keySet().contains(areaCodeB))
            return new LinkedList<>();

        Station root = stations.get(areaCodeA);

        /*
         * This class represent a Parent,Station pair, useful for representing the trajectory as a list.
         */
        class Pair {
            Station parent;
            Station station;

            private Pair(Station parent, Station station) {
                this.parent = parent;
                this.station = station;
            }
        }

        // Create a set to store visited nodes.
        HashSet<Station> visited = new HashSet<>();
        // Queue to remove recursion.
        Queue<Station> queue = new LinkedList<>();
        // Begin BFS
        boolean found = false;
        LinkedList<Pair> tree = new LinkedList<>();
        tree.add(new Pair(null, root));
        visited.add(root);
        queue.offer(root);
        while (!queue.isEmpty()) {
            Station current = queue.poll();
            // If we reach it.
            if (current.getAreaCode() == areaCodeB) {
                found = true;
                break;
            }
            // visit all neighbors, and queue them for BFS.
            for (Station neighbor : current.getNeighbors()) {
                if (visited.add(neighbor)) {
                    tree.add(new Pair(current, neighbor));
                    queue.offer(neighbor);
                }
            }
        }
        // If the node was reached, extract the trajectory.
        if (found) {
            LinkedList<Station> trajectory = new LinkedList<>();
            Station end = stations.get(areaCodeB);
            for (int i = tree.size() - 1; i >= 0; i--) {
                Pair current = tree.get(i);
                if (current.station.equals(end)) {
                    trajectory.add(end);
                    end = current.parent;
                }
            }
            Collections.reverse(trajectory);
            return trajectory;
        } else return new LinkedList<>();
    }

    @Override
    public boolean isEmpty() {
        return stations.size() == 0;
    }

    @Override
    public boolean contains(Station vertex) {
        return vertex != null && stations.get(vertex.getAreaCode()).equals(vertex);
    }

    @Override
    public Map<Integer, Station> getLabeledVertices() {
        // Update and validate network.
        edgesSize();
        return stations;
    }

    @Override
    public boolean areAdjacent(int i, int j) throws IllegalArgumentException {
        if (i < 0 || j < 0) throw new IllegalArgumentException("Labels should be non-negative.");
        Station a = stations.get(i);
        Station b = stations.get(j);
        return a != null && b != null && a.isAdjacent(b);
    }

    @Override
    public boolean areAdjacent(Station v, Station u) {
        return v.isAdjacent(u);
    }

    /**
     * Linear implementation of the <a href="https://en.wikipedia.org/wiki/Erd%C5%91s%E2%80%93Gallai_theorem">Erdos-Gallai criterion</a>.
     * Adapted from <a href="http://www.cs.elte.hu/egres/tr/egres-11-11.pdf">this paper</a>. Takes a decreasing degree sequence and tests
     * if it's graphical.
     *
     * @param degreeSequence Decreasing degree sequence of positive integers.
     * @return True if teh sequence is graphical, false otherwise.
     */
    private boolean linearErdosGallai(List<Integer> degreeSequence) {
        int n = degreeSequence.size();
        int w = n;
        int b = 0;
        int s = 0;
        int c = 0;
        for (int k = 1; k <= n; k++) {
            b += degreeSequence.get(k - 1);
            c += w - 1;
            while (w > k && degreeSequence.get(w - 1) <= k) {
                s += s + degreeSequence.get(w - 1);
                c += c - k;
                w--;
            }
            if (b > c + s) {
                return false;
            } else if (w == k) {
                return true;
            }
        }
        return false;
    }

    /**
     * Consolidates the graph, counting all vertices, then adds their degrees and returns teh network's degree sum.
     *
     * @return The degree sum of this network.
     */
    @Override
    public int degreeSum() throws UnstableGraphException {
        int n = verticesSize();
        int acc = 0;
        for (Station station : stations.values()) {
            int d = station.degree();
            if (d < n) {
                acc += d;
            } else {
                throw new UnstableGraphException("The degree of a vertex exceeds the available vertices in the graph, therefore this can't be a simple graph.");
            }
        }
        if (acc % 2 != 0)
            throw new UnstableGraphException("A graph with odd number of odd degree vertices was found, this can't be a valid graph.");
        return acc;
    }

    /**
     * Retrieves all the clients currently in the network, puts them in a list and sorts them by phone number.
     *
     * @return Sorted list with all the clients in the Network.
     */
    public ArrayList<Client> getAllClientsByPhone() {
        // Exhaustively expand the graph.
        int n = verticesSize();
        ArrayList<Client> clients = new ArrayList<>(n * 5);
        // Iterate over stations.
        Enumeration<Station> stations = this.stations.elements();
        while (stations.hasMoreElements()) {
            clients.addAll(stations.nextElement().getClients());
        }
        Collections.sort(clients);
        return clients;
    }

    /**
     * Retrieves all the clients currently in the network, puts them in a list and sorts them by station.
     *
     * @return Sorted list with all the clients in the Network.
     */
    public ArrayList<Client> getAllClientsByStation() {
        // Exhaustively expand the graph.
        int n = verticesSize();
        ArrayList<Client> clients = new ArrayList<>(n * 5);
        ArrayList<Client> stationClients;
        // Iterate over sorted stations.
        ArrayList<Station> stations = new ArrayList<>(this.stations.values());
        Collections.sort(stations);
        for (Station station : stations) {
            stationClients = new ArrayList<>(station.getClients());
            Collections.sort(stationClients);
            clients.addAll(stationClients);
        }
        return clients;
    }
}
