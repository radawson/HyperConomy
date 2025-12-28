package org.clockworx.hyperconomy.event.minecraft;

import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.minecraft.HBlock;

public class HBlockPistonRetractEvent extends HyperEvent {

	private HBlock block;
	
	public HBlockPistonRetractEvent(HBlock block) {
		this.block = block;
	}
	
	public HBlock getRetractedBlock() {
		return block;
	}
	
}
