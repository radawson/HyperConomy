package org.clockworx.hyperconomy;


import org.clockworx.hyperconomy.account.HyperBankManager;
import org.clockworx.hyperconomy.account.HyperPlayerManager;
import org.clockworx.hyperconomy.api.API;
import org.clockworx.hyperconomy.api.HEconomyProvider;
import org.clockworx.hyperconomy.api.MineCraftConnector;
import org.clockworx.hyperconomy.command.Additem;
import org.clockworx.hyperconomy.command.Audit;
import org.clockworx.hyperconomy.command.Browseshop;
import org.clockworx.hyperconomy.command.Buy;
import org.clockworx.hyperconomy.command.Economyinfo;
import org.clockworx.hyperconomy.command.Frameshopcommand;
import org.clockworx.hyperconomy.command.Hb;
import org.clockworx.hyperconomy.command.HcCommand;
import org.clockworx.hyperconomy.command.Hcbalance;
import org.clockworx.hyperconomy.command.Hcbank;
import org.clockworx.hyperconomy.command.Hcchestshop;
import org.clockworx.hyperconomy.command.Hcdata;
import org.clockworx.hyperconomy.command.Hcdelete;
import org.clockworx.hyperconomy.command.Hceconomy;
import org.clockworx.hyperconomy.command.Hcgive;
import org.clockworx.hyperconomy.command.Hcpay;
import org.clockworx.hyperconomy.command.Hcset;
import org.clockworx.hyperconomy.command.Hctest;
import org.clockworx.hyperconomy.command.Hctop;
import org.clockworx.hyperconomy.command.Hcweb;
import org.clockworx.hyperconomy.command.Hs;
import org.clockworx.hyperconomy.command.Hv;
import org.clockworx.hyperconomy.command.Hyperlog;
import org.clockworx.hyperconomy.command.Importbalance;
import org.clockworx.hyperconomy.command.Intervals;
import org.clockworx.hyperconomy.command.Iteminfo;
import org.clockworx.hyperconomy.command.Listcategories;
import org.clockworx.hyperconomy.command.Lockshop;
import org.clockworx.hyperconomy.command.Lowstock;
import org.clockworx.hyperconomy.command.Makeaccount;
import org.clockworx.hyperconomy.command.Makedisplay;
import org.clockworx.hyperconomy.command.Manageshop;
import org.clockworx.hyperconomy.command.Notify;
import org.clockworx.hyperconomy.command.Objectsettings;
import org.clockworx.hyperconomy.command.Removedisplay;
import org.clockworx.hyperconomy.command.Repairsigns;
import org.clockworx.hyperconomy.command.Scalebypercent;
import org.clockworx.hyperconomy.command.Sell;
import org.clockworx.hyperconomy.command.Sellall;
import org.clockworx.hyperconomy.command.Servershopcommand;
import org.clockworx.hyperconomy.command.Seteconomy;
import org.clockworx.hyperconomy.command.Setlanguage;
import org.clockworx.hyperconomy.command.Setpassword;
import org.clockworx.hyperconomy.command.Settax;
import org.clockworx.hyperconomy.command.Taxsettings;
import org.clockworx.hyperconomy.command.Timeeffect;
import org.clockworx.hyperconomy.command.Toggleeconomy;
import org.clockworx.hyperconomy.command.Topenchants;
import org.clockworx.hyperconomy.command.Topitems;
import org.clockworx.hyperconomy.command.Value;
import org.clockworx.hyperconomy.command.Xpinfo;
import org.clockworx.hyperconomy.display.FrameShopHandler;
import org.clockworx.hyperconomy.display.InfoSignHandler;
import org.clockworx.hyperconomy.display.ItemDisplayHandler;
import org.clockworx.hyperconomy.display.TransactionSignHandler;
import org.clockworx.hyperconomy.event.DataLoadEvent;
import org.clockworx.hyperconomy.event.DataLoadEvent.DataLoadType;
import org.clockworx.hyperconomy.event.DisableEvent;
import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.event.HyperEventHandler;
import org.clockworx.hyperconomy.event.HyperEventListener;
import org.clockworx.hyperconomy.gui.RemoteGUIServer;
import org.clockworx.hyperconomy.inventory.HItemStack;
import org.clockworx.hyperconomy.multiserver.MultiServer;
import org.clockworx.hyperconomy.shop.HyperShopManager;
import org.clockworx.hyperconomy.timeeffects.TimeEffectsManager;
import org.clockworx.hyperconomy.currency.CurrencyManager;
import org.clockworx.hyperconomy.integration.ShopkeepersIntegration;
import org.clockworx.hyperconomy.util.DebugMode;
import org.clockworx.hyperconomy.util.History;
import org.clockworx.hyperconomy.util.HyperLock;
import org.clockworx.hyperconomy.util.LanguageFile;
import org.clockworx.hyperconomy.util.LibraryManager;
import org.clockworx.hyperconomy.util.Log;
import org.clockworx.hyperconomy.util.UpdateChecker;
import org.clockworx.hyperconomy.util.UpdateYML;
import org.clockworx.hyperconomy.webpage.HyperConomy_Web;
import regalowl.simpledatalib.SimpleDataLib;
import regalowl.simpledatalib.event.SDLEventListener;
import regalowl.simpledatalib.event.SDLEvent;
import regalowl.simpledatalib.events.LogEvent;
import regalowl.simpledatalib.events.LogLevel;
import regalowl.simpledatalib.events.ShutdownEvent;
import regalowl.simpledatalib.file.FileTools;
import regalowl.simpledatalib.file.FileConfiguration;
import regalowl.simpledatalib.file.YamlHandler;
import regalowl.simpledatalib.sql.SQLManager;
import regalowl.simpledatalib.sql.SQLWrite;
import regalowl.simpledatalib.sql.SQLRead;

