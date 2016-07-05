package hr.fer.zavrsni.antoniosostar.application;

import hr.fer.zavrsni.antoniosostar.algorithms.subgraphsearch.PreprocessingAlgorithms;
import hr.fer.zavrsni.antoniosostar.algorithms.subgraphsearch.MatchingPhaseAlgorithms;
import hr.fer.zavrsni.antoniosostar.application.parsing.GraphParser;
import hr.fer.zavrsni.antoniosostar.domain.graph.BipartitiveGraphEdge;
import hr.fer.zavrsni.antoniosostar.domain.graph.MyGraph;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import org.ejml.simple.SimpleMatrix;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Created by Antonio on 1.5.2016..
 */
public class Application {

    public static void main(String[] args) throws IOException, XMLStreamException, InterruptedException {
        //Pre-processing phase
        String fileLocation = "gexf/69789-118.gexf";
        if (args.length > 0) {
            fileLocation = args[0];
        }
        String fileName = fileLocation.substring(fileLocation.lastIndexOf('/'), fileLocation.lastIndexOf('.'));
        String outputMapFileName = "graphMaps" + fileName + "_" + PreprocessingAlgorithms.graphletSize + "_" + PreprocessingAlgorithms.bfsDepth + ".txt";
        Path ouputMapPath = Paths.get(outputMapFileName);
        GraphParser graphParser = new GraphParser(fileLocation);
        MyGraph graph = graphParser.parseGEXF();

        if (!Files.exists(ouputMapPath)) {
            PreprocessingAlgorithms.computeFrequencyVectors(graph);
            Util.saveGraphInfo(graph, ouputMapPath);
        } else {
            Util.readInputFromFileMap(graph, ouputMapPath);
        }
        //TODO: will we use a k-d tree structure or Map(Hashmap) is sufficient? For now map is OK because every vertex knows about its neighbors?

        //Matching phase
        String queryFileLocation = "gexf/doubleStar20-20.gexf";
        GraphParser queryGraphParser = new GraphParser(queryFileLocation);
        MyGraph queryGraph = queryGraphParser.parseGEXF();
        PreprocessingAlgorithms.computeFrequencyVectors(queryGraph);

        //selection phase
        Map<Vertex, Set<Vertex>> selectedVertices = MatchingPhaseAlgorithms.selectVertexSubset(graph, queryGraph);

        //seed match generation phase
        List<BipartitiveGraphEdge> bipartitiveGraph = MatchingPhaseAlgorithms.seedMatchGenerate(selectedVertices);
        Collections.sort(bipartitiveGraph, (o1, o2) -> {
            if (o2.getWeight() > o1.getWeight())
                return 1;
            else if (o2.getWeight() < o1.getWeight())
                return -1;
            else return 0;
        });
        System.out.println();
        if(queryFileLocation.contains("star")){
            bipartitiveGraph = (bipartitiveGraph.stream().filter(edge -> edge.getVertexW().getNeigbors().size() > 4)).collect(Collectors.toList());
        }
        Util.displayGraph(graph);

        Set<Vertex> vertices = new HashSet<>();
        bipartitiveGraph.forEach(bipartitiveGraphEdge -> System.out.println(bipartitiveGraphEdge.getVertexW()));
    }
}
