package com.jofkos.signs.utils.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.jofkos.signs.Signs;
import com.jofkos.signs.utils.Config;

public class I18n {
	
	private static ResourceBundle localeBundle;
	
	public static void load() {
		localeBundle = ResourceBundle.getBundle("messages", Config.LOCALE, new FileClassloader(Signs.class.getClassLoader()));
	}
	
	public static String _(String string, Object... objs) {
		String translated = localeBundle.getString(string);
		return objs != null && objs.length > 0 ? MessageFormat.format(translated, objs).replaceAll("(?<=\\d),(?=\\d)", ".") : translated;
	}
	

}
