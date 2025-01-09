package org.kdepo.games.tankstilldeath;

public class Constants {

    public static int SCREEN_WIDTH = 1280;
    public static int SCREEN_HEIGHT = 960;

    public interface Bonuses {
        int STAR_ID = 0;
        int SHIELD_ID = 1;
    }

    public interface Bullets {
        /**
         * Can destroy half of brick block in one hit
         * Cannot destroy concrete block
         * Can destroy standard tank in one hit
         * Can destroy armored tank in three hits
         */
        int STANDARD_ID = 0;

        /**
         * Can destroy full brick block in one hit
         * Can destroy concrete block in one hit
         * Can destroy armored tanks in one hit
         */
        int ARMOUR_PIERCING_ID = 1;
    }

    public interface Explosions {
        String ANIMATION_SMALL = "animation_explosion_00";
        String ANIMATION_MEDIUM = "animation_explosion_01";
        String ANIMATION_BIG = "animation_explosion_02";
    }

    public interface Screens {
        String BATTLE = "battle";
    }

    public interface Teams {
        int PLAYER_ID = 0;
        int ENEMY_ID = 1;
    }

    public interface Tiles {
        int FULL_BRICKS_BLOCK_ID = 2;
        int BRICKS_AT_THE_NORTH_BLOCK_ID = 3;
        int BRICKS_AT_THE_EAST_BLOCK_ID = 4;
        int BRICKS_AT_THE_SOUTH_BLOCK_ID = 5;
        int BRICKS_AT_THE_WEST_BLOCK_ID = 6;
        int CONCRETE_BLOCK_ID = 7;
    }

    private Constants() {
        throw new RuntimeException("Instantiation is not allowed");
    }
}
