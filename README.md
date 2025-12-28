HyperConomy
===========

HyperConomy is the all-in-one economy and shop plugin for Minecraft servers. It can be a simple chest or sign shop plugin, a Vault/ServiceIO compatible economy plugin, a region-based shop plugin with many configurable options, or even a complex system of economically competing towns and kingdoms. Any vanilla Minecraft item can be traded in HyperConomy and virtually any custom item can be added as well. Prices can be configured to change dynamically or they can be set as desired. HyperConomy has many features including item price linking, showcase-style item displays, and a flexible currency system.

**Requirements:**
- Paper/Spigot 1.21+ (Paper recommended)
- Java 21 or later
- ServiceIO (for economy integration) or Vault (legacy)

## Installation

### For Server Owners

1. Download HyperConomy releases from [GitHub Releases](https://github.com/radawson/HyperConomy/releases) or build from source
2. Place the HyperConomy jar file in your server's `plugins` folder
3. Restart your server
4. Configure HyperConomy via `plugins/HyperConomy/config.yml`
5. See the [Configuration Guide](docs/configuration.md) for detailed setup instructions

### Building from Source

```bash
git clone https://github.com/radawson/HyperConomy.git
cd HyperConomy
./gradlew build
```

The built jar will be in `build/libs/` with the `-paper.jar` suffix.

**Note:** HyperConomy uses Gradle with Paperweight for building. Java 21+ is required for development.


## Features

### Core Features
* **Comprehensive Item Support**: Supports all standard Minecraft items (fireworks, lore, books, maps, damaged items, etc.)
* **Custom Items**: Supports the addition of unlimited custom items
* **Economy Integration**: Integrated economy plugin with ServiceIO/Vault compatibility
* **Multiple Shop Types**: Server shops, player shops, chest shops, and item frame shops
* **Dynamic Pricing**: Hyperbolic curve-based pricing system that responds to supply and demand
* **Item Price Linking**: Recipe items automatically price based on component items
* **Multiple Economies**: Create unlimited separate economies for competing towns, kingdoms, factions, or groups
* **Flexible Currency System**: Item-based currency or traditional balance-based economy

### Trading & Commerce
* **Player Trading**: Player-to-player, player-to-shop, and shop-to-player trading
* **Transaction Signs**: Command-free economies using interactive signs
* **Chest Shops**: Container-based shops that pull from player inventories
* **Item Displays**: Showcase-style item displays for shop presentation
* **Info Signs**: Automatically updating information signs showing prices, history, and more

### Economy & Pricing
* **Tax System**: Static and dynamic tax rates (rich players can be taxed more than poor ones)
* **Pricing Controls**: Price floor, price ceiling, static pricing, initial pricing, and unlimited stock options
* **Price History**: Track price changes over time with configurable retention
* **Price Notifications**: Get notified when prices change for specific items
* **Experience Trading**: Trade experience points and enchantments

### Advanced Features
* **Multi-Server Support**: Synchronize shops, accounts, and trade objects across multiple servers
* **Remote GUI**: Access and manage HyperConomy remotely via GUI
* **Web Interface**: Optional web page for viewing economy data
* **Bank System**: Player-owned banks with configurable ownership limits
* **Time Effects**: Optional time-based effects on prices and availability
* **Localization**: Multi-language support
* **Hot Configuration**: Nearly everything can be changed while the server is running
* **Feature Toggles**: Disable unwanted features for a streamlined experience

### Technical Features
* **Database Support**: MySQL, SQLite, and PostgreSQL support via Hibernate
* **Modern Architecture**: Built on Paper API with modern Java patterns
* **Shopkeepers Integration**: Compatible with Shopkeepers plugin
* **Migration System**: Automatic database migrations using Flyway

For detailed feature documentation, see [Features Guide](docs/features.md).


## Documentation

Comprehensive documentation is available in the `docs/` folder:

* **[Features Guide](docs/features.md)**: Detailed documentation of all HyperConomy features
* **[Configuration Guide](docs/configuration.md)**: Complete configuration reference
* **[Usage Guide](docs/usage.md)**: User guide for commands, permissions, and common tasks

## Recent Improvements

HyperConomy has been modernized with the following improvements:

* **Paper API**: Migrated from deprecated Bukkit/Spigot APIs to modern Paper API
* **Java 21+**: Updated to require Java 21 for better performance and modern language features
* **Modern Database Stack**: Integrated Hibernate ORM with Flyway migrations for better database management
* **Connection Pooling**: Uses HikariCP for efficient database connection management
* **Currency System**: New flexible currency system supporting both item-based and balance-based economies
* **Shopkeepers Integration**: Enhanced integration with Shopkeepers plugin
* **Improved Architecture**: Better code organization and maintainability

## Releases

Releases are available on [GitHub Releases](https://github.com/radawson/HyperConomy/releases).

* **Stable Releases**: Recommended for production servers
* **Beta Builds**: Feature-complete but may contain bugs from large changes
* **Development Builds**: Latest features and bug fixes, may be unstable

When reporting bugs, please include:
* HyperConomy version
* Server type and version (Paper/Spigot)
* Full error message or stack trace
* Steps to reproduce the issue


## Add-ons & Integrations

### Compatible Plugins
* **ServiceIO**: Modern economy API (recommended)
* **Vault**: Legacy economy API (still supported)
* **Shopkeepers**: Enhanced shopkeeper integration
* **Essentials**: Optional integration for economy features
* **WorldGuard**: Region-based shop restrictions
* **Citizens**: NPC shopkeeper support via HyperMerchant

### Add-on Plugins
* **[HyperMerchant](http://dev.bukkit.org/bukkit-plugins/hypermerchant/)**: Graphical user interface for players to interact with HyperConomy shops. Also includes the ability to easily create Citizens NPC shopkeepers for your HyperConomy shops.


## Support & Contributing

### Getting Help
* Check the [Usage Guide](docs/usage.md) for common tasks
* Review the [Configuration Guide](docs/configuration.md) for setup questions
* Search existing [GitHub Issues](https://github.com/radawson/HyperConomy/issues)
* Create a new issue with:
  * HyperConomy version
  * Server type and version
  * Full error message or stack trace
  * Steps to reproduce (if applicable)

### Contributing
Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes following the project's coding standards
4. Submit a pull request

See the project's `.cursorrules` file for development guidelines.

## Links

* **[GitHub Repository](https://github.com/radawson/HyperConomy)**: Source code and issue tracking
* **[GitHub Releases](https://github.com/radawson/HyperConomy/releases)**: Download latest releases
* **[Documentation](docs/)**: Complete user and configuration documentation
* **[Legacy Documentation](https://github.com/RegalOwl/HyperConomy-Documentation)**: Original documentation wiki

## License

See [LICENSE](LICENSE) file for details.
