package fciencias.edatos.network;

import fciencias.edatos.util.Graph;
import fciencias.edatos.util.UnstableGraphException;
import fciencias.edatos.util.Vertex;

import java.util.Hashtable;

/**
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class Network implements Graph<Station> {
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
    public boolean addVertex(Vertex<Station> vertex) throws UnstableGraphException {
        return false;
    }

    @Override
    public boolean addEdge(Vertex<Station> fromV, Vertex<Station> toV) throws UnstableGraphException {
        return false;
    }

    @Override
    public Vertex<Station> getVertex(Station VertexKey) {
        return null;
    }

    @Override
    public Hashtable<Station, Vertex<Station>> getVertices() {
        return null;
    }

    @Override
    public boolean contains(Vertex<Station> vertex) {
        return false;
    }

    @Override
    public boolean areAdjacent(Vertex<Station> fromV, Vertex<Station> toV) throws UnstableGraphException {
        return false;
    }
}