import java.util.concurrent.atomic.AtomicBoolean;

public class HyperConomy implements HyperEventListener, SDLEventListener {

	private transient MineCraftConnector mc;
	private transient API api;
	private transient DataManager dm;
	private transient SimpleDataLib sdl;
	private transient Log l;
	private transient InfoSignHandler isign;
	private transient History hist;
	private transient ItemDisplayHandler itdi;
	private transient FrameShopHandler fsh;
	private transient HyperLock hl;
	private transient LanguageFile L;
	private transient HyperEventHandler heh;
	private transient FileTools ft;
	private transient FileConfiguration hConfig;
	private transient DebugMode dMode;
	private transient HyperConomy_Web hcweb;
	private transient RemoteGUIServer rgs;
	private transient TimeEffectsManager tem;
	private transient CurrencyManager currencyManager;
	private transient ShopkeepersIntegration shopkeepersIntegration;
	private transient HItemStack blankStack;
	//private transient DisabledProtection dp;
	private final int saveInterval = 1200000;
	private AtomicBoolean enabled = new AtomicBoolean();
	private AtomicBoolean loaded = new AtomicBoolean();
	private AtomicBoolean loadingStarted = new AtomicBoolean();
	private AtomicBoolean waitingForLibraries = new AtomicBoolean();
	private AtomicBoolean preEnabled = new AtomicBoolean();
	private String consoleEconomy;
	private LibraryManager lm;

	public HyperConomy(MineCraftConnector mc) {
		this.mc = mc;
		this.consoleEconomy = "default";
		this.blankStack = new HItemStack(this);
	}
	
	
	@Override
	public void handleSDLEvent(SDLEvent event) {
		if (event instanceof LogEvent) {
			LogEvent levent = (LogEvent)event;
			if (levent.getException() != null) levent.getException().printStackTrace();
			if (levent.getLevel() == LogLevel.SEVERE || levent.getLevel() == LogLevel.ERROR) mc.logSevere(levent.getMessage());
			if (levent.getLevel() == LogLevel.INFO) mc.logInfo(levent.getMessage());
		} else if (event instanceof ShutdownEvent) {
			disable(false);
		} 
	}
	
	@Override
	public void handleHyperEvent(HyperEvent event) {
		if (event instanceof DataLoadEvent) {
			DataLoadEvent devent = (DataLoadEvent)event;
			if (devent.loadType == DataLoadType.COMPLETE) {
				hist = new History(this);
				itdi = new ItemDisplayHandler(this);
				isign = new InfoSignHandler(this);
				fsh = mc.getFrameShopHandler();
				tem = new TimeEffectsManager(this);
				hcweb = new HyperConomy_Web(this);
				registerCommands();
				loaded.set(true);;
				hl.setLoadLock(false);
				mc.setListenerState(false);
				dMode.syncDebugConsoleMessage("Data loading completed.");
				UpdateChecker uc = new UpdateChecker(this);
				uc.runCheck();
			} else if (devent.loadType == DataLoadType.LIBRARIES) {
				if (lm.dependencyError()) {
					disable(true);
					return;
				}
				waitingForLibraries.set(false);
				if (enabled.get() && !loadingStarted.get()) enable();
			}
		}
	}



	public void load() {
		loaded.set(false);
		enabled.set(false);
		loadingStarted.set(false);
		preEnabled.set(false);
		if (sdl != null) sdl.shutDown();
		sdl = new SimpleDataLib("HyperConomy");
		sdl.initialize();
		sdl.getEventPublisher().registerListener(this);
		ft = sdl.getFileTools();
		if (heh != null) heh.clearListeners();
		heh = new HyperEventHandler(this);
		heh.registerListener(this);
		//dp = new DisabledProtection(this);
		//dp.enable();
		waitingForLibraries.set(true);
		lm = new LibraryManager(this,heh);
	}

