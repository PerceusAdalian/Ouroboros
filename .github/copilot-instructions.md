# Copilot Instructions for Ouroboros

## Project Overview
Ouroboros is a Minecraft plugin with a modular architecture, organized by gameplay domains (abilities, accounts, menus, mobs, objects, utils, etc.). The codebase is structured under `src/com/ouroboros/`, with compiled classes in `bin/com/ouroboros/`. External dependencies are managed via the `lib/` directory (notably Paper and Spigot APIs).

## Key Architectural Patterns
- **Modular Packages:** Each major gameplay feature (abilities, accounts, menus, mobs, objects) is separated into its own package. Cross-feature logic is handled via shared `utils` and `enums`.
- **Event-Driven:** Core plugin logic (e.g., `GeneralEvents.java`, `Ouroboros.java`) relies on Minecraft event listeners and command handlers.
- **GUI System:** Menus are implemented as classes in `menus/instances/`, with reusable components in `menus/` and `menus/store/`.
- **Ability System:** Abilities are registered and managed via `abilities/AbilityRegistry.java` and related handler classes. Each ability type/category is defined in `enums/` and implemented in `ability/instances/`.

## Developer Workflows
- **Build:** Compile with the Paper/Spigot API jars in `lib/`. Example (from project root):
  ```bash
  javac -cp "lib/paper-1.21.8.jar:lib/spigot-api-1.21.8-R0.1-SNAPSHOT-shaded.jar" -d bin $(find src -name '*.java')
  ```
- **Deploy:** Copy the plugin jar to your Minecraft server's `plugins/` directory.
- **Debug:** Use Minecraft server logs and in-game feedback. Event and command classes are entry points for debugging.

## Project-Specific Conventions
- **Naming:** Classes and packages use domain-specific prefixes (e.g., `Obs`, `OBStandardTimer`, `Ability*`, `AbstractObs*`).
- **Enums:** All game logic enums are centralized in `enums/` for consistency.
- **Handlers:** Logic for abilities, experience, and objects is encapsulated in handler classes (e.g., `AbilityHandler`, `ExpHandler`, `ObsObjectCastHandler`).
- **Nullable Pattern:** Utility class `utils/Nullable.java` is used for safe null handling.

## Integration Points
- **Minecraft API:** All gameplay logic interacts with Paper/Spigot APIs (see `lib/`).
- **Cross-Component Communication:** Handlers and registries are used for communication between abilities, objects, and player data.

## Examples
- **Registering an Ability:**
  - Implement in `ability/instances/`
  - Register via `AbilityRegistry.java`
- **Creating a GUI Page:**
  - Extend `AbstractOBSGui.java` in `menus/`
  - Add to `GuiHandler.java` and instantiate in `menus/instances/`

## Key Files & Directories
- `src/com/ouroboros/Ouroboros.java`: Main plugin class
- `src/com/ouroboros/GeneralEvents.java`: Event listeners
- `src/com/ouroboros/abilities/AbilityRegistry.java`: Ability registration
- `src/com/ouroboros/menus/instances/`: GUI pages
- `src/com/ouroboros/utils/`: Shared utilities
- `lib/`: External dependencies

---
For unclear workflows or missing conventions, ask the user for clarification or examples from their development process.
