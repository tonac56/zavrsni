package hr.fer.zavrsni.antoniosostar.application;

import hr.fer.zavrsni.antoniosostar.domain.graph.MyGraph;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import org.ejml.simple.SimpleMatrix;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antonio on 14.6.2016..
 */
public class Util {

    public static void saveGraphInfo(MyGraph graph, Path ouputMapPath) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(ouputMapPath);
        for (Vertex vertex : graph.getVertices()) {
            String s = vertex.getIndex() + " ";
            int numRows = vertex.getFrequencyVector().numRows();
            for (int i = 0; i < numRows; i++) {
                if (i == numRows - 1) {
                    s += vertex.getFrequencyVector().get(i, 0);

                } else {
                    s += vertex.getFrequencyVector().get(i, 0) + ",";
                }
            }
            s += "\n";
            writer.write(s, 0, s.length());
        }
        writer.close();
    }

    public static void readInputFromFileMap(MyGraph graph, Path ouputMapPath) throws IOException {
        Map<Integer, SimpleMatrix> vectors = new HashMap<>();
        for (String line : Files.readAllLines(ouputMapPath)) {
            String[] parts = line.split(" ");
            Integer index = Integer.parseInt(parts[0]);
            String[] values = parts[1].split(",");
            SimpleMatrix vector = new SimpleMatrix(values.length, 1);
            for (int i = 0; i < values.length; i++) {
                vector.set(i, 0, Double.parseDouble(values[i]));
            }
            vectors.put(index, vector);
        }
        for (Vertex v : graph.getVertices()) {
            v.setFrequencyVector(vectors.get(v.getIndex()));
        }
    }

    public static void displayGraph(MyGraph graph) {
        Graph graph1 = new SingleGraph("display");
        graph.getVertices().forEach(vertex -> {
            String id = String.valueOf(vertex.getIndex());
            Node n = graph1.addNode(id);
            n.addAttribute("ui.label", "vertex" + id);
        });
        final int[] i = {0};
        graph.getEdges().forEach(edge -> {
            try {

                graph1.addEdge("_" + i[0], graph1.getNode(String.valueOf(edge.getVertexA().getIndex())).getIndex(),
                        graph1.getNode(String.valueOf(edge.getVertexB().getIndex())).getIndex());
                i[0]++;
            } catch (Exception e) {
            }
        });
//        graph1.addAttribute("ui.stylesheet", "graph { font-size: 200%; }");

        graph1.display();
    }
}
