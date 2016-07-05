
package hr.fer.zavrsni.antoniosostar.algorithms.subgraphsearch;

import hr.fer.zavrsni.antoniosostar.domain.graph.Edge;
import hr.fer.zavrsni.antoniosostar.domain.graph.MyGraph;
import hr.fer.zavrsni.antoniosostar.domain.graph.Vertex;
import org.ejml.simple.SimpleMatrix;

import java.util.*;

/**
 * Created by Antonio on 6.5.2016..
 */
public class PreprocessingAlgorithms {

    public static final int MIN_GRAPHLET_SIZE = 3;

    public static final int MAX_GRAPHLET_SIZE = 5;

    public static int graphletSize = 4;

    public static int bfsDepth = 2;

    public static int neighbours = 0;

    public static int maxNeighbours = 0;

    public static int veceOd100 = 0;

    public static int counted = 0;

    public static int all = 0;

    public static SimpleMatrix statistic = new SimpleMatrix(6, 1);

    private PreprocessingAlgorithms() {
    }

    public static void setGraphletSize(int graphletSize) {
        if (graphletSize < MIN_GRAPHLET_SIZE) {
            System.out.println("Min graphlet size is " + MIN_GRAPHLET_SIZE + ", graphlet size set to it!");
            PreprocessingAlgorithms.graphletSize = MIN_GRAPHLET_SIZE;
        } else if (graphletSize > MAX_GRAPHLET_SIZE) {
            System.out.println("MAX graphlet size is " + MAX_GRAPHLET_SIZE + ", graphlet size set to it!");
            PreprocessingAlgorithms.graphletSize = MAX_GRAPHLET_SIZE;
        } else {
            PreprocessingAlgorithms.graphletSize = graphletSize;
        }
    }

    public static void setBfsDepth(int bfsDepth) {
        PreprocessingAlgorithms.bfsDepth = bfsDepth;
    }

    public static void computeFrequencyVectors(MyGraph graph) throws InterruptedException {
        List<MyThread> threads = new ArrayList<>();

        List<Vertex> vertices1 = new ArrayList<>();
        List<Vertex> vertices2 = new ArrayList<>();
        List<Vertex> vertices3 = new ArrayList<>();
        List<Vertex> vertices4 = new ArrayList<>();
        int counter = 0;
        int size = graph.getVertices().size();
        all = size;
        for (Vertex vertex : graph.getVertices()) {
            if (counter < size / 4) {
                vertices1.add(vertex);
            } else if (counter < size / 2) {
                vertices2.add(vertex);
            } else if (counter < 3 * size / 4) {
                vertices3.add(vertex);
            } else {
                vertices4.add(vertex);
            }
            counter++;
//            vertex.setFrequencyVector(computeFrequencyVector(vertex));
//            counted++;
//            System.out.println(counted);
        }
        threads.add(new MyThread(vertices1));
        threads.add(new MyThread(vertices2));
        threads.add(new MyThread(vertices3));
        threads.add(new MyThread(vertices4));
        for (MyThread thread : threads) {
            thread.start();
        }
        for (MyThread thread : threads) {
            thread.join();
        }
        counted = 0;
        System.out.println(statistic);
    }

    private static class MyThread extends Thread {

        private List<Vertex> vertices;

        public MyThread(List<Vertex> vertices) {
            this.vertices = vertices;
        }

        @Override
        public void run() {
            vertices.forEach(vertex -> {
                vertex.setFrequencyVector(computeFrequencyVector(vertex));
                counted++;
                if (counted % 100 == 0) {
                    System.out.println((int) (100.0 * counted / all) + "%");
                }

            });
        }
    }

    private static SimpleMatrix computeFrequencyVector(Vertex vertex) {
        ArrayList<Vertex> neighborhood = getNeighborhood(vertex);
        int size = neighborhood.size();
        if (size > maxNeighbours) {
            maxNeighbours = size;
        }
        if (size > 100) {
            veceOd100++;
        }
        neighbours += size;
        SimpleMatrix fv;
        switch (graphletSize) {
            case 3:
                fv = computeFvForGraphletSize3(neighborhood);
                break;
            case 4:
                fv = computeFvForGraphletSize4(neighborhood);
                break;
            case 5:
                fv = computeFvForGraphletSize5(neighborhood);
                break;
            default:
                throw new IllegalArgumentException("Supported graphlet sizes are 3, 4, 5");
        }
        return fv;
    }

