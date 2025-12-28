package org.clockworx.hyperconomy.event;

import org.clockworx.hyperconomy.shop.Shop;

public class ShopCreationEvent extends HyperEvent {
	private Shop s;
	
	public ShopCreationEvent(Shop s) {
		this.s = s;
	}
	
	public Shop getShop() {
		return s;
	}
}