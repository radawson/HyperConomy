package org.clockworx.hyperconomy.gui;

import org.clockworx.hyperconomy.api.MineCraftConnector;
import org.clockworx.hyperconomy.api.ServerConnectionType;
import org.clockworx.hyperconomy.util.DefaultConnector;

public class GUIConnector extends DefaultConnector implements MineCraftConnector {
	

	
	public GUIConnector() {
		super();
	}
	
	@Override
	public void logInfo(String message) {
		System.out.println(message);
	}

	@Override
	public void logSevere(String message) {
		System.out.println(message);
	}
	
	@Override
	public ServerConnectionType getServerConnectionType() {
		return ServerConnectionType.GUI;
	}


}