    public static ArrayList<Vertex> getNeighborhood(Vertex vertex) {
        Set<Vertex> neighborhood = new HashSet<>();
        neighborhood.add(vertex);
        neighborhood.addAll(vertex.getNeigbors());
        for (int neighbourDistance = 1; neighbourDistance < bfsDepth; neighbourDistance++) {
            Set<Vertex> newNeighborhood = new HashSet<>();
            neighborhood.stream().forEach(v -> newNeighborhood.addAll(v.getNeigbors()));
            neighborhood.addAll(newNeighborhood);
        }
        return new ArrayList<>(neighborhood);
    }

    private static SimpleMatrix computeFvForGraphletSize3(List<Vertex> neighborhood) {
        SimpleMatrix matrix = new SimpleMatrix(2, 1);
        double oneConnectionNumber = 0;
        double twoConnectionNumber = 0;
        double threeConnectionNumber = 0;
        int size = neighborhood.size();
        for (Vertex vertex1 : neighborhood) {
            for (Vertex vertex2 : neighborhood) {
                if (vertex1.isNeighbour(vertex2)) {
                    for (Vertex vertex3 : neighborhood) {
                        ThreeVertices threeVertices = new ThreeVertices(vertex1, vertex2, vertex3);
                        int numberOfConnections = threeVertices.getNumberOfConnections();
                        if (numberOfConnections == 1) {
                            oneConnectionNumber++;
                        } else if (numberOfConnections == 2) {
                            twoConnectionNumber++;
                        } else if (numberOfConnections == 3) {
                            threeConnectionNumber++;
                        }
                    }
                }
            }
        }
        threeConnectionNumber /= 6;
        twoConnectionNumber /= 4;
        matrix.set(0, 0, twoConnectionNumber);
        matrix.set(1, 0, threeConnectionNumber);
//        matrix.set(2, 0, threeConnectionNumber);
        double normF = matrix.normF();
        if (normF != 0) {
            return matrix.divide(normF);
        } else {
            return matrix;
        }
    }

    private static List<ThreeVertices> computeAllThreeVerticesCombos(List<Vertex> neighborhood) {

        List<ThreeVertices> threeVertices = new ArrayList<>();

        int size = neighborhood.size();
        for (int counter1 = 0; counter1 < size; counter1++) {
            Vertex vertex1 = neighborhood.get(counter1);
            for (int counter2 = counter1 + 1; counter2 < size; counter2++) {
                Vertex vertex2 = neighborhood.get(counter2);
                for (int counter3 = counter2 + 1; counter3 < size; counter3++) {
                    Vertex vertex3 = neighborhood.get(counter3);
                    threeVertices.add(new ThreeVertices(vertex1, vertex2, vertex3));
                }
            }
        }

//        for (Vertex vertex1 : neighborhood) {
//            for (Vertex vertex2 : neighborhood) {
//                for (Vertex vertex3 : neighborhood) {
//                    if (vertex1 != vertex2 && vertex3 != vertex2 && vertex1 != vertex3) {
//                        threeVertices.add(new ThreeVertices(vertex1, vertex2, vertex3));
//                    }
//                }
//            }
//        }
        return threeVertices;
    }

