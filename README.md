# Project Ouroboros

A modular, feature-rich MMO plugin for Minecraft (Spigot/Paper) that transforms the vanilla experience into a living, breathing RPG world. Built on a carefully architected plugin framework, Ouroboros delivers procedurally-generated equipment, dynamic ability systems, and progression mechanics that evolve as players engage with the world.

---

## Overview

Ouroboros is organized into **three distinct expansions**, each introducing new gameplay layers and mechanical depth:

1. **Ouroboros (Main)** — The foundational MMO experience
2. **Echoes of Lumina** — Combat systems, weapons, and dynamic loot
3. **Legends of Lumina** — Spell-casting and advanced magic systems

---

## Branch Expansions

### ♾️ **Ωuroboros (Main Branch)**

The core MMO experience that establishes player progression, statistics, and general game systems.

#### Features

- **Progressive Statistics System**
  - Players earn and allocate stat points across attributes like Strength, Dexterity, Intelligence, Vitality, and Wisdom
  - Stats directly influence combat calculations, ability effectiveness, and survivability
  - Per-character progression tied to account systems

- **Account and Character Management**
  - Multi-character support per account
  - Persistent character data storage and loading
  - Account-level features for cosmetics, titles, and cosmetic persistence

- **General Events and Core Systems**
  - Integrated event listeners for damage, healing, and player interactions
  - Command handlers for stat allocation, character creation, and utility commands
  - Server-wide configuration for balance and difficulty tuning

- **Experience and Leveling**
  - Activity-based experience gain (combat, exploration, resource gathering)
  - Level thresholds that unlock higher stat allocations
  - Experience handlers for granular control over progression curves

- **Player Menus and GUIs**
  - Intuitive in-game interface for stat viewing and allocation
  - Character sheet menus displaying current stats and progression
  - Menu-driven cosmetic and account settings

#### Developer Anecdote

- The foundation branch was architected to be extensible from day one. The modular package structure (abilities, accounts, menus, objects, utils) ensures that new features—like the Echoes system or spell-casting—can be grafted on without disrupting core systems. The stat system uses a centralized enum (`EnumStats`) and event-driven handlers, allowing expansions to read and react to stat changes without tight coupling.

#### Player Experience

- A new player joins and immediately is emersed into the action -- Mine, hunt, kill mobs (albeit difficult without proper weaponry), and collect items to go about their journey. Along the way, players will passively increase levels to each of the main stats separated by two categories: General, and Combat. Leveling Combat grants access to abilities for crafted Echoes, while General stats increase overall QOL for the player. As the player defeats mobs and complete objectives, they earn experience and can allocate AP (Ability Points) to respective stat branches to obtain perks and other level-up rewards. The early game teaches the feedback loop: defeat mobs → gain XP → level up → grow stronger → tackle harder content. Crafting Plugin-Specific gear is crucial for content throughout the experience on the server. 

---

### ⚔️ **Σchoes of Lumina (Echoes Branch)**

A combat-focused expansion introducing procedurally-generated weapons and equipment, tied to a dynamic "Echo" system that injects infinite variety into loot. Also includes custom damage types, mob affinities, custom mobs, player and mob huds, and more. 
We've included the type chart on this repository under Resources.

#### Features

- **Procedural Echo System**
  - Echoes are auto-generated items (EOLs) with randomized stats based on rarity, material, and binding type
  - Each Echo rolls modifier slots (1-7 depending on rarity) with both active and passive effects
  - Stat ranges are defined in `echo_config.yml`, making balance tweaks live without recompilation
  
  **Rarity Tiers:**
  - Rarity 1-5: Common to Legendary (standard drops)
  - Rarity 6: Royalty (Special themed echoes with unique aesthetics)
  - Rarity 7: Unique (Story-driven, hand-crafted encounters)

- **Material System**
  - 11+ material types: Wooden, Stone, Copper, Iron, Golden, Diamond, Netherite, Hammer, Armament, Bow, Crossbow
  - Each material defines base attack, durability, and critical rate ranges
  - Example: Netherite weapons deal 30-100 base damage with 2500-10000 durability; Golden weapons are high-crit (50% base) but fragile (50-200 durability)

- **Binding System**
  - Three binding types modify weapon tempo and playstyle:
    - **STRING** (Light/Fast): 2.0-4.5 attack rating, bonus crit chance — dagger/rapier feel
    - **PELT** (Balanced): 2.0-4.0 attack rating, neutral — standard sword feel
    - **LEATHER** (Heavy): 1.0-2.5 attack rating, bonus damage and durability — greatsword/hammer feel

