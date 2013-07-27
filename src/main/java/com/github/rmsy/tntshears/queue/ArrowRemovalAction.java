package com.github.rmsy.tntshears.queue;

import com.github.rmsy.tntshears.queue.base.Action;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Arrow;
import org.joda.time.Instant;

import javax.annotation.Nonnull;

/**
 * Removes an arrow.
 */
public final class ArrowRemovalAction extends Action {
    /**
     * The minimum number of ticks that an infinity arrow must exist for. Included in this class because we lost all
     * hope for sanity when we started this venture in TNTShears, anyway. TODO: MOVE ME ELSEWHERE PLEASE OMG PLS MOEV
     * ELESWARE
     */
    private static final long INFINITY_ARROW_MIN_EXISTENCE_DURATION = 200L;
    @Nonnull
    private final Arrow arrow;
    private boolean completed = false;

    private ArrowRemovalAction() {
        this.arrow = null;
    }

    /**
     * Creates a new action.
     *
     * @param arrow The arrow to be removed.
     */
    public ArrowRemovalAction(@Nonnull final Arrow arrow) {
        this.arrow = Preconditions.checkNotNull(arrow, "Arrow");
    }

    /**
     * Carries out the action.
     *
     * @return The action's success.
     */
    @Override
    public boolean execute() {
        Instant impactTime = (Instant) arrow.getMetadata("initial-impact.time").get(0).value();
        //  TODO: Check and make sure this is PGM's metadata
        if (impactTime != null && impactTime.plus((ArrowRemovalAction.INFINITY_ARROW_MIN_EXISTENCE_DURATION / 2L) * 100L).isBeforeNow()) {
            this.arrow.remove();
            this.completed = true;
            return true;
        }
        return false;
    }

    /**
     * Gets whether or not the action is still valid, and should still be carried out.
     *
     * @return Whether or not the action is still valid, and should still be carried out.
     */
    @Override
    public boolean isValid() {
        return this.arrow.isValid();
    }

    /**
     * Gets whether or not the action has been successfully completed.
     *
     * @return Whether or not the action has been successfully completed.
     */
    @Override
    public boolean isCompleted() {
        return this.completed;
    }
}
