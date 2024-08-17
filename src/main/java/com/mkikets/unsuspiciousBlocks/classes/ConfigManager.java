package com.mkikets.unsuspiciousBlocks.classes;

import com.mkikets.unsuspiciousBlocks.UnsuspiciousBlocks;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private ConfigManager() {}
    private static ConfigManager manager = new ConfigManager();
    public static ConfigManager getManager() {return manager;}

    private UnsuspiciousBlocks plugin = UnsuspiciousBlocks.getPlugin(UnsuspiciousBlocks.class);

    public FileConfiguration config;

    public File configfile;

    public void setupFiles() {
        configfile = new File(plugin.getDataFolder(), "config.yml");

        if (!this.configfile.exists()) {
            this.configfile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        this.config = new YamlConfiguration();

        try {
            this.config.load(this.configfile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }


    public void saveConfig() {
        try {
            this.config.save(this.configfile);
            Bukkit.getServer().getConsoleSender().sendMessage("Config.yml has been saved");
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage("Config.yml could not be saved");
        }
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configfile);
    }
}
