package fciencias.edatos.util;

import java.util.Collection;

/**
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public interface LabeledVertex<T> {
    /**
     * Retrieves the degree of this vertex.
     *
     * @return this station's degree.
     */
    int degree();

    /**
     * Checks if this labeledVertex is adjacent to the provided labeledVertex.
     *
     * @param labeledVertex The provided labeledVertex.
     * @return True if it's adjacent to this labeledVertex, false otherwise.
     */
    boolean isAdjacent(LabeledVertex<T> labeledVertex);

    /**
     * Checks if this vertex is adjacent to the vertex that corresponds to the given label.
     *
     * @param label The provided label.
     * @return True if it's adjacent to this station, false otherwise.
     */
    boolean isAdjacent(int label);

    int getLabel();

    T getVertex();

    Collection<T> getNeighbors();
}
