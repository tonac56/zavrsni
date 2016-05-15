package hr.fer.zavrsni.antoniosostar.application;

import hr.fer.zavrsni.antoniosostar.application.parsing.GraphParser;
import hr.fer.zavrsni.antoniosostar.domain.graph.Edge;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import org.jgrapht.Graph;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

/**
 * Created by Antonio on 15.5.2016..
 */
public class Application {

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        GraphParser graphParser = new GraphParser("gexf/60-23.gexf");
        Graph<Vertex, Edge> graph = graphParser.parse();
        System.out.println(graph);
    }
}
