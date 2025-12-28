package org.clockworx.hyperconomy.event;

import org.clockworx.hyperconomy.account.HyperPlayer;

public class HyperPlayerModificationEvent extends HyperEvent {
	private HyperPlayer hp;
	
	public HyperPlayerModificationEvent(HyperPlayer hp) {
		this.hp = hp;
	}
	
	public HyperPlayer getHyperPlayer() {
		return hp;
	}
}