package hr.fer.zavrsni.antoniosostar.domain.graph;

/**
 * Created by Antonio on 15.5.2016..
 */
public class Edge {

   private Vertex vertexA;

   private Vertex vertexB;

   public Edge( Vertex vertexA, Vertex vertexB ) {
      this.vertexA = vertexA;
      this.vertexB = vertexB;
   }

   public Vertex getVertexA() {
      return vertexA;
   }

   public Vertex getVertexB() {
      return vertexB;
   }
}
