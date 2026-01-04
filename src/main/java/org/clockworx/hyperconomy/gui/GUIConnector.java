package org.clockworx.hyperconomy.gui;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.clockworx.hyperconomy.api.MineCraftConnector;
import org.clockworx.hyperconomy.api.ServerConnectionType;
import org.clockworx.hyperconomy.util.DefaultConnector;

public class GUIConnector extends DefaultConnector implements MineCraftConnector {
	
	/** Logger instance for logging messages. */
	private static final Logger logger = Logger.getLogger("HyperConomy");

	
	public GUIConnector() {
		super();
	}
	
	@Override
	public void logInfo(String message) {
		logger.info(message);
	}

	@Override
	public void logSevere(String message) {
		logger.log(Level.SEVERE, message);
	}
	
	@Override
	public ServerConnectionType getServerConnectionType() {
		return ServerConnectionType.GUI;
	}


}
