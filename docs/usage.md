# HyperConomy Usage Guide

This guide covers how to use HyperConomy commands, permissions, and common tasks.

## Table of Contents

1. [Basic Commands](#basic-commands)
2. [Trading Commands](#trading-commands)
3. [Shop Management](#shop-management)
4. [Economy Commands](#economy-commands)
5. [Admin Commands](#admin-commands)
6. [Permissions](#permissions)
7. [Common Tasks](#common-tasks)
8. [Troubleshooting](#troubleshooting)

## Basic Commands

### `/hc` or `/hyperconomy`
Main HyperConomy command. Displays help and plugin information.

**Usage:**
- `/hc` - Display help information
- `/hc enable` - Enable HyperConomy (admin only)
- `/hc disable` - Disable HyperConomy (admin only)

**Permission:** `hyperconomy.use`

### `/hcbalance` or `/hcb`
Display your account balance.

**Usage:**
- `/hcbalance` - Show your balance
- `/hcbalance <player>` - Show another player's balance (requires `hyperconomy.balanceall`)

**Permission:** `hyperconomy.use` (own balance), `hyperconomy.balanceall` (others)

### `/economyinfo` or `/ei`
Display which economy you are currently using.

**Usage:**
- `/economyinfo`

**Permission:** `hyperconomy.use`

## Trading Commands

### `/buy` or `/by`
Buy items from shops.

**Usage:**
- `/buy <item> <amount>` - Buy specified amount of item
- `/buy <item>` - Buy 1 of the item

**Examples:**
- `/buy diamond 10` - Buy 10 diamonds
- `/buy iron_ingot` - Buy 1 iron ingot

**Permission:** `hyperconomy.buy`

### `/sell` or `/sl`
Sell items to shops.

**Usage:**
- `/sell <item> <amount>` - Sell specified amount of item
- `/sell <item>` - Sell 1 of the item

**Examples:**
- `/sell diamond 5` - Sell 5 diamonds
- `/sell cobblestone 64` - Sell 64 cobblestone

**Permission:** `hyperconomy.sell`

### `/sellall`
Sell all items in your inventory.

**Usage:**
- `/sellall` - Sell all sellable items in inventory

**Permission:** `hyperconomy.sellall`

### `/heldbuy` or `/hb`
Buy the item you're currently holding.

**Usage:**
- `/heldbuy <amount>` - Buy specified amount
- `/heldbuy` - Buy 1

**Permission:** `hyperconomy.buy`

### `/heldsell` or `/hs`
Sell the item you're currently holding.

**Usage:**
- `/heldsell <amount>` - Sell specified amount
- `/heldsell` - Sell 1

**Permission:** `hyperconomy.sell`

### `/heldvalue` or `/hv`
Get value information about the item you're holding.

**Usage:**
- `/heldvalue`

**Permission:** `hyperconomy.value`

## Information Commands

### `/value` or `/vl`
Get information about an item's value.

**Usage:**
- `/value <item>` - Show item value information

**Examples:**
- `/value diamond`
- `/value iron_ingot`

**Permission:** `hyperconomy.value`

### `/iteminfo` or `/ii`
Display information about the item you're holding.

**Usage:**
- `/iteminfo`

**Permission:** `hyperconomy.info`

### `/topitems` or `/ti`
List items with the most stock.

**Usage:**
- `/topitems` - Show top items by stock

**Permission:** `hyperconomy.top`

### `/lowstock` or `/low`
List items with the least stock.

**Usage:**
- `/lowstock` - Show items with low stock

**Permission:** `hyperconomy.top`

### `/topenchants` or `/te`
List enchantments with the most stock.

**Usage:**
- `/topenchants`

**Permission:** `hyperconomy.top`

### `/browseshop` or `/bs`
Display items in the global shop or specified shop.

**Usage:**
- `/browseshop` - Browse global shop
- `/browseshop <shop>` - Browse specific shop

**Permission:** `hyperconomy.top`

### `/xpinfo` or `/xpi`
Display information about experience trading.

**Usage:**
- `/xpinfo`

**Permission:** `hyperconomy.info`

## Shop Management

### `/manageshop` or `/ms`
Manage player shops.

**Usage:**
- `/ms create` - Create a new player shop
- `/ms list` - List your shops
- `/ms info` - Get shop information
- `/ms remove` - Remove a shop

**Permission:** `hyperconomy.playershop` (general), `hyperconomy.playershop.create` (create only)

### `/servershop` or `/ss`
Manage server shops (admin only).

**Usage:**
- `/servershop create` - Create server shop
- `/servershop list` - List server shops
- `/servershop info` - Shop information
- `/servershop remove` - Remove shop

**Permission:** `hyperconomy.admin`

### `/hcchestshop` or `/hccs`
Manage chest shops.

**Usage:**
- `/hcchestshop create` - Create chest shop
- `/hcchestshop list` - List your chest shops
- `/hcchestshop remove` - Remove chest shop

**Permission:** `hyperconomy.chestshop`

### `/frameshop` or `/fs`
Create frame shops.

**Usage:**
- `/frameshop create` - Create item frame shop

**Permission:** `hyperconomy.frameshop`

### `/lockshop` or `/ls`
Lock down shops and most commands (admin only).

**Usage:**
- `/lockshop` - Lock/unlock shops

**Permission:** `hyperconomy.admin`

## Economy Commands

### `/hcpay` or `/hcp`
Pay another player or account.

**Usage:**
- `/hcpay <player> <amount>` - Pay a player
- `/hcpay <account> <amount>` - Pay an account

**Examples:**
- `/hcpay PlayerName 1000`
- `/hcpay shop_account 500`

**Permission:** `hyperconomy.pay`

### `/hctop` or `/hct`
Display top player balances.

**Usage:**
- `/hctop` - Show top balances

**Permission:** `hyperconomy.topbalance`

### `/setpassword`
Set your account password.

**Usage:**
- `/setpassword <password>` - Set account password

**Permission:** `hyperconomy.use`

### `/seteconomy` or `/se`
Set your economy (admin only).

**Usage:**
- `/seteconomy <economy>` - Switch to different economy

**Permission:** `hyperconomy.admin`

### `/hceconomy` or `/he`
Manage economies (admin only).

**Usage:**
- `/hceconomy create <name>` - Create new economy
- `/hceconomy list` - List all economies
- `/hceconomy delete <name>` - Delete economy

**Permission:** `hyperconomy.admin`

### `/makeaccount`
Create a new economy account (admin only).

**Usage:**
- `/makeaccount <name>` - Create account

**Permission:** `hyperconomy.admin`

### `/importbalance`
Import player balances from other economy plugins (admin only).

**Usage:**
- `/importbalance` - Import balances

**Permission:** `hyperconomy.admin`

### `/audit`
Check if an account's balance is correct (admin only).

**Usage:**
- `/audit <account>` - Audit account balance

**Permission:** `hyperconomy.admin`

## Admin Commands

### `/hcset` or `/hcs`
Set various HyperConomy settings (admin only).

**Usage:**
- `/hcset <setting> <value>` - Change setting

**Permission:** `hyperconomy.admin`

### `/hcdelete` or `/hcd`
Delete objects and accounts (admin only).

**Usage:**
- `/hcdelete <type> <name>` - Delete object/account

**Permission:** `hyperconomy.admin`

### `/hcgive` or `/hcg`
Give items or money to players (admin only).

**Usage:**
- `/hcgive <player> <item> <amount>` - Give item
- `/hcgive <player> money <amount>` - Give money

**Permission:** `hyperconomy.admin`

### `/additem` or `/ai`
Add the item you're holding to the database (admin only).

**Usage:**
- `/additem` - Add held item to economy

**Permission:** `hyperconomy.admin`

### `/objectsettings` or `/os`
Display shop settings for an object (admin only).

**Usage:**
- `/objectsettings` - Settings for held object
- `/objectsettings <object>` - Settings for specified object

**Permission:** `hyperconomy.settings`

### `/taxsettings` or `/ts`
Display current tax values (admin only).

**Usage:**
- `/taxsettings`

**Permission:** `hyperconomy.settings`

### `/settax` or `/stax`
Set sales tax for purchases (admin only).

**Usage:**
- `/settax <percent>` - Set tax percentage

**Permission:** `hyperconomy.admin`

### `/notify` or `/not`
Toggle price notifications (admin only).

**Usage:**
- `/notify <item>` - Toggle notifications for item

**Permission:** `hyperconomy.admin`

### `/scalebypercent`
Scale selected value by percent (admin only).

**Usage:**
- `/scalebypercent <percent>` - Scale value

**Permission:** `hyperconomy.scalebypercent`

### `/makedisplay` or `/mkd`
Create an item display (admin only).

**Usage:**
- `/makedisplay` - Create display from held item

**Permission:** `hyperconomy.admin`

### `/removedisplay` or `/rdis`
Remove an item display (admin only).

**Usage:**
- `/removedisplay` - Remove nearby display

**Permission:** `hyperconomy.admin`

### `/repairsigns`
Repair info signs within given radius (admin only).

**Usage:**
- `/repairsigns <radius>` - Repair signs in radius

**Permission:** `hyperconomy.admin`

### `/setlanguage` or `/sela`
Switch language files (admin only).

**Usage:**
- `/setlanguage <language>` - Change language

**Permission:** `hyperconomy.admin`

### `/listcategories` or `/lcat`
List all item categories (admin only).

**Usage:**
- `/listcategories`

**Permission:** `hyperconomy.admin`

### `/intervals` or `/ints`
Display secondary thread repeat intervals (admin only).

**Usage:**
- `/intervals`

**Permission:** `hyperconomy.admin`

### `/hyperlog` or `/hl`
Display HyperConomy log information (admin only).

**Usage:**
- `/hyperlog`

**Permission:** `hyperconomy.admin`

### `/hcdata`
Export data from HyperConomy (admin only).

**Usage:**
- `/hcdata` - Export data

**Permission:** `hyperconomy.admin`

### `/hcweb`
Manage web page (admin only).

**Usage:**
- `/hcweb` - Web interface management

**Permission:** `hyperconomy.admin`

### `/timeeffect` or `/hcte`
Add/remove time effects (admin only).

**Usage:**
- `/timeeffect add <name>` - Add time effect
- `/timeeffect remove <name>` - Remove time effect

**Permission:** `hyperconomy.admin`

### `/toggleeconomy`
Switch between external and internal economy (admin only).

**Usage:**
- `/toggleeconomy` - Toggle economy mode

**Permission:** `hyperconomy.admin`

### `/hctest`
Run plugin tests (admin only).

**Usage:**
- `/hctest` - Run tests

**Permission:** `hyperconomy.admin`

## Permissions

### Permission Hierarchy

```
hyperconomy.* (all permissions, default: op)
├── hyperconomy.use (default: true)
│   ├── hyperconomy.buy
│   ├── hyperconomy.sell
│   ├── hyperconomy.value
│   ├── hyperconomy.sellall
│   ├── hyperconomy.info
│   ├── hyperconomy.top
│   ├── hyperconomy.xp
│   ├── hyperconomy.chestshop
│   ├── hyperconomy.playershop
│   ├── hyperconomy.playershop.create
│   ├── hyperconomy.buysign
│   ├── hyperconomy.sellsign
│   └── hyperconomy.pay
├── hyperconomy.admin (default: false)
├── hyperconomy.settings (default: false)
├── hyperconomy.scalebypercent (default: false)
├── hyperconomy.createsign (default: false)
├── hyperconomy.notify (default: false)
├── hyperconomy.shop (default: false)
├── hyperconomy.balanceall (default: false)
├── hyperconomy.topbalance (default: false)
├── hyperconomy.frameshop (default: false)
├── hyperconomy.bank (default: false)
├── hyperconomy.viewbanks (default: false)
└── hyperconomy.taxexempt (default: false)
```

### Common Permission Setups

**Basic Player:**
```yaml
permissions:
  - hyperconomy.use
```

**Trader (Can Buy/Sell):**
```yaml
permissions:
  - hyperconomy.use
  - hyperconomy.buy
  - hyperconomy.sell
```

**Shop Owner:**
```yaml
permissions:
  - hyperconomy.use
  - hyperconomy.playershop
  - hyperconomy.chestshop
```

**Admin:**
```yaml
permissions:
  - hyperconomy.*
```

## Common Tasks

### Setting Up a Player Shop

1. Ensure you have `hyperconomy.playershop` permission
2. Place a container (chest, barrel, etc.)
3. Stock the container with items to sell
4. Run `/manageshop create`
5. Configure shop settings
6. Set prices for items

### Creating a Chest Shop

1. Ensure you have `hyperconomy.chestshop` permission
2. Place a chest
3. Stock the chest with items
4. Run `/hcchestshop create`
5. Configure buy/sell prices
6. Players can now trade with your chest shop

### Adding a Custom Item

1. Hold the item you want to add
2. Run `/additem` (requires admin)
3. Configure item settings:
   - Initial price
   - Stock
   - Price floor/ceiling
   - Static or dynamic pricing

### Setting Up Transaction Signs

1. Place a sign
2. Right-click with the appropriate item (if required)
3. Configure sign type (buy/sell)
4. Set item and price
5. Players can click to trade

### Managing Multiple Economies

1. Create economy: `/hceconomy create <name>`
2. Switch economy: `/seteconomy <name>`
3. Configure economy-specific settings
4. Create shops in that economy

### Setting Up Banks

1. Ensure bank feature is enabled
2. Create bank account: `/hcbank create`
3. Configure bank settings
4. Players can deposit/withdraw

### Configuring Price Notifications

1. Run `/notify <item>` to toggle notifications
2. Or configure in `config.yml`:
   ```yaml
   shop:
     send-price-change-notifications-for: diamond,emerald,gold_ingot,
   ```

## Troubleshooting

### Commands Not Working

**Check Permissions:**
- Verify you have the required permission
- Check permission plugin configuration
- Use `/hc` to see available commands

**Check Shop Lock:**
- If shops are locked, most commands won't work
- Admins can unlock with `/lockshop`

### Items Not Trading

**Check Item Registration:**
- Item must be added to economy (`/additem`)
- Check if item is enabled in config

**Check Stock:**
- Server shops need stock/money
- Player shops need items in container
- Check with `/value <item>`

### Balance Issues

**Check Economy Integration:**
- Verify ServiceIO/Vault is installed
- Check `economy-plugin.use-external` in config
- Use `/economyinfo` to check current economy

**Check Account:**
- Use `/hcbalance` to check balance
- Use `/audit <account>` to verify account integrity

### Shop Not Working

**Check Shop Status:**
- Use `/manageshop info` or `/servershop info`
- Verify shop is not locked
- Check shop region (if required)

**Check Permissions:**
- Verify shop access permissions
- Check per-shop permissions (if enabled)

### Configuration Issues

**Check YAML Syntax:**
- Use YAML validator
- Check for indentation errors
- Verify all required fields are present

**Reload Configuration:**
- Most changes require server restart
- Some can be changed with `/hcset`
- Check server logs for errors

### Performance Issues

**Reduce Update Intervals:**
```yaml
intervals:
  shop-check: 12  # Increase from 6
  save: 48000     # Increase from 24000
```

**Disable Unused Features:**
- Disable features you don't use in `enable-feature` section
- Reduce price history retention
- Limit multi-server sync frequency

### Getting Help

1. Check this documentation
2. Review [Configuration Guide](configuration.md)
3. Check [Features Guide](features.md)
4. Search GitHub Issues
5. Create a new issue with:
   - HyperConomy version
   - Server type and version
   - Error messages
   - Steps to reproduce

## Tips & Best Practices

1. **Start Simple**: Enable only features you need
2. **Test First**: Test changes on a test server
3. **Backup Regularly**: Backup database and config files
4. **Monitor Prices**: Use price notifications for important items
5. **Use Categories**: Organize items with categories
6. **Set Limits**: Configure shop and stock limits appropriately
7. **Document Custom Items**: Keep notes on custom item configurations
8. **Regular Maintenance**: Audit accounts and clean up old data periodically

For more information, see the [Features Guide](features.md) and [Configuration Guide](configuration.md).

