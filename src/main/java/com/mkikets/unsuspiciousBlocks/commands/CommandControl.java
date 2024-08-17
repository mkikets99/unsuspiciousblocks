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
                );
                return true;
            }
            switch (args[0]){
                case "enable":
                    ConfigManager.getManager().config.set("plugin.enabled",true);
                    break;
                case "disable":
                    ConfigManager.getManager().config.set("plugin.enabled",false);
                    break;
                case "hint-toggle":
                    ConfigManager.getManager().config.set("plugin.show_hints",!((boolean)ConfigManager.getManager().config.get("plugin.show_hints",false)));
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
                return List.of("enable", "disable", "hint-toggle", "help");
            }
        }
        return List.of();
    }
}
