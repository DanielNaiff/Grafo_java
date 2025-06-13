package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Graph myGraph = new Graph();

        myGraph.addVertex("A");
        myGraph.addVertex("B");
        myGraph.addVertex("C");
        myGraph.addVertex("D");

        myGraph.addEdge("A", "B");
        myGraph.addEdge("A", "C");
        myGraph.addEdge("A", "D");
        myGraph.addEdge("B", "D");
        myGraph.addEdge("C", "D");

        System.out.println(myGraph.toString());

        System.out.println("##################################");

        System.out.println(myGraph.getVertexDegree("A"));

        System.out.println("##################################");
        System.out.println(myGraph.getAllVertexDegree());
        System.out.println("##################################");


        myGraph.removeVertex("D");

        System.out.println(myGraph.toString());

        System.out.println("##################################");

        System.out.println(myGraph.getVertexDegree("A"));

        System.out.println("##################################");
        System.out.println(myGraph.getAllVertexDegree());
        System.out.println("##################################");

    }
}