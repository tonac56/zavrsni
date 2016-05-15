package hr.fer.zavrsni.antoniosostar.algorithms.subgraphsearch;

import java.util.*;

import hr.fer.zavrsni.antoniosostar.domain.graph.Edge;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import org.ejml.simple.SimpleMatrix;
import org.jgrapht.Graph;
import org.jgrapht.alg.NeighborIndex;

/**
 * Created by Antonio on 14.5.2016..
 */
public class ComputeLabelFv {

    public static final int MIN_GRAPHLET_SIZE = 3;

    public static final int MAX_GRAPHLET_SIZE = 5;

    public static int graphletSize = 3;

    public static int bfsDepth = 5;

    private static NeighborIndex<Vertex, Edge> neighborIndex;

    private ComputeLabelFv() {
    }

    public static void setGraphletSize(int graphletSize) {
        if (graphletSize < MIN_GRAPHLET_SIZE) {
            System.out.println("Min graphlet size is " + MIN_GRAPHLET_SIZE + ", graphlet size set to it!");
            ComputeLabelFv.graphletSize = MIN_GRAPHLET_SIZE;
        } else if (graphletSize > MAX_GRAPHLET_SIZE) {
            System.out.println("MAX graphlet size is " + MAX_GRAPHLET_SIZE + ", graphlet size set to it!");
            ComputeLabelFv.graphletSize = MAX_GRAPHLET_SIZE;
        } else {
            ComputeLabelFv.graphletSize = graphletSize;
        }
    }

    public static void setBfsDepth(int bfsDepth) {
        ComputeLabelFv.bfsDepth = bfsDepth;
    }

    public static Map<Vertex, SimpleMatrix> compute(Graph<Vertex, Edge> graph) {
        neighborIndex = new NeighborIndex<>(graph);
        Map<Vertex, SimpleMatrix> vectorsFv = new HashMap<>();
        for (Vertex vertex : graph.vertexSet()) {
            vectorsFv.put(vertex, computeFv(vertex));
        }
        return vectorsFv;
    }

    private static SimpleMatrix computeFv(Vertex vertex) {
        Set<Vertex> neighborhood = neighborIndex.neighborsOf(vertex);
        for (int neighbourDistance = 1; neighbourDistance < bfsDepth; neighbourDistance++) {
            Set<Vertex> newNeighborhood = new HashSet<>();
            neighborhood.stream().forEach(v -> newNeighborhood.addAll(neighborIndex.neighborsOf(v)));
            neighborhood.addAll(newNeighborhood);
        }
        SimpleMatrix fv;
        switch (graphletSize) {
            case 3:
                fv = computeFvForGraphletSize3(neighborhood);
                break;
            case 4:
                fv = computeFvForGraphletSize4(neighborhood);
                break;
            case 5:
                fv = computeFvForGraphletSize5(neighborhood);
                break;
            default:
                throw new IllegalArgumentException("Supported graphlet sizes are 3, 4, 5");
        }
        return fv;
    }

    private static SimpleMatrix computeFvForGraphletSize3(Set<Vertex> neighborhood) {
        for(ThreeVertices threeVertices : computeAllThreeVerticesCombos(neighborhood)){

        }
        return null;
    }

    private static List<ThreeVertices> computeAllThreeVerticesCombos(Set<Vertex> neighborhood) {

        return null;
    }

    private static SimpleMatrix computeFvForGraphletSize4(Set<Vertex> neighborhood) {
        return null;
    }

    private static SimpleMatrix computeFvForGraphletSize5(Set<Vertex> neighborhood) {
        return null;
    }

    private class ThreeVertices {
        Vertex first;
        Vertex second;
        Vertex third;

        public ThreeVertices(Vertex first, Vertex second, Vertex third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }
}
