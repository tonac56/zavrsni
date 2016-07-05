package hr.fer.zavrsni.antoniosostar.application.parsing;

import hr.fer.zavrsni.antoniosostar.domain.graph.Edge;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import hr.fer.zavrsni.antoniosostar.domain.graph.MyGraph;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antonio on 1.5.2016..
 */
public class GraphParser {

    public static final String DELIMETER = " ";
    private String path;

    public GraphParser(String fileLocation) {
        path = fileLocation;
    }

    public MyGraph parseGEXF() throws FileNotFoundException, XMLStreamException {
        MyGraph graph = new MyGraph();
        Map<Integer, Vertex> vertices = new HashMap<>();
        FileInputStream fis = null;
        fis = new FileInputStream(path);
        XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        reader = xmlInFact.createXMLStreamReader(fis);
        while (reader.hasNext()) {
            int status = reader.next(); // do something here
            try {
                String nodeName = reader.getLocalName();
                if (nodeName.equals("node")) {
                    String nodeId = reader.getAttributeValue("", "id");
                    int index = Integer.parseInt(nodeId);
                    Vertex vertex = new Vertex(index);
                    graph.addVertex(vertex);
                    vertices.put(index, vertex);
                } else if (nodeName.equals("edge")) {
                    String source = reader.getAttributeValue("", "source");
                    String target = reader.getAttributeValue("", "target");
                    Vertex sourceVertex = vertices.get(Integer.parseInt(source));
                    Vertex targetVertex = vertices.get(Integer.parseInt(target));
                    graph.addEdge(new Edge(sourceVertex, targetVertex));
                }
            } catch (Throwable t) {
                int i;
            }
        }
        graph.addVertexNeigbours();
        return graph;
    }

    public MyGraph parseTxtToFrom() throws IOException {
        MyGraph graph = new MyGraph();
        Map<Integer, Vertex> vertices = new HashMap<>();
        for(String line : Files.readAllLines(Paths.get(path))){
            line = line.trim();
            if(!line.startsWith("#")){
                String[] parts = line.trim().split(DELIMETER);
                int fromId = Integer.parseInt(parts[0]);
                int toId = Integer.parseInt(parts[1]);
                Vertex vertexFrom = vertices.get(fromId);
                Vertex vertexTo = vertices.get(toId);
                if(vertexFrom == null){
                    vertexFrom = new Vertex(fromId);
                    vertices.put(fromId, vertexFrom);
                    graph.addVertex(vertexFrom);
                }
                if(vertexTo == null){
                    vertexTo = new Vertex(toId);
                    vertices.put(toId, vertexTo);
                    graph.addVertex(vertexTo);
                }
                graph.addEdge(new Edge(vertexFrom, vertexTo));
            }
        }
        graph.addVertexNeigbours();
        return graph;
    }
}
