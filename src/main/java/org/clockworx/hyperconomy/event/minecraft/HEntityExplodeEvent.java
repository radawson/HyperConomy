package org.clockworx.hyperconomy.event.minecraft;

import java.util.ArrayList;

import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.minecraft.HBlock;

public class HEntityExplodeEvent extends HyperEvent {

	private ArrayList<HBlock> blocks;
	
	public HEntityExplodeEvent(ArrayList<HBlock> blocks) {
		this.blocks = blocks;
	}
	
	public ArrayList<HBlock> getBrokenBlocks() {
		return blocks;
	}
	
}
