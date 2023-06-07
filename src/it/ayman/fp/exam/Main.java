package it.ayman.fp.exam;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numRoadMap = 10;
        Game game = new Game(numRoadMap);
        UserInteraction.printTitle();
        for (int i = 0; i < 10 && game.getPlayer().getNumLives() != 0; i++) {
            int choiceIndex = UserInteraction.mainMenu(game) - 1;
            game.getMaps().get(choiceIndex).traverse(game.getPlayer(), game);
        }
    }
}
