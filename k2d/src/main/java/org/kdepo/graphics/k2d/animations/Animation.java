package org.kdepo.graphics.k2d.animations;

import java.util.List;

/**
 * All animation frames related to a single animation
 */
public class Animation {

    /**
     * Animation name. Should be unique within the animations used in one animation controller
     */
    private final String name;

    /**
     * Animation frames related to the current animation
     */
    private final List<AnimationFrame> animationFrames;

    public Animation(String name, List<AnimationFrame> animationFrames) {
        this.name = name;
        this.animationFrames = animationFrames;
    }

    public String getName() {
        return this.name;
    }

    public List<AnimationFrame> getAnimationFrames() {
        return animationFrames;
    }

}