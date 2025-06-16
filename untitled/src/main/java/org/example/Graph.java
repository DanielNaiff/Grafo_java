package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Map;


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

    public String isEulerian() {
        HashMap<String, Integer> degrees = getAllVertexDegree();
        int oddCount = 0;

        for (int degree : degrees.values()) {
            if (degree % 2 != 0) oddCount++;
        }

        if (oddCount == 0) return "Eulerian Circuit";
        if (oddCount == 2) return "Eulerian Path";
        return "Not Eulerian";
    }

    public List<String> fleuryAlgorithm() {
        String type = isEulerian();
        if (type.equals("Not Eulerian")) {
            throw new IllegalStateException("O grafo não é Euleriano.");
        }

        // Encontra vértice inicial
        String start = adjList.keySet().iterator().next();
        if (type.equals("Eulerian Path")) {
            for (String vertex : adjList.keySet()) {
                if (adjList.get(vertex).size() % 2 != 0) {
                    start = vertex;
                    break;
                }
            }
        }

        return findEulerPathOrCircuit(start);
    }

    private List<String> findEulerPathOrCircuit(String start) {
        Stack<String> stack = new Stack<>();
        List<String> path = new ArrayList<>();

        HashMap<String, ArrayList<String>> tempGraph = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : adjList.entrySet()) {
            tempGraph.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        stack.push(start);

        while (!stack.isEmpty()) {
            String current = stack.peek();

            if (!tempGraph.get(current).isEmpty()) {
                String next = tempGraph.get(current).get(0);

                tempGraph.get(current).remove(next);
                tempGraph.get(next).remove(current);

                stack.push(next);
            } else {
                path.add(stack.pop());
            }
        }

        Collections.reverse(path);
        return path;
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
        myGraph.addEdge("B", "C");
        myGraph.addEdge("C", "D");


        try {
            System.out.println("Caminho/Circuito de Euler: " + myGraph.fleuryAlgorithm());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

    }
}
