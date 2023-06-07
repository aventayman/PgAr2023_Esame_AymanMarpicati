package it.ayman.fp.exam;

import it.ayman.fp.lib.RandomDraws;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int id;
    private final int healthModifier;
    private final int attackModifier;
    private final String identifier;
    private final List<Node> connectedNodes;
    private Monster monster;
    private final String name;
    private int distance;
    private static final String[] fantasyCityNames = {
            "Eldoria",
            "Silvercrest",
            "Stormholm",
            "Mythosia",
            "Azurehaven",
            "Shadowmere",
            "Celestria",
            "Oakthorn",
            "Frostholm",
            "Ironhold",
            "Seraphia",
            "Eldermist",
            "Ravenwatch",
            "Duskwood",
            "Emberfall",
            "Thunderkeep",
            "Crystalwyn",
            "Ironforge",
            "Starhaven",
            "Verdantis",
            "Mistgrove",
            "Everglade",
            "Dragonreach",
            "Willowbrook",
            "Arcanum",
            "Winterhaven",
            "Sablewood",
            "Amberfall",
            "Moonstone",
            "Dragonspire",
            "Blackwater",
            "Goldenleaf",
            "Emberwood",
            "Stormholm",
            "Swiftwind",
            "Frostholm",
            "Silvermeadow",
            "Crimsonport",
            "Ironpeak",
            "Willowbrook",
            "Shadowveil",
            "Moonshadow",
            "Thunderholme",
            "Mistralis",
            "Stonewall",
            "Nightshade",
            "Stormwatch",
            "Celestialis",
            "Dragonfire",
            "Ironwood",
            "Silverwing",
            "Emberhaven",
            "Ashendale",
            "Frostfall",
            "Starreach",
            "Dragonfall",
            "Everfrost",
            "Nightfall",
            "Stormgate",
            "Crystalwyn",
            "Mistveil",
            "Ironhold",
            "Whisperwind",
            "Moonhaven",
            "Shadowridge",
            "Stardust",
            "Emberholme",
            "Thunderpeak",
            "Silverglen",
            "Verdantia",
            "Stormkeep",
            "Azurewood",
            "Eldergrove",
            "Twilight",
            "Blackthorn",
            "Frostwind",
            "Sunstone",
            "Dragonwatch",
            "Ravencrest",
            "Ashenwood",
            "Starfall",
            "Mistborne",
            "Thunderstorm",
            "Ironvale",
            "Silvermoon",
            "Shadowbrook",
            "Evergreen",
            "Stormhaven",
            "Celestial",
            "Emberwick",
            "Duskfall",
            "Frostfire",
            "Moonrise",
            "Verdantvale",
            "Nightwind",
            "Crystalvale",
            "Thunderblade",
            "Starfield",
            "Mistwood",
            "Ashenholme"
    };

    public Node(int id, String identifier, Monster monster) {
        this.id = id;
        this.identifier = identifier;
        this.connectedNodes = new ArrayList<>();
        this.monster = monster;
        this.healthModifier = RandomDraws.drawInteger(-5, 10);
        this.attackModifier = RandomDraws.drawInteger(-3, 3);
        this.name = fantasyCityNames[RandomDraws.drawInteger(0, fantasyCityNames.length - 1)];
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

    public String getName() {
        return name;
    }

    public int getHealthModifier() {
        return healthModifier;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
