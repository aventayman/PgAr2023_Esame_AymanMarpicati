package it.ayman.fp.exam;

import it.ayman.fp.lib.Menu;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numRoadMap = 10;
        Game game = new Game(numRoadMap);
        UserInteraction.printTitle();
        for (int i = 0; i < 10 && game.getPlayer().getNumLives() != 0; i++) {
            int mapIndex = UserInteraction.mainMenu(game) - 1;
            game.getMaps().get(mapIndex).traverse(game.getPlayer(), game, mapIndex);
        }
    }
}
