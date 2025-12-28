package org.clockworx.hyperconomy.event.minecraft;

import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.minecraft.HBlock;

public class HBlockPlaceEvent extends HyperEvent {

	private HBlock block;
	
	public HBlockPlaceEvent(HBlock block) {
		this.block = block;
	}
	
	public HBlock getBlock() {
		return block;
	}
	
}
