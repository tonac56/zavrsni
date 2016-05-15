package hr.fer.zavrsni.antoniosostar.application.parsing;

import hr.fer.zavrsni.antoniosostar.domain.graph.Edge;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.Pseudograph;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Antonio on 15.5.2016..
 */
public class GraphParser {

   private String path;

   public GraphParser( String fileLocation ) {
      path = fileLocation;
   }

   public UndirectedGraph<Vertex, Edge> parse() throws FileNotFoundException, XMLStreamException {
      Pseudograph<Vertex, Edge> graph = new Pseudograph<Vertex, Edge>( Edge::new);
      ArrayList<Vertex> vertices = new ArrayList<>();
      FileInputStream fis = null;
      fis = new FileInputStream( path );
      XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
      XMLStreamReader reader = null;
      reader = xmlInFact.createXMLStreamReader( fis );
      while ( reader.hasNext() ) {
        int status = reader.next(); // do something here
         try {
            String nodeName = reader.getLocalName();
            if(nodeName.equals("node")){
               String nodeId = reader.getAttributeValue("","id");
               Vertex vertex = new Vertex(Integer.parseInt(nodeId));
               graph.addVertex(vertex);
               vertices.add(vertex);
            } else if(nodeName.equals("edge")){
               String source = reader.getAttributeValue("","source");
               String target = reader.getAttributeValue("","target");
               Vertex sourceVertex = vertices.get(Integer.parseInt(source));
               Vertex targetVertex = vertices.get(Integer.parseInt(target));
               graph.addEdge(sourceVertex, targetVertex);
            }
         }catch (Throwable t){
            int i;
         }
      }
      return graph;
   }
}
