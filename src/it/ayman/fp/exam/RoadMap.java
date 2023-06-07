package it.ayman.fp.exam;

import it.ayman.fp.lib.RandomDraws;

import java.util.*;

public class RoadMap {
    private static final String START_INDENTIFIER = "START";
    private static final String INTERMEDIATE_INDENTIFIER = "INTERMEDIATE";
    private static final String END_INDENTIFIER = "END";


    private final Map<Integer, Node> nodesMap;
    private int score;

    public RoadMap(int numNodes) {
        nodesMap = generateNodeMap(numNodes);
        score = getScore();
    }

    /**
     * A method that creates a random map of Nodes each with its connections
     * @param numNodes number of nodes in the map
     * @return a map of nodes and their connections
     */
    private Map<Integer, Node> generateNodeMap(int numNodes) {
        Map<Integer, Node> nodeMap = new HashMap<>();

        // There has to be a path between the start and end node, so we iterate the
        // generation process until there is a valid map
        do {
            // The first node is always the start node
            nodeMap.put(1, new Node(1, START_INDENTIFIER));

            // Every other node in the middle is an intermediate node
            for (int i = 2; i < numNodes; i++) {
                Node node = new Node(i, INTERMEDIATE_INDENTIFIER);
                nodeMap.put(i, node);
            }

            // The last node is always the end node
            nodeMap.put(numNodes, new Node(numNodes, END_INDENTIFIER));

            // For every node we add a random amount of connections to other random nodes
            for (Node node : nodeMap.values()) {
                // The amount of connections is set randomly and logarithmically
                // between 2 and the log(numNodes) so that it doesn't become too large
                int numConnections = RandomDraws.drawInteger(1, (int) Math.floor(Math.log(numNodes))) + 1;

                // For every node we add the connections to other nodes
                for (int i = 0; i < numConnections; i++) {
                    int randomNodeId;
                    Node randomNode;
                    // Check if the node is not itself and if it's not already in
                    // the connections of the node, in that case regenerate it
                    do {
                        randomNodeId = RandomDraws.drawInteger(1, nodeMap.size());
                        randomNode = nodeMap.get(randomNodeId);
                    } while (randomNodeId == node.getId() || node.getConnectedNodes().contains(randomNode));

                    // Add the connection to both nodes, the start and the destination node
                    node.addConnectedNode(randomNode);
                    randomNode.addConnectedNode(node);
                }
            }
        } while (!hasPathBetween(nodeMap, getStartNode(nodeMap), getEndNode(nodeMap)));

        return nodeMap;
    }

    private boolean hasPathBetween(Map<Integer, Node> nodeMap, Node startNode, Node endNode) {
        // Check if startNode and endNode are the same node
        if (startNode == endNode) {
            return true;
        }

        // Initialize distances to infinity for all nodes except startNode
        Map<Node, Integer> distances = new HashMap<>();
        for (Node node : nodeMap.values()) {
            if (node == startNode) {
                distances.put(node, 0);
            } else {
                distances.put(node, Integer.MAX_VALUE);
            }
        }

        // Set of visited nodes
        Set<Node> visited = new HashSet<>();

        // Priority queue to store nodes based on their minimum distance
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            visited.add(currentNode);

            if (currentNode == endNode) {
                return true;
            }

            for (Node neighbor : currentNode.getConnectedNodes()) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int distanceToNeighbor = distances.get(currentNode) + 1; // Assuming all edges have weight 1

                if (distanceToNeighbor < distances.get(neighbor)) {
                    distances.put(neighbor, distanceToNeighbor);
                    pq.add(neighbor);
                }
            }
        }

        return false;
    }

    private Node getStartNode(Map<Integer, Node> nodeMap) {
        for (Node node : nodeMap.values()) {
            if (node.getIdentifier().equals(START_INDENTIFIER)) {
                return node;
            }
        }
        return null; // If start node is not found
    }

    private Node getEndNode(Map<Integer, Node> nodeMap) {
        for (Node node : nodeMap.values()) {
            if (node.getIdentifier().equals(END_INDENTIFIER)) {
                return node;
            }
        }
        return null; // If start node is not found
    }

    public int getScore() {
        return nodesMap.size();
    }

    public Map<Integer, Node> getNodesMap() {
        return nodesMap;
    }
}
