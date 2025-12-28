package org.clockworx.hyperconomy.event;

import org.clockworx.hyperconomy.account.HyperBank;

public class HyperBankModificationEvent extends HyperEvent {
	private HyperBank hb;
	
	public HyperBankModificationEvent(HyperBank hb) {
		this.hb = hb;
	}
	
	public HyperBank getHyperBank() {
		return hb;
	}
}