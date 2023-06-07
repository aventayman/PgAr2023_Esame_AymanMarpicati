package it.ayman.fp.exam;

import it.ayman.fp.lib.Menu;
import it.ayman.fp.lib.Title;

import java.util.Arrays;

public class UserInteraction {
    private static final String TITLE_NAME = "ARNALDOVERSE";
    private static final String WELCOME = "> Welcome to this far land brave explorer!";
    private static final String MENU_HEADER = """
            Here are all the kingdoms in the world, stay cautious! To the right of the names are their sizes.
            I warn you not to be too greedy, this lands are difficult to traverse, do it at your own risk!
            Choose the kingdom you wish to conquer, courageous adventurer!""";
    private static final String ARROW = " -> ";
    private static final String COMPLETED = "[X]";
    private static final String CURRENT_SCORE = "Your current score at the moment is";
    private static final String[] mapNames = {
            "Aetheria",
            "Eldermyst",
            "Crystalia",
            "Faerundor",
            "Celestria",
            "Enchantia",
            "Lumaria",
            "Mythoria",
            "Arcanalia",
            "Sylvanor",
            "Magoria",
            "Dreamhaven",
            "Spellforge",
            "Shadowmere",
            "Eldoria",
            "Evergladea",
            "Miragea",
            "Etherealyn",
            "Mystoria",
            "Radiantia"
    };

    public static void printTitle () throws InterruptedException {
        System.out.println(Title.createTitle(TITLE_NAME, true));
        System.out.println(WELCOME);
    }

    public static int mainMenu (Game game) {
        Menu menu = new Menu(MENU_HEADER, createMapsList(game));
        return menu.choose(true, false);
    }

    private static String[] createMapsList (Game game) {
        String [] options = new String[game.getMaps().size()];
        for (int i = 0; i < game.getMaps().size(); i++) {
            String option = mapNames[i] + ARROW + game.getMaps().get(i).getScore();
            if (game.getScores()[i] == 0)
                options[i] = option;
            else
                options[i] = option + " " + COMPLETED;
        }
        return options;
    }

    public static void printCurrentScore (Game game) {
        System.out.println(CURRENT_SCORE + " " + Arrays.stream(game.getScores()).sum());
    }
}
