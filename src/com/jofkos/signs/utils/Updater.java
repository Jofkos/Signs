package com.jofkos.signs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.Bukkit;
import org.bukkit.util.NumberConversions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.common.io.Files;
import com.jofkos.signs.Signs;
import com.jofkos.signs.utils.i18n.I18n;
import com.jofkos.signs.utils.nms.NMSUtils;

public class Updater implements Runnable {
	
	private static final File JAR = new File("plugins", "Signs.jar");
	private static final File TEMP = new File("plugins/Signs", ".tempupdate");
	
	private static double CB_LOCAL, CB_REMOTE;
	private static double SIGNS_LOCAL, SIGNS_REMOTE;
	private static String expectedHash;
		
	private BlockLogger logger = new BlockLogger();
	
	public static void checkVersion() {
		if (!Config.NOTIFY_UPDATES  && !Config.AUTO_UPDATE) return;
		Bukkit.getScheduler().runTaskAsynchronously(Signs.getInstance(), new Updater());
	}

	@Override
	public void run() {
		CB_LOCAL = formatVersion(NMSUtils.getMCVersion());
		SIGNS_LOCAL = formatVersion(Signs.getInstance().getDescription().getVersion());
		
		JSONObject last;
		
		try {
			URLConnection con = new URL("https://api.curseforge.com/servermods/files?projectIds=73860").openConnection();
			con.setConnectTimeout(15 * 1000);
			JSONArray remote = (JSONArray) JSONValue.parse(new BufferedReader(new InputStreamReader(con.getInputStream())).readLine());
			last = (JSONObject) remote.get(remote.size()-1);
		} catch (Exception e) {
			Signs.log("An error occurred while trying to contact the update server");
			return;
		}		
		
		CB_REMOTE = formatVersion(getMCVersion((String) last.get("gameVersion")));
		SIGNS_REMOTE = formatVersion(((String) last.get("name")).substring(7));
		expectedHash = (String) last.get("md5");
				
		if (SIGNS_LOCAL >= SIGNS_REMOTE) {
			Signs.log(i18n("updater.uptodate", SIGNS_LOCAL));
			return;
		}
		
		logger.add("=========== Updater ===========");
		if (Config.AUTO_UPDATE) {
			autoUpdate((String) last.get("downloadUrl"));
		} else {
			logger.add(i18n("updater.outofdate", SIGNS_LOCAL));
			if (checkCB()) {
				logger.add(i18n("updater.newversion", SIGNS_REMOTE));
			} else {
				logger.add(i18n("updater.newversion2", SIGNS_REMOTE, CB_REMOTE));
			}
		}
		logger.add("===============================").out();
	}
	
	private boolean checkCB() {
		if (CB_LOCAL >= 1.7 && SIGNS_REMOTE > 1.5) {
			return true;
		} else {
			return CB_LOCAL >= CB_REMOTE;
		}
	}
	
	private String getMCVersion(String cb) {
		if (!cb.contains("-") && !cb.contains(" ")) return cb;
		try {
			return cb.split("-")[0].split(" ")[1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cb;
	}
	
	private void autoUpdate(String url) {
		if (checkCB()) {
			logger.add(i18n("updater.download.starting"));
			
			long time = System.currentTimeMillis();
			
			if (downloadFile(url)) {
				logger.add(i18n("updater.download.complete", System.currentTimeMillis() - time, SIGNS_REMOTE));
			}
		} else {
			logger.add(i18n("updater.othermc", SIGNS_LOCAL, CB_LOCAL, SIGNS_REMOTE, CB_REMOTE));
		}
	}
	
	private boolean downloadFile(String url) {
		try {
			InputStream in = new URL(url.replace("http://servermods.cursecdn.com", "http://dev.bukkit.org/media")).openConnection().getInputStream();
			SystemUtils.copy(in, TEMP);
			
			if (SystemUtils.isWin()) SystemUtils.run("attrib", "+h", TEMP.getAbsolutePath());
			
			return true;
		} catch (Exception e) {
			Signs.log("An error occured while downloading the new version: " + e.getMessage());
			return false;
		}
	}
	

	private String formatVersion(double version) {
		return Double.toString(version).replaceAll("(\\d{1})(?!\\.)(?=\\d)", "$1.");
	}
	
	private double formatVersion(String version) {
		return NumberConversions.toDouble(version.replaceAll("(?<=(?:\\d\\.))(.)\\.", "$1"));
	}
	
	private String i18n(String key, Object...objs) {
		for (int i = 0; i < objs.length; i++) {
			Object obj = objs[i];
			if (obj instanceof Double) {
				objs[i] = formatVersion((double) obj);
			}
		}
		return I18n._(key, objs);
	}
	
	public static boolean moveTemp() throws Exception {
		if (!TEMP.exists()) return false;
		boolean is = isExpectedFile(TEMP);
		if (is) {
			JAR.delete();
			SystemUtils.copy(new FileInputStream(TEMP), JAR);
		}
		
		if (SystemUtils.isWin()) System.gc();
		TEMP.delete();
		
		if (!is) {
			throw new Exception();
		}
		Signs.log(I18n._("updater.installed"));
		return true;
	}

	private static boolean isExpectedFile(File f) {
		try {
			return expectedHash.equals(DatatypeConverter.printHexBinary(MessageDigest.getInstance("md5").digest(Files.toByteArray(f))).toLowerCase());
		} catch(Exception e) {
			return false;
		}
	}
}
