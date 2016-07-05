package hr.fer.zavrsni.antoniosostar.domain.graph;

/**
 * Created by Antonio on 1.5.2016..
 */
public class Edge {

    private Vertex vertexA;

    private Vertex vertexB;

    public Edge(Vertex vertexA, Vertex vertexB) {
        this.vertexA = vertexA;
        this.vertexB = vertexB;
    }

    public Edge(Vertex vertexA, Vertex vertexB, float weight) {
        this(vertexA, vertexB);
    }

    public Edge(Edge edge) {
        vertexB = edge.vertexB;
        vertexA = edge.vertexA;
    }

    public Vertex getVertexA() {
        return vertexA;
    }

    public Vertex getVertexB() {
        return vertexB;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) {
            return false;
        }
        if (hashCode() != obj.hashCode()) {
            return false;
        }
        Edge other = (Edge) obj;
        if (vertexA.equals(other.getVertexA()) && vertexB.equals(other.vertexB)) {
            return true;
        }
        if (vertexA.equals(other.getVertexB()) && vertexB.equals(other.vertexA)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * vertexA.hashCode() + 31 * vertexB.hashCode();
    }
}
