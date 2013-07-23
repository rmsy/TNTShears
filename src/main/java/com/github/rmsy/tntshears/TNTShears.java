/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Isaac Moore
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.rmsy.tntshears;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TNTShears plugin class.
 */
public class TNTShears extends JavaPlugin implements Listener {

    private Metrics metrics;
    private Logger console;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityInteract(@Nonnull final PlayerInteractEntityEvent event) {
        Entity clicked = Preconditions.checkNotNull(event, "Event").getRightClicked();
        Player player = event.getPlayer();
        if (player.hasPermission("tntshears.defuse") && player.getItemInHand().getType().equals(Material.SHEARS) && clicked instanceof TNTPrimed) {
            clicked.remove();
            player.sendMessage(ChatColor.RED + "You defused TNT with " + ChatColor.BLUE + Math.round((((double) ((TNTPrimed) clicked).getFuseTicks()) / 2.0D) * 100.0D) + ChatColor.RED + " milliseconds to spare.");
        }
    }

    @Override
    public void onDisable() {
        this.metrics = null;
        HandlerList.unregisterAll((Plugin) this);
    }

    @Override
    public void onEnable() {
        this.console = this.getLogger();

        //  Enable MCStats metrics
        try {
            this.metrics = new Metrics(this);
            this.metrics.start();
        } catch (IOException exception) {
            console.log(Level.WARNING, "Metrics could not be started.");
            console.log(Level.WARNING, "========= BEGIN STACKTRACE =========");
            for (StackTraceElement step : exception.getStackTrace()) {
                this.console.log(Level.WARNING, step.toString());
            }
            console.log(Level.WARNING, "=========  END STACKTRACE  =========");
            this.metrics = null;
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }
}
