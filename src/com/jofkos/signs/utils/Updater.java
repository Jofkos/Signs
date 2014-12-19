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
	
	private static String CB_LOCAL, CB_REMOTE;
	private static double SIGNS_LOCAL, SIGNS_REMOTE;
	private static String expectedHash;
		
	private BlockLogger logger = new BlockLogger();
	
	public static void checkVersion() {
		if (!Config.NOTIFY_UPDATES) return;
		Bukkit.getScheduler().runTaskAsynchronously(Signs.getInstance(), new Updater());
	}

	@Override
	public void run() {
		CB_LOCAL = NMSUtils.getVersion();
		SIGNS_LOCAL = NumberConversions.toDouble(Signs.getInstance().getDescription().getVersion());
		
		JSONArray remote; JSONObject last;
		try {
			last = getLast(remote = getRemoteValues());
		} catch (Exception e) {
			Signs.log("An error occurred while trying to contact the update server");
			return;
		}		
		
		setRemotes(last);
		
		if (!checkCB()) {
			while ((SIGNS_REMOTE > SIGNS_LOCAL) && (!CB_LOCAL.equals(CB_REMOTE))) {
				last = (JSONObject) remote.get(remote.lastIndexOf(last) - 1);
				setRemotes(last);
			}
			if (!checkCB() || (SIGNS_REMOTE <= SIGNS_LOCAL)) {
				last = getLast(remote);
			}
		}
		
		if (SIGNS_LOCAL >= SIGNS_REMOTE) {
			Signs.log(I18n._("updater.uptodate", SIGNS_LOCAL));
			return;
		}
		
		logger.add("=========== Updater ===========");
		if (Config.AUTO_UPDATE) {
			autoUpdate((String) last.get("downloadUrl"));
		} else {
			logger.add(I18n._("updater.outofdate", SIGNS_LOCAL));
			if (checkCB()) {
				logger.add(I18n._("updater.newversion", SIGNS_REMOTE));
			} else {
				logger.add(I18n._("updater.newversion2", SIGNS_REMOTE, CB_REMOTE));
			}
		}
		logger.add("===============================").out();
	}
	
	private void setRemotes(JSONObject o) {
		CB_REMOTE = getMCVersion((String) o.get("gameVersion"));
		SIGNS_REMOTE = NumberConversions.toDouble(((String) o.get("name")).replace("Signs v", ""));
		expectedHash = (String) o.get("md5");
	}
	
	private boolean checkCB() {
		if (CB_LOCAL.startsWith("1.7") && SIGNS_REMOTE > 1.5) {
			return true;
		} else {
			return CB_LOCAL.contains(CB_REMOTE);
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
	
	private JSONObject getLast(JSONArray a) {
		return (JSONObject) a.get(a.size()-1);
	}
	
	private JSONArray getRemoteValues() throws Exception {
		URLConnection con = new URL("https://api.curseforge.com/servermods/files?projectIds=73860").openConnection();
		con.setConnectTimeout(15 * 1000);
		return (JSONArray) JSONValue.parse(new BufferedReader(new InputStreamReader(con.getInputStream())).readLine());
	}
	
	private void autoUpdate(String url) {
		if (checkCB()) {
			logger.add(I18n._("updater.download.starting"));
			
			long time = System.currentTimeMillis();
			
			if (downloadFile(url)) {
				logger.add(I18n._("updater.download.complete", System.currentTimeMillis() - time, SIGNS_REMOTE));
			}
		} else {
			logger.add(I18n._("updater.othermc", SIGNS_LOCAL, CB_LOCAL, SIGNS_REMOTE, CB_REMOTE));
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
