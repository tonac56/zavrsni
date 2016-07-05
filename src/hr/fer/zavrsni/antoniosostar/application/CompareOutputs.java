package hr.fer.zavrsni.antoniosostar.application;

import hr.fer.zavrsni.antoniosostar.algorithms.subgraphsearch.PreprocessingAlgorithms;
import hr.fer.zavrsni.antoniosostar.application.parsing.GraphParser;
import hr.fer.zavrsni.antoniosostar.domain.graph.MyGraph;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antonio on 4.7.2016..
 */
public class CompareOutputs {

    public static void main(String[] args) throws IOException {

        String fileLocation = "txt/CA-CondMat.txt";
        String fileName = fileLocation.substring(fileLocation.lastIndexOf('/'), fileLocation.lastIndexOf('.'));
        String outputMapFileName1 = "graphMaps" + fileName + "_" + PreprocessingAlgorithms.graphletSize + "_" + PreprocessingAlgorithms.bfsDepth + ".txt";
        String outputMapFileName2 = "graphMaps2" + fileName + "_" + PreprocessingAlgorithms.graphletSize + "_" + PreprocessingAlgorithms.bfsDepth + ".txt";
        Path ouputMapPath1 = Paths.get(outputMapFileName1);
        Path ouputMapPath2 = Paths.get(outputMapFileName2);
        GraphParser graphParser = new GraphParser(fileLocation);
        MyGraph graph1 = graphParser.parseTxtToFrom();
        MyGraph graph2 = graphParser.parseTxtToFrom();
        Util.readInputFromFileMap(graph1, ouputMapPath1);
        Util.readInputFromFileMap(graph2, ouputMapPath2);
        Map<Integer, Vertex> map1 = new HashMap<>();
        Map<Integer, Vertex> map2 = new HashMap<>();
        for (Vertex vertex1 : graph1.getVertices()) {
            map1.put(vertex1.getIndex(), vertex1);
        }
        for (Vertex vertex2 : graph2.getVertices()) {
            map2.put(vertex2.getIndex(), vertex2);
        }
        final double[] difference = {0.0};
        final int[] numofveciod01 = {0};
        map1.forEach((integer, vertex) -> {
            Vertex vertex2 = map2.get(integer);
            SimpleMatrix matrix1 = vertex.getFrequencyVector();
            SimpleMatrix matrix2 = vertex2.getFrequencyVector();
            for (int i = 0; i < matrix1.numRows(); i++) {
                double abs = Math.abs(matrix1.get(i, 0) - matrix2.get(i, 0));
                if(abs > 0.01){
                    numofveciod01[0]++;
                }
                difference[0] += abs;
            }
        });
        System.out.println(numofveciod01[0]);
        System.out.println(difference[0]);
        System.out.println(difference[0] / graph1.getVertices().size());
    }
}
