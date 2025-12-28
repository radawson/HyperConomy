package org.clockworx.hyperconomy.event;


import org.clockworx.hyperconomy.shop.Shop;

public class ShopModificationEvent extends HyperEvent {
	private Shop s;
	
	public ShopModificationEvent(Shop s) {
		this.s = s;
	}
	
	public Shop getShop() {
		return s;
	}
}