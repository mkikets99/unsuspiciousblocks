package com.mkikets.unsuspiciousBlocks.commands;

import com.mkikets.unsuspiciousBlocks.classes.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandControl implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            if(args.length == 0 || args[0].equals("help")){
                sender.sendMessage(
                        Component.text("=========UnSUSpiciousBlocks==========").color(NamedTextColor.GOLD)
                                .appendNewline().append(Component.text("/unsuspiciousblocks enable ").color(NamedTextColor.AQUA).append(
                                Component.text("- enable plugin").color(NamedTextColor.GOLD)))
                                .appendNewline().append(Component.text("/unsuspiciousblocks disable ").color(NamedTextColor.AQUA).append(
                                Component.text("- disable plugin").color(NamedTextColor.GOLD)))
                                .appendNewline().append(Component.text("/unsuspiciousblocks hint-toggle ").color(NamedTextColor.AQUA).append(
                                Component.text("- enable/disable ItemMeta showing information about block's type/content").color(NamedTextColor.GOLD)))
                                .appendNewline().append(Component.text("/unsuspiciousblocks help ").color(NamedTextColor.AQUA).append(
                                Component.text("- print this information").color(NamedTextColor.GOLD)))
                                .appendNewline().append(Component.text("/unsuspiciousblocks status ").color(NamedTextColor.AQUA).append(
                                        Component.text("- check plugin status").color(NamedTextColor.GOLD)))
                );
                return true;
            }
            switch (args[0]){
                case "enable":
                    ConfigManager.getManager().config.set("plugin.enabled",true);
                    sender.sendMessage(Component.text("Plugin enabled").color(NamedTextColor.GREEN));
                    ConfigManager.getManager().saveConfig();
                    break;
                case "disable":
                    ConfigManager.getManager().config.set("plugin.enabled",false);
                    sender.sendMessage(Component.text("Plugin disabled").color(NamedTextColor.RED));
                    ConfigManager.getManager().saveConfig();
                    break;
                case "hint-toggle":
                    ConfigManager.getManager().config.set("plugin.show_hints",!((boolean)ConfigManager.getManager().config.get("plugin.show_hints",false)));
                    boolean showHintsStatus = (boolean) ConfigManager.getManager().config.get("plugin.show_hints",false);
                    sender.sendMessage(Component.text("Hints ").color(NamedTextColor.GOLD)
                            .append(Component.text(showHintsStatus ? "enabled" : "disabled").color(NamedTextColor.GREEN)));
                    ConfigManager.getManager().saveConfig();
                    break;
                case "status":
                    boolean enabled = (boolean) ConfigManager.getManager().config.get("plugin.enabled",true);
                    boolean showHints = (boolean) ConfigManager.getManager().config.get("plugin.show_hints",false);
                    sender.sendMessage(Component.text("Plugin status: ").color(NamedTextColor.GOLD)
                            .append(Component.text(enabled ? "enabled" : "disabled").color(NamedTextColor.GREEN))
                            .appendNewline()
                            .append(Component.text("Hints: ").color(NamedTextColor.GOLD))
                            .append(Component.text(showHints ? "enabled" : "disabled").color(NamedTextColor.GREEN))
                    );
                    break;
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            if(args.length == 1){
                return List.of("enable", "disable", "hint-toggle", "help", "status");
            }
        }
        return List.of();
    }
}
