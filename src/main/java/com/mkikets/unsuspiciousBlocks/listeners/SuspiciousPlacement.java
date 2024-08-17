package com.mkikets.unsuspiciousBlocks.listeners;

import com.mkikets.unsuspiciousBlocks.classes.ConfigManager;
import de.tr7zw.changeme.nbtapi.NBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrushableBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.*;
import java.util.stream.Collectors;

public class SuspiciousPlacement implements Listener {

    private final List<LootTable> randomizer_sand = Arrays.asList(
            LootTables.DESERT_PYRAMID_ARCHAEOLOGY.getLootTable(),
            LootTables.DESERT_WELL_ARCHAEOLOGY.getLootTable(),
            LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY.getLootTable()
    );
    private final List<LootTable> randomizer_gravel = Arrays.asList(
            LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY.getLootTable(),
            LootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON.getLootTable(),
            LootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE.getLootTable()
    );
    private final Map<String,LootTable> mappedLTNames = Map.of(
            "minecraft:archaeology/desert_pyramid",LootTables.DESERT_PYRAMID_ARCHAEOLOGY.getLootTable(),
            "minecraft:archaeology/desert_well",LootTables.DESERT_WELL_ARCHAEOLOGY.getLootTable(),
            "minecraft:archaeology/ocean_ruin_warm",LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY.getLootTable(),
            "minecraft:archaeology/ocean_ruin_cold",LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY.getLootTable(),
            "minecraft:archaeology/trial_ruins_common",LootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON.getLootTable(),
            "minecraft:archaeology/trial_ruins_rare",LootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE.getLootTable()
    );
    private final Map<String,Object> mappedLTLore = Map.of(
            "minecraft:archaeology/desert_pyramid", Component.text("Desert Pyramid").color(NamedTextColor.YELLOW),
            "minecraft:archaeology/desert_well",Component.text("Desert Well").color(NamedTextColor.YELLOW),
            "minecraft:archaeology/ocean_ruin_warm",Component.text("Warm Ocean Ruin").color(NamedTextColor.YELLOW),
            "minecraft:archaeology/ocean_ruin_cold",Component.text("Cold Ocean Ruin").color(NamedTextColor.GRAY),
            "minecraft:archaeology/trial_ruins_common",Component.text("Common Trial Ruins").color(NamedTextColor.GRAY),
            "minecraft:archaeology/trial_ruins_rare",Component.text("Rare Trial Ruins").color(NamedTextColor.GRAY)
    );

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlacement(BlockPlaceEvent event){
        if((boolean) ConfigManager.getManager().getConfig().get("plugin.enabled",true)) {
            Block block = event.getBlockPlaced();
            ItemStack item = event.getItemInHand();
            Material blockType = block.getType();
            if (blockType == Material.SUSPICIOUS_SAND || blockType == Material.SUSPICIOUS_GRAVEL) {
                BrushableBlock brushableBlock = (BrushableBlock) block.getState(false);
                if (!brushableBlock.hasLootTable()) {
                    boolean has_any_data = NBT.get(item, nbt->{
                        String loot_table_str = nbt.getString("SavedLootTable");
                        ItemStack loot_item = nbt.getItemStack("SavedLootItem");
                        return loot_item != null || loot_table_str != null;
                    });
                    if (!has_any_data) {
                        LootTable chosenOne = getStandardLootTable(blockType == Material.SUSPICIOUS_SAND ? randomizer_sand : randomizer_gravel);
                        brushableBlock.setLootTable(chosenOne, new Random().nextLong());
                    } else {
                        NBT.modify(brushableBlock, nbt -> {
                            nbt.removeKey("components");
                        });
                        NBT.get(item, nbt -> {
                            String loot_table_str = nbt.getString("SavedLootTable");
                            ItemStack loot_item = nbt.getItemStack("SavedLootItem");
                            Long loot_seed = nbt.getLong("SavedLootTableSeed");
                            if (loot_item != null) brushableBlock.setItem(loot_item);
                            if (loot_table_str != null)
                                brushableBlock.setLootTable(mappedLTNames.get(loot_table_str), loot_seed);
                        });
                    }
                }
            }
        }
    }

    private LootTable getStandardLootTable(List<LootTable> randomizer) {
        Random random = new Random();
        return randomizer.get(random.nextInt(randomizer.size()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityChangeBlock (EntityChangeBlockEvent event){
        if((boolean) ConfigManager.getManager().getConfig().get("plugin.enabled",true)) {
            Entity entity = event.getEntity();
            Block block = event.getBlock();
            if(entity.getType() == EntityType.FALLING_BLOCK){
                if(block.getType() == Material.SUSPICIOUS_SAND || block.getType() == Material.SUSPICIOUS_GRAVEL){
                    NBT.modifyPersistentData(entity,nbt->{
                        String loot_table = (((BrushableBlock) block.getState()).getLootTable() == null)? null : ((BrushableBlock) block.getState()).getLootTable().getKey().toString();
                        if(loot_table != null) nbt.setString("SavedLootTable",loot_table);
                        ItemStack itemsInside = ((BrushableBlock) block.getState()).getItem();
                        if (itemsInside.getAmount() > 0 ) nbt.setItemStack("SavedLootItem",itemsInside);
                        nbt.setLong("SavedLootTableSeed",((BrushableBlock) block.getState()).getSeed());
                    });
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemSpawn(ItemSpawnEvent event) {
        if ((boolean) ConfigManager.getManager().getConfig().get("plugin.enabled", true)) {
            Entity fb = null;
            List<Entity> entities = event.getEntity().getNearbyEntities(2, 2, 2);
            for (Entity e : entities) {
                if (e instanceof FallingBlock) {
                    fb = e;
                }
            }
            ItemStack item = event.getEntity().getItemStack();

            if (item.getType() == Material.SUSPICIOUS_SAND || item.getType() == Material.SUSPICIOUS_GRAVEL) {
                ItemMeta meta = item.getItemMeta();
                assert fb != null;
                String loot_table = NBT.getPersistentData(fb, nbt_entity -> nbt_entity.getString("SavedLootTable"));
                Long loot_seed = NBT.getPersistentData(fb, nbt_entity -> nbt_entity.getLong("SavedLootTableSeed"));
                ItemStack loot_item = NBT.getPersistentData(fb, nbt_entity -> nbt_entity.getItemStack("SavedLootItem"));
                if ((boolean) ConfigManager.getManager().getConfig().get("plugin.show_hints", false)) {
                    if (loot_table != null && !loot_table.isEmpty()) {
                        meta.lore(List.of((Component) mappedLTLore.get(loot_table)));
                    } else {
                        meta.lore(List.of(Component.text("Item inside").color(NamedTextColor.GRAY), Component.text("   " + Arrays.stream(loot_item.getType().toString().toLowerCase().split("_")).map(e -> e.substring(0, 1).toUpperCase() + e.substring(1)).collect(Collectors.joining(" "))).color(NamedTextColor.GRAY)));
                    }
                }
                item.setItemMeta(meta);
                NBT.modify(item, nbt -> {
                    nbt.setLong("SavedLootTableSeed", loot_seed);
                    if (loot_item != null) nbt.setItemStack("SavedLootItem", loot_item);
                    if (loot_table != null) nbt.setString("SavedLootTable", loot_table);
                });
            }
        }
    }
}
