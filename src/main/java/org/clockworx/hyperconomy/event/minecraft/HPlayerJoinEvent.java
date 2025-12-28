package org.clockworx.hyperconomy.event.minecraft;


import org.clockworx.hyperconomy.account.HyperPlayer;
import org.clockworx.hyperconomy.event.HyperEvent;

public class HPlayerJoinEvent extends HyperEvent {

		private HyperPlayer hp;
		
		public HPlayerJoinEvent(HyperPlayer hp) {
			this.hp = hp;
		}

		public HyperPlayer getHyperPlayer() {
			return hp;
		}
	
}