package org.example;

import java.util.*;


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

    private boolean isBridge(String u, String v, HashMap<String, ArrayList<String>> graphTemp) {
        int count1 = dfsCount(u, graphTemp, new HashSet<>());

        graphTemp.get(u).remove(v);
        graphTemp.get(v).remove(u);

        int count2 = dfsCount(u, graphTemp, new HashSet<>());

        graphTemp.get(u).add(v);
        graphTemp.get(v).add(u);

        return count2 < count1;
    }

    private int dfsCount(String vertex, HashMap<String, ArrayList<String>> graphTemp, HashSet<String> visited) {
        visited.add(vertex);
        int count = 1;
        for (String adj : graphTemp.get(vertex)) {
            if (!visited.contains(adj)) {
                count += dfsCount(adj, graphTemp, visited);
            }
        }
        return count;
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
                String next = null;

                for (String adj : tempGraph.get(current)) {
                    if (!isBridge(current, adj, tempGraph)) {
                        next = adj;
                        break;
                    }
                }

                if (next == null) {
                    next = tempGraph.get(current).get(0);
                }

                // Remove a aresta
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
        myGraph.addVertex("E");

        myGraph.addEdge("A", "B");
        myGraph.addEdge("A", "C");
        myGraph.addEdge("B", "C");
        myGraph.addEdge("B", "D"); // Ponte
        myGraph.addEdge("D", "E");

        System.out.println("Tipo de grafo: " + myGraph.isEulerian());

        try {
            System.out.println("Caminho/Circuito de Euler: " + myGraph.fleuryAlgorithm());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }


    }
}
