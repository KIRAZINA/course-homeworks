package app;

import java.util.*;

/**
 * Class representing a simple undirected graph using adjacency list.
 */
public class Graph {
    // Adjacency list: each vertex maps to a set of connected vertices
    private Map<Integer, Set<Integer>> adjacencyList;

    /**
     * Constructs an empty graph.
     */
    public Graph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds a vertex to the graph.
     * Negative vertex IDs are not allowed.
     * @param vertex the vertex to add
     * @throws IllegalArgumentException if vertex < 0
     */
    public void addVertex(int vertex) {
        if (vertex < 0) {
            throw new IllegalArgumentException("Vertex ID must be non-negative.");
        }
        adjacencyList.putIfAbsent(vertex, new TreeSet<>());
    }

    /**
     * Adds an undirected edge between two vertices.
     * If the edge already exists, nothing changes.
     * @param source the first vertex
     * @param destination the second vertex
     */
    public void addEdge(int source, int destination) {
        if (source == destination) {
            return;
        }
        addVertex(source);
        addVertex(destination);
        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source);
    }
    /**
     * Removes a vertex and all its edges from the graph.
     * @param vertex the vertex to remove
     */
    public void removeVertex(int vertex) {
        if (adjacencyList.containsKey(vertex)) {
            for (Integer neighbor : adjacencyList.get(vertex)) {
                adjacencyList.get(neighbor).remove(vertex);
            }
            adjacencyList.remove(vertex);
        }
    }

    /**
     * Removes an undirected edge between two vertices.
     * @param source the first vertex
     * @param destination the second vertex
     */
    public void removeEdge(int source, int destination) {
        if (adjacencyList.containsKey(source)) {
            adjacencyList.get(source).remove(destination);
        }
        if (adjacencyList.containsKey(destination)) {
            adjacencyList.get(destination).remove(source);
        }
    }

    /**
     * Checks if a vertex exists in the graph.
     * @param vertex the vertex to check
     * @return true if the vertex exists, false otherwise
     */
    public boolean hasVertex(int vertex) {
        return adjacencyList.containsKey(vertex);
    }

    /**
     * Checks if an undirected edge exists between two vertices.
     * Since the graph is undirected, hasEdge(a,b) is equivalent to hasEdge(b,a).
     * @param source the first vertex
     * @param destination the second vertex
     * @return true if the edge exists, false otherwise
     */
    public boolean hasEdge(int source, int destination) {
        return adjacencyList.containsKey(source) &&
                adjacencyList.get(source).contains(destination);
    }

    /**
     * Returns a string representation of the graph.
     * Vertices and adjacency lists are sorted for stable output.
     * @return adjacency list as a string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer vertex : new TreeSet<>(adjacencyList.keySet())) {
            sb.append(vertex)
                    .append(" -> ")
                    .append(adjacencyList.get(vertex))
                    .append("\n");
        }
        return sb.toString();
    }

    /**
     * Demonstrates the functionality of the Graph class.
     */
    public static void main(String[] args) {
        Graph graph = new Graph();

        System.out.println("=== Adding vertices and edges ===");
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        System.out.println(graph);

        System.out.println("=== Checking existence ===");
        System.out.println("Has vertex 2? " + graph.hasVertex(2));
        System.out.println("Has edge 1-3? " + graph.hasEdge(1, 3));

        System.out.println("=== Removing edge 1-2 ===");
        graph.removeEdge(1, 2);
        System.out.println(graph);

        System.out.println("=== Removing vertex 2 ===");
        graph.removeVertex(2);
        System.out.println(graph);

        System.out.println("=== Attempting to remove non-existing elements ===");
        graph.removeEdge(99, 100);
        graph.removeVertex(777);
        System.out.println(graph);
    }
}
