package org.clockworx.hyperconomy.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.clockworx.hyperconomy.HyperConomy;
import org.clockworx.hyperconomy.api.ServerConnectionType;
import org.clockworx.hyperconomy.event.DataLoadEvent;
import org.clockworx.hyperconomy.event.DataLoadEvent.DataLoadType;
import org.clockworx.hyperconomy.event.HyperEvent;
import org.clockworx.hyperconomy.event.HyperEventHandler;
import org.clockworx.hyperconomy.event.HyperEventListener;
import regalowl.simpledatalib.CommonFunctions;
import regalowl.simpledatalib.file.FileTools;


public class LibraryManager implements HyperEventListener {
	private String libFolder;
	private HyperConomy hc;
	private HyperEventHandler heh;
	private LibraryLoadEventTask libraryLoadEventTask;
	private Timer t = new Timer();
	private boolean librariesLoaded;
	private ArrayList<String> dependencyLoadErrors = new ArrayList<String>();
	private boolean dependencyError = false;

	public LibraryManager(HyperConomy hc, HyperEventHandler heh) {
		this.hc = hc;
		this.heh = heh;
		librariesLoaded = false;
		new Thread(new LibraryLoader()).start();
		libraryLoadEventTask = new LibraryLoadEventTask();
		t.schedule(libraryLoadEventTask, 500, 500);
		heh.registerListener(this);
	}
	
	
	
    private class LibraryLoader implements Runnable {

