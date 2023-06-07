package it.ayman.fp.exam;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private List<Node> connectedNodes;

    public Node(int id) {
        this.id = id;
        this.connectedNodes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public void addConnectedNode(Node node) {
        connectedNodes.add(node);
    }
}
