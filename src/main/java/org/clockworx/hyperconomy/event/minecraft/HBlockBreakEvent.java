package org.clockworx.hyperconomy.event.minecraft;

import org.clockworx.hyperconomy.account.HyperPlayer;
import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.minecraft.HBlock;

public class HBlockBreakEvent extends HyperEvent {

	private HBlock block;
	private HyperPlayer hp;
	
	
	public HBlockBreakEvent(HBlock block, HyperPlayer hp) {
		this.block = block;
		this.hp = hp;
	}


	public HBlock getBlock() {
		return block;
	}
	
	public HyperPlayer getPlayer() {
		return hp;
	}
	
	
	
}
