package org.kdepo.graphics.k2d.animations;

import java.util.Map;

public class AnimationController {

    /**
     * All animations available for animation controller
     * Key - animation name
     * Value - animation
     */
    private Map<String, Animation> animationsMap;

    /**
     * Current active animation
     */
    private Animation activeAnimation;

    /**
     * Frame to render
     */
    private AnimationFrame activeFrame;

    private float activeFrameTimer;

    /**
     * Animation speed
     * 1 - normal speed (is used by default)
     * 1.1 - speed increased
     * 0.9 - speed decreased
     */
    private float animationSpeed;

    private AnimationPlayDirection animationPlayDirection;
    private AnimationPlayMode animationPlayMode;

    private boolean isActiveFrameCompleted;
    private boolean isActiveAnimationCompleted;

    public AnimationController(Map<String, Animation> animationsMap, Animation activeAnimation, AnimationPlayDirection animationPlayDirection, AnimationPlayMode animationPlayMode) {
        this.animationsMap = animationsMap;
        this.activeAnimation = activeAnimation;

        this.animationPlayDirection = animationPlayDirection;
        if (AnimationPlayDirection.FORWARD.equals(animationPlayDirection)) {
            this.activeFrame = this.activeAnimation.getAnimationFrames().get(0);
        } else if (AnimationPlayDirection.BACKWARD.equals(animationPlayDirection)) {
            //this.activeFrame = this.activeAnimation.getAnimationFrames().get(this.activeAnimation.getAnimationFrames().size() - 1); // Wrong first frame?
            this.activeFrame = this.activeAnimation.getAnimationFrames().get(0);
        } else {
            throw new RuntimeException("Unknown animation play direction: " + animationPlayDirection.name());
        }
        this.activeFrameTimer = 0;
        this.animationSpeed = 1.0f;

        this.animationPlayMode = animationPlayMode;

        this.isActiveFrameCompleted = false;
        this.isActiveAnimationCompleted = false;
    }

    public void setAnimationsMap(Map<String, Animation> animationsMap) {
        this.animationsMap = animationsMap;
    }

    public void setActiveAnimation(Animation animation) {
        this.activeAnimation = animation;
    }

    public AnimationFrame getActiveFrame() {
        return this.activeFrame;
    }

    public void update() {
        if (isActiveAnimationCompleted) {
            switch (animationPlayMode) {
                case ONCE: {
                    // Do nothing ?
                    break;
                }
                case LOOP: {
                    restartActiveAnimation();
                    break;
                }
                case BOUNCE: {
                    // Change play direction
                    switch (animationPlayDirection) {
                        case FORWARD: {
                            animationPlayDirection = AnimationPlayDirection.BACKWARD;
                            break;
                        }
                        case BACKWARD: {
                            animationPlayDirection = AnimationPlayDirection.FORWARD;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Animation play direction is not supported: " + animationPlayDirection.name());
                        }
                    }
                    restartActiveAnimation();
                    break;
                }
                default: {
                    throw new RuntimeException("Animation play mode is not supported: " + animationPlayMode.name());
                }
            }

        } else if (isActiveFrameCompleted) {

            if ((AnimationPlayDirection.FORWARD.equals(animationPlayDirection) && activeFrame.getNextFrame() == null)
                    || (AnimationPlayDirection.BACKWARD.equals(animationPlayDirection) && activeFrame.getPreviousFrame() == null)) {
                // No more frames to switch - just set the flag on
                isActiveAnimationCompleted = true;

            } else {
                // Switch to the next frame and set the flag off
                switch (animationPlayDirection) {
                    case FORWARD: {
                        activeFrame = activeFrame.getNextFrame();
                        break;
                    }
                    case BACKWARD: {
                        activeFrame = activeFrame.getPreviousFrame();
                        break;
                    }
                    default: {
                        throw new RuntimeException("Animation play direction is not supported: " + animationPlayDirection.name());
                    }
                }
                isActiveFrameCompleted = false;
                activeFrameTimer = 0.0f;
            }

        } else {
            // Check if active frame completed
            if (activeFrameTimer >= activeFrame.getDuration()) {
                isActiveFrameCompleted = true;
            } else {
                activeFrameTimer = activeFrameTimer + animationSpeed;
            }
        }
    }

    public void restartActiveAnimation() {
        switch (animationPlayDirection) {
            case FORWARD: {
                activeFrame = activeAnimation.getAnimationFrames().get(0);
                break;
            }
            case BACKWARD: {
                //activeFrame = activeAnimation.getAnimationFrames().get(activeAnimation.getAnimationFrames().size() - 1);
                activeFrame = activeAnimation.getAnimationFrames().get(0);
                break;
            }
            default: {
                throw new RuntimeException("Animation play direction is not supported: " + animationPlayDirection.name());
            }
        }

        isActiveFrameCompleted = false;
        isActiveAnimationCompleted = false;
        activeFrameTimer = 0.0f;
    }

    public void switchToAnimation(String name) {
        if (!activeAnimation.getName().equals(name)) {
            activeAnimation = getAnimation(name);
        }
        restartActiveAnimation();
    }

    private Animation getAnimation(String name) {
        return animationsMap.get(name);
    }

    public boolean isAnimationCompleted() {
        return isActiveAnimationCompleted;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public void setAnimationPlayMode(AnimationPlayMode animationPlayMode) {
        this.animationPlayMode = animationPlayMode;
    }

    public AnimationPlayDirection getAnimationPlayDirection() {
        return animationPlayDirection;
    }

    public void setAnimationPlayDirection(AnimationPlayDirection animationPlayDirection) {
        this.animationPlayDirection = animationPlayDirection;
    }
}