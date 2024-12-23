package org.kdepo.graphics.k2d.animations;

import java.awt.image.BufferedImage;

/**
 * Single frame of animation
 */
public class AnimationFrame {

    /**
     * Frame id. Should be unique within all animations in animation controller
     */
    private final int id;

    /**
     * How long animation frame should be displayed
     */
    private final float duration;

    /**
     * Frame image to display
     */
    private final BufferedImage image;

    /**
     * Reference to the frame when AnimationPlayDirection is FORWARD
     * Should be null for the last frame
     */
    private AnimationFrame nextFrame;

    /**
     * Reference to the frame when AnimationPlayDirection is BACKWARD
     * Should be null on the first frame
     */
    private AnimationFrame previousFrame;

    public AnimationFrame(int id, float displayTime, BufferedImage image) {
        this.id = id;
        this.duration = displayTime;
        this.image = image;
    }

    public int getId() {
        return this.id;
    }

    public float getDuration() {
        return this.duration;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public AnimationFrame getNextFrame() {
        return nextFrame;
    }

    public void setNextFrame(AnimationFrame nextFrame) {
        this.nextFrame = nextFrame;
    }

    public AnimationFrame getPreviousFrame() {
        return previousFrame;
    }

    public void setPreviousFrame(AnimationFrame previousFrame) {
        this.previousFrame = previousFrame;
    }

}