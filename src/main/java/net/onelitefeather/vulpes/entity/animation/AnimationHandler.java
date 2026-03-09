package net.onelitefeather.vulpes.entity.animation;

import net.worldseed.multipart.GenericModel;
import net.worldseed.multipart.animations.AnimationHandlerImpl;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 **/
@ApiStatus.Experimental
public class AnimationHandler extends AnimationHandlerImpl {

    private final Map<String, Integer> animationPrios;

    public AnimationHandler(GenericModel genericModel, Map<String, Integer> animationPrios) {
        super(genericModel);
        this.animationPrios = animationPrios;
    }

    public AnimationHandler(GenericModel genericModel) {
        super(genericModel);
        this.animationPrios = new HashMap<>();
    }

    public void addAnimation(String name, int prio) {
        this.animationPrios.putIfAbsent(name, prio);
    }

    public void remove(String name) {
        this.animationPrios.remove(name);
    }

    public int getPrio(String name) {
        return this.animationPrios.getOrDefault(name, Integer.MIN_VALUE);
    }

    @Override
    public Map<String, Integer> animationPriorities() {
        return animationPrios;
    }
}
