package org.clockworx.hyperconomy.event.minecraft;

import org.clockworx.hyperconomy.account.HyperPlayer;
import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.shop.ChestShop;

public class ChestShopCloseEvent extends HyperEvent {

	private HyperPlayer closer;
	private ChestShop chestShop;
	
	public ChestShopCloseEvent(HyperPlayer clicker, ChestShop chestShop) {
		this.closer = clicker;
		this.chestShop = chestShop;
	}

	public HyperPlayer getCloser() {
		return closer;
	}
	public ChestShop getChestShop() {
		return chestShop;
	}

	
}
