package org.kdepo.games.tankstilldeath.desktop.model;

public enum MoveDirection {

    NORTH {
        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return -1;
        }
    },
    EAST {
        @Override
        public int getX() {
            return 1;
        }

        @Override
        public int getY() {
            return 0;
        }
    },
    SOUTH {
        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 1;
        }
    },
    WEST {
        @Override
        public int getX() {
            return -1;
        }

        @Override
        public int getY() {
            return 0;
        }
    };

    public abstract int getX();

    public abstract int getY();

}

