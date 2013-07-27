package com.github.rmsy.tntshears.queue;

import com.github.rmsy.tntshears.queue.base.Action;
import com.github.rmsy.tntshears.queue.event.ActionExecuteEvent;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Basic queue.
 */
public class Queue extends BukkitRunnable {

    private final ArrayList<Action> queuedActions;
    private final ArrayList<Action> completedActions;
    private boolean paused = false;
    private int tasksPerExecution;

    private Queue() {
        this.queuedActions = new ArrayList<Action>();
        this.completedActions = new ArrayList<Action>();
    }

    /**
     * Creates a new queue.
     *
     * @param queuedActions     The actions to be queued.
     * @param tasksPerExecution The number of tasks to be executed per execution.
     */
    public Queue(@Nonnull final List<Action> queuedActions, int tasksPerExecution) {
        this();
        this.queuedActions.addAll(Preconditions.checkNotNull(queuedActions, "Queued actions"));
        this.tasksPerExecution = tasksPerExecution;
    }

    /**
     * Gets the number of tasks to execute per execution.
     *
     * @return The number of tasks to execute per execution.
     */
    public int getTasksPerExecution() {
        return this.tasksPerExecution;
    }

    /**
     * Sets the number of tasks to be executed per execution.
     *
     * @param tasksPerExecution The number of tasks to be executed per execution.
     */
    public void setTasksPerExecution(int tasksPerExecution) {
        this.tasksPerExecution = tasksPerExecution;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's <code>run</code> method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if (!this.paused)
            for (int i = this.tasksPerExecution - 1; i > 0; i--) {
                Action action = this.queuedActions.get(i);
                if (action.isValid()) {
                    if (action.execute()) {
                        this.queuedActions.remove(action);
                        this.completedActions.add(action);
                    }
                    Bukkit.getPluginManager().callEvent(new ActionExecuteEvent(this, action));
                } else {
                    this.queuedActions.remove(action);
                    i++;
                }
            }
    }

    /**
     * Removes the specified action from the queue.
     *
     * @param action The action to be removed.
     */
    public void removeAction(@Nonnull final Action action) {
        this.queuedActions.remove(action);
    }

    /**
     * Gets the actions that are queued but that have not yet been completed.
     *
     * @return The actions that are queued but that have not yet been complted.
     */
    @Nonnull
    public List<Action> getQueuedActions() {
        return Collections.unmodifiableList(this.queuedActions);
    }

    /**
     * Gets the actions that have already been completed.
     *
     * @return The actions that have already been completed.
     */
    @Nonnull
    public List<Action> getCompletedActions() {
        return Collections.unmodifiableList(this.completedActions);

    }

    /**
     * Gets whether or not the queue is paused.
     *
     * @return Whether or not the queue is paused.
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * Sets whether or not the queue is paused.
     *
     * @param paused Whether or not the queue is paused.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Gets all of the actions.
     *
     * @return All of the actions.
     */
    @Nonnull
    public List<Action> getActions() {
        ArrayList<Action> returnedList = new ArrayList<>(this.completedActions);
        returnedList.addAll(this.queuedActions);
        return Collections.unmodifiableList(returnedList);
    }
}
