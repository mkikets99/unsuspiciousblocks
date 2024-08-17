package com.mkikets.unsuspiciousBlocks;

import com.mkikets.unsuspiciousBlocks.classes.ConfigManager;
import com.mkikets.unsuspiciousBlocks.commands.CommandControl;
import com.mkikets.unsuspiciousBlocks.listeners.SuspiciousPlacement;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.changeme.nbtapi.NBT;

import java.util.Objects;

public final class UnsuspiciousBlocks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!NBT.preloadApi()) {
            getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        ConfigManager.getManager().setupFiles();
        getServer().getPluginManager().registerEvents(new SuspiciousPlacement(), this);
        CommandControl cc = new CommandControl();
        Objects.requireNonNull(getCommand("unsuspiciousblocks")).setExecutor(cc);
        Objects.requireNonNull(getCommand("unsuspiciousblocks")).setTabCompleter(cc);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ConfigManager.getManager().saveConfig();
    }
}
