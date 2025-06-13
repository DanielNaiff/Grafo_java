package org.example;

import java.util.ArrayList;
import java.util.HashMap;

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
}
