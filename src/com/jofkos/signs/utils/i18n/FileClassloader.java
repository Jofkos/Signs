package com.jofkos.signs.utils.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import com.jofkos.signs.Signs;

public class FileClassloader extends ClassLoader {
	
	public FileClassloader(ClassLoader loader) {
		super(loader);
	}
	
	@Override
	public URL getResource(String name) {
		try {
			File local = getLocalFile(name);
			if (local.exists()) {
				return local.toURI().toURL();
			}
		} catch (Exception e) {}
		return super.getResource(name);
	}
	
	@Override
	public InputStream getResourceAsStream(String name) {
		try {
			File local = getLocalFile(name);
			if (local.exists()) {
				return new FileInputStream(local);
			}
		} catch (Exception e) {}
		return super.getResourceAsStream(name);
	}
	
	private File getLocalFile(String name) {
		return new File(Signs.getInstance().getDataFolder(), name);
	}
	
}