		public void run() {
			FileTools ft = hc.getSimpleDataLib().getFileTools();
			libFolder = hc.getSimpleDataLib().getStoragePath() + File.separator + "lib";
			ft.makeFolder(libFolder);
			
			ArrayList<Dependency> dependencies = new ArrayList<Dependency>();

			dependencies.add(new Dependency(libFolder + File.separator + "sqlite-jdbc-3.7.2.jar", "https://bitbucket.org/xerial/sqlite-jdbc/downloads/sqlite-jdbc-3.20.0.jar", true));
			dependencies.add(new Dependency(libFolder + File.separator + "mysql-connector-java-5.1.44.jar", "https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.44/mysql-connector-java-5.1.44.jar", true));
			dependencies.add(new Dependency(libFolder + File.separator + "json-simple-1.1.1.jar", "https://repo1.maven.org/maven2/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar", true));
			
			//include in jar so that config can load quicker
			//dependencies.add(new Dependency(libFolder + File.separator + "snakeyaml-1.15.jar", "https://oss.sonatype.org/content/groups/public/org/yaml/snakeyaml/1.15/snakeyaml-1.15.jar", true));
			
			
			
			//dependencies.add(new Dependency(libFolder + File.separator + "c3p0-0.9.1.2.jar", "https://repo1.maven.org/maven2/c3p0/c3p0/0.9.1.2/c3p0-0.9.1.2.jar", true));
			//dependencies.add(new Dependency(libFolder + File.separator + "slf4j-api-1.6.1.jar", "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar", true));
			
			
			
			// Optional dependencies for web interface (should be shaded, but download as fallback)
			// These are optional - plugin can run without them if web interface is disabled
			dependencies.add(new Dependency(libFolder + File.separator + "javax.servlet-api-3.0.1.jar", "https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar", false, true));
			dependencies.add(new Dependency(libFolder + File.separator + "jetty-servlet-8.1.9.v20130131.jar", "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-servlet/8.1.9.v20130131/jetty-servlet-8.1.9.v20130131.jar", false, true));
			dependencies.add(new Dependency(libFolder + File.separator + "jetty-continuation-8.1.9.v20130131.jar", "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-continuation/8.1.9.v20130131/jetty-continuation-8.1.9.v20130131.jar", false, true));
			dependencies.add(new Dependency(libFolder + File.separator + "jetty-http-8.1.9.v20130131.jar", "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-http/8.1.9.v20130131/jetty-http-8.1.9.v20130131.jar", false, true));
			dependencies.add(new Dependency(libFolder + File.separator + "jetty-io-8.1.9.v20130131.jar", "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-io/8.1.9.v20130131/jetty-io-8.1.9.v20130131.jar", false, true));
			dependencies.add(new Dependency(libFolder + File.separator + "jetty-server-8.1.9.v20130131.jar", "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-server/8.1.9.v20130131/jetty-server-8.1.9.v20130131.jar", false, true));
			dependencies.add(new Dependency(libFolder + File.separator + "jetty-util-8.1.9.v20130131.jar", "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-util/8.1.9.v20130131/jetty-util-8.1.9.v20130131.jar", false, true));
			dependencies.add(new Dependency(libFolder + File.separator + "jetty-security-8.1.9.v20130131.jar", "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-security/8.1.9.v20130131/jetty-security-8.1.9.v20130131.jar", false, true));
			dependencies.add(new Dependency(libFolder + File.separator + "jetty-jmx-8.1.9.v20130131.jar", "https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-jmx/8.1.9.v20130131/jetty-jmx-8.1.9.v20130131.jar", false, true));
			
			// Optional dependency for CSV functionality (should be shaded, but download as fallback)
			dependencies.add(new Dependency(libFolder + File.separator + "opencsv-2.3.jar", "https://repo1.maven.org/maven2/net/sf/opencsv/opencsv/2.3/opencsv-2.3.jar", false, true));

			//download missing dependencies	
			for (Dependency d:dependencies) {
				if (ft.fileExists(d.filePath)) continue;
				if (hc.getMC().getServerConnectionType() != ServerConnectionType.GUI && d.guiOnly) continue; //skip dependencies only needed for the GUI
				
				// Check if class is already available in classpath (e.g., from shaded dependencies)
				if (d.optional) {
					try {
						// Try to detect if the dependency is already available
						String className = getClassNameFromDependency(d);
						if (className != null) {
							try {
								Class.forName(className);
								// Class is available, skip download
								hc.getMC().logInfo("[HyperConomy]Dependency " + d.getFileName() + " is already available, skipping download.");
								continue;
							} catch (ClassNotFoundException e) {
								// Class not found, need to download
							}
						}
					} catch (Exception e) {
						// Ignore errors during classpath check
					}
				}
				
				try {
					URL link = new URL(d.url);
					InputStream in = new BufferedInputStream(link.openStream());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int n = 0;
					while (-1 != (n = in.read(buf))) {
						out.write(buf, 0, n);
					}
					out.close();
					in.close();
					byte[] response = out.toByteArray();
					FileOutputStream fos = new FileOutputStream(d.filePath);
					fos.write(response);
					fos.close();
					hc.getMC().logInfo("[HyperConomy]Downloaded dependency: " + d.getFileName());
				} catch (IOException e) {
					if (d.optional) {
						hc.getMC().logInfo("[HyperConomy]Failed to download optional dependency: " + d.getFileName() + " (this is OK if the feature is not used)");
						hc.getMC().logInfo("[HyperConomy]If you need this feature, check your internet connection or manually install the library.");
					} else {
						hc.getMC().logSevere("[HyperConomy]Failed to download required dependency: " + d.getFileName());
						hc.getMC().logSevere("[HyperConomy]Check your internet connection or manually install libraries. Cannot run with missing dependencies.");
						e.printStackTrace();
						dependencyError = true;
					}
				} catch (Exception e) {
					if (d.optional) {
						hc.getMC().logInfo("[HyperConomy]Error while downloading optional dependency: " + d.getFileName() + " (this is OK if the feature is not used)");
					} else {
						hc.getMC().logSevere("[HyperConomy]Error while downloading required dependency: " + d.getFileName());
						e.printStackTrace();
						dependencyError = true;
					}
				}
			}
			
			if (dependencyError) {
				librariesLoaded = true;
				return;
			}
			
			//generate list of classes to load and add to classpath
			ArrayList<ClassToLoad> classesToLoad = new ArrayList<ClassToLoad>();
			for (Dependency d:dependencies) {
				if (hc.getMC().getServerConnectionType() != ServerConnectionType.GUI && d.guiOnly) continue;
				File f = new File(d.filePath);
				// Only process if file exists
				if (!f.exists()) {
					if (d.optional) {
						hc.getMC().logInfo("[HyperConomy]Optional dependency " + d.getFileName() + " not found, skipping.");
					}
					continue;
				}
				try {
					addURL(f.toURI().toURL());
					JarFile jar = new JarFile(f);
					Enumeration<JarEntry> j = jar.entries();
					while (j.hasMoreElements()) {
						JarEntry e = j.nextElement();
						if (e.isDirectory() || !e.getName().endsWith(".class")) continue;
						String className = e.getName().substring(0, e.getName().length() - 6);
						className = className.replace('/', '.');
						classesToLoad.add(new ClassToLoad(className));
					}
					jar.close();
				} catch (Exception e) {
					if (d.optional) {
						hc.getMC().logInfo("[HyperConomy]Error processing optional dependency " + d.getFileName() + ": " + e.getMessage());
					} else {
						hc.getMC().logSevere("[HyperConomy]Error processing required dependency " + d.getFileName() + ": " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
			
			

			//load classes in dependencies
			int count = 0;
			boolean complete = false;
			while (!complete) {
				complete = true;
				Iterator<ClassToLoad> itr = classesToLoad.iterator();
				while (itr.hasNext()) {
					ClassToLoad c = itr.next();
					boolean success = c.load();
					if (success) {
						itr.remove();
					} else {
						complete = false;
					}
				}
				count++;
				if (count > 20) {
					for (ClassToLoad c:classesToLoad) {
						dependencyLoadErrors.add("["+c.className+"]"+c.lastError);
					}
					break;
				}
			}

			librariesLoaded = true;
		}
    }
    
    private class ClassToLoad {
    	String className;
    	String lastError;
    	
    	ClassToLoad(String className) {
    		this.className = className;
    	}
    	
    	boolean load() {
    		try {
    			Class.forName(className);
    			return true;
    		} catch (ClassNotFoundException e) {
    			lastError = CommonFunctions.getErrorString(e);
    			return false;
    		} catch (NoClassDefFoundError e) {
    			lastError = CommonFunctions.getErrorString(e);
    			return false;
    		} catch (Exception e) {
    			lastError = CommonFunctions.getErrorString(e);
    			return false;
    		}
    	}
    }
    
    private class Dependency {
    	String filePath;
    	String url;
    	boolean guiOnly;
    	boolean optional; // If true, failure to download won't prevent plugin from loading
    	Dependency(String filePath, String url, boolean guiOnly) {
    		this.url = url;
    		this.filePath = filePath;
    		this.guiOnly = guiOnly;
    		this.optional = false;
    	}
    	Dependency(String filePath, String url, boolean guiOnly, boolean optional) {
    		this.url = url;
    		this.filePath = filePath;
    		this.guiOnly = guiOnly;
    		this.optional = optional;
    	}
    	String getFileName() {
    		return url.substring(url.lastIndexOf("/") + 1, url.length());
    	}
    }
     
    private class LibraryLoadEventTask extends TimerTask {
    	@Override
		public synchronized void run() {
    		if (librariesLoaded && hc.enabled()) {
    			heh.fireEventFromAsyncThread(new DataLoadEvent(DataLoadType.LIBRARIES));
    			libraryLoadEventTask.cancel();
    		}
		}

    }

	private void addURL(URL url) {
		try {
			// Try to use the plugin's classloader first (should be a URLClassLoader)
			ClassLoader pluginClassLoader = hc.getClass().getClassLoader();
			if (pluginClassLoader instanceof URLClassLoader) {
				URLClassLoader loader = (URLClassLoader) pluginClassLoader;
				Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
				method.setAccessible(true);
				method.invoke(loader, url);
				return;
			}
			
			// For Java 9+, try to use reflection to access the URLClassPath field
			try {
				Method addURLMethod = pluginClassLoader.getClass().getDeclaredMethod("addURL", URL.class);
				addURLMethod.setAccessible(true);
				addURLMethod.invoke(pluginClassLoader, url);
				return;
			} catch (NoSuchMethodException e) {
				// Method doesn't exist, try alternative approach
			}
			
			// Last resort: try to use the system classloader if it's a URLClassLoader
			ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
			if (systemClassLoader instanceof URLClassLoader) {
				URLClassLoader loader = (URLClassLoader) systemClassLoader;
				Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
				method.setAccessible(true);
				method.invoke(loader, url);
				return;
			}
			
			// If all else fails, log a warning but don't crash
			hc.getMC().logInfo("[HyperConomy]Could not add URL to classpath (Java 9+ module system). Dependencies may need to be shaded into the plugin JAR.");
		} catch (Exception e) {
			// Don't crash if we can't add the URL - dependencies should be shaded anyway
			hc.getMC().logInfo("[HyperConomy]Could not add URL to classpath: " + e.getMessage());
		}
	}
	
	/**
	 * Attempts to determine a class name from a dependency to check if it's already available.
	 * Returns null if the class name cannot be determined.
	 */
	private String getClassNameFromDependency(Dependency d) {
		String fileName = d.getFileName();
		// Map common dependencies to their main classes
		if (fileName.contains("servlet-api")) {
			return "javax.servlet.Servlet";
		} else if (fileName.contains("jetty")) {
			return "org.eclipse.jetty.server.Server";
		} else if (fileName.contains("opencsv")) {
			return "com.opencsv.CSVReader";
		}
		return null;
	}

	@Override
	public void handleHyperEvent(HyperEvent event) {
		if (event instanceof DataLoadEvent) {
			DataLoadEvent devent = (DataLoadEvent)event;
			if (devent.loadType == DataLoadType.COMPLETE) {
				for (String e:dependencyLoadErrors) {
					hc.getDebugMode().debugWriteMessage("[LibraryManager: failed to load class]"+e);
				}
			}
		}
	}
	
	public boolean dependencyError() {
		return dependencyError;
	}

}