    private static List<FourVertices> computeAllFourVerticesCombos(ArrayList<Vertex> neighborhood) {
        List<FourVertices> fourVertices = new ArrayList<>();
        int size = neighborhood.size();
//        for (int counter1 = 0; counter1 < size; counter1++) {
//            Vertex vertex1 = neighborhood.get(counter1);
//            for (int counter2 = counter1 + 1; counter2 < size; counter2++) {
//                Vertex vertex2 = neighborhood.get(counter2);
//                for (int counter3 = counter2 + 1; counter3 < size; counter3++) {
//                    Vertex vertex3 = neighborhood.get(counter3);
//                    for (int counter4 = counter3 + 1; counter4 < size; counter4++) {
//                        Vertex vertex4 = neighborhood.get(counter4);
//                        fourVertices.add(new FourVertices(vertex1, vertex2, vertex3, vertex4));
//                    }
//                }
//            }
//        }
        for (Vertex vertex1 : neighborhood) {
            for (Vertex vertex2 : neighborhood) {
                if (vertex1.isNeighbour(vertex2)) {
                    for (Vertex vertex3 : neighborhood) {
                        if (vertex1 != vertex3 && vertex2 != vertex3) {
                            for (Vertex vertex4 : neighborhood) {
                                if (vertex1 != vertex4 && vertex2 != vertex4 && vertex4 != vertex3)
                                    fourVertices.add(new FourVertices(vertex1, vertex2, vertex3, vertex4));
                            }
                        }
                    }
                }
            }
        }
        return fourVertices;
    }

    public static void main(String[] args) throws InterruptedException {
        Vertex vertex = new Vertex(1);
        Vertex vertex2 = new Vertex(2);
        Vertex vertex3 = new Vertex(3);
        Vertex vertex4 = new Vertex(4);
        MyGraph myGraph = new MyGraph();
        myGraph.addVertex(vertex);
        myGraph.addVertex(vertex2);
        myGraph.addVertex(vertex3);
        myGraph.addVertex(vertex4);
        myGraph.addEdge(new Edge(vertex, vertex2));
        myGraph.addEdge(new Edge(vertex2, vertex3));
        myGraph.addEdge(new Edge(vertex3, vertex4));
        myGraph.addVertexNeigbours();
        computeFrequencyVectors(myGraph);
    }

