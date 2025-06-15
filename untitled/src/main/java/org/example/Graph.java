package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {
    private HashMap<String, ArrayList<String>> adjList = new HashMap<>();

    public boolean addVertex(String vertex){
        if(adjList.get(vertex) == null){
            adjList.put(vertex, new ArrayList<String>());

            return true;
        }

        return false;
    }

    public boolean addEdge(String vertex1, String vertex2){
        if(adjList.get(vertex1) != null && adjList.get(vertex2) != null){
            adjList.get(vertex1).add(vertex2);
            adjList.get(vertex2).add(vertex1);

            return true;
        }
        return false;
    }

    public boolean removeEdge(String vertex1, String vertex2){
        if(adjList.get(vertex1) != null && adjList.get(vertex2) != null){
            adjList.get(vertex1).remove(vertex2);
            adjList.get(vertex2).remove(vertex1);

            return true;
        }

        return false;
    }

    public boolean removeVertex(String vertex){
        if (adjList.get(vertex) == null) return false;
        for(String otherVertex : adjList.get(vertex)){
            adjList.get(otherVertex).remove(vertex);
        }
        adjList.remove(vertex);
        return true;
    }

    public List<String> adjacency(String vertex) {
        if (!adjList.containsKey(vertex)) {
            throw new IllegalArgumentException("O vértice \"" + vertex + "\" não existe no grafo.");
        }
        return new ArrayList<>(adjList.get(vertex));
    }

    public int getVertexDegree(String vertex){
        if (!adjList.containsKey(vertex)) {
            throw new IllegalArgumentException("O Vertice \"" + vertex + "\" nao existe no grafo.");
        }
        return adjList.get(vertex).size();
    }

    public HashMap<String, Integer> getAllVertexDegree(){
        HashMap<String, Integer> degreeMap = new HashMap<>();
        for(String vertex : adjList.keySet()){
            degreeMap.put(vertex, getVertexDegree(vertex));
        }

        return degreeMap;
    }

    @Override
    public String toString() {
        return "Graph{" +

                "adjList=" + adjList +

                '}';
    }

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
        System.out.println(myGraph.adjacency("A"));
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
