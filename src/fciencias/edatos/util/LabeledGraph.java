package fciencias.edatos.util;

import java.util.Hashtable;

/**
 * <h1>Labeled Graph interface.</h1>
 * <p>This interface provides operational specifications for an unweighted undirected simple graph.</p>
 * <h2>Definitions taken from Gordon College's course of <a href="http://math.gordon.edu/courses/mat230/handouts/graphs.pdf">Discrete Mathematics MAT230 (Fall 2016)</a>:</h2>
 * <ol>
 * <li>A <b>graph</b> G = (V, E) consists of a set V of vertices (also called nodes) and a set E of edges.</li>
 * <li>If an edge connects to a vertex we say the edge is <b>incident</b> to the vertex and say the vertex is an <b>endpoint</b> of the edge.</li>
 * <li>If an edge has only one endpoint then it is called a <b>loop edge</b>.</li>
 * <li>If two or more edges have the same endpoints then they are called <b>multiple or parallel</b> edges.</li>
 * <li>Two vertices that are joined by an edge are called <b>adjacent</b> vertices.</li>
 * <li>A <b>simple graph</b> is a graph with no loop edges or multiple edges. Edges in a simple graph may be specified by a set {v<sub>i</sub> , v<sub>j</sub>} of the two vertices that the edge makes adjacent.</li>
 * <li>The <b>degree</b> of a vertex is the number of edges incident to the vertex and is denoted deg(v).</li>
 * <li>A labeled graph is a simple graph in which all the vertices are labeled with a unique natural number.</li>
 * </ol>
 * <h2>Theorems:</h2>
 * <ol>
 * <li>In any graph with <i>n</i> vertices v<sub>i</sub> and <i>m</i> edges: <br><img src="./doc-files/theorem_01.gif"/></li>
 * </ol>
 * <h2>Axioms:</h2>
 * If {@code g} is a Graph, {@code v} and {@code u} are labels, and {@code t} is an object of type {@code T}, then:
 * <ol>
 * <li>For {@code g.contains(T)} to return true it's necessary that {@code g.addVertex(v, T)} was called in the past.</li>
 * <li>If {@code v} and {@code u} are in the graph, {@code g.areAdjacent(v,u)} <b>iff</b> {@code g.areAdjacent(u,v)}.</li>
 * <li>For {@code g.areAdjacent(v,u)} to return true it's necessary that {@code g.addEdge(v,u)} was called in the past. (It's very important that the implementation guarantees this.)</li>
 * </ol>
 *
 * @param <T> Type associated to the vertices of the graph.
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public interface LabeledGraph<T> {
    /**
     * Adds the provided vertex to the graph and labels it with the given label.
     * If the label is already taken, it overwrites the previous vertex with the new vertex.
     *
     * @param label  the label of the vertex.
     * @param vertex the vertex that should be added to the graph.
     * @return True if the graph was modified as a result of this operation, false otherwise.
     * @throws UnstableGraphException if completing this call would result in an unstable or invalid graph.
     */
    boolean addVertex(int label, T vertex) throws UnstableGraphException;

    /**
     * Adds the provided edge to the graph using the vertices' labels.
     *
     * @param fromV the label of the first vertex of the edge.
     * @param toU   the label of the second vertex of the edge.
     * @return True if the graph was modified as a result of this operation, false otherwise.
     * @throws UnstableGraphException if completing this call would result in an unstable or invalid graph.
     */
    boolean addEdge(int fromV, int toU) throws UnstableGraphException;

    /**
     * Finds the vertex corresponding to the given label.
     *
     * @param vertexKey the object the desired vertex holds.
     * @return The vertex that is labeled with the given vertexKey, null if it's not in the graph.
     */
    T getVertex(int vertexKey);

    /**
     * Returns the dictionary of label-vertex pairs of this graph.
     *
     * @return this graph's set of labeled vertices.
     */
    Hashtable<Integer, T> getLabeledVertices();

    /**
     * Tests if two vertices are adjacent using their labels. Ie, if there's an edge connecting them.
     *
     * @param fromV the label of the first vertex of the edge.
     * @param toV   the label of the second vertex of the edge.
     * @return True if the edge exists, false otherwise.
     * @throws UnstableGraphException if while completing this call an unstable or invalid graph is detected.
     */
    boolean areAdjacent(int fromV, int toV) throws UnstableGraphException;

    /**
     * Retrieves the cardinality of this graph's edge set. Per theorem 1, this is is half of the sum of the graph's vertices' degrees.
     *
     * @return How many edges are in this graph.
     * @throws UnstableGraphException if while completing this call an unstable or invalid graph is detected.
     */
    int edgesSize() throws UnstableGraphException;

    /**
     * Retrieves the cardinality of this graph's vertex set.
     *
     * @return How many vertices are in this graph.
     * * @throws UnstableGraphException   if while completing this call an unstable or invalid graph is detected.
     */
    int verticesSize() throws UnstableGraphException;

    /**
     * Checks if the graph is empty.
     *
     * @return True if it has no vertices, false otherwise.
     */
    boolean isEmpty();

    /**
     * Tests if the provided vertex is part of the graph.
     *
     * @param vertex The vertex for which membership is tested.
     * @return True if the labeledVertex is inside the graph, false otherwise.
     */
    boolean contains(T vertex);
}