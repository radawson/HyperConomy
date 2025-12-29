# HyperConomy Features

This document provides a comprehensive overview of all features available in HyperConomy.

## Table of Contents

1. [Shop Types](#shop-types)
2. [Economy System](#economy-system)
3. [Currency System](#currency-system)
4. [Pricing System](#pricing-system)
5. [Tax System](#tax-system)
6. [Item Management](#item-management)
7. [Trading Features](#trading-features)
8. [Display Features](#display-features)
9. [Multi-Server Support](#multi-server-support)
10. [Web Interface](#web-interface)
11. [Bank System](#bank-system)

## Shop Types

### Server Shops
Server shops are admin-controlled shops that can have unlimited money and stock. They serve as the default trading interface for players.

**Features:**
- Unlimited money option (configurable)
- Unlimited stock for static items
- Customizable account balance
- Can be locked to prevent trading

**Use Cases:**
- Central marketplace
- Resource distribution
- Economy stabilization

### Player Shops
Player-owned shops allow players to create and manage their own trading locations.

**Features:**
- Limited stock from player containers
- Configurable maximum shops per player
- Volume-based shop limits
- Stock management per item
- Owner permissions and access control

**Use Cases:**
- Player-to-player trading
- Player-run businesses
- Decentralized economy

### Chest Shops
Chest shops are container-based shops that pull items directly from chests.

**Features:**
- Direct container integration
- Automatic stock management
- Configurable per-player limits
- Can require shop region placement

**Use Cases:**
- Automated trading posts
- Resource collection points
- Simple shop setup

### Frame Shops
Item frame shops use item frames as shop interfaces.

**Features:**
- Visual item display
- Interactive frame clicking
- Compact shop design

**Use Cases:**
- Decorative shops
- Space-efficient trading
- Visual shop displays

## Economy System

### Internal Economy
HyperConomy includes a built-in economy system that can function independently.

**Features:**
- Player account management
- Balance tracking
- Transaction logging
- Account passwords
- Starting balance configuration

### External Economy Integration
HyperConomy can integrate with external economy plugins via ServiceIO or Vault.

**Supported Plugins:**
- ServiceIO (recommended, modern API)
- Vault (legacy support)
- Any plugin implementing Vault Economy interface

**Features:**
- Seamless integration
- Balance synchronization
- Import/export capabilities

### Multiple Economies
HyperConomy supports creating unlimited separate economies.

**Use Cases:**
- Competing towns or kingdoms
- Faction-based economies
- Regional economies
- Separate game modes

**Features:**
- Economy isolation
- Independent pricing
- Separate accounts
- Economy-specific shops

## Currency System

HyperConomy supports two currency modes:

### Item-Based Currency
Uses physical items (e.g., gold nuggets) as currency.

**Configuration:**
- Material selection
- Custom display name
- Custom model data support

**Features:**
- Physical currency items
- Inventory-based transactions
- Visual currency representation

### Balance-Based Currency
Traditional balance-based economy (default with external economy plugins).

**Features:**
- Digital currency
- Account balances
- Transaction history

## Pricing System

### Dynamic Pricing
HyperConomy uses a hyperbolic curve-based pricing system that responds to supply and demand.

**How It Works:**
- Prices increase when items are bought
- Prices decrease when items are sold
- Curve shape is configurable per item
- Responds to stock levels

**Benefits:**
- Natural market forces
- Prevents inflation/deflation
- Self-balancing economy

### Static Pricing
Items can be set to static prices that never change.

**Use Cases:**
- Fixed-price items
- Currency items
- Special items

### Price Controls

**Price Floor:**
- Minimum price an item can reach
- Prevents items from becoming worthless

**Price Ceiling:**
- Maximum price an item can reach
- Prevents excessive inflation

**Initial Pricing:**
- Starting price for new items
- Can be based on recipe components

### Item Price Linking
Recipe items can automatically price based on their component items.

**Example:**
- Diamond Sword price = (Diamond price × 2) + (Stick price) + crafting fee
- Automatically updates when components change

**Benefits:**
- Maintains logical pricing relationships
- Reduces manual price management
- Creates realistic economy

## Tax System

### Static Taxes
Fixed percentage taxes applied to transactions.

**Tax Types:**
- Purchase tax: Applied when buying from shops
- Sales tax: Applied when selling to shops
- Initial tax: Applied to initial item pricing
- Static tax: Applied to static-priced items
- Enchantment tax: Applied to enchantment trading

### Dynamic Taxes
Tax rates that change based on player wealth.

**Configuration:**
- Money floor: Minimum balance for dynamic tax
- Money cap: Maximum balance for dynamic tax
- Max tax percent: Maximum tax rate

**Features:**
- Progressive taxation
- Wealth redistribution
- Economy balancing

### Tax Exemptions
Players with `hyperconomy.taxexempt` permission bypass all taxes.

## Item Management

### Item Support
HyperConomy supports all standard Minecraft items including:
- Items with custom names and lore
- Enchanted items
- Damaged items
- Fireworks
- Books
- Maps
- Custom model data items

### Custom Items
Unlimited custom items can be added to the economy.

**Adding Items:**
- Use `/additem` command while holding the item
- Configure pricing and settings
- Set initial stock and price

### Item Categories
Items can be organized into categories for easier management.

**Features:**
- Category-based organization
- Category-specific settings
- List categories with `/listcategories`

### Composite Items
Items can be linked to recipes for automatic pricing (optional feature).

## Trading Features

### Transaction Signs
Interactive signs that allow trading without commands.

**Types:**
- Buy signs: Purchase items by clicking
- Sell signs: Sell items by clicking
- Scrolling signs: Multiple items on one sign (optional)

**Features:**
- Command-free trading
- Visual shop interface
- Can require shop region placement

### Player-to-Player Trading
Direct trading between players via shops.

**Features:**
- Player shop access
- Trade notifications
- Transaction logging

### Experience Trading
Trade experience points and enchantments.

**Features:**
- XP as currency
- Enchantment trading
- Configurable enchantment values

## Display Features

### Item Displays
Showcase-style item displays for shop presentation.

**Features:**
- Visual item representation
- Automatic updates
- Customizable appearance
- Create with `/makedisplay`
- Remove with `/removedisplay`

### Info Signs
Automatically updating signs showing item information.

**Information Displayed:**
- Current price
- Price history
- Stock levels
- Price changes

**Features:**
- Auto-updating
- Configurable update interval
- Repair with `/repairsigns`

### Shop Entry/Exit Messages
Customizable messages when players enter or leave shops.

**Features:**
- Welcome messages
- Exit messages
- Configurable per shop
- Can be disabled

## Multi-Server Support

Synchronize HyperConomy data across multiple servers.

**Synchronized Data:**
- Shops
- Trade objects
- Player accounts
- Prices and stock

**Configuration:**
- Remote server addresses
- Sync intervals
- Connection timeouts
- Selective synchronization

**Use Cases:**
- Network-wide economy
- Cross-server trading
- Unified economy management

## Web Interface

Optional web interface for viewing economy data.

**Features:**
- Price viewing
- Item browsing
- Economy statistics
- Customizable appearance
- Web API (optional)

**Configuration:**
- Port selection
- Color customization
- Font settings
- API enablement

## Bank System

Player-owned banks for advanced economy management.

**Features:**
- Multiple bank ownership per player (configurable limit)
- Bank account management
- Interest rates (if configured)
- Bank transactions

**Use Cases:**
- Player-run banks
- Economy services
- Advanced trading

## Additional Features

### Price History
Track price changes over time.

**Features:**
- Historical price data
- Configurable retention period
- Price trend analysis

### Price Notifications
Get notified when prices change.

**Features:**
- Item-specific notifications
- Global notifications
- Configurable notification list

### Automatic Backups
Automatic database backups (optional).

**Features:**
- Scheduled backups
- Backup retention
- Manual backup triggers

### Localization
Multi-language support.

**Features:**
- Language file system
- Switch languages with `/setlanguage`
- Community translations

### Time Effects
Optional time-based effects on prices and availability.

**Features:**
- Time-based price modifiers
- Availability windows
- Seasonal pricing

### Debug Mode
Development and troubleshooting features.

**Features:**
- Detailed logging
- Performance monitoring
- Error tracking

## Feature Toggles

All major features can be enabled or disabled via configuration:

- Shops
- Item displays
- Chest shops
- Info signs
- Composite items
- Player shops
- Scrolling transaction signs
- Price history storage
- Transaction signs
- Automatic backups
- Per-shop permissions
- Price change notifications
- Time effects

See [Configuration Guide](configuration.md) for details on enabling/disabling features.


