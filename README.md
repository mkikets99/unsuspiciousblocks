# Unsuspicious Blocks

Bring missed functionalities for Suspicious Blocks

---

### What is this for?

This plugin brings an ability fo "safely" save `LootTable`, `LootTableSeed` and `Item` NBT data from `Suspicious Sand` and `Suspicious Gravel` blocks and transfer them into the "Fallen" items. 

### How this works?

When Block was manually placed (from creative menu) it will generate Loot Table and Seed for that block. Loot tables are semi-random, which means that they generate propper random loot table for their type (Sand - warm/Gravel - cold).

If block is turned into falling block entity, data about Loot table or Seed Number is transferred to that entity.
After certain time, Falling block is turned into item, which also means that entity data (about Loot table and Seed Number) is transferred to that item.

When player places block item with "saved" Loot table and Seed Number, those are applied back to block itself.

Plugin also have ability to show lore for item that player picks up, by showing Type of `Suspicious Block` (which loot table is used). If Item is brushed - What Item is inside is presented in lore.

Same logic is applied when block been brushed, but not entirely, but instead of saving LootTable and Seed, NBT tag `item` is saved instead.

### Commands

| Command                         | Behaviour                                                                        |
|---------------------------------|----------------------------------------------------------------------------------|
| /unsuspiciousblocks enable      | Enables plugin behaviour                                                         |
| /unsuspiciousblocks disable     | Disables plugin behaviour                                                        |
| /unsuspiciousblocks help        | Prints Commands and their behaviour                                              |
| /unsuspiciousblocks hint-toggle | Toggle's Lore data with info on what type of `Suspicious block`/`Item` is inside |

### Contribute

Feel free to visit Issues and Discussion tab for questions/features/bugs info's. Also, you are free to request Pulls on changes. Those will be merged only after review.

### Debug

 - Clone this repository. <sub>(Of course you need to do this first!)</sub>
 - Open in your Loved IDE <sub>(IDEA, Eclipse, VS Code, whatever.)</sub>
 - Synchronise Gradle tasks
   - Execute `jar` task to build Jar file.
   - Execute `runServer` task for quick server deploy for testing.

### Special Thanks!!!

 - [`PaperMC`](https://papermc.io/) for Documentation on easy plugin setup
 - [`jpenilla/run-paper`](https://github.com/jpenilla/run-task) for easy `runServer` implementation for quick plugin testing
 - [`tr7zw/Item-NBT-API`](https://github.com/tr7zw/Item-NBT-API) for easy and understandable access to NBT data
 - [`Spigot Forums`](https://www.spigotmc.org/forums/) for some explanations for API development
 - [`RunsWithShovels`](https://bukkit.org/members/runswithshovels.91032045/) for Configurations Manager class