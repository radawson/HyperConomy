package org.clockworx.hyperconomy.event.minecraft;

import org.clockworx.hyperconomy.account.HyperPlayer;
import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.minecraft.HItem;

public class HPlayerDropItemEvent extends HyperEvent {

	private HItem i;
	private HyperPlayer hp;
	
	public HPlayerDropItemEvent(HItem i, HyperPlayer hp) {
		this.i = i;
		this.hp = hp;
	}
	
	public HItem getItem() {
		return i;
	}
	public HyperPlayer getPlayer() {
		return hp;
	}
	
}
