<p align="center">
  <a href="https://play.mc" target="_blank">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://github.com/playonmc/PlayStats/blob/main/.github/logo-dark.png?raw=true">
      <source media="(prefers-color-scheme: light)" srcset="https://github.com/playonmc/PlayStats/blob/main/.github/logo-light.png?raw=true">
      <img alt="PlayMC" src="https://github.com/playonmc/PlayStats/blob/main/.github/logo-light.png?raw=true" width="425" height="134" style="max-width: 100%;">
    </picture>
  </a>
</p>
    
# PlayStats

**PlayStats** is a powerful Minecraft analytics plugin for Spigot/Paper servers. Track every in-game action—from block breaks and placements to mob kills, player kills, crafting events, anvil uses, and more—and display real-time leaderboards via a web API.

Built by [PlayMC](https://play.mc) initially as an internal analytics tool, PlayStats is now open-source for full transparency and community contributions.

## Features

- **Comprehensive Event Tracking**
    - Block break & place
    - Mob kills & player kills
    - Item crafting, smelting & anvil use
    - Chest opens, furnace uses, and custom events
- **Web API & Leaderboards**
    - Expose stats through a RESTful API
    - Customizable web-page leaderboards
    - Caching & pagination support
- **Highly Configurable**
    - Enable/disable specific event types
    - Per-world and per-permission group filters
    - Data retention policies
- **Performance-Focused**
    - Asynchronous logging & batch writes
    - Built-in in-memory buffer for small servers

## Installation

1. **Download** the latest `PlayStats.jar` from the [Releases][release] page.
2. **Drop** the JAR into your server’s `plugins/` directory.
3. **Restart** or **reload** the server to generate default config.
4. **Configure** `plugins/PlayStats/config.yml` to your needs.
