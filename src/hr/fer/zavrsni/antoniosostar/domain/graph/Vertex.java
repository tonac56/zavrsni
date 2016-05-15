package hr.fer.zavrsni.antoniosostar.domain.graph;

/**
 * Created by Antonio on 15.5.2016..
 */
public class Vertex {

    private int index;

    public Vertex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex)) {
            return false;
        }
        Vertex other = (Vertex) obj;
        return this.index == other.index;
    }

    @Override
    public int hashCode() {
        return index;
    }
}
