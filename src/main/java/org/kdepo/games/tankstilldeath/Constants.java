package org.kdepo.games.tankstilldeath;

public class Constants {

    public static int SCREEN_WIDTH = 1280;
    public static int SCREEN_HEIGHT = 916;

    public interface Bonuses {
        int STAR_ID = 0;
        int SHIELD_ID = 1;
    }

    private Constants() {
        throw new RuntimeException("Instantiation is not allowed");
    }
}
