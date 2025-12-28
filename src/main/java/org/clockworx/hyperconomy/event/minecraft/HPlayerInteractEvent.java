package org.clockworx.hyperconomy.event.minecraft;


import org.clockworx.hyperconomy.account.HyperPlayer;
import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.minecraft.HBlock;

public class HPlayerInteractEvent extends HyperEvent {

		private HyperPlayer hp;
		private HBlock block;
		private boolean isLeftClick;
		
		public HPlayerInteractEvent(HyperPlayer hp, HBlock block, boolean isLeftClick) {
			this.hp = hp;
			this.block = block;
			this.isLeftClick = isLeftClick;
		}

		public HyperPlayer getHyperPlayer() {
			return hp;
		}
	
		public HBlock getBlock() {
			return block;
		}
		
		public boolean isLeftClick() {
			return isLeftClick;
		}
}