	public void enable() {
		if (!preEnabled.get()) {
			preEnable();
		}
		if (lm.dependencyError()) {
			return;
		}
		enabled.set(true);
		if (waitingForLibraries.get()) {
			return;
		}
		loadingStarted.set(true);
		if (hConfig.getBoolean("sql.use-mysql")) {
			String username = hConfig.getString("sql.mysql-connection.username");
			String password = hConfig.getString("sql.mysql-connection.password");
			int port = hConfig.getInt("sql.mysql-connection.port");
			String host = hConfig.getString("sql.mysql-connection.host");
			String database = hConfig.getString("sql.mysql-connection.database");
			boolean usessl = hConfig.getBoolean("sql.mysql-connection.usesll");
			sdl.getSQLManager().enableMySQL(host, database, username, password, port, usessl);
		}
		dMode.syncDebugConsoleMessage("Expected plugin folder path: [" + sdl.getStoragePath() + "]");
		sdl.getSQLManager().createDatabase();
		dMode.syncDebugConsoleMessage("Database created.");
		sdl.getSQLManager().getSQLWrite().setLogSQL(hConfig.getBoolean("sql.log-sql-statements"));
		l = new Log(this);
		new TransactionSignHandler(this);
		sdl.getYamlHandler().startSaveTask(saveInterval);
		new MultiServer(this);
		rgs = new RemoteGUIServer(this);
		dMode.syncDebugConsoleMessage("Data loading started.");
		heh.fireEvent(new DataLoadEvent(DataLoadType.START));
	}
	
	private void preEnable() {
		preEnabled.set(true);
		mc.unregisterAllListeners();
		mc.registerListeners();
		YamlHandler yh = sdl.getYamlHandler();
		yh.copyFromJar("config");
		yh.registerFileConfiguration("config");
		hConfig = yh.gFC("config");
		new UpdateYML(this);
		L = new LanguageFile(this);
		hl = new HyperLock(this, true, false, false);
		dMode = new DebugMode(this);
		dMode.syncDebugConsoleMessage("HyperConomy loaded with Debug Mode enabled.  Configuration files created and loaded.");
		dm = new DataManager(this);
		dm.initialize();
		currencyManager = new CurrencyManager(this);
		shopkeepersIntegration = new ShopkeepersIntegration(this);
		mc.checkExternalEconomyRegistration();
		api = new HyperAPI(this);
		mc.setupHEconomyProvider();
	}
	
	public void disable(boolean protect) {
		heh.fireEvent(new DisableEvent());
		mc.unRegisterAsExternalEconomy();
		enabled.set(false);
		loadingStarted.set(false);
		loaded.set(false);
		if (!protect) mc.unregisterAllListeners();
		if (hcweb != null) hcweb.disable();
		if (itdi != null) itdi.unloadDisplays();
		if (hist != null) hist.stopHistoryLog();
		if (tem != null) tem.disable();
		if (dm != null) dm.shutDown();
		mc.cancelAllTasks();
		if (heh != null && !protect) heh.clearListeners();
		if (sdl != null) sdl.shutDown();
		if (protect) mc.setListenerState(true);
	}
	
	public void restart() {
		disable(true);
		load();
		enable();
	}

