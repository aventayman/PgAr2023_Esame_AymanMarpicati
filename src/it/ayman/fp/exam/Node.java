package it.ayman.fp.exam;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private String identifier;
    private List<Node> connectedNodes;

    public Node(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
        this.connectedNodes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public void addConnectedNode(Node node) {
        connectedNodes.add(node);
    }
}
