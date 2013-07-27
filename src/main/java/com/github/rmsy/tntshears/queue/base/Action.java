package com.github.rmsy.tntshears.queue.base;

/**
 * Represents an action that can be carried out by a {@link com.github.rmsy.tntshears.queue.Queue}.
 */
public abstract class Action {

    private boolean completed = false;

    /**
     * Carries out the action.
     *
     * @return The action's success. If false, <b>the action will continue to be executed by the queue until true is
     *         returned or {@link com.github.rmsy.tntshears.queue.Queue#removeAction} is invoked</b>.
     */
    public abstract boolean execute();

    /**
     * Gets whether or not the action is still valid, and should still be carried out.
     *
     * @return Whether or not the action is still valid, and should still be carried out.
     */
    public abstract boolean isValid();

    /**
     * Gets whether or not the action has been successfully completed.
     *
     * @return Whether or not the action has been successfully completed.
     */
    public abstract boolean isCompleted();
}