- **Weapon-Based Abilities**
  - Ability registry ties combat actions to weapon type
  - Current abilities include: Flamelash, Gemini Slash, Cleave, Heavy Swing, Lunge, Heavy Chop, Riptide, Bulwark, Launch, Quick Load, and 13+ special Echo abilities
  - Each ability has configurable cooldowns, resource costs, and scaling off player stats

- **Armor Echo System**
  - Procedurally-generated armor with armor rating, block rate, and critical mitigation
  - Material-based stat ranges and binding modifiers apply to armor too
  - Example: Diamond armor rolls 50-100 armor rating with 0.10-0.25 block chance

- **Combat Events and Damage Scaling**
  - Integrated event listeners for combat instance calculations
  - Modifiers apply dynamically based on Echo stats and player stats (Strength, Dexterity, etc.)
  - Critical hits trigger enhanced damage and crit armor mitigation checks

#### Developmental Anecdote

- The Echo system emerged from a design goal: "infinite unique loot without hand-crafting 10,000 items." The config-driven approach (`echo_config.yml`) means the design team can tweak drop rates, modifier chances, and stat ceilings mid-season without a code push. During playtesting, the team discovered that certain material/binding combinations (e.g., String + Netherite) created degenerate playstyles. The binding multipliers were then tuned to trade-off speed for durability, forcing meaningful loadout choices.

The special abilities registry showcases the system's flexibility: abilities like `PhotonCannon`, `LuminasRadiance`, and `SpatialRend` are named after Echo themes (light, preservation, spatial distortion), leading to the development of thematic weapon combos and end-game progression paths.

#### Player Experience

A player defeats a mob to obtain a "Creation Catalyst". This is a core component for the entire procedural generation system. The player has two options:
1. To craft a weapon.
2. Or craft an Armor piece.

- The player will naturally come across Materia that can be synthesized and refined into 1-5 rarities, while special case instances for Catalysts and Materia only obtainable through certain criteria (Custom Mob Event, Level Ranges, World Conditions, etc).
Scrap Materia is obtained through failed attempts to refine Materia, or by intentionally scrapping old Echoes. Either way, this Materia is crucial for crafting certain components, or by purchasing items in the Player Nexus Shop (Ouroboros Branch).

- The cycle is as follows: Kill Mobs to gain XP + Catalysts -> Obtain/Refine Materia -> Craft Stronger Weapons/Armor -> Repeat.  
---

### 🔮 **Legends of Lumina (LoL Branch)**

An advanced expansion introducing spell-casting mechanics, mana systems, and magical abilities that complement or replace traditional melee combat.

#### Features

- **Spell-Casting Framework**
  - Spells/Wands tied to player 'Gnosis' stat
  - Spell schools (Evocation, Abjuration, Divination, etc.) with distinct mechanics
  - Cooldown and resource management systems

- **Magic Ability Registry**
  - Dedicated spell registries for fast iteration and live rebalancing
  - Spell effects include area-of-effect damage, crowd control, buffs, and debuffs
  - Scaling tied to Intelligence, Wisdom, and Spell Power stats

- **Mana and Energy Systems**
  - Natural mana regeneration influenced by Wisdom
  - Efficiency modifiers and spell combo chains
  - Resource trade-offs: powerful spells cost more mana or have longer cooldowns

- **Dynamic Spell Effects**
  - Particle effects and sound cues tied to spell school
  - Environmental interactions (fire spells ignite blocks, frost spells create ice, etc.)
  - Spell stacking and combo systems for skilled players

- **Integration with Main Branch**
  - Spells scale off Intelligence instead of Strength but respect the same stat framework
  - Magical Echoes: special equipment with spell-affinity modifiers
  - Hybrid builds supported: players can blend melee abilities and spells

#### Developmental Anecdote

- The Legends of Lumina branch started as an experimental question: "Can we support two divergent playstyles (melee vs. magic) without creating balance nightmares?" The answer lay in creating parallel stat channels—Melee for, well, melee, and Gnosis for magic—and ensuring the core event systems (damage calculation, cooldown management, stat scaling) were agnostic to the source of power. This modularity meant the spell system could launch as a separate branch without touching the main codebase, reducing regression risk and allowing parallel development. Designer feedback led to mana regeneration being tied to Wisdom rather than a global value, creating interesting stat allocation puzzles (high Intelligence high-damage spells vs. high Wisdom sustained casting).

#### Player Experience

