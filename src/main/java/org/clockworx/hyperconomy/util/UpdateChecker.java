package org.clockworx.hyperconomy.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.clockworx.hyperconomy.HyperConomy;
import org.clockworx.hyperconomy.account.HyperPlayer;

public class UpdateChecker {
	
	private HyperConomy hc;
	private String currentVersion;
	private String latestVersion;
	private String type = "";
	private boolean dev;
	private boolean beta;
	private boolean rb;
	private boolean notifyInGame;
	
	private boolean upgradeAvailable = false;
	private boolean runningDevBuild = false;
	
	public UpdateChecker(HyperConomy hc) {
		this.hc = hc;
		String version = hc.getMC().getVersion();
		// Remove 'v' prefix if present for consistent version comparison
		this.currentVersion = version.startsWith("v") ? version.substring(1) : version;
	}
	
	public void runCheck() {
		if (!hc.getConf().getBoolean("updater.enabled")) return;
		dev = hc.getConf().getBoolean("updater.notify-for.dev-builds");
		beta = hc.getConf().getBoolean("updater.notify-for.beta-builds");
		rb = hc.getConf().getBoolean("updater.notify-for.recommended-builds");
		notifyInGame = hc.getConf().getBoolean("updater.notify-in-game");
		
		hc.getMC().logInfo("[HyperConomy]Checking for updates...");
		new Thread(new Runnable() {
			public void run() {
				try {
					// Use GitHub Releases API instead of CurseForge
					URL url = new URL("https://api.github.com/repos/radawson/HyperConomy/releases");
					URLConnection conn = url.openConnection();
					conn.setReadTimeout(10000);
					conn.addRequestProperty("User-Agent", "HyperConomy/v"+hc.getMC().getVersion()+" (by RegalOwl)");
					conn.addRequestProperty("Accept", "application/vnd.github.v3+json");
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder responseBuilder = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						responseBuilder.append(line);
					}
					reader.close();
					String response = responseBuilder.toString();
					JSONArray array = (JSONArray) JSONValue.parse(response);
					if (array == null || array.size() == 0) return;
					
					ArrayList<String> acceptableUpgrades = new ArrayList<String>();
					for (Object o : array) {
						JSONObject release = (JSONObject) o;
						String tagName = (String) release.get("tag_name");
						if (tagName == null) continue;
						
						// Remove 'v' prefix if present (e.g., "v0.975.11" -> "0.975.11")
						String version = tagName.startsWith("v") ? tagName.substring(1) : tagName;
						
						// Get release type from prerelease flag and tag name
						Boolean isPrerelease = (Boolean) release.get("prerelease");
						boolean isDev = isPrerelease != null && isPrerelease;
						boolean isBeta = tagName.toLowerCase().contains("beta");
						boolean isRB = !isDev && !isBeta; // Recommended build = stable release
						
						// Determine type string for display
						String releaseType = isDev ? "DEV" : (isBeta ? "BETA" : "RB");
						
						int code = getVersionComparisonCode(currentVersion, version);
						if (code >= 0) continue; // Current version is same or newer
						
						// Filter by user preferences
						if (isDev && !dev) continue;
						if (isBeta && !beta) continue;
						if (isRB && !rb) continue;
						
						// Format: "v{version} [{type}]" for compatibility with existing code
						String nameData = "v" + version + " [" + releaseType + "]";
						acceptableUpgrades.add(nameData);
					}
					
					if (acceptableUpgrades.size() == 0) {
						// Check if we're running a dev build (newer than latest release)
						if (array.size() > 0) {
							JSONObject latestRelease = (JSONObject) array.get(0);
							String latestTag = (String) latestRelease.get("tag_name");
							if (latestTag != null) {
								String latest = latestTag.startsWith("v") ? latestTag.substring(1) : latestTag;
								int code = getVersionComparisonCode(currentVersion, latest);
								if (code == 1) runningDevBuild = true;
							}
						}
					} else {
						// Get the most recent acceptable upgrade (first in list since GitHub returns newest first)
						String nameData = acceptableUpgrades.get(0);
						type = getType(nameData);
						latestVersion = nameData.substring(1, nameData.indexOf(" "));
						upgradeAvailable = true;
					}
					hc.getMC().runTask(new Runnable() {
						public void run() {
							if (upgradeAvailable) {
								if (notifyInGame) notifyAdmins();
								hc.getMC().logInfo("[HyperConomy]A new "+"["+type+"] build (" + latestVersion + ") is available for download.");
							} else if (runningDevBuild) {
								hc.getMC().logInfo("[HyperConomy]No updates available. You are running a development build.");
							} else {
								hc.getMC().logInfo("[HyperConomy]No updates available.");
							}
						}
					});
				} catch (SocketTimeoutException te) {
					hc.getMC().logInfo("[HyperConomy]Could not connect to server.");
				} catch (Exception e) {
					hc.gSDL().getErrorWriter().writeError(e);
				}
			}
		}).start();
	}
	
	private String getType(String data) {
		String type = "";
		if (data.contains("[") && data.contains("]")) {
			type = data.replace("[Lite]", "");
			type = type.substring(type.indexOf("[") + 1, type.length());
			type = type.substring(0, type.indexOf("]"));
		}
		return type;
	}
	
	private void notifyAdmins() {
		hc.getMC().runTaskLater(new Runnable() {
			public void run() {
				MessageBuilder mb = new MessageBuilder(hc, "NEW_VERSION_AVAILABLE");
				mb.setValue(latestVersion);
				mb.setType(" ["+type+"]");
				for (HyperPlayer hp:hc.getMC().getOnlinePlayers()) {
					if (hp.hasPermission("hyperconomy.admin")) {
						hp.sendMessage(mb.build());
					}
				}
			}
		}, 600L);
	}

	private int getVersionComparisonCode(String currentVersion, String latestVersion) {
		try {
			String[] values = currentVersion.split("\\.");
			String[] referenceValues = latestVersion.split("\\.");
			if (referenceValues.length < 3) return 1;
			if (Integer.parseInt(values[0]) > Integer.parseInt(referenceValues[0])) return 1;
			if (Integer.parseInt(values[0]) < Integer.parseInt(referenceValues[0])) return -1;
			if (Integer.parseInt(values[1]) > Integer.parseInt(referenceValues[1])) return 1;
			if (Integer.parseInt(values[1]) < Integer.parseInt(referenceValues[1])) return -1;
			if (Integer.parseInt(values[2]) > Integer.parseInt(referenceValues[2])) return 1;
			if (Integer.parseInt(values[2]) < Integer.parseInt(referenceValues[2])) return -1;
			return 0;
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	
	
}
