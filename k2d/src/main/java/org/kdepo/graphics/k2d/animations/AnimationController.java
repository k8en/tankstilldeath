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
        if (activeFrameTimer >= activeFrame.getDuration()) {
            // Current frame time is expired
            this.isActiveFrameCompleted = true;

            // Trying to set next frame as active
            if (AnimationPlayDirection.FORWARD.equals(animationPlayDirection)) {
                if (activeFrame.getNextFrame() != null) {
                    activeFrame = activeFrame.getNextFrame();
                    isActiveFrameCompleted = false;
                    activeFrameTimer = 0.0f;

                } else {
                    this.isActiveAnimationCompleted = true;
                }

            } else if (AnimationPlayDirection.BACKWARD.equals(animationPlayDirection)) {
                if (activeFrame.getPreviousFrame() != null) {
                    activeFrame = activeFrame.getPreviousFrame();
                    isActiveFrameCompleted = false;
                    activeFrameTimer = 0.0f;

                } else {
                    this.isActiveAnimationCompleted = true;
                }

            } else {
                throw new RuntimeException("Animation play direction is not supported: " + animationPlayDirection.name());
            }

            // Next frame was not found
            if (this.isActiveAnimationCompleted) {
                if (AnimationPlayMode.LOOP.equals(animationPlayMode)) {
                    if (AnimationPlayDirection.FORWARD.equals(animationPlayDirection)) {
                        this.isActiveAnimationCompleted = false;
                        activeFrame = activeAnimation.getAnimationFrames().get(0);
                        isActiveFrameCompleted = false;
                        activeFrameTimer = 0.0f;

                    } else if (AnimationPlayDirection.BACKWARD.equals(animationPlayDirection)) {
                        this.isActiveAnimationCompleted = false;
                        activeFrame = activeAnimation.getAnimationFrames().get(activeAnimation.getAnimationFrames().size() - 1);
                        isActiveFrameCompleted = false;
                        activeFrameTimer = 0.0f;

                    } else {
                        throw new RuntimeException("Animation play direction is not supported: " + animationPlayDirection.name());
                    }

                } else if (AnimationPlayMode.BOUNCE.equals(animationPlayMode)) {
                    if (AnimationPlayDirection.FORWARD.equals(animationPlayDirection)) {
                        this.isActiveAnimationCompleted = false;
                        activeFrame = activeAnimation.getAnimationFrames().get(activeAnimation.getAnimationFrames().size() - 1);
                        isActiveFrameCompleted = false;
                        activeFrameTimer = 0.0f;
                        animationPlayDirection = AnimationPlayDirection.BACKWARD;

                    } else if (AnimationPlayDirection.BACKWARD.equals(animationPlayDirection)) {
                        this.isActiveAnimationCompleted = false;
                        activeFrame = activeAnimation.getAnimationFrames().get(0);
                        isActiveFrameCompleted = false;
                        activeFrameTimer = 0.0f;
                        animationPlayDirection = AnimationPlayDirection.FORWARD;

                    } else {
                        throw new RuntimeException("Animation play direction is not supported: " + animationPlayDirection.name());
                    }

                } else if (AnimationPlayMode.ONCE.equals(animationPlayMode)) {
                    // Consume update method invocation
                    //..

                } else {
                    throw new RuntimeException("Animation play direction is not supported: " + animationPlayDirection.name());
                }

            }


        } else {
            // Update current frame timer
            activeFrameTimer = activeFrameTimer + animationSpeed;
        }
    }

    public void restartActiveAnimation() {
        switch (animationPlayDirection) {
            case FORWARD: {
                activeFrame = activeAnimation.getAnimationFrames().get(0);
                break;
            }
            case BACKWARD: {
                activeFrame = activeAnimation.getAnimationFrames().get(activeAnimation.getAnimationFrames().size() - 1);
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