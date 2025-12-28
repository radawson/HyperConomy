package org.clockworx.hyperconomy.event.minecraft;


import org.clockworx.hyperconomy.account.HyperPlayer;
import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.minecraft.HSign;

public class HSignChangeEvent extends HyperEvent {

		private HSign sign;
		private HyperPlayer hp;
		
		public HSignChangeEvent(HSign sign, HyperPlayer hp) {
			this.sign = sign;
			this.hp = hp;
		}

		public HSign getSign() {
			return sign;
		}
		
		public HyperPlayer getHyperPlayer() {
			return hp;
		}
	
}