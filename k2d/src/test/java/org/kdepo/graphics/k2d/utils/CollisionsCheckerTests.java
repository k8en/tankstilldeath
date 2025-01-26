package org.kdepo.graphics.k2d.utils;

import org.junit.jupiter.api.*;
import org.kdepo.graphics.k2d.geometry.Rectangle;

public class CollisionsCheckerTests {

    @BeforeAll
    static void setup() {
        System.out.println("CollisionsCheckerTests - Tests started");
    }

    @BeforeEach
    void init() {

    }

    @Test
    void testHasCollisionRectVsPoint() {
        System.out.println("CollisionsCheckerTests.testHasCollisionRectVsPoint - Tests started");

        Rectangle rect = new Rectangle(10, 10, 20, 20);

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 5, 5), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 10, 5), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 20, 5), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 30, 5), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 35, 5), "Collision is not expected!");

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 5, 10), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 10, 10), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 20, 10), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 30, 10), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 35, 10), "Collision is not expected!");

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 5, 20), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 10, 20), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 20, 20), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 30, 20), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 35, 20), "Collision is not expected!");

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 5, 30), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 10, 30), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 20, 30), "Collision is not expected!");
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, 30, 30), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 35, 30), "Collision is not expected!");

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 5, 35), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 10, 35), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 20, 35), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 30, 35), "Collision is not expected!");
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, 35, 35), "Collision is not expected!");

        System.out.println("CollisionsCheckerTests.testHasCollisionRectVsPoint - Tests completed");
    }

    @Test
    void testHasCollisionRectVsRect() {
        System.out.println("CollisionsCheckerTests.testHasCollisionRectVsRect - Tests started");

        Rectangle rect = new Rectangle(10, 10, 20, 20);

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 1, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(2, 1, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(6, 1, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(10, 1, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(16, 1, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(22, 1, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(23, 1, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 1, 8, 8)));

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 2, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(2, 2, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(6, 2, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(10, 2, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(16, 2, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(22, 2, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(23, 2, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 2, 8, 8)));

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 3, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(2, 3, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(6, 3, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(10, 3, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(16, 3, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(22, 3, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(23, 3, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 3, 8, 8)));

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 9, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(2, 9, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(6, 9, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(10, 9, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(16, 9, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(22, 9, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(23, 9, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 9, 8, 8)));

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 10, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(2, 10, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(6, 10, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(10, 10, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(16, 10, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(22, 10, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(23, 10, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 10, 8, 8)));

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 16, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(2, 16, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(6, 16, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(10, 16, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(16, 16, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(22, 16, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(23, 16, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 16, 8, 8)));

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 22, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(2, 22, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(6, 22, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(10, 22, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(16, 22, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(22, 22, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(23, 22, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 22, 8, 8)));

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 30, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(2, 30, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(6, 30, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(10, 30, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(16, 30, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(22, 30, 8, 8)));
        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(23, 30, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 30, 8, 8)));

        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(1, 31, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(2, 31, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(6, 31, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(10, 31, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(16, 31, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(22, 31, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(23, 31, 8, 8)));
        Assertions.assertFalse(CollisionsChecker.hasCollision(rect, new Rectangle(31, 31, 8, 8)));

        Assertions.assertTrue(CollisionsChecker.hasCollision(rect, new Rectangle(10, 10, 20, 20)));

        System.out.println("CollisionsCheckerTests.testHasCollisionRectVsRect - Tests completed");
    }

    @AfterEach
    void tearDown() {

    }

    @AfterAll
    static void done() {
        System.out.println("CollisionsCheckerTests - Tests completed");
    }
}
