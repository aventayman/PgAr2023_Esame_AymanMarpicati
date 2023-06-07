package it.ayman.fp.exam;

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
    private static final String MOST_PROMISING_NODE = "The most promising node is %s, you may follow my suggestion or" +
            " continue on your own...";
    private static final String MONSTER_DEFEATED = "You have destroyed the monster!!!";
    private static final int DELAY = 700;
    private static final String SPACER = """
            
            """;


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
            // There has to be a path between the starting and the ending nodes
        } while (!hasPathBetween(nodeMap, getStartNode(nodeMap), getEndNode(nodeMap)));

        return nodeMap;
    }

    /**
     * A method that checks if there is a path between two nodes without the prerequisite that some nodes may
     * have already been travelled in.
     * @param nodeMap the map where to check
     * @param startNode the starting node
     * @param endNode the destination node
     * @return if there is a path it returns true, otherwise false
     */
    private boolean hasPathBetween(Map<Integer, Node> nodeMap, Node startNode, Node endNode) {
        // Set of visited nodes
        Set<Node> visited = new HashSet<>();
        return runDijkstra(nodeMap, startNode, endNode, visited);
    }

    /**
     * A method that checks if there is a path between two nodes and takes into account that some nodes may
     * already have been travelled in
     * @param nodeMap the map where to check
     * @param startNode the starting node
     * @param endNode the destination node
     * @param previouslyVisited a set of previously visited nodes
     * @return if there is a path it returns true, otherwise false
     */
    private boolean hasPathBetween(Map<Integer, Node> nodeMap, Node startNode, Node endNode, Set<Node> previouslyVisited) {
        Set<Node> visited = new HashSet<>(previouslyVisited);
        return runDijkstra(nodeMap, startNode, endNode, visited);
    }

    /**
     * The algorithm of pathfinding
     * @param nodeMap the map where to make the search
     * @param startNode the starting node
     * @param endNode the destination node
     * @param visited the already visited nodes
     * @return true if there is a path, otherwise false
     */
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
                    currentNode.setDistance(distanceToNeighbor);
                    distances.put(neighbor, distanceToNeighbor);
                    pq.add(neighbor);
                }
            }
        }

        return false;
    }

    /**
     * A method to find the closest node to the destination node
     * @param nodes a list of the nodes to take into consideration
     * @return the closest node to the destination
     */
    private static Node findMostPromisingNode(List<Node> nodes) {
        Node mostPromisingNode = null;
        int minDistance = Integer.MAX_VALUE;

        for (Node node : nodes) {
            int distance = node.getDistance();
            if (distance < minDistance) {
                minDistance = distance;
                mostPromisingNode = node;
            }
        }
        return mostPromisingNode;
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

    /**
     * A method that makes the player traverse the map and interact with the monsters
     * @param player the player that traverses the map
     * @param game the current game
     * @param mapIndex the index of the map that is being traversed
     */
    public void traverse(Player player, Game game, int mapIndex) throws InterruptedException {
        System.out.println(SPACER);
        // A set with the already visited nodes inside the map
        Set<Node> visited = new HashSet<>();

        // The current node that is being traversed
        Node currentNode = getStartNode(nodesMap);
        visited.add(currentNode);

        // While the player is still alive we can continue traversing the map
        while (player.getHp() > 0) {
            System.out.println(SPACER);
            // List of the possible nodes where the player can travel next
            List<Node> possibleNodes = new ArrayList<>();
            List<String> possibleNodeNames = new ArrayList<>();

            // We check all the nodes connected to the current node
            for (Node node : currentNode.getConnectedNodes()) {
                // If the neighbouring node has already been visited or could lead to a dead end we discard it
                // otherwise we add it to the possible nodes
                if (!visited.contains(node) && hasPathBetween(nodesMap, node, getEndNode(nodesMap), visited)) {
                    possibleNodes.add(node);
                    possibleNodeNames.add(node.getName() + " (id: " + node.getId() + ")");
                }
            }

            // Printing the most promising node with the previous method
            System.out.printf(MOST_PROMISING_NODE + "%n", findMostPromisingNode(possibleNodes).getName());

            // Making the player choose where to go next
            Menu menu = new Menu(String.format(SELECT_NODE, currentNode.getName(), currentNode.getId()),
                    possibleNodeNames.toArray(new String[0]));
            int choiceIndex = menu.choose(true, false) - 1;

            // Changing the current node to the next node
            currentNode = possibleNodes.get(choiceIndex);

            // Adding the next node to the visited nodes
            visited.add(currentNode);

            System.out.println(SPACER);

            // If there is a health modifier display it and let the player know his current hp level
            if (currentNode.getHealthModifier() != 0) {
                player.setHp(player.getHp() + currentNode.getHealthModifier());
                System.out.printf(HEALTH_MODIFIER + "%n", currentNode.getHealthModifier(), player.getHp());
            }

            // If there is an attack modifier display it and let the player know his current attack level
            if (currentNode.getAttackModifier() != 0) {
                player.setAttack(player.getAttack() + currentNode.getAttackModifier());
                System.out.printf(ATTACK_MODIFIER + "%n", currentNode.getAttackModifier(), player.getAttack());
            }

            // If there is a monster in the node
            if (currentNode.getMonster() != null) {

                // Print all the info of the monster encounter
                System.out.printf((MONSTER_ENCOUNTER) + "%n", currentNode.getMonster().getName());

                // Let the monster and the player fight and record the result inside a variable
                boolean playerVictory = fight(player, currentNode.getMonster());

                // If the player has lost
                if (!playerVictory) {
                    // Diminish the number of lives
                    player.setNumLives(player.getNumLives() - 1);
                    System.out.printf((LIFE_LOST) + "%n", player.getNumLives());

                    // Reset the player stats
                    player.setHp(20);
                    player.setAttack(5);

                    // Reset the monsters' stats
                    resetMonsters();

                    //Print the current score
                    UserInteraction.printCurrentScore(game);
                    System.out.println(SPACER);
                    return;
                }
                if (currentNode == getEndNode(nodesMap)) {
                    System.out.println(VICTORY);
                    // If the player hasn't already beat that level add the score to the list
                    if (game.getScores()[mapIndex] == 0)
                        game.getScores()[mapIndex] = getScore();

                    // Reset the monsters' stats
                    resetMonsters();

                    //Print the current score
                    UserInteraction.printCurrentScore(game);
                    System.out.println(SPACER);
                    return;
                }
            }
        }
    }

    /**
     * A method that resets the monsters' lives once the map has been traversed
     */
    private void resetMonsters() {
        for (Node node : nodesMap.values()) {
            if (node.getMonster() != null && !node.getMonster().getName().equals(FINAL_BOSS)) {
                node.setMonster(new Monster());
            }

            if (node.getMonster() != null && node.getMonster().getName().equals(FINAL_BOSS))
                node.setMonster(new Monster(FINAL_BOSS));
        }
    }

    /**
     * A method that reconstructs the battle between the player and the monster
     * @param player the player
     * @param monster the monster
     * @return if the player has won it returns true, otherwise it returns false
     */
    private boolean fight(Player player, Monster monster) throws InterruptedException {
        System.out.println(SPACER);
        // While the player and the monster are both still alive
        while (player.getHp() > 0 && monster.getHp() > 0) {
            // Make the player attack the monster and record the result
            monster.setHp(monster.getHp() - player.getAttack());
            System.out.printf((MONSTER_DAMAGE) + "%n", player.getAttack(), Math.max(monster.getHp(), 0));

            // If the monster has died let the player know
            if (monster.getHp() < 1) {
                System.out.println(MONSTER_DEFEATED);
                return true;
            }

            // Small delay to add some suspence
            Menu.wait(DELAY);

            // Make the monster attack the player and record the result
            player.setHp(player.getHp() - monster.getAttack());
            System.out.printf((PLAYER_DAMAGE) + "%n", monster.getAttack(), Math.max(player.getHp(), 0));

            // If the player has died let the player know
            if (player.getHp() < 1)
                return false;

            // Small delay to add some suspence
            Menu.wait(DELAY);
        }
        return false;
    }
}
