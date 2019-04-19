/* PrimVsKruskal.java
   CSC 226 - Summer 2018
   Assignment 2 - Prim MST versus Kruskal MST Template
   
   The file includes the "import edu.princeton.cs.algs4.*;" so that yo can use
   any of the code in the algs4.jar file. You should be able to compile your program
   with the command
   
    javac -cp .;algs4.jar PrimVsKruskal.java
    
   To conveniently test the algorithm with a large input, create a text file
   containing a test graphs (in the format described below) and run
   the program with
   
    java -cp .;algs4.jar PrimVsKruskal file.txt
    
   where file.txt is replaced by the name of the text file.
   
   The input consists of a graph (as an adjacency matrix) in the following format:
   
    <number of vertices>
    <adjacency matrix row 1>
    ...
    <adjacency matrix row n>
    
   Entry G[i][j] >= 0.0 of the adjacency matrix gives the weight (as type double) of the edge from 
   vertex i to vertex j (if G[i][j] is 0.0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that G[i][j]
   is always equal to G[j][i].


   R. Little - 06/22/2018
 */

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

//Do not change the name of the PrimVsKruskal class
public class PrimVsKruskal {

    /*
     * PrimVsKruskal(G) Given an adjacency matrix for connected graph G, with no
     * self-loops or parallel edges, determine if the minimum spanning tree of G
     * found by Prim's algorithm is equal to the minimum spanning tree of G found by
     * Kruskal's algorithm.
     * 
     * If G[i][j] == 0.0, there is no edge between vertex i and vertex j If G[i][j]
     * > 0.0, there is an edge between vertices i and j, and the value of G[i][j]
     * gives the weight of the edge. No entries of G will be negative.
     */
    static boolean PrimVsKruskal(double[][] G) {
        int n = G.length;

        /* Build the MST by Prim's and the MST by Kruskal's */
 /* (You may add extra methods if necessary) */
        // convert matrix to EdgeWeightedGraph object
        EdgeWeightedGraph g = new EdgeWeightedGraph(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (G[i][j] != 0 && i != j) {
                    double weight = G[i][j];
                    Edge e = new Edge(i, j, weight);
                    g.addEdge(e);
                }
            }
        }

        // run Prim
        PrimMST prim = new PrimMST(g);
        List<Edge> primEList = new ArrayList<Edge>();
        System.out.printf("Prim Tree: \n");
        for (Edge e : prim.edges()) {
            System.out.println(e);
            primEList.add(e);
        }
        System.out.printf("%.5f\n", prim.weight());
        System.out.printf(" \n");

        // run Kruskal
        KruskalMST kruskal = new KruskalMST(g);
        List<Edge> kruskalEList = new ArrayList<Edge>();
        System.out.printf("Kruskal Tree: \n");
        for (Edge e : kruskal.edges()) {
            System.out.println(e);
            kruskalEList.add(e);
        }
        System.out.printf("%.5f\n", kruskal.weight());

        /* Determine if the MST by Prim equals the MST by Kruskal */
        boolean pvk = true;
        /* ... Your code here ... */

        if (kruskal.weight() != prim.weight()) {
            pvk = false;
        }
        // compare edge
        int count = 0;
        for (int i = 0; i < primEList.size(); i++) {
            Edge primE = primEList.get(i);
            int primV1 = primE.either();
            int primV2 = primE.other(primV1);
            for (int m = 0; m < kruskalEList.size(); m++) {
                Edge kruskalE = kruskalEList.get(m);
                int kruskalV1 = kruskalE.either();
                int kruskalV2 = kruskalE.other(kruskalV1);
                if ((primV1 == kruskalV1 && primV2 == kruskalV2)
                        || (primV1 == kruskalV2 && primV2 == kruskalV1)) {

                    break;
                } else {
                    count++;
                    if (count == kruskalEList.size()) {
                        pvk = false;
                    }
                }
            }
            count = 0;
        }
        return pvk;
    }

    /*
     * main() Contains code to test the PrimVsKruskal function. You may modify the
     * testing code if needed, but nothing in this function will be considered
     * during marking, and the testing process used for marking will not execute any
     * of the code below.
     */
    public static void main(String[] args) {
        Scanner s;
        if (args.length > 0) {
            try {
                s = new Scanner(new File(args[0]));
            } catch (java.io.FileNotFoundException e) {
                System.out.printf("Unable to open %s\n", args[0]);
                return;
            }
            System.out.printf("Reading input values from %s.\n", args[0]);
        } else {
            s = new Scanner(System.in);
            System.out.printf("Reading input values from stdin.\n");
        }

        int n = s.nextInt();
        double[][] G = new double[n][n];
        int valuesRead = 0;
        for (int i = 0; i < n && s.hasNextDouble(); i++) {
            for (int j = 0; j < n && s.hasNextDouble(); j++) {
                G[i][j] = s.nextDouble();
                if (i == j && G[i][j] != 0.0) {
                    System.out.printf("Adjacency matrix contains self-loops.\n");
                    return;
                }
                if (G[i][j] < 0.0) {
                    System.out.printf("Adjacency matrix contains negative values.\n");
                    return;
                }
                if (j < i && G[i][j] != G[j][i]) {
                    System.out.printf("Adjacency matrix is not symmetric.\n");
                    return;
                }
                valuesRead++;
            }
        }

        if (valuesRead < n * n) {
            System.out.printf("Adjacency matrix for the graph contains too few values.\n");
            return;
        }

        boolean pvk = PrimVsKruskal(G);
        System.out.printf("Does Prim MST = Kruskal MST? %b\n", pvk);
    }
}
