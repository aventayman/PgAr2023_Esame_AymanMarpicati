package it.ayman.fp.exam;

import com.sun.jdi.connect.AttachingConnector;
import it.ayman.fp.lib.Menu;
import it.ayman.fp.lib.RandomDraws;

import java.util.*;

public class RoadMap {
    private static final String START_INDENTIFIER = "START";
    private static final String INTERMEDIATE_INDENTIFIER = "INTERMEDIATE";
    private static final String END_INDENTIFIER = "END";
    private static final String FINAL_BOSS = "Cammo";
    private static final String SELECT_NODE = "You are now in %s (id: %d). Select the next city you want to travel to:";
    private static final String MONSTER_DAMAGE = "The monster took %d in damage and now has %d hp";
    private static final String PLAYER_DAMAGE = "The player took %d in damage and now has %d hp";
    private static final String MONSTER_ENCOUNTER = "You have encountered a mighty monster by the name %s, get ready to fight!";
    private static final String LIFE_LOST = "You have lost a life and are now at %d";
    private static final String VICTORY = "You have defeated Cammo, may this be a day of joy!";
    private static final String HEALTH_MODIFIER = "You have stumbled upon a health modifier of %d, " +
            "your health is now %d";
    private static final String ATTACK_MODIFIER = "You have stumbled upon an attack modifier of %d, " +
            "your attack is now %d";


    private final Map<Integer, Node> nodesMap;

    public RoadMap(int numNodes) {
        nodesMap = generateNodeMap(numNodes);
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
            nodeMap.put(1, new Node(1, START_INDENTIFIER, null));

            // Every other node in the middle is an intermediate node
            for (int i = 2; i < numNodes; i++) {
                // One time out of three there will be a monster on a node
                Node node = (RandomDraws.drawInteger(1, 3) % 3 == 0) ? new Node(i, INTERMEDIATE_INDENTIFIER, null)
                        : new Node(i, INTERMEDIATE_INDENTIFIER, new Monster());
                nodeMap.put(i, node);
            }

            // The last node is always the end node
            nodeMap.put(numNodes, new Node(numNodes, END_INDENTIFIER, new Monster(FINAL_BOSS)));

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
        // Set of visited nodes
        Set<Node> visited = new HashSet<>();
        return runDijkstra(nodeMap, startNode, endNode, visited);
    }

    private boolean hasPathBetween(Map<Integer, Node> nodeMap, Node startNode, Node endNode, Set<Node> previouslyVisited) {
        Set<Node> visited = new HashSet<>(previouslyVisited);
        return runDijkstra(nodeMap, startNode, endNode, visited);
    }

    private boolean runDijkstra(Map<Integer, Node> nodeMap, Node startNode, Node endNode, Set<Node> visited) {
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

    public boolean traverse(Player player, Game game) throws InterruptedException {
        Set<Node> visited = new HashSet<>();
        Node currentNode = getStartNode(nodesMap);
        visited.add(currentNode);

        while (player.getHp() > 0) {
            List<Node> possibleNodes = new ArrayList<>();
            List<String> possibleNodeNames = new ArrayList<>();

            for (Node node : currentNode.getConnectedNodes()) {
                if (!visited.contains(node) && hasPathBetween(nodesMap, node, getEndNode(nodesMap), visited)) {
                    possibleNodes.add(node);
                    possibleNodeNames.add(node.getName() + " (id: " + node.getId() + ")");
                }
            }

            Menu menu = new Menu(String.format(SELECT_NODE, currentNode.getName(), currentNode.getId()),
                    possibleNodeNames.toArray(new String[0]));
            int choiceIndex = menu.choose(true, false) - 1;

            currentNode = possibleNodes.get(choiceIndex);
            visited.add(currentNode);

            if (currentNode.getHealthModifier() != 0) {
                player.setHp(player.getHp() + currentNode.getHealthModifier());
                System.out.printf(HEALTH_MODIFIER + "%n", currentNode.getHealthModifier(), player.getHp());
            }

            if (currentNode.getAttackModifier() != 0) {
                player.setAttack(player.getAttack() + currentNode.getAttackModifier());
                System.out.printf(ATTACK_MODIFIER + "%n", currentNode.getAttackModifier(), player.getAttack());
            }

            if (currentNode.getMonster() != null) {
                System.out.printf((MONSTER_ENCOUNTER) + "%n", currentNode.getMonster().getName());
                boolean playerVictory = fight(player, currentNode.getMonster());
                if (!playerVictory) {
                    player.setNumLives(player.getNumLives() - 1);
                    System.out.printf((LIFE_LOST) + "%n", player.getNumLives());
                    player.setHp(20);
                    player.setAttack(5);
                    resetMonsters();
                    return false;
                }
                if (currentNode == getEndNode(nodesMap)) {
                    System.out.println(VICTORY);
                    if (game.getScores()[choiceIndex] == 0)
                        game.getScores()[choiceIndex] = getScore();
                    resetMonsters();
                    return true;
                }
            }
        }

        return false;
    }

    private void resetMonsters() {
        for (Node node : nodesMap.values()) {
            if (node.getMonster() != null && !node.getMonster().getName().equals(FINAL_BOSS)) {
                node.setMonster(new Monster());
            }

            if (node.getMonster() != null && node.getMonster().getName().equals(FINAL_BOSS))
                node.setMonster(new Monster(FINAL_BOSS));
        }
    }

    private boolean fight(Player player, Monster monster) throws InterruptedException {
        while (player.getHp() > 0 && monster.getHp() > 0) {
            monster.setHp(monster.getHp() - player.getAttack());
            System.out.printf((MONSTER_DAMAGE) + "%n", player.getAttack(), Math.max(monster.getHp(), 0));
            if (monster.getHp() < 1)
                return true;

            Menu.wait(400);

            player.setHp(player.getHp() - monster.getAttack());
            System.out.printf((PLAYER_DAMAGE) + "%n", monster.getAttack(), Math.max(player.getHp(), 0));
            if (player.getHp() < 1)
                return false;

            Menu.wait(400);
        }

        return false;
    }
}
