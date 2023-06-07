package it.ayman.fp.exam;

import it.ayman.fp.lib.RandomDraws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Game {
    private final List<RoadMap> roadMapList;
    private final Player player;
    private final int[] scores;

    private static class RoadMapDifficultyComparator implements Comparator<RoadMap> {
        @Override
        public int compare(RoadMap map1, RoadMap map2) {
            return Integer.compare(map1.getScore(), map2.getScore());
        }
    }

    public Game(int numRoadMap) {
        // The maps are all initialized in the constructor
        this.roadMapList = new ArrayList<>();
        for (int i = 0; i < numRoadMap; i++)
            // The maps are more or less difficult depending on the amount of nodes
            roadMapList.add(new RoadMap(RandomDraws.drawInteger(7, 50)));
        // A new player is created
        this.player = new Player();

        // The list of scores is initialized to all zeroes at the beginning
        this.scores = new int[numRoadMap];
        Arrays.fill(scores, 0);

        // Sort all the maps from the easiest to the most difficult
        roadMapList.sort(new RoadMapDifficultyComparator());
    }

    public List<RoadMap> getMaps() {
        return roadMapList;
    }

    public Player getPlayer() {
        return player;
    }

    public int[] getScores() {
        return scores;
    }
}
