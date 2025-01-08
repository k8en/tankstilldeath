package org.kdepo.games.tankstilldeath;

public class Constants {

    public static int SCREEN_WIDTH = 1280;
    public static int SCREEN_HEIGHT = 960;

    public interface Bonuses {
        int STAR_ID = 0;
        int SHIELD_ID = 1;
    }

    public interface Explosions {
        String ANIMATION_SMALL = "animation_explosion_00";
        String ANIMATION_MEDIUM = "animation_explosion_01";
        String ANIMATION_BIG = "animation_explosion_02";
    }

    public interface Teams {
        int PLAYER_ID = 0;
        int ENEMY_ID = 1;
    }

    private Constants() {
        throw new RuntimeException("Instantiation is not allowed");
    }
}
