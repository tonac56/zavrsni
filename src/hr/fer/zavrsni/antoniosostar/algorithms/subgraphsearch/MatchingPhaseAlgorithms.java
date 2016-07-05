package hr.fer.zavrsni.antoniosostar.algorithms.subgraphsearch;

import hr.fer.zavrsni.antoniosostar.domain.graph.BipartitiveGraphEdge;
import hr.fer.zavrsni.antoniosostar.domain.graph.MyGraph;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import org.ejml.simple.SimpleMatrix;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Antonio on 10.5.2016..
 */
public class MatchingPhaseAlgorithms {

    /**
     * parameter k
     */
    public static final int PARAMETER_K = 10;

    /**
     * parameter lambda
     */
    public static final double PARAMETER_LAMBDA = 1;

    /**
     * min distance for vectors
     */
    public static final double MIN_DISTANCE = 1E-5;

    private MatchingPhaseAlgorithms() {
    }

    /**
     * For each vertex u of query graph is selected k vertices from search graph that are closest to u based on their Euclidean distance.
     *
     * @param graph      graph
     * @param queryGraph query graph
     * @return selected vertices.
     */
    public static Map<Vertex, Set<Vertex>> selectVertexSubset(MyGraph graph, MyGraph queryGraph) {
        Map<Vertex, Set<Vertex>> selectedVertices = new HashMap<>();

        queryGraph.getVertices().forEach(queryVertex -> {
            Map<Vertex, Double> distances = new HashMap<>();
            graph.getVertices().forEach(graphVertex -> distances.put(graphVertex, calculateEuclideanDistance(graphVertex.getFrequencyVector(), queryVertex.getFrequencyVector())));
            selectedVertices.put(queryVertex, getMinKDistances(distances, graph));
        });

        return selectedVertices;
    }

    private static Set<Vertex> getMinKDistances(Map<Vertex, Double> distances, MyGraph graphMap) {
        Set<Vertex> topK = new HashSet<>();
        Map<Vertex, Double> result = new LinkedHashMap<>();
        Stream<Map.Entry<Vertex, Double>> st = distances.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        final int[] i = {0};
        result.forEach((vertex, distance) -> {
            if (i[0] < PARAMETER_K || distance < MIN_DISTANCE) {
                topK.add(vertex);
                i[0]++;
            }
        });
        return topK;
    }

    private static double calculateEuclideanDistance(SimpleMatrix graphFv, SimpleMatrix queryFv) {
        double sum = 0;
        double sum1 = 0;
        double sum2 = 0;
        for (int i = 0; i < graphFv.numRows(); i++) {
            double v1 = graphFv.get(i, 0);
            double v2 = queryFv.get(i, 0);
            sum += Math.pow(v1 - v2, 2);
            sum1 += v1;
            sum2 += v2;
        }
        if (sum1 == 0 ^ sum2 == 0) {
            return 3 * (sum1 + sum2);
        }
        return Math.sqrt(sum);
    }

    public static List<BipartitiveGraphEdge> seedMatchGenerate(Map<Vertex, Set<Vertex>> selectedVertices) {
        MyGraph bipartitiveGraph = new MyGraph();
        Map<Vertex, Vertex> graphBipartitiveGraphMap = new HashMap<>();
        selectedVertices.forEach((vertex, vertexSimpleMatrixMap) -> {
            bipartitiveGraph.addVertex(new Vertex(vertex));
            vertexSimpleMatrixMap.forEach(v -> {
                bipartitiveGraph.addVertex(new Vertex(v));
            });
        });

        Map<Vertex, BipartitiveGraphEdge> bipartitiveGraphEdgeMap = new HashMap<>();
        double oneDivLamda = 1.0 / PARAMETER_LAMBDA;
        final int[] qSubsetCardinality = {1};
        List<BipartitiveGraphEdge> bipartitiveGraphEdges = new ArrayList<>();
        selectedVertices.forEach((v, vertexSimpleMatrixMap) -> {
            vertexSimpleMatrixMap.forEach(w -> {
                final double[] weight = {Math.pow(w.similarity(v), PARAMETER_LAMBDA)};
                List<Vertex> neighbors = new ArrayList<>(w.getNeigbors());
                neighbors.add(w);
                selectedVertices.forEach((vertex, vertices) -> {
                    if (vertex != v) {
                        qSubsetCardinality[0]++;
                        final double[] maxSimilarity = {0.0};
                        neighbors.forEach(neighbor -> {
                            if (vertices.contains(neighbor)) {
                                double similarity = vertex.similarity(neighbor);
                                if (similarity > maxSimilarity[0]) {
                                    maxSimilarity[0] = similarity;
                                }
                            }
                        });
                        weight[0] += Math.pow(maxSimilarity[0], PARAMETER_LAMBDA);
                    }
                });
                weight[0] = Math.pow(weight[0], oneDivLamda) / qSubsetCardinality[0];
                bipartitiveGraphEdges.add(new BipartitiveGraphEdge(v, w, weight[0]));
            });
        });

        return bipartitiveGraphEdges;
    }


}
