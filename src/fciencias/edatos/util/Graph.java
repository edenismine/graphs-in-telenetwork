package fciencias.edatos.util;

import java.util.Hashtable;

/**
 * This interface provides operational specifications for a Graph.
 *
 * @param <T> Type associated to the nodes of the graph.
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public interface Graph<T> {
    /**
     * Retrieves the cardinality of this graph's vertex set.
     *
     * @return How many vertices are in this graph.
     */
    int size();

    /**
     * Checks if the graph is empty.
     *
     * @return True if it has no vertices, false otherwise.
     */
    boolean isEmpty();

    /**
     * Adds the provided vertex to the graph.
     *
     * @param vertex the vertex that should be added to the graph.
     * @return True if the graph was modified as a result of this operation, false otherwise.
     */
    boolean addVertex(Vertex<T> vertex);

    /**
     * Adds the provided edge to the graph.
     *
     * @param fromV the first vertex of the edge.
     * @param toV   the second vertex of the edge.
     * @return True if the graph was modified as a result of this operation, false otherwise.
     */
    boolean addEdge(Vertex<T> fromV, Vertex<T> toV);

    /**
     * Finds the vertex in the graph that wraps the provided object.
     * @param VertexKey the object the desired vertex holds.
     * @return The vertex that holds the provided object, null if it's not in the graph.
     */
    Vertex<T> getVertex(T VertexKey);

    /**
     * Returns the set of vertices of this graph.
     * @return this graph's set of vertices.
     */
    Hashtable<T, Vertex<T>> getVertices();

    /**
     * Tests if the provided vertex is part of the graph.
     * @param vertex  The vertex for which membership is tested.
     * @return True if the vertex is inside the graph, false otherwise.
     */
    boolean contains(Vertex<T> vertex);
}