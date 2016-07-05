package hr.fer.zavrsni.antoniosostar.application;

import hr.fer.zavrsni.antoniosostar.algorithms.subgraphsearch.PreprocessingAlgorithms;
import hr.fer.zavrsni.antoniosostar.application.parsing.GraphParser;
import hr.fer.zavrsni.antoniosostar.domain.graph.MyGraph;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Antonio on 14.6.2016..
 */
public class Main {
    public static void main(String[] args) throws IOException, XMLStreamException, InterruptedException {
        //Pre-processing phase

        String fileLocation = "classificationMaps/star3.txt";
        if (args.length > 0) {
            fileLocation = args[0];
        }
        long startTime = System.nanoTime();
        String fileName = fileLocation.substring(fileLocation.lastIndexOf('/'), fileLocation.lastIndexOf('.'));
        String outputMapFileName = "graphMaps2" + fileName + "_" + PreprocessingAlgorithms.graphletSize + "_" + PreprocessingAlgorithms.bfsDepth + ".txt";
        Path ouputMapPath = Paths.get(outputMapFileName);
        GraphParser graphParser = new GraphParser(fileLocation);
        MyGraph graph = graphParser.parseTxtToFrom();
        int repeat = 1;

        int neighbours = 0;
        int maxNeighbour = 0;
        for(Vertex v : graph.getVertices()){
            int size = v.getNeigbors().size();
            neighbours += size;
            if(size > maxNeighbour){
                maxNeighbour = size;
            }
        }
        PreprocessingAlgorithms.computeFrequencyVectors(graph);

        if (!Files.exists(ouputMapPath)) {
            for (int i = 0; i < repeat; i++) {
                PreprocessingAlgorithms.computeFrequencyVectors(graph);
//            System.out.println();
            }
            Util.saveGraphInfo(graph, ouputMapPath);
        } else {
            Util.readInputFromFileMap(graph, ouputMapPath);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration / 1000000.0 / 1000 / repeat);
    }
}
