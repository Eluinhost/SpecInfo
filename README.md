SpecInfo
========

**Supports Spigot 1.8+ and Java 7+**

All log messages are sent to console and all online spectators. Spectators can click on `TP` in the message to teleport
to the associated player with the message. Anyone with the permission `uhc.specinfo.specate` will also receive spectate
log messages and be able to TP (permission default is false, you must use a permission plugin to give it).

# Logging

## Veins

Keeps track of configured veins of blocks as players mine them. When a player mines a tracked block a message like this 
is sent:

`S» <playerName> dug <block> (<dug>/<vein> <total>T) at <x>:<y>:<z>`

Where:

`<playerName>` = the name of the player who dug the block

`<block>` = the name of the block that was dug

`<dug>` = how many of the vein has been dug so far

`<vein>` = how many blocks are in the vein total

`<total>` = how many of this block the player has dug since last clear/last restart/last reload

`<x>:<y>:<z>` = the coordinates of the block

Blocks are counted as part of a vein when they are next to each other (including diagonally; a 3x3)

### Relevant configuration

```yaml
digging blocks: [GOLD_ORE, DIAMOND_ORE, LAPIS_ORE, OBSIDIAN]
max vein size: 30
store vein ticks: 400
```

`digging blocks` = A list of [Material Names](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) to enable vein tracking on

`max vein size` = How many blocks to check before stopping traversing a vein (stops using DIRT as a trackable block killing the server)

`store vein ticks` = How many ticks before we forget about a vein after it was found. After this time if it is mined again it will count as a new vein.

### Relevant commands

`/specinfo clear`

Clears the stored grand totals for all players

Permission: `uhc.specinfo.command` default op

## Crafting

Keeps track of when particular items are crafted. Sends a message in the format:

`S» <playerName> crafted a <item> [displayName]`

Where:

`<playerName>` = the name of the crafter

`<item>` = the name of the item crafted

`[displayName]` = if an item has a display name it will show up in brackets

### Relevant configuration

```yaml
crafting items: [ENCHANTMENT_TABLE, GOLDEN_APPLE, DIAMOND_SWORD, DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS, ANVIL]
```

`crafting items` = A list of [Material Names](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) to enable crafting logging on

## Damage Taken

Keeps track of when player damage is taken. Sends a message in the format:

`S» <playerName> took <damage> damage from <source> [entityName]`

Where:

`<playerName>` = the name of the player who took damage

`<damage>` = how much damage was taken

`<source>` = the type of damage

`[entityName]` = if the damage was caused by an entity then it will show up here, if it was a player it shows their name

## Eating

Keeps track of consumed items. Sends a message in the format:

`S» <playerName> ate a <item> [displayName]`

Where:

`<playerName>` = the name of the crafter

`<item>` = the name of the item eaten

`[displayName]` = if an item has a display name it will show up in brackets

### Relevant configuration

```yaml
eating items: [GOLDEN_APPLE]
```

`eating items` = A list of [Material Names](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) to enable eating logging on

## Potions

Keeps track of when a potion is drank or thrown. Sends a message in the format:

`S» <playerName> drank a potion (<effects>)` OR `S» <playerName> threw a potion (<effects>)`

Where:

`<playerName>` = the name of the crafter

`<effects>` = The list of potion effects in the format `<name>:<amplifer>` separated by ` + `