- The original intention for the player to discover magic would be to follow a Harry-Potter themed progression system. As this branch is heavily inspired by Hogwarts, the player will need to craft a "Training Wand", combining a simple stick and Harry's favourite colour, red as red dye. Once used for the first time, the player will unlock magic by increasing their Gnosis by one. The player may then craft their first wand, unlock cantrips, register and equip spells, and upgrade their wand/spells as possible. When the player upgrades their wand for the first time, and all subsequent times, their Gnosis will yet again increase, unlocking new tiers of magic, wands, etc based on this Gnosis. A player may not use a wand of a rarity they've yet to unlock, likewise with the rarity of the spells they collect. This ensures a balanced, steady, and predictable upgrade schema.
---

## Technical Architecture

### Modular Structure

```
src/com/ouroboros/
├── abilities/              # Ability registry and base classes
├── accounts/               # Character and account management
├── enums/                  # Game logic enums (stats, ability types, etc.)
├── events/                 # Event listeners (GeneralEvents, etc.)
├── menus/                  # GUI system (instances/, store/, base classes)
├── mobs/                   # Mob behavior and custom mob types
├── objects/                # Object mechanics (special items, traps, etc.)
├── utils/                  # Shared utilities (Nullable, handlers, etc.)
└── Ouroboros.java          # Main plugin class

lib/                        # Paper/Spigot APIs
bin/                        # Compiled classes
echo_config.yml             # Echo system tuning (live-reloadable)
plugin.yml                  # Spigot configuration
```

### Key Design Patterns

1. **Event-Driven Architecture**
   - Listeners for damage, healing, stat changes, and custom events
   - Handlers (AbilityHandler, ExpHandler, etc.) encapsulate logic

2. **Registry Pattern**
   - Centralized ability, item, and menu registries for runtime lookup
   - Enables live reloading and mod-friendly extension points

3. **Config-Driven Balance**
   - YAML-based tuning for drops, stat ranges, and modifier probabilities
   - Designers can iterate without recompilation

4. **Extensible GUI System**
   - Abstract base classes for menus (AbstractOBSGui)
   - Menu instances in `menus/instances/` follow a consistent structure

### Build & Deployment

```bash
# Compile (from project root)
javac -cp "lib/paper-1.21.8.jar:lib/spigot-api-1.21.8-R0.1-SNAPSHOT-shaded.jar" -d bin $(find src -name '*.java')

# Deploy
cp bin/com/ouroboros/Ouroboros.jar ~/minecraft/plugins/
```

---

## Progression Path & Feature Integration

Players experience the three branches in this natural progression:

1. **Start on Main:** Create a character, allocate stats, understand the progression loop
2. **Transition to Echoes:** Defeat mobs, receive procedural equipment, discover ability synergies
3. **Branch to LoL (Optional):** Roll a mage alt, experience a completely different playstyle

Server admins can:
- Run only **Main** for a pure MMO stat-based experience
- Run **Main + Echoes** for a loot-driven combat focus
- Run **Main + LoL** for magic-only progression
- Run all three for a full, complex MMO

---

## Design Philosophy

**Modularity Without Coupling:**
Every system is designed to stand alone or integrate seamlessly. The stat system doesn't know about Echoes; Echoes don't require spells; spells respect the core event framework. This means:
- A bug fix on Main doesn't require retesting Echoes
- A new ability type can be added in parallel
- Server admins can choose their complexity level

**Balance Through Configuration:**
Hard-coded values are avoided where possible. The `echo_config.yml` exemplifies this: rarity bands, material stats, binding modifiers, and armor ranges are all tunable. This extends design iteration cycles and empowers server customization.

**Infinite Variety:**
Procedural generation (Echoes) combined with modular progression (stats) and branching playstyles (melee vs. magic) ensures players always encounter fresh challenges. A Rarity 5 Netherite Armament with PhotonCannon + Lifesteal is mechanically different from a Rarity 3 Stone Dagger with Crit aura.

---

## Future Roadmap

- **Instanced Dungeons:** Procedural dungeons with Echo boss drops
- **PvP Arena:** Stat-balanced combat zones
- **Crafting & Enchanting:** Player-driven item modification
- **Guilds & Territory Control:** Faction systems
- **Seasonal Content:** Time-limited challenges and exclusive Echoes

---

## Contributing

This is a solo project by **PerceusAdalian**, built to showcase advanced Minecraft plugin architecture. While primarily for portfolio purposes, the codebase demonstrates:
- Professional Java patterns and modularity
- Configuration-driven game balance
- Event-driven architecture
- Scalable progression systems
- Live reloadable content tuning

For questions or discussion, reach out on GitHub issues.

---

**Project Status:** Active Development 
**Target Release Window:** 2026-2027
**Language:** Java (100%)  
**ReadMe Last Updated:** July 5, 2026  
**License:** See LICENSE file
