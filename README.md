# 🎯 MultiRTP_MX

## 📝 Description
**MultiRTP_MX** is a powerful plugin for Minecraft servers that adds random teleportation functionality. Players can explore the world with `/rtp` command, teleporting to random locations within specified coordinates. The plugin also includes a `/back` command to return to previous locations, making exploration safe and fun!

## ✨ Features
* 🎲 Random teleportation within configurable X and Z coordinates
* ↩️ Return to previous location with `/back` command
* 🌊 Smart water detection system
* 🧙‍♂️ Automatic water breathing effect when needed
* ⚙️ Fully configurable messages and settings
* 🛡️ Permission-based command system
* 🔍 Persistent location search until safe spot is found
* 🌐 Interactive UI forms with [FormConstructor](https://github.com/MEFRREEX/FormConstructor/releases/tag/3.0.0)
* 👪 Teleportation near other online players
* 🗺️ World selection through user interface
* 🚀 Chunk pre-loading for smoother teleportation

## 🔧 Commands & Permissions
| Command | Permission | Description |
|---------|------------|-------------|
| `/rtp` | `miroshka.rtp` | Random teleport within world borders |
| `/rtp` | `miroshka.rtp.form` | Access to UI forms for RTP |
| `/rtp` | `miroshka.rtp.near` | Teleport near other players |
| `/back` | `miroshka.back` | Return to previous location |

## ⚙️ Configuration
The plugin is highly configurable through `config.yml`:
* Customize teleport boundaries (X, Z coordinates)
* Enable/disable water checks
* Configure water breathing duration
* Enable/disable UI forms system
* Configure teleport-near-player feature
* Customize all messages and titles
* Set target world for teleportation

## 📋 Dependencies
* [FormConstructor 3.0.0](https://github.com/MEFRREEX/FormConstructor/releases/tag/3.0.0) - Required for UI forms

## 💡 Tips
* Use `/back` after death to return to your death location
* Configure world borders in config.yml to prevent unwanted teleports
* Adjust water breathing duration based on your server's needs
* The plugin will automatically use command mode if FormConstructor is missing

## 🔒 Default Permissions
All permissions default to OP level for security.

---
Made with ❤️ by MIROSHKA