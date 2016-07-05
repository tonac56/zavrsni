package hr.fer.zavrsni.antoniosostar.domain.graph;

/**
 * Created by Antonio on 29.5.2016..
 */
public class BipartitiveGraphEdge {

    private Vertex vertexV;

    private Vertex vertexW;

    private double weight;

    public BipartitiveGraphEdge(Vertex vertexV, Vertex vertexW) {
        this.vertexV = vertexV;
        this.vertexW = vertexW;
    }

    public BipartitiveGraphEdge(Vertex vertexV, Vertex vertexW, double weight) {
        this(vertexV, vertexW);
        this.weight = weight;
    }

    public Vertex getVertexV() {
        return vertexV;
    }

    public Vertex getVertexW() {
        return vertexW;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "v=" + vertexV.getIndex() + ", w=" + vertexW.getIndex() + ", weight=" + weight;
    }
}
