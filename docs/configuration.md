# HyperConomy Configuration Guide

This guide covers all configuration options available in HyperConomy's `config.yml` file.

## Table of Contents

1. [Feature Toggles](#feature-toggles)
2. [Economy Plugin Settings](#economy-plugin-settings)
3. [Currency Configuration](#currency-configuration)
4. [Database Settings](#database-settings)
5. [Tax Configuration](#tax-configuration)
6. [Shop Settings](#shop-settings)
7. [Multi-Server Configuration](#multi-server-configuration)
8. [Remote GUI Settings](#remote-gui-settings)
9. [Web Page Settings](#web-page-settings)
10. [Other Settings](#other-settings)

## Feature Toggles

The `enable-feature` section controls which features are active in HyperConomy.

```yaml
enable-feature:
  shops: true                    # Enable/disable all shop functionality
  item-displays: true            # Enable item display showcases
  chest-shops: true              # Enable chest shop functionality
  info-signs: true               # Enable information signs
  composite-items: false         # Enable item price linking to recipes
  player-shops: true              # Enable player-owned shops
  scrolling-transaction-signs: false  # Enable scrolling transaction signs
  price-history-storage: true    # Enable price history tracking
  transaction-signs: true       # Enable transaction sign trading
  automatic-backups: true        # Enable automatic database backups
  per-shop-permissions: false     # Enable per-shop permission system
  price-change-notifications: true  # Enable price change notifications
  treat-damaged-items-as-equals-to-undamaged-ones: true  # Treat damaged items same as undamaged
  debug-mode: false              # Enable debug logging
  uuid-support: true             # Use UUIDs for player identification
  time-effects: false            # Enable time-based price effects
```

**Recommendations:**
- Keep `uuid-support: true` for modern servers
- Enable `automatic-backups: true` for production servers
- Disable unused features to improve performance

## Economy Plugin Settings

Configure how HyperConomy handles economy integration.

```yaml
economy-plugin:
  use-external: true             # Use external economy plugin (ServiceIO/Vault)
  hook-internal-economy-into-serviceio: false  # Register internal economy with ServiceIO
  starting-player-account-balance: 0.0  # Starting balance for new players (internal economy)
```

**Settings Explained:**

- **use-external**: When `true`, HyperConomy uses ServiceIO or Vault for economy. When `false`, uses internal economy.
- **hook-internal-economy-into-serviceio**: If using internal economy, register it with ServiceIO so other plugins can use it.
- **starting-player-account-balance**: Initial balance for new players when using internal economy.

**Example Configurations:**

External Economy (Recommended):
```yaml
economy-plugin:
  use-external: true
  hook-internal-economy-into-serviceio: false
  starting-player-account-balance: 0.0
```

Internal Economy:
```yaml
economy-plugin:
  use-external: false
  hook-internal-economy-into-serviceio: true
  starting-player-account-balance: 1000.0
```

## Currency Configuration

Configure the currency system for item-based economies.

```yaml
currency:
  type: ITEM                     # Currency type: ITEM or BALANCE
  item:
    material: GOLD_NUGGET       # Material for item currency
    display-name: "Gold Nugget"  # Display name for currency item
    custom-model-data: null      # Custom model data (optional)
  balance-mode: false            # Use balance mode instead of items
```

**Settings Explained:**

- **type**: `ITEM` for physical item currency, `BALANCE` for balance-based (requires external economy)
- **item.material**: Minecraft material name for currency item
- **item.display-name**: Custom display name for currency item
- **item.custom-model-data**: Custom model data value (for resource pack customization)
- **balance-mode**: When `true`, uses balance-based currency even with item type

**Example Configurations:**

Gold Nugget Currency:
```yaml
currency:
  type: ITEM
  item:
    material: GOLD_NUGGET
    display-name: "Gold Nugget"
    custom-model-data: null
  balance-mode: false
```

Diamond Currency:
```yaml
currency:
  type: ITEM
  item:
    material: DIAMOND
    display-name: "Diamond"
    custom-model-data: null
  balance-mode: false
```

## Database Settings

Configure database connection and behavior.

```yaml
sql:
  use-mysql: false               # Use MySQL instead of SQLite
  log-sql-statements: false      # Log all SQL statements (debugging)
  mysql-connection:
    username: default_username    # MySQL username
    port: 3306                    # MySQL port
    password: default_password    # MySQL password
    host: localhost               # MySQL host
    database: minecraft           # MySQL database name
    usessl: false                 # Use SSL for MySQL connection
```

**Settings Explained:**

- **use-mysql**: When `true`, uses MySQL. When `false`, uses SQLite (default).
- **log-sql-statements**: Enable to log all SQL queries (useful for debugging, but verbose).
- **mysql-connection**: MySQL connection details (only used if `use-mysql: true`).

**Example Configurations:**

SQLite (Default):
```yaml
sql:
  use-mysql: false
  log-sql-statements: false
```

MySQL:
```yaml
sql:
  use-mysql: true
  log-sql-statements: false
  mysql-connection:
    username: hyperconomy_user
    port: 3306
    password: secure_password_here
    host: db.example.com
    database: hyperconomy
    usessl: true
```

**Security Note:** Never commit database passwords to version control. Use environment variables or secure configuration management.

## Tax Configuration

Configure tax rates and behavior.

```yaml
tax:
  account: hyperconomy           # Account that receives tax revenue
  purchase: 3                     # Purchase tax percentage
  initial: 100                    # Initial item tax percentage
  static: 100                     # Static item tax percentage
  enchant: 100                    # Enchantment tax percentage
  sales: 0                        # Sales tax percentage
  dynamic:
    enable: false                 # Enable dynamic tax rates
    money-floor: 0                # Minimum balance for dynamic tax
    money-cap: 1000000           # Maximum balance for dynamic tax
    max-tax-percent: 100         # Maximum tax percentage
```

**Settings Explained:**

- **account**: Account name that receives collected taxes
- **purchase**: Tax percentage when buying from shops
- **sales**: Tax percentage when selling to shops
- **initial**: Tax percentage for initial item pricing
- **static**: Tax percentage for static-priced items
- **enchant**: Tax percentage for enchantment trading
- **dynamic.enable**: Enable progressive tax based on player wealth
- **dynamic.money-floor**: Minimum balance before dynamic tax applies
- **dynamic.money-cap**: Balance at which maximum tax is reached
- **dynamic.max-tax-percent**: Maximum tax percentage for rich players

**Example Configurations:**

Simple Tax:
```yaml
tax:
  account: hyperconomy
  purchase: 5
  sales: 2
  initial: 0
  static: 0
  enchant: 0
  sales: 0
  dynamic:
    enable: false
```

Progressive Tax:
```yaml
tax:
  account: hyperconomy
  purchase: 3
  sales: 0
  initial: 0
  static: 0
  enchant: 0
  dynamic:
    enable: true
    money-floor: 10000
    money-cap: 1000000
    max-tax-percent: 20
```

## Shop Settings

Configure shop behavior and limits.

```yaml
shop:
  default-server-shop-account: hyperconomy  # Default account for server shops
  default-server-shop-account-initial-balance: 20000000  # Starting balance
  display-shop-exit-message: true            # Show message when leaving shop
  max-stock-per-item-in-playershops: 100000  # Max stock per item in player shops
  max-player-shop-volume: 1000               # Max volume for player shops
  max-player-shops-per-player: 1             # Max shops per player
  sell-remaining-if-less-than-requested-amount: true  # Sell partial amounts
  server-shops-have-unlimited-money: false   # Server shops have infinite money
  limit-info-commands-to-shops: false        # Restrict info commands to shops
  block-selling-in-creative-mode: false      # Prevent selling in creative
  show-currency-symbol-after-price: false   # Show currency after price
  unlimited-stock-for-static-items: false    # Unlimited stock for static items
  require-chest-shops-to-be-in-shop: false   # Chest shops must be in shop region
  require-transaction-signs-to-be-in-shop: false  # Signs must be in shop region
  send-price-change-notifications-for: diamond,diamondblock,  # Items to notify for
```

**Settings Explained:**

- **default-server-shop-account**: Account name for server shops
- **default-server-shop-account-initial-balance**: Starting money for server shop account
- **display-shop-exit-message**: Show message when players leave shops
- **max-stock-per-item-in-playershops**: Maximum stock per item in player shops
- **max-player-shop-volume**: Maximum total volume for player shops
- **max-player-shops-per-player**: Maximum number of shops a player can own
- **sell-remaining-if-less-than-requested-amount**: Sell partial amounts if requested amount unavailable
- **server-shops-have-unlimited-money**: Server shops have infinite money (bypasses balance checks)
- **limit-info-commands-to-shops**: Restrict info commands to shop regions only
- **block-selling-in-creative-mode**: Prevent selling items in creative mode
- **show-currency-symbol-after-price**: Display currency symbol after price number
- **unlimited-stock-for-static-items**: Static-priced items have unlimited stock
- **require-chest-shops-to-be-in-shop**: Chest shops must be placed in shop regions
- **require-transaction-signs-to-be-in-shop**: Transaction signs must be in shop regions
- **send-price-change-notifications-for**: Comma-separated list of items to notify about

**Example Configuration:**

Balanced Economy:
```yaml
shop:
  default-server-shop-account: hyperconomy
  default-server-shop-account-initial-balance: 10000000
  display-shop-exit-message: true
  max-stock-per-item-in-playershops: 64000
  max-player-shop-volume: 1000
  max-player-shops-per-player: 3
  sell-remaining-if-less-than-requested-amount: true
  server-shops-have-unlimited-money: false
  limit-info-commands-to-shops: false
  block-selling-in-creative-mode: true
  show-currency-symbol-after-price: false
  unlimited-stock-for-static-items: false
  require-chest-shops-to-be-in-shop: false
  require-transaction-signs-to-be-in-shop: false
  send-price-change-notifications-for: diamond,emerald,gold_ingot,
```

## History Settings

Configure price history tracking.

```yaml
history:
  days-to-save: 7                # Number of days to keep price history
```

**Settings Explained:**

- **days-to-save**: How many days of price history to retain (older data is deleted)

**Recommendations:**
- 7 days: Lightweight, good for most servers
- 30 days: More data, useful for analysis
- 90+ days: Heavy storage usage, only if needed for analysis

## Enchantment Settings

Configure enchantment trading values.

```yaml
enchantment:
  classvalue:
    wood: .1                      # Wood tool enchantment value multiplier
    leather: .1                   # Leather armor enchantment value multiplier
    stone: .15                    # Stone tool enchantment value multiplier
    chainmail: .2                 # Chainmail armor enchantment value multiplier
    iron: .25                     # Iron tool/armor enchantment value multiplier
    gold: .1                      # Gold tool/armor enchantment value multiplier
    diamond: 1                    # Diamond tool/armor enchantment value multiplier
    bow: .25                      # Bow enchantment value multiplier
    book: 1                       # Enchanted book value multiplier
```

**Settings Explained:**

Each value is a multiplier for the base enchantment price. Higher values = more expensive enchantments.

## Bank Settings

Configure bank system limits.

```yaml
bank:
  max-ownerships-per-player: 3   # Maximum banks a player can own
```

## Multi-Server Configuration

Configure multi-server synchronization.

```yaml
multi-server:
  enable: false                   # Enable multi-server sync
  remote-server-ip-addresses: 192.168.1.1,3313;192.168.1.1,3314;192.168.1.2,3313;  # Server list
  port: 3313                      # Port for multi-server communication
  update-interval: 500            # Sync interval in milliseconds
  connection-timeout-ms: 2000     # Connection timeout
  sync-shops: true                # Sync shop data
  sync-trade-objects: true        # Sync trade object data
  sync-accounts: true              # Sync account data
```

**Settings Explained:**

- **enable**: Enable multi-server synchronization
- **remote-server-ip-addresses**: Semicolon-separated list of `ip,port` pairs
- **port**: Port for multi-server communication
- **update-interval**: How often to sync (milliseconds)
- **connection-timeout-ms**: Connection timeout
- **sync-shops**: Synchronize shop data
- **sync-trade-objects**: Synchronize trade objects
- **sync-accounts**: Synchronize account balances

**Example Configuration:**

```yaml
multi-server:
  enable: true
  remote-server-ip-addresses: 192.168.1.10,3313;192.168.1.11,3313;
  port: 3313
  update-interval: 1000
  connection-timeout-ms: 3000
  sync-shops: true
  sync-trade-objects: true
  sync-accounts: true
```

## Remote GUI Settings

Configure remote GUI access.

```yaml
remote-gui:
  enable: false                   # Enable remote GUI
  server: false                   # Act as GUI server
  remote-server-ip: 192.168.1.1   # Remote server IP
  remote-server-port: 3313        # Remote server port
  listen-port: 3313               # Port to listen on
  connection-timeout-ms: 10000     # Connection timeout
  refresh-rate-ms: 20000          # GUI refresh rate
  auth-key: change-me             # Authentication key (CHANGE THIS!)
```

**Security Warning:** Always change the `auth-key` to a secure random string!

## Web Page Settings

Configure web interface.

```yaml
web-page:
  enable: false                   # Enable web interface
  port: 7777                      # Web server port
  background-color: 8FA685        # Background color (hex)
  font-color: F2F2F2              # Font color (hex)
  border-color: "091926"          # Border color (hex)
  increase-value-color: C8D9B0    # Price increase color (hex)
  decrease-value-color: F2B2A8    # Price decrease color (hex)
  highlight-row-color: 8FA685     # Highlight color (hex)
  header-color: "091926"          # Header color (hex)
  table-data-color: 314A59        # Table data color (hex)
  font-size: 12                   # Font size
  font: verdana                   # Font family
  enable-web-api: false           # Enable web API
  web-api-path: API               # API path
```

## Chest Shop Settings

Configure chest shop limits.

```yaml
chest-shop:
  limit-chest-shops: true         # Enable chest shop limits
  max-per-player: 5               # Maximum chest shops per player
```

## Language Settings

```yaml
language: english                 # Language file to use
```

Change the language by setting this to a supported language code, or use `/setlanguage` command in-game.

## Interval Settings

Configure plugin intervals.

```yaml
intervals:
  shop-check: 6                   # Shop check interval (ticks)
  save: 24000                     # Auto-save interval (ticks, 24000 = 20 minutes)
```

**Settings Explained:**

- **shop-check**: How often to check shop status (in Minecraft ticks, 20 ticks = 1 second)
- **save**: How often to save data (in Minecraft ticks)

**Recommendations:**
- Lower intervals = more frequent updates but more server load
- Higher intervals = less frequent updates but less server load
- Default values are balanced for most servers

## Updater Settings

Configure update checking.

```yaml
updater:
  enabled: true                   # Enable update checking
  notify-in-game: true            # Notify admins in-game
  notify-for:
    dev-builds: true              # Notify for dev builds
    beta-builds: true             # Notify for beta builds
    recommended-builds: true      # Notify for recommended builds
```

## Configuration Best Practices

1. **Backup First**: Always backup your `config.yml` before making changes
2. **Test Changes**: Test configuration changes on a test server first
3. **Document Custom Settings**: Keep notes on why you changed specific settings
4. **Security**: Never commit passwords or sensitive data to version control
5. **Performance**: Disable unused features to improve performance
6. **Gradual Changes**: Make changes gradually and test each one

## Reloading Configuration

Most configuration changes require a server restart. Some settings can be changed via commands:

- `/hcset` - Change various settings in-game
- `/hc reload` - Reload configuration (if supported)

**Note:** Database settings always require a restart.

## Troubleshooting

**Configuration Not Loading:**
- Check YAML syntax (use a YAML validator)
- Check file encoding (should be UTF-8)
- Check file permissions

**Settings Not Taking Effect:**
- Restart the server
- Check for errors in server logs
- Verify configuration syntax

**Performance Issues:**
- Reduce update intervals
- Disable unused features
- Check database connection settings

For more help, see the [Usage Guide](usage.md) or check GitHub Issues.


