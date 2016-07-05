package hr.fer.zavrsni.antoniosostar.domain.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Antonio on 1.5.2016..
 */
public class MyGraph {

    private Set<Vertex> vertices;

    private Set<Edge> edges;

    public MyGraph() {
        vertices = new HashSet<>();
        edges = new HashSet<>();
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public void addVertex(Vertex v) {
        if (!vertices.contains(v)) {
            vertices.add(v);
        }
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void addVertexNeigbours() {
        edges.forEach(edge -> {
            Vertex vertexA = edge.getVertexA();
            Vertex vertexB = edge.getVertexB();
            vertexA.addNeighbor(vertexB);
            vertexB.addNeighbor(vertexA);
        });
    }
}
