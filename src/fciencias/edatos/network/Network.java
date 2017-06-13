package fciencias.edatos.network;

import fciencias.edatos.util.LabeledGraph;
import fciencias.edatos.util.UnstableGraphException;

import java.util.*;

/**
 * TODO: JavaDoc
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
     * This method performs BFS from this node and attempts to reach the station that
     * corresponds to the provided areaCode.
     *
     * @return The trajectory if it exists, null otherwise.
     */
    public List<Station> getTrajectory(Station root, int areaCode) {
        /**
         * This class represent a Parent,Station pair, useful for representing the trajectory as a list.
         */
        class Pair {
            Station parent;
            Station station;

            Pair(Station parent, Station station) {
                this.parent = parent;
                this.station = station;
            }
        }
        return null;
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
}
