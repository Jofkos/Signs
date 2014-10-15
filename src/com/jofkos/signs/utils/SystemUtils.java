package com.jofkos.signs.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SystemUtils {

	public static boolean isWin() {
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}

	public static void copy(InputStream src, File targ) throws Exception {
		FileOutputStream out = new FileOutputStream(targ);
		byte[] buffer = new byte[1024];
		int readBytes;
		while ((readBytes = src.read(buffer)) > 0) {
			out.write(buffer, 0, readBytes);
		}
		src.close(); out.close();
	}

	public static void hide(File f) {
		try {
			if (isWin()) run("attrib", "+h", f.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Process run(String... cmd) throws Exception {
		ProcessBuilder b = new ProcessBuilder(cmd);
		b.redirectErrorStream(true);
		return b.start();
	}

}