	private void registerCommands() {
		mc.registerCommand("additem", new Additem(this));
		mc.registerCommand("audit", new Audit(this));
		mc.registerCommand("browseshop", new Browseshop(this));
		mc.registerCommand("buy", new Buy(this));
		mc.registerCommand("economyinfo", new Economyinfo(this));
		mc.registerCommand("frameshop", new Frameshopcommand(this));
		mc.registerCommand("heldbuy", new Hb(this));
		mc.registerCommand("hc", new HcCommand(this));
		mc.registerCommand("hcbalance", new Hcbalance(this));
		mc.registerCommand("hcbank", new Hcbank(this));
		mc.registerCommand("hcchestshop", new Hcchestshop(this));
		mc.registerCommand("hcdata", new Hcdata(this));
		mc.registerCommand("hcdelete", new Hcdelete(this));
		mc.registerCommand("hceconomy", new Hceconomy(this));
		mc.registerCommand("hcpay", new Hcpay(this));
		mc.registerCommand("hcgive", new Hcgive(this));
		mc.registerCommand("hcset", new Hcset(this));
		mc.registerCommand("hctest", new Hctest(this));
		mc.registerCommand("hctop", new Hctop(this));
		mc.registerCommand("hcweb", new Hcweb(this));
		mc.registerCommand("heldsell", new Hs(this));
		mc.registerCommand("heldvalue", new Hv(this));
		mc.registerCommand("hyperlog", new Hyperlog(this));
		mc.registerCommand("importbalance", new Importbalance(this));
		mc.registerCommand("intervals", new Intervals(this));
		mc.registerCommand("iteminfo", new Iteminfo(this));
		mc.registerCommand("listcategories", new Listcategories(this));
		mc.registerCommand("lockshop", new Lockshop(this));
		mc.registerCommand("lowstock", new Lowstock(this));
		mc.registerCommand("makeaccount", new Makeaccount(this));
		mc.registerCommand("makedisplay", new Makedisplay(this));
		mc.registerCommand("manageshop", new Manageshop(this));
		mc.registerCommand("notify", new Notify(this));
		mc.registerCommand("objectsettings", new Objectsettings(this));
		mc.registerCommand("removedisplay", new Removedisplay(this));
		mc.registerCommand("repairsigns", new Repairsigns(this));
		mc.registerCommand("scalebypercent", new Scalebypercent(this));
		mc.registerCommand("sell", new Sell(this));
		mc.registerCommand("sellall", new Sellall(this));
		mc.registerCommand("servershop", new Servershopcommand(this));
		mc.registerCommand("seteconomy", new Seteconomy(this));
		mc.registerCommand("setlanguage", new Setlanguage(this));
		mc.registerCommand("setpassword", new Setpassword(this));
		mc.registerCommand("settax", new Settax(this));
		mc.registerCommand("taxsettings", new Taxsettings(this));
		mc.registerCommand("timeeffect", new Timeeffect(this));
		mc.registerCommand("toggleeconomy", new Toggleeconomy(this));
		mc.registerCommand("topenchants", new Topenchants(this));
		mc.registerCommand("topitems", new Topitems(this));
		mc.registerCommand("value", new Value(this));
		mc.registerCommand("xpinfo", new Xpinfo(this));
	}

	
	
	public HyperLock getHyperLock() {
		return hl;
	}
	public YamlHandler getYamlHandler() {
		return sdl.getYamlHandler();
	}
	public YamlHandler gYH() {
		return sdl.getYamlHandler();
	}
	public FileConfiguration getConf() {
		return hConfig;
	}
	public DataManager getDataManager() {
		return dm;
	}
	public HyperPlayerManager getHyperPlayerManager() {
		return dm.getHyperPlayerManager();
	}
	public HyperBankManager getHyperBankManager() {
		return dm.getHyperBankManager();
	}
	public HyperShopManager getHyperShopManager() {
		return dm.getHyperShopManager();
	}
	public Log getLog() {
		return l;
	}
	public InfoSignHandler getInfoSignHandler() {
		return isign;
	}
	public SQLWrite getSQLWrite() {
		return sdl.getSQLManager().getSQLWrite();
	}
	public SQLRead getSQLRead() {
		return sdl.getSQLManager().getSQLRead();
	}
	public ItemDisplayHandler getItemDisplay() {
		return itdi;
	}
	public History getHistory() {
		return hist;
	}
	public LanguageFile getLanguageFile() {
		return L;
	}
	
	public boolean enabled() {
		return enabled.get();
	}
	
	public boolean loaded() {
		return loaded.get();
	}
	public FrameShopHandler getFrameShopHandler() {
		return fsh;
	}
	public SimpleDataLib getSimpleDataLib() {
		return sdl;
	}
	public SimpleDataLib gSDL() {
		return sdl;
	}
	public SQLManager getSQLManager() {
		return sdl.getSQLManager();
	}
	public FileTools getFileTools() {
		return ft;
	}
	public String getConsoleEconomy() {
		return consoleEconomy;
	}
	public void setConsoleEconomy(String economy) {
		this.consoleEconomy = economy;
	}
	public HyperEventHandler getHyperEventHandler() {
		return heh;
	}
	public String getFolderPath() {
		return sdl.getStoragePath();
	}
	public DebugMode getDebugMode() {
		return dMode;
	}
	public API getAPI() {
		return api;
	}
	public HEconomyProvider getEconomyAPI() {
		return mc.getEconomyProvider();
	}
	public MineCraftConnector getMC() {
		return mc;
	}
	public HyperConomy_Web getHyperConomyWeb() {
		return hcweb;
	}
	public RemoteGUIServer getRemoteGUIServer() {
		return rgs;
	}
	public TimeEffectsManager getTimeEffectsManager() {
		return tem;
	}
	public LibraryManager getLibraryManager() {
		return lm;
	}
	public CurrencyManager getCurrencyManager() {
		return currencyManager;
	}
	
	public ShopkeepersIntegration getShopkeepersIntegration() {
		return shopkeepersIntegration;
	}
	public HItemStack getBlankStack() {
		return blankStack;
	}


}
