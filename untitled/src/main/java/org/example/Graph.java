package org.example;

import java.util.*;

public class Graph {
    private HashMap<String, ArrayList<String>> adjList = new HashMap<>();
    private HashMap<String, Integer> vertexWeights = new HashMap<>(); // Mapa para armazenar pesos dos vértices

    public boolean addVertex(String vertex) {
        return addVertex(vertex, 0); // Peso padrão 0
    }

    public boolean addVertex(String vertex, int weight) {
        if(adjList.get(vertex) == null) {
            adjList.put(vertex, new ArrayList<String>());
            vertexWeights.put(vertex, weight);
            return true;
        }
        return false;
    }

    public boolean addEdge(String vertex1, String vertex2) {
        if(adjList.get(vertex1) != null && adjList.get(vertex2) != null) {
            adjList.get(vertex1).add(vertex2);
            adjList.get(vertex2).add(vertex1);
            return true;
        }
        return false;
    }

    public boolean removeEdge(String vertex1, String vertex2) {
        if(adjList.get(vertex1) != null && adjList.get(vertex2) != null) {
            adjList.get(vertex1).remove(vertex2);
            adjList.get(vertex2).remove(vertex1);
            return true;
        }
        return false;
    }

    public boolean removeVertex(String vertex) {
        if (adjList.get(vertex) == null) return false;
        for(String otherVertex : adjList.get(vertex)) {
            adjList.get(otherVertex).remove(vertex);
        }
        adjList.remove(vertex);
        vertexWeights.remove(vertex);
        return true;
    }

    // Métodos para manipulação de pesos dos vértices
    public boolean setVertexWeight(String vertex, int weight) {
        if (!adjList.containsKey(vertex)) {
            return false;
        }
        vertexWeights.put(vertex, weight);
        return true;
    }

    public int getVertexWeight(String vertex) {
        if (!vertexWeights.containsKey(vertex)) {
            throw new IllegalArgumentException("O vértice \"" + vertex + "\" não existe no grafo.");
        }
        return vertexWeights.get(vertex);
    }

    public HashMap<String, Integer> getAllVertexWeights() {
        return new HashMap<>(vertexWeights);
    }

    public List<String> adjacency(String vertex) {
        if (!adjList.containsKey(vertex)) {
            throw new IllegalArgumentException("O vértice \"" + vertex + "\" não existe no grafo.");
        }
        return new ArrayList<>(adjList.get(vertex));
    }

    public int getVertexDegree(String vertex) {
        if (!adjList.containsKey(vertex)) {
            throw new IllegalArgumentException("O Vértice \"" + vertex + "\" não existe no grafo.");
        }
        return adjList.get(vertex).size();
    }

    public HashMap<String, Integer> getAllVertexDegree() {
        HashMap<String, Integer> degreeMap = new HashMap<>();
        for(String vertex : adjList.keySet()) {
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

    public int getPathWeight(List<String> path) {
        if (path == null || path.isEmpty()) {
            return 0;
        }

        int totalWeight = 0;
        for (String vertex : path) {
            totalWeight += getVertexWeight(vertex);
        }
        return totalWeight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph{\n");
        for (String vertex : adjList.keySet()) {
            sb.append("  ").append(vertex).append(" (peso: ").append(vertexWeights.get(vertex))
                    .append(") -> ").append(adjList.get(vertex)).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public List<String> bellmoreTSP(String startVertex) {
        if (!adjList.containsKey(startVertex)) {
            throw new IllegalArgumentException("Vértice inicial não existe no grafo.");
        }

        if (!isCompleteGraph()) {
            throw new IllegalStateException("O grafo precisa ser completo para o TSP tradicional.");
        }

        List<String> vertices = new ArrayList<>(adjList.keySet());
        int n = vertices.size();

        List<String> unvisited = new ArrayList<>(vertices);
        unvisited.remove(startVertex);

        List<String> currentPath = new ArrayList<>();
        currentPath.add(startVertex);

        int[] bestCost = {Integer.MAX_VALUE};
        List<String> bestPath = new ArrayList<>();

        bellmoreTSPRecursive(startVertex, unvisited, currentPath, 0, bestCost, bestPath);

        if (bestPath.isEmpty()) {
            throw new IllegalStateException("Não foi possível encontrar um ciclo hamiltoniano.");
        }

        bestPath.add(startVertex);
        return bestPath;
    }

    private void bellmoreTSPRecursive(String startVertex, List<String> unvisited,
                                      List<String> currentPath, int currentCost,
                                      int[] bestCost, List<String> bestPath) {
        if (unvisited.isEmpty()) {
            int finalCost = currentCost + getEdgeWeight(currentPath.get(currentPath.size()-1), startVertex);

            if (finalCost < bestCost[0]) {
                bestCost[0] = finalCost;
                bestPath.clear();
                bestPath.addAll(currentPath);
            }
            return;
        }

        if (currentCost >= bestCost[0]) {
            return;
        }

        unvisited.sort(Comparator.comparingInt(this::getVertexWeight));

        for (int i = 0; i < unvisited.size(); i++) {
            String nextVertex = unvisited.get(i);

            int edgeCost = getEdgeWeight(currentPath.get(currentPath.size()-1), nextVertex);

            currentPath.add(nextVertex);
            unvisited.remove(i);

            bellmoreTSPRecursive(startVertex, unvisited, currentPath,
                    currentCost + edgeCost + getVertexWeight(nextVertex),
                    bestCost, bestPath);

            currentPath.remove(currentPath.size()-1);
            unvisited.add(i, nextVertex);
        }
    }

    private boolean isCompleteGraph() {
        int n = adjList.size();
        for (String vertex : adjList.keySet()) {
            if (adjList.get(vertex).size() != n - 1) {
                return false;
            }
        }
        return true;
    }

    private int getEdgeWeight(String vertex1, String vertex2) {
        return getVertexWeight(vertex1) + getVertexWeight(vertex2);
    }


    public static void main(String[] args) {
        Graph myGraph = new Graph();

        myGraph.addVertex("A", 1);
        myGraph.addVertex("B", 2);
        myGraph.addVertex("C", 3);
        myGraph.addVertex("D", 4);

        myGraph.addEdge("A", "B");
        myGraph.addEdge("A", "C");
        myGraph.addEdge("A", "D");
        myGraph.addEdge("B", "C");
        myGraph.addEdge("B", "D");
        myGraph.addEdge("C", "D");

        System.out.println("Grafo completo para TSP:");
        System.out.println(myGraph);

        try {
            List<String> tspPath = myGraph.bellmoreTSP("A");
            System.out.println("\nMelhor caminho TSP: " + tspPath);
            System.out.println("Custo total: " +
                    (myGraph.getPathWeight(tspPath) - myGraph.getVertexWeight("A"))); // Subtrai o peso duplicado do vértice inicial
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}