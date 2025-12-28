package org.clockworx.hyperconomy.event;

import java.io.Serializable;

import org.clockworx.hyperconomy.HyperEconomy;

public class HyperEconomyCreationEvent extends HyperEvent implements Serializable {
	

	private static final long serialVersionUID = -6574521760650516023L;
	private HyperEconomy he;
	
	public HyperEconomyCreationEvent(HyperEconomy he) {
		this.he = he;
	}
	
	public HyperEconomy getHyperEconomy() {
		return he;
	}
	
	
}