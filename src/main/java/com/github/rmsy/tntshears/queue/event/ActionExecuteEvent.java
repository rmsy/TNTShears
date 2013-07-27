package com.github.rmsy.tntshears.queue.event;

import com.github.rmsy.tntshears.queue.Queue;
import com.github.rmsy.tntshears.queue.base.Action;
import com.google.common.base.Preconditions;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Raised when an {@link com.github.rmsy.tntshears.queue.base.Action} is executed.
 */
public class ActionExecuteEvent extends Event {
    private final Queue queue;
    private final Action action;

    private ActionExecuteEvent() {
        this.queue = null;
        this.action = null;
    }

    /**
     * Creates a new event.
     *
     * @param queue  The queue that executed the action.
     * @param action The action.
     */
    public ActionExecuteEvent(@Nonnull final Queue queue, @Nonnull final Action action) {
        this.queue = Preconditions.checkNotNull(queue, "Queue");
        this.action = Preconditions.checkNotNull(action, "Action");
        Preconditions.checkArgument(queue.getActions().contains(action), "Queue does not contain action");
    }

    @Override
    public HandlerList getHandlers() {
        //  TODO: too lazy to do this crap.
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Gets the action.
     *
     * @return The action.
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Gets the queue that executed the action.
     *
     * @return The queue that executed the action.
     */
    public Queue getQueue() {
        return this.queue;
    }
}
