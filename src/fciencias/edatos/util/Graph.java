package fciencias.edatos.util;

import java.util.Hashtable;

/**
 * This interface provides operational specifications for a Graph.
 *
 * @param <T> Type associated to the nodes of the graph.
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public interface Graph<T> {
    int size();

    boolean isEmpty();

    boolean addVertex(Vertex<T> vertex);

    boolean addEdge(Vertex<T> fromV, Vertex<T> toV);

    Vertex<T> getVertex(T VertexKey);

    Hashtable<T, Vertex<T>> getVertices();

    boolean contains(Vertex<T> vertex);
}