    public static SimpleMatrix computeFvForGraphletSize4(ArrayList<Vertex> neighborhood) {
        SimpleMatrix matrix = new SimpleMatrix(6, 1);
        double connection0 = 0;
        double connection1 = 0;
        double connection2 = 0;
        double connection3 = 0;
        double connection4 = 0;
        double connection5 = 0;

        int size = neighborhood.size();
        Random rand = new Random();
        int mod = 1;
        int plus = 1;
        if (size > 3) {
            if (size > 100000) {
                mod = (int) (size / (Math.pow(4, size / 500000)));
                plus = 1000;
            } else if (size > 50000) {
                mod = 5000;
                plus = 500;
            } else if (size > 10000) {
                mod = 500;
                plus = 50;
            } else if (size > 1000) {
                mod = 50;
                plus = 10;
            } else if (size > 300) {
                mod = 6;
                plus = 3;
            }
            for (int index1 = 0; index1 < size; index1 += rand.nextInt() % mod + plus) {
                int index1get = index1;
                if (index1get < 0) {
                    index1get = 0;
                }
                Vertex vertex1 = neighborhood.get(index1get);
                for (int index2 = 0; index2 < size; index2 += rand.nextInt() % mod + plus) {
                    int index2get = index2;
                    if (index2get < 0) {
                        index2get = 0;
                    }
                    Vertex vertex2 = neighborhood.get(index2get);
                    if (vertex1.isNeighbour(vertex2)) {
                        for (int index3 = 0; index3 < size; index3 += rand.nextInt() % mod + plus) {
                            int index3get = index3;
                            if (index3get < 0) {
                                index3get = 0;
                            }
                            Vertex vertex3 = neighborhood.get(index3get);
                            if (vertex1.isNeighbour(vertex3) || vertex2.isNeighbour(vertex3)) {
                                for (int index4 = 0; index4 < size; index4 += rand.nextInt() % mod + plus) {
                                    int index4get = index4;
                                    if (index4get < 0) {
                                        index4get = 0;
                                    }
                                    Vertex vertex4 = neighborhood.get(index4get);
                                    if (vertex1 != vertex4 && vertex1 != vertex3 && vertex3 != vertex2 && vertex4 != vertex2 && vertex4 != vertex3) {
                                        FourVertices fourVertices = new FourVertices(vertex1, vertex2, vertex3, vertex4);
                                        int connectionType = fourVertices.getConnectionType();
                                        switch (connectionType) {
                                            case 5:
                                                connection5++;
                                                break;
                                            case 4:
                                                connection4++;
                                                break;
                                            case 3:
                                                connection3++;
                                                break;
                                            case 2:
                                                connection2++;
                                                break;
                                            case 1:
                                                connection1++;
                                                break;
                                            case 0:
                                                connection0++;
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        connection5 /= 24;
        connection4 /= 20;
        connection3 /= 14;
        connection2 /= 12;
        connection1 /= 16;
        connection0 /= 8;
        matrix.set(0, 0, connection0);
        matrix.set(1, 0, connection1);
        matrix.set(2, 0, connection2);
        matrix.set(3, 0, connection3);
        matrix.set(4, 0, connection4);
        matrix.set(5, 0, connection5);
        statistic.set(0, 0, connection0 + statistic.get(0, 0));
        statistic.set(1, 0, connection1 + statistic.get(1, 0));
        statistic.set(2, 0, connection2 + statistic.get(2, 0));
        statistic.set(3, 0, connection3 + statistic.get(3, 0));
        statistic.set(4, 0, connection4 + statistic.get(4, 0));
        statistic.set(5, 0, connection5 + statistic.get(5, 0));

        double normF = matrix.normF();
        if (normF != 0) {
            return matrix.divide(normF);
        } else {
            return matrix;
        }
    }

    private static SimpleMatrix computeFvForGraphletSize5(ArrayList<Vertex> neighborhood) {
        return null;
    }

    private static class ThreeVertices {
        Vertex first;
        Vertex second;
        Vertex third;

        public ThreeVertices(Vertex first, Vertex second, Vertex third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public int getNumberOfConnections() {
            int count = 0;
            if (first.isNeighbour(second)) {
                count++;
            }
            if (first.isNeighbour(third)) {
                count++;
            }
            if (second.isNeighbour(third)) {
                count++;
            }
            return count;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ThreeVertices)) {
                return false;
            }
            ThreeVertices other = (ThreeVertices) obj;
            Set<Vertex> vertices1 = new HashSet<>(3);
            vertices1.add(first);
            vertices1.add(second);
            vertices1.add(third);
            Set<Vertex> vertices2 = new HashSet<>(3);
            vertices2.add(other.first);
            vertices2.add(other.second);
            vertices2.add(other.third);
            if (vertices1.containsAll(vertices2)) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return first.hashCode() * 31 + second.hashCode() * 31 + third.hashCode() * 31;
        }
    }

    private static class FourVertices {
        Vertex first;
        Vertex second;
        Vertex third;
        Vertex fourth;

        public FourVertices(Vertex first, Vertex second, Vertex third, Vertex fourth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
        }

        public int getConnectionType() {
            int count = 0;
            int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            int count4 = 0;
            if (first.isNeighbour(second)) {
                count++;
                count1++;
                count2++;
            }
            if (first.isNeighbour(third)) {
                count++;
                count1++;
                count3++;
            }
            if (first.isNeighbour(fourth)) {
                count++;
                count1++;
                count4++;
            }
            if (second.isNeighbour(third)) {
                count++;
                count2++;
                count3++;
            }
            if (second.isNeighbour(fourth)) {
                count++;
                count2++;
                count4++;
            }
            if (third.isNeighbour(fourth)) {
                count++;
                count3++;
                count4++;
            }
            switch (count) {
                case 6:
                    return 5;
                case 5:
                    return 4;
                case 4:
                    if (count1 == 3 || count2 == 3 || count3 == 3 || count4 == 3) {
                        return 3;
                    } else {
                        return 1;
                    }
                case 3:
                    if (count1 == 3 || count2 == 3 || count3 == 3 || count4 == 3) {
                        return 2;
                    }
                    if (count1 == 0 || count2 == 0 || count3 == 0 || count4 == 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                default:
                    return -1;
            }
        }
    }
}
