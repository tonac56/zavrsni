package hr.fer.zavrsni.antoniosostar.application;

import hr.fer.zavrsni.antoniosostar.algorithms.subgraphsearch.PreprocessingAlgorithms;
import hr.fer.zavrsni.antoniosostar.application.parsing.GraphParser;
import hr.fer.zavrsni.antoniosostar.domain.graph.MyGraph;
import org.ejml.simple.SimpleMatrix;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Antonio on 5.7.2016..
 */
public class Classification {

    private enum Category {
        UNKNOWN(0, "none"), STAR(1, "star"), DOUBLE_STAR(2, "doubleStar"), FULLY_CONECTED(3, "fullyConnected");

        private int type_number;
        private String startFileName;

        Category(int cat, String name) {
            type_number = cat;
            startFileName = name;
        }

        public int getType_number() {
            return type_number;
        }

        public String getStartFileName() {
            return startFileName;
        }
    }

    public static void main(String[] args) throws IOException, XMLStreamException {
        Map<Category, List<SimpleMatrix>> graphletVectorsForCategory = new HashMap<>();
        File folder = new File("classificationMaps");
        File[] listOfFiles = folder.listFiles();
        for (Category category : Category.values()) {
            String fileLocationStart = category.getStartFileName();

            List<SimpleMatrix> graphletVectorList = new ArrayList<>();
            for (File file : listOfFiles) {
                if (file.getName().startsWith(fileLocationStart)) {
                    GraphParser graphParser = new GraphParser("classificationMaps/" + file.getName());
                    MyGraph graph = graphParser.parseTxtToFrom();
                    graphletVectorList.add(PreprocessingAlgorithms.computeFvForGraphletSize4(new ArrayList<>(graph.getVertices())));
                }
            }
            graphletVectorsForCategory.put(category, graphletVectorList);
        }

//        String fileLocation = "gexf/60-23.gexf";
        String fileLocation = "classificationMaps/star3.txt";
        GraphParser graphParser = new GraphParser(fileLocation);
        MyGraph graph = graphParser.parseTxtToFrom();
        SimpleMatrix targetGraphletVector = PreprocessingAlgorithms.computeFvForGraphletSize4(new ArrayList<>(graph.getVertices()));
        Map<Category, Double> statistic = new HashMap<>();
        for (Category category : Category.values()) {
            statistic.put(category, 0.0);
        }
        graphletVectorsForCategory.forEach((category, simpleMatrices) -> {
            simpleMatrices.forEach(simpleMatrix -> {
                    statistic.put(category, statistic.get(category) + similarity(simpleMatrix, targetGraphletVector));
            });
        });
        final Category[] classification = {Category.UNKNOWN};
        final double[] max = {0};
        statistic.forEach((category, similar) -> {
            if(similar > max[0]){
                classification[0] = category;
                max[0] = similar;
            }
        });
        System.out.println(classification[0]);
        Util.displayGraph(graph);
    }

    private static double similarity(SimpleMatrix frequencyVector, SimpleMatrix frequencyVector2) {
        double similarity = 0.0;
        for (int i = 0; i < frequencyVector.numRows(); i++) {
            similarity += frequencyVector.get(i, 0) * frequencyVector2.get(i, 0);
        }
        return similarity;
    }
}
