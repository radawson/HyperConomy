package org.clockworx.hyperconomy.event.minecraft;

import org.clockworx.hyperconomy.account.HyperPlayer;
import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.shop.ChestShop;

public class ChestShopOpenEvent extends HyperEvent {

	private HyperPlayer opener;
	private ChestShop chestShop;
	
	public ChestShopOpenEvent(HyperPlayer clicker, ChestShop chestShop) {
		this.opener = clicker;
		this.chestShop = chestShop;
	}

	public HyperPlayer getOpener() {
		return opener;
	}
	public ChestShop getChestShop() {
		return chestShop;
	}

	
}
