package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import app.Graph;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for the Graph class")
class GraphTest {

    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new Graph();
    }

    @Test
    @DisplayName("Newly created graph should be empty")
    void testEmptyGraph() {
        assertFalse(graph.hasVertex(1));
        assertFalse(graph.hasEdge(1, 2));
        assertEquals("", graph.toString().trim());
    }

    @Test
    @DisplayName("addVertex should correctly add a vertex")
    void testAddVertex() {
        graph.addVertex(5);
        assertTrue(graph.hasVertex(5));
        assertFalse(graph.hasVertex(6));

        // Adding the same vertex again should not change anything
        graph.addVertex(5);
        assertTrue(graph.hasVertex(5));
    }

    @Test
    @DisplayName("addVertex should reject negative vertex IDs")
    void testAddVertexNegativeId() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> graph.addVertex(-1)
        );
        assertTrue(exception.getMessage().contains("non-negative"));
    }

    @Test
    @DisplayName("addEdge should automatically create vertices and add bidirectional edge")
    void testAddEdgeCreatesVertices() {
        graph.addEdge(10, 20);

        assertTrue(graph.hasVertex(10));
        assertTrue(graph.hasVertex(20));
        assertTrue(graph.hasEdge(10, 20));
        assertTrue(graph.hasEdge(20, 10)); // undirected graph
    }

    @Test
    @DisplayName("addEdge should not create self-loops")
    void testAddEdgeSelfLoop() {
        Graph localGraph = new Graph();

        localGraph.addEdge(7, 7);

        assertFalse(localGraph.hasVertex(7),   // ← changed: vertex NOT created
                "Vertex should NOT be created on self-loop attempt");
        assertFalse(localGraph.hasEdge(7, 7),
                "Self-loop should NOT be present");
    }

    @Test
    @DisplayName("Adding an existing edge should not change the graph")
    void testAddExistingEdge() {
        graph.addEdge(1, 2);
        String stateBefore = graph.toString();

        graph.addEdge(1, 2);   // duplicate
        graph.addEdge(2, 1);   // reverse order

        assertEquals(stateBefore, graph.toString());
    }

    @Test
    @DisplayName("removeEdge should remove the bidirectional connection")
    void testRemoveEdge() {
        graph.addEdge(4, 5);
        graph.addEdge(5, 6);

        graph.removeEdge(4, 5);

        assertFalse(graph.hasEdge(4, 5));
        assertFalse(graph.hasEdge(5, 4));
        assertTrue(graph.hasEdge(5, 6)); // other edges remain
    }

    @Test
    @DisplayName("removeEdge on non-existing edge should do nothing")
    void testRemoveNonExistingEdge() {
        graph.addVertex(1);
        graph.addVertex(2);

        String stateBefore = graph.toString();
        graph.removeEdge(1, 2);
        assertEquals(stateBefore, graph.toString());

        graph.removeEdge(999, 888); // completely non-existing
        assertEquals(stateBefore, graph.toString());
    }

    @Test
    @DisplayName("removeVertex should remove the vertex and all incident edges")
    void testRemoveVertex() {
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(1, 3);

        graph.removeVertex(2);

        assertFalse(graph.hasVertex(2));
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 3));
        assertTrue(graph.hasEdge(1, 3)); // edge 1-3 remains

        String expected = "1 -> [3]\n3 -> [1]\n";
        assertEquals(expected, graph.toString());
    }

    @Test
    @DisplayName("removeVertex on non-existing vertex should do nothing")
    void testRemoveNonExistingVertex() {
        graph.addVertex(10);
        String stateBefore = graph.toString();

        graph.removeVertex(777);
        assertEquals(stateBefore, graph.toString());
    }

    @Test
    @DisplayName("toString should produce sorted and correctly formatted output")
    void testToStringFormatting() {
        graph.addEdge(5, 1);
        graph.addEdge(5, 8);
        graph.addEdge(1, 3);
        graph.addVertex(10); // isolated vertex

        String expected =
                "1 -> [3, 5]\n" +
                        "3 -> [1]\n" +
                        "5 -> [1, 8]\n" +
                        "8 -> [5]\n" +
                        "10 -> []\n";

        assertEquals(expected, graph.toString());
    }

    @Test
    @DisplayName("Complex scenario: add → remove → add again")
    void testComplexLifecycle() {
        graph.addEdge(100, 200);
        graph.addEdge(200, 300);
        graph.removeVertex(200);
        graph.addEdge(100, 300);

        assertTrue(graph.hasEdge(100, 300));
        assertFalse(graph.hasVertex(200));
        assertFalse(graph.hasEdge(100, 200));
    }

    @Test
    @DisplayName("Isolated vertex can be added and has no edges")
    void testIsolatedVertex() {
        graph.addVertex(42);
        assertTrue(graph.hasVertex(42));
        assertFalse(graph.hasEdge(42, 42));
        assertEquals("42 -> []\n", graph.toString());
    }
}