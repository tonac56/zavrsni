package hr.fer.zavrsni.antoniosostar.domain.graph;

import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Antonio on 1.5.2016..
 */
public class Vertex {

    private int index;

    private Set<Vertex> neighbors;

    private SimpleMatrix frequencyVector;

    public Vertex(int index) {
        this.index = index;
        neighbors = new HashSet<>();
    }

    public Vertex(Vertex vertex) {
        this(vertex.index);
        frequencyVector = vertex.frequencyVector;
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

    @Override
    public String toString() {
        final String[] s = {", neigborVerteices=("};
        neighbors.forEach(vertex -> {
            s[0] += vertex.index + ",";
        });
        return "vertex" + index + s[0] + ")";
    }

    public void addNeighbor(Vertex vertex) {
        neighbors.add(vertex);
    }

    public Set<Vertex> getNeigbors() {
        return neighbors;
    }

    public boolean isNeighbour(Vertex vertex) {
        return neighbors.contains(vertex);
    }

    public SimpleMatrix getFrequencyVector() {
        return frequencyVector;
    }

    public void setFrequencyVector(SimpleMatrix frequencyVector) {
        this.frequencyVector = frequencyVector;
    }

    public double similarity(Vertex vertex) {
        double similarity = 0.0;
        for (int i = 0; i < frequencyVector.numRows(); i++) {
            similarity += frequencyVector.get(i, 0) * vertex.frequencyVector.get(i, 0);
        }
        return similarity;
    }
}
