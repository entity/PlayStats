<p align="center">
  <a href="https://play.mc" target="_blank">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://github.com/playonmc/PlayStats/blob/main/.github/logo-dark.png?raw=true">
      <source media="(prefers-color-scheme: light)" srcset="https://github.com/playonmc/PlayStats/blob/main/.github/logo-light.png?raw=true">
      <img alt="PlayMC" src="https://github.com/playonmc/PlayStats/blob/main/.github/logo-light.png?raw=true" width="376" height="134" style="max-width: 100%;">
    </picture>
  </a>
</p>

# PlayStats

PlayStats is an analytics plugin for Minecraft and Hytale servers. It tracks in-game events and sends them to an API, which can be used for building player profiles and leaderboards.

**[Example player profile](https://talesmp.com/player/npc)**

Built by [PlayMC](https://play.mc) as an internal tool, now open-source.

## Platforms

| Platform | Version | Status |
|----------|---------|--------|
| Minecraft (Spigot/Paper) | 1.20+ | Primary |
| Hytale | Early Access | Secondary |

## What it tracks

**Player activity**
- Joins, quits, chat messages, commands
- Periodic heartbeat with health, XP, food level, and rank

**Blocks**
- Placements and breaks with block type and tool used

**Combat**
- Player kills, mob kills, deaths
- Damage dealt with weapon tracking

**Items**
- Crafting, smelting, anvil usage
- Pickups and drops
- Enchanting

**Movement**
- Walk, sprint, swim, climb, fly distances
- Vehicle travel (boat, minecart, horse, etc.)

**Exploration**
- Advancements, portal usage, level changes

## How it works

Events are collected in memory and sent to your API in batches every 10 seconds (up to 250 events per batch). Everything runs async so there's no impact on server performance.

Your API receives events like:

```json
{
  "identifier": "block:break",
  "playerName": "npc",
  "playerUuid": "...",
  "metadata": {
    "blockType": "DIAMOND_ORE",
    "tool": "DIAMOND_PICKAXE",
    "world": "world"
  }
}
```

## Installation

**Minecraft**
1. Download `PlayStats-x.x.x.jar` from [Releases][release]
2. Drop it in your `plugins/` folder
3. Restart the server
4. Edit `plugins/PlayStats/config.yml`

**Hytale**
1. Download `PlayStats-Hytale-x.x.x.jar` from [Releases][release]
2. Drop it in your `mods/` folder
3. Restart the server

Releases are built automatically when a version tag is pushed (e.g., `v1.0.0`).

## Configuration

```yaml
secret-key: 'your-api-key'
base-url: 'https://your-api.com/api/v1'
debug: false
```

## Integrations

- **LuckPerms** for rank tracking
- **PlaceholderAPI** for leaderboard placeholders

## Project structure

```
PlayStats/
├── common/      # Shared HTTP client and data models
├── minecraft/   # Paper plugin
└── hytale/      # Hytale plugin
```

## License

Open source. See [LICENSE](LICENSE).

[release]: https://github.com/playonmc/PlayStats/releases
