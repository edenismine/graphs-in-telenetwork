package fciencias.edatos.network;

import fciencias.edatos.util.LabeledGraph;
import fciencias.edatos.util.UnstableGraphException;

import java.util.Hashtable;

/**
 * TODO: implement stubs.
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class Network implements LabeledGraph<Station> {
    /**
     * The network's stations.
     */
    private Hashtable<Integer, Station> stations = new Hashtable<>();

    @Override
    public boolean addVertex(int label, Station vertex) throws UnstableGraphException {
        return false;
    }

    @Override
    public boolean addEdge(int fromV, int toU) throws UnstableGraphException {
        return false;
    }

    @Override
    public Station getVertex(int vertexKey) {
        return null;
    }

    @Override
    public Hashtable<Integer, Station> getLabeledVertices() {
        return stations;
    }

    @Override
    public boolean areAdjacent(int fromV, int toV) throws UnstableGraphException {
        return false;
    }

    @Override
    public int edgesSize() throws UnstableGraphException {
        return 0;
    }

    @Override
    public int verticesSize() throws UnstableGraphException {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Station vertex) {
        return false;
    }